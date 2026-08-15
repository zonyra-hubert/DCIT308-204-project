package com.ghana.optimizer.algorithm.graph;

import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.VertexIndex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import com.ghana.optimizer.ds.graph.list.ListNode;
import com.ghana.optimizer.ds.queue.MyQueue;

/**
 * Breadth-First Search over an AdjacencyListGraph.
 *
 * Explores the graph outward one hop-distance at a time using a FIFO
 * queue (MyQueue), which is exactly what gives BFS its defining
 * property: the first time a vertex is reached, it has been reached by
 * the fewest possible number of edges from the start. Swapping the
 * queue for a stack would turn this into DFS instead — same skeleton,
 * different exploration order, because a stack always continues down
 * the most recently discovered path instead of spreading evenly.
 *
 * Correctness detail worth defending: a vertex is marked visited the
 * MOMENT it is enqueued, not when it is dequeued. If marking were
 * delayed until dequeue, a vertex reachable from two different
 * already-processed vertices could be enqueued twice before either
 * enqueue is processed, corrupting both the traversal order and the
 * hop-distance table below.
 *
 * Complexity: O(V + E) — every vertex is enqueued/dequeued exactly
 * once (V), and every edge is inspected exactly once when its source
 * vertex's adjacency list is walked (E). No comparisons/swaps here —
 * the cost units are vertex visits and edge inspections.
 *
 * Bidirectionality note: AdjacencyListGraph.addEdge() already inserts
 * both directions for every road, so BFS here does not need any special
 * handling for undirected edges — it simply walks whatever adjacency
 * list each vertex already has.
 */
public class BFS {

    /** One traversal step, for building the trace table / result. */
    public static class BFSStep {
        public final String vertexId;
        public final String vertexName;
        public final int distance;      // hop count from start
        public final String predecessorId; // null for the start vertex

        BFSStep(String vertexId, String vertexName, int distance, String predecessorId) {
            this.vertexId = vertexId;
            this.vertexName = vertexName;
            this.distance = distance;
            this.predecessorId = predecessorId;
        }
    }

    /**
     * Runs BFS from startId over the given graph.
     *
     * @param graph   the adjacency-list graph to traverse
     * @param startId the id of the vertex to start from
     * @return an ordered list of BFSStep, in the order vertices were
     *         VISITED (dequeued) — i.e. the actual BFS traversal order,
     *         each carrying its hop distance from the start and its
     *         predecessor (for path reconstruction). Vertices
     *         unreachable from startId are simply absent from the list.
     */
    public static java.util.List<BFSStep> bfs(AdjacencyListGraph graph, String startId) {
        int n = graph.getVertexCount();
        VertexIndex vertexIndex = graph.getVertexIndex();

        int startIdx = vertexIndex.indexOf(startId);
        if (startIdx == -1) {
            throw new IllegalArgumentException("Start vertex not found: " + startId);
        }

        boolean[] visited = new boolean[n];
        int[] distance = new int[n];
        String[] predecessorId = new String[n];

        java.util.List<BFSStep> order = new java.util.ArrayList<>();
        MyQueue<Integer> queue = new MyQueue<>();

        Vertex startVertex = graph.getVertices()[startIdx];
        visited[startIdx] = true;
        distance[startIdx] = 0;
        predecessorId[startIdx] = null;
        queue.enqueue(startIdx);

        while (!queue.isEmpty()) {
            int currentIdx = queue.dequeue();
            Vertex current = graph.getVertices()[currentIdx];

            order.add(new BFSStep(current.getId(), current.getName(),
                    distance[currentIdx], predecessorId[currentIdx]));

            ListNode neighborNode = graph.getNeighbors(currentIdx);
            while (neighborNode != null) {
                Vertex neighbor = neighborNode.getEdge().getDestination();
                int neighborIdx = vertexIndex.indexOf(neighbor);

                if (!visited[neighborIdx]) {
                    visited[neighborIdx] = true; // mark on enqueue, not on dequeue
                    distance[neighborIdx] = distance[currentIdx] + 1;
                    predecessorId[neighborIdx] = current.getId();
                    queue.enqueue(neighborIdx);
                }

                neighborNode = neighborNode.getNext();
            }
        }

        return order;
    }

    /**
     * Reconstructs the shortest (fewest-hops) path from the BFS start
     * vertex to targetId, using the predecessor chain captured in the
     * BFSStep list. Returns an empty list if targetId is unreachable
     * (i.e. was never visited by bfs()).
     */
    public static java.util.List<String> reconstructPath(java.util.List<BFSStep> bfsResult, String targetId) {
        java.util.Map<String, BFSStep> byId = new java.util.HashMap<>();
        for (BFSStep step : bfsResult) {
            byId.put(step.vertexId, step);
        }

        if (!byId.containsKey(targetId)) {
            return java.util.Collections.emptyList(); // unreachable
        }

        java.util.LinkedList<String> path = new java.util.LinkedList<>();
        String cursor = targetId;
        while (cursor != null) {
            path.addFirst(cursor);
            cursor = byId.get(cursor).predecessorId;
        }
        return path;
    }

    /**
     * Prints a trace table of the BFS run: each row is the order a
     * vertex was VISITED (dequeued), its hop distance from the start,
     * and which vertex discovered it.
     */
    public static String printTrace(java.util.List<BFSStep> bfsResult, String startId) {
        StringBuilder trace = new StringBuilder();
        trace.append(String.format("%-5s %-10s %-20s %-10s %-12s%n",
                "Step", "VertexId", "Name", "Distance", "Predecessor"));
        trace.append("-".repeat(65)).append(System.lineSeparator());

        int step = 1;
        for (BFSStep s : bfsResult) {
            trace.append(String.format("%-5d %-10s %-20s %-10d %-12s%n",
                    step++, s.vertexId, s.vertexName, s.distance,
                    s.predecessorId == null ? "-" : s.predecessorId));
        }

        trace.append("-".repeat(65)).append(System.lineSeparator());
        trace.append("Start vertex: ").append(startId).append(System.lineSeparator());
        trace.append("Vertices reached: ").append(bfsResult.size()).append(System.lineSeparator());

        String result = trace.toString();
        System.out.println(result);
        return result;
    }
}
