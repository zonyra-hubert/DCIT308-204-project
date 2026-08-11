package com.ghana.optimizer.ds.graph.matrix;

import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Graph;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.VertexIndex;

/**
 * Graph implementation using an Adjacency Matrix.
 * Each cell stores an Edge object. A null value means no direct road exists.
 */
public class AdjacencyMatrixGraph extends Graph {

    private final Edge[][] matrix;
    private final VertexIndex vertexIndex;

    public AdjacencyMatrixGraph(int maxVertices) {
        super(maxVertices);
        matrix = new Edge[maxVertices][maxVertices];
        vertexIndex = new VertexIndex(vertices);
    }

    @Override
    public void addVertex(Vertex vertex) {
        if (vertexCount >= vertices.length) {
            throw new IllegalStateException("Maximum number of vertices reached.");
        }
        vertices[vertexCount++] = vertex;
    }

    @Override
    public void addEdge(Edge edge) {
        int sourceIndex = vertexIndex.indexOf(edge.getSource());
        int destinationIndex = vertexIndex.indexOf(edge.getDestination());

        if (sourceIndex == -1 || destinationIndex == -1) {
            throw new IllegalArgumentException("Vertex not found.");
        }

        matrix[sourceIndex][destinationIndex] = edge;

        // Campus roads are bidirectional; store the reverse edge
        Edge reverseEdge = new Edge(
                edge.getDestination(),
                edge.getSource(),
                edge.getDistanceMeters(),
                edge.getTravelTimeMinutes(),
                edge.getConditionScore(),
                edge.getPenaltyWeight()
        );
        matrix[destinationIndex][sourceIndex] = reverseEdge;
    }

    @Override
    public boolean hasEdge(Vertex source, Vertex destination) {
        int sourceIndex = vertexIndex.indexOf(source);
        int destinationIndex = vertexIndex.indexOf(destination);

        if (sourceIndex == -1 || destinationIndex == -1) {
            return false;
        }

        return matrix[sourceIndex][destinationIndex] != null;
    }

    public Edge getEdge(Vertex source, Vertex destination) {
        int sourceIndex = vertexIndex.indexOf(source);
        int destinationIndex = vertexIndex.indexOf(destination);

        if (sourceIndex == -1 || destinationIndex == -1) {
            return null;
        }

        return matrix[sourceIndex][destinationIndex];
    }

    public Edge[][] getMatrix() {
        return matrix;
    }

    public VertexIndex getVertexIndex() {
        return vertexIndex;
    }

    @Override
    public void printGraph() {
        System.out.println("\n=========== ADJACENCY MATRIX ===========");
        System.out.printf("%-12s", "FROM \\ TO");
        for (int j = 0; j < vertexCount; j++) {
            System.out.printf("%-10s", vertices[j].getId());
        }
        System.out.println();

        for (int i = 0; i < vertexCount; i++) {
            System.out.printf("%-12s", vertices[i].getId());
            for (int j = 0; j < vertexCount; j++) {
                if (matrix[i][j] != null) {
                    System.out.printf("%-10d", matrix[i][j].getDistanceMeters());
                } else {
                    System.out.printf("%-10s", "-");
                }
            }
            System.out.println();
        }
        System.out.println("=========================================\n");
    }
}
