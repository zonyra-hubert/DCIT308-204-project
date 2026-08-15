package com.ghana.optimizer.algorithm.graph;

import com.ghana.optimizer.ds.disjoint.DisjointSet;
import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Graph;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import com.ghana.optimizer.ds.graph.list.ListNode;
import com.ghana.optimizer.ds.graph.matrix.AdjacencyMatrixGraph;
import com.ghana.optimizer.ds.list.DynamicArray;

import java.util.Comparator;

/**
 * Kruskal's Minimum Spanning Tree (MST) algorithm.
 *
 * Computes the minimum cost road network backbone connecting all campus locations
 * using custom DisjointSet (Union-Find with path compression and rank) to prevent cycles.
 *
 * Time Complexity: O(E log E) for sorting edges + O(E * alpha(V)) for Union-Find operations.
 */
public class KruskalMST {

    public static final double DEFAULT_PENALTY_WEIGHT = 43.0;

    /**
     * Immutable container holding MST result data.
     */
    public static class MSTResult {
        private final DynamicArray<Edge> mstEdges;
        private final double totalEffectiveCost;
        private final int totalDistanceMeters;
        private final int totalVertices;
        private final int connectedComponents;
        private final boolean isSpanning;

        public MSTResult(DynamicArray<Edge> mstEdges,
                         double totalEffectiveCost,
                         int totalDistanceMeters,
                         int totalVertices,
                         int connectedComponents,
                         boolean isSpanning) {
            this.mstEdges = mstEdges;
            this.totalEffectiveCost = totalEffectiveCost;
            this.totalDistanceMeters = totalDistanceMeters;
            this.totalVertices = totalVertices;
            this.connectedComponents = connectedComponents;
            this.isSpanning = isSpanning;
        }

        public DynamicArray<Edge> getMstEdges() {
            return mstEdges;
        }

        public double getTotalEffectiveCost() {
            return totalEffectiveCost;
        }

        public int getTotalDistanceMeters() {
            return totalDistanceMeters;
        }

        public int getTotalVertices() {
            return totalVertices;
        }

        public int getConnectedComponents() {
            return connectedComponents;
        }

        public boolean isSpanning() {
            return isSpanning;
        }

        public String formatMSTSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("================================================================================\n");
            sb.append("  UG CAMPUS MINIMUM SPANNING TREE (Kruskal's MST - DisjointSet)\n");
            sb.append("================================================================================\n");
            sb.append(String.format(" Total Campus Vertices : %d\n", totalVertices));
            sb.append(String.format(" MST Backbone Edges    : %d (Expected V-1 = %d)\n", mstEdges.size(), totalVertices - 1));
            sb.append(String.format(" Total Effective Cost  : %.2f weighted units\n", totalEffectiveCost));
            sb.append(String.format(" Total Distance        : %d meters (%.2f km)\n", totalDistanceMeters, totalDistanceMeters / 1000.0));
            sb.append(String.format(" Spanning Tree Complete: %s\n", isSpanning ? "YES (Fully Connected)" : "NO (Forest with " + connectedComponents + " components)"));
            sb.append("--------------------------------------------------------------------------------\n");
            sb.append(" Selected Backbone Edge List:\n");

            for (int i = 0; i < mstEdges.size(); i++) {
                Edge e = mstEdges.get(i);
                sb.append(String.format("   [%02d] %s <--> %s | Dist: %5dm | Cond: %.1f | Cost: %.1f\n",
                        i + 1, e.getSource().getId(), e.getDestination().getId(),
                        e.getDistanceMeters(), e.getConditionScore(), e.getEffectiveWeight()));
            }
            sb.append("================================================================================\n");
            return sb.toString();
        }
    }

    /**
     * Computes the Minimum Spanning Tree of the campus road graph.
     *
     * @param graph Campus graph.
     * @return MSTResult containing the backbone edges and metrics.
     */
    public static MSTResult computeMST(Graph graph) {
        return computeMST(graph, DEFAULT_PENALTY_WEIGHT);
    }

    /**
     * Computes the Minimum Spanning Tree of the campus road graph with custom penalty weight.
     *
     * @param graph Campus graph.
     * @param penaltyWeight Lambda road condition penalty factor.
     * @return MSTResult containing the backbone edges and metrics.
     */
    public static MSTResult computeMST(Graph graph, double penaltyWeight) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }

        int vertexCount = graph.getVertexCount();
        Vertex[] vertices = graph.getVertices();

        if (vertexCount == 0) {
            return new MSTResult(new DynamicArray<>(), 0.0, 0, 0, 0, true);
        }

        DynamicArray<Edge> allEdges = extractUniqueEdges(graph);

        // Sort edges by effective weight ascending
        sortEdgesByWeight(allEdges, penaltyWeight);

        DisjointSet disjointSet = new DisjointSet(vertexCount);
        DynamicArray<Edge> mstEdges = new DynamicArray<>();
        double totalCost = 0.0;
        int totalDistance = 0;

        for (int i = 0; i < allEdges.size(); i++) {
            Edge edge = allEdges.get(i);
            int u = findVertexIndex(vertices, vertexCount, edge.getSource().getId());
            int v = findVertexIndex(vertices, vertexCount, edge.getDestination().getId());

            if (u != -1 && v != -1) {
                if (disjointSet.find(u) != disjointSet.find(v)) {
                    disjointSet.union(u, v);
                    mstEdges.add(edge);
                    double weight = edge.getDistanceMeters() + penaltyWeight * (5.0 - edge.getConditionScore());
                    totalCost += weight;
                    totalDistance += edge.getDistanceMeters();

                    if (mstEdges.size() == vertexCount - 1) {
                        break; // Spanning tree complete
                    }
                }
            }
        }

        int components = disjointSet.countSets();
        boolean isSpanning = (mstEdges.size() == vertexCount - 1) || (vertexCount <= 1);

        return new MSTResult(mstEdges, totalCost, totalDistance, vertexCount, components, isSpanning);
    }

    private static DynamicArray<Edge> extractUniqueEdges(Graph graph) {
        DynamicArray<Edge> edges = new DynamicArray<>();
        int vertexCount = graph.getVertexCount();
        Vertex[] vertices = graph.getVertices();

        if (graph instanceof AdjacencyListGraph) {
            AdjacencyListGraph listGraph = (AdjacencyListGraph) graph;
            for (int i = 0; i < vertexCount; i++) {
                ListNode node = listGraph.getNeighbors(i);
                while (node != null) {
                    Edge e = node.getEdge();
                    // To avoid duplicates in undirected graph, only add where source ID < destination ID
                    if (e.getSource().getId().compareTo(e.getDestination().getId()) < 0) {
                        edges.add(e);
                    }
                    node = node.getNext();
                }
            }
        } else if (graph instanceof AdjacencyMatrixGraph) {
            AdjacencyMatrixGraph matrixGraph = (AdjacencyMatrixGraph) graph;
            Edge[][] matrix = matrixGraph.getMatrix();
            for (int i = 0; i < vertexCount; i++) {
                for (int j = i + 1; j < vertexCount; j++) {
                    if (matrix[i][j] != null) {
                        edges.add(matrix[i][j]);
                    }
                }
            }
        }

        return edges;
    }

    private static void sortEdgesByWeight(DynamicArray<Edge> edges, double penaltyWeight) {
        // In-place merge sort or insertion sort on DynamicArray<Edge>
        int n = edges.size();
        if (n <= 1) return;

        Edge[] temp = new Edge[n];
        mergeSort(edges, temp, 0, n - 1, penaltyWeight);
    }

    private static void mergeSort(DynamicArray<Edge> edges, Edge[] temp, int left, int right, double penaltyWeight) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(edges, temp, left, mid, penaltyWeight);
        mergeSort(edges, temp, mid + 1, right, penaltyWeight);
        merge(edges, temp, left, mid, right, penaltyWeight);
    }

    private static void merge(DynamicArray<Edge> edges, Edge[] temp, int left, int mid, int right, double penaltyWeight) {
        for (int i = left; i <= right; i++) {
            temp[i] = edges.get(i);
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            double weightI = temp[i].getDistanceMeters() + penaltyWeight * (5.0 - temp[i].getConditionScore());
            double weightJ = temp[j].getDistanceMeters() + penaltyWeight * (5.0 - temp[j].getConditionScore());

            if (weightI <= weightJ) {
                edges.set(k++, temp[i++]);
            } else {
                edges.set(k++, temp[j++]);
            }
        }

        while (i <= mid) {
            edges.set(k++, temp[i++]);
        }
        while (j <= right) {
            edges.set(k++, temp[j++]);
        }
    }

    private static int findVertexIndex(Vertex[] vertices, int count, String id) {
        for (int i = 0; i < count; i++) {
            if (vertices[i] != null && vertices[i].getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
