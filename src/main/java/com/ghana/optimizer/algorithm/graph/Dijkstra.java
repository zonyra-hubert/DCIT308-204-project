package com.ghana.optimizer.algorithm.graph;

import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.VertexIndex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import com.ghana.optimizer.ds.graph.list.ListNode;
import com.ghana.optimizer.ds.queue.PriorityQueue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dijkstra's shortest-path algorithm over an AdjacencyListGraph.
 *
 * IMPORTANT WEIGHT CHOICE: this minimizes Edge.getEffectiveWeight(),
 * NOT raw distanceMeters. getEffectiveWeight() is
 *     distanceMeters + penaltyWeight * (5.0 - conditionScore)
 * i.e. distance plus a penalty for poor road condition (penaltyWeight
 * is the project's Route Penalty parameter, lambda = 43). Using raw
 * distance instead would silently ignore that penalty entirely and
 * always route purely by physical distance, defeating the point of
 * having a condition-aware routing engine. This is the single most
 * important design decision in this file and the first thing to state
 * in a defense: "what am I minimizing, and why."
 *
 * PRIORITY QUEUE STRATEGY: your PriorityQueue/BinaryHeap has no
 * decrease-key operation (removing/re-prioritizing an entry already
 * sitting inside the heap). The standard textbook Dijkstra relies on
 * decrease-key to update a vertex's priority when a shorter path is
 * found. Without it, this uses the standard workaround: LAZY DELETION.
 * Every time a shorter distance to a vertex is found, a NEW entry for
 * that vertex is pushed onto the heap (the old, now-stale entry is
 * simply left inside it). When popping, any entry whose distance no
 * longer matches the current best known distance for that vertex is
 * recognized as stale and skipped rather than processed. This costs
 * extra heap entries (and a little wasted work skipping stale ones)
 * but requires no changes to BinaryHeap/PriorityQueue at all, and is
 * the standard, defensible approach when a heap doesn't support
 * decrease-key.
 *
 * Complexity: O((V + E) log V). Each edge relaxation can push a new
 * heap entry (E pushes worst case), each vertex is finalized once (V
 * pops worst case after skipping stale entries), and each heap
 * push/pop is O(log V) on a binary heap holding up to O(E) entries.
 */
public class Dijkstra {

    /** One vertex's finalized shortest-path result. */
    public static class DijkstraStep {
        public final String vertexId;
        public final String vertexName;
        public final double distance;       // shortest EFFECTIVE weight from start
        public final String predecessorId;  // null for the start vertex

        DijkstraStep(String vertexId, String vertexName, double distance, String predecessorId) {
            this.vertexId = vertexId;
            this.vertexName = vertexName;
            this.distance = distance;
            this.predecessorId = predecessorId;
        }
    }

    /** One entry sitting in the priority queue: a candidate distance to a vertex. */
    private static class PQEntry {
        final int vertexIdx;
        final double distance;

        PQEntry(int vertexIdx, double distance) {
            this.vertexIdx = vertexIdx;
            this.distance = distance;
        }
    }

    /**
     * Runs Dijkstra from startId over the given graph, finding the
     * shortest EFFECTIVE-WEIGHT path (see class javadoc) to every
     * reachable vertex.
     *
     * @return one DijkstraStep per reachable vertex, in the order they
     *         were FINALIZED (popped and confirmed shortest) — this is
     *         always non-decreasing by distance, a defining property
     *         of Dijkstra. Unreachable vertices are absent.
     */
    public static List<DijkstraStep> dijkstra(AdjacencyListGraph graph, String startId) {
        int n = graph.getVertexCount();
        VertexIndex vertexIndex = graph.getVertexIndex();

        int startIdx = vertexIndex.indexOf(startId);
        if (startIdx == -1) {
            throw new IllegalArgumentException("Start vertex not found: " + startId);
        }

        double[] bestDistance = new double[n];
        String[] predecessorId = new String[n];
        boolean[] finalized = new boolean[n];
        for (int i = 0; i < n; i++) bestDistance[i] = Double.POSITIVE_INFINITY;
        bestDistance[startIdx] = 0.0;

        PriorityQueue<PQEntry> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distance));
        pq.enqueue(new PQEntry(startIdx, 0.0));

        List<DijkstraStep> order = new ArrayList<>();

        while (!pq.isEmpty()) {
            PQEntry current = pq.dequeue();

            // Stale entry check (the lazy-deletion mechanism described
            // above): if a shorter path to this vertex was already
            // finalized since this entry was pushed, skip it.
            if (current.distance > bestDistance[current.vertexIdx]) {
                continue;
            }
            if (finalized[current.vertexIdx]) {
                continue; // already finalized via an earlier, equally-good entry
            }
            finalized[current.vertexIdx] = true;

            Vertex currentVertex = graph.getVertices()[current.vertexIdx];
            order.add(new DijkstraStep(currentVertex.getId(), currentVertex.getName(),
                    current.distance, predecessorId[current.vertexIdx]));

            ListNode neighborNode = graph.getNeighbors(current.vertexIdx);
            while (neighborNode != null) {
                Vertex neighbor = neighborNode.getEdge().getDestination();
                int neighborIdx = vertexIndex.indexOf(neighbor);
                double edgeWeight = neighborNode.getEdge().getEffectiveWeight();

                if (!finalized[neighborIdx]) {
                    double candidateDistance = current.distance + edgeWeight;
                    if (candidateDistance < bestDistance[neighborIdx]) {
                        bestDistance[neighborIdx] = candidateDistance;
                        predecessorId[neighborIdx] = currentVertex.getId();
                        pq.enqueue(new PQEntry(neighborIdx, candidateDistance));
                    }
                }

                neighborNode = neighborNode.getNext();
            }
        }

        return order;
    }

    /**
     * Reconstructs the shortest (minimum effective-weight) path from
     * the Dijkstra start vertex to targetId. Returns an empty list if
     * targetId is unreachable.
     */
    public static List<String> reconstructPath(List<DijkstraStep> dijkstraResult, String targetId) {
        Map<String, DijkstraStep> byId = new HashMap<>();
        for (DijkstraStep step : dijkstraResult) {
            byId.put(step.vertexId, step);
        }

        if (!byId.containsKey(targetId)) {
            return java.util.Collections.emptyList();
        }

        java.util.LinkedList<String> path = new java.util.LinkedList<>();
        String cursor = targetId;
        while (cursor != null) {
            path.addFirst(cursor);
            cursor = byId.get(cursor).predecessorId;
        }
        return path;
    }

    /** Prints a trace table: finalization order, vertex, cumulative effective distance, predecessor. */
    public static String printTrace(List<DijkstraStep> dijkstraResult, String startId) {
        StringBuilder trace = new StringBuilder();
        trace.append(String.format("%-5s %-10s %-20s %-14s %-12s%n",
                "Order", "VertexId", "Name", "Distance", "Predecessor"));
        trace.append("-".repeat(65)).append(System.lineSeparator());

        int order = 1;
        for (DijkstraStep s : dijkstraResult) {
            trace.append(String.format("%-5d %-10s %-20s %-14.2f %-12s%n",
                    order++, s.vertexId, s.vertexName, s.distance,
                    s.predecessorId == null ? "-" : s.predecessorId));
        }

        trace.append("-".repeat(65)).append(System.lineSeparator());
        trace.append("Start vertex: ").append(startId).append(System.lineSeparator());
        trace.append("Vertices reached: ").append(dijkstraResult.size()).append(System.lineSeparator());
        trace.append("Weight metric: effective weight = distance + penaltyWeight * (5.0 - conditionScore)")
                .append(System.lineSeparator());

        String result = trace.toString();
        System.out.println(result);
        return result;
    }
}