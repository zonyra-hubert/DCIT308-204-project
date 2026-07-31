package com.ghana.optimizer.graph.matrix;

import com.ghana.optimizer.graph.Edge;
import com.ghana.optimizer.graph.Graph;
import com.ghana.optimizer.graph.Vertex;
import com.ghana.optimizer.graph.VertexIndex;

/**
 * Graph implementation using an Adjacency Matrix.
 *
 * Each cell stores an Edge object.
 * A null value means no direct road exists.
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

        /*
         * Campus roads are bidirectional.
         * Store the same edge in the opposite direction.
         */
        matrix[destinationIndex][sourceIndex] = edge;
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

    /**
     * Returns the Edge between two vertices.
     */
    public Edge getEdge(Vertex source, Vertex destination) {

        int sourceIndex = vertexIndex.indexOf(source);

        int destinationIndex = vertexIndex.indexOf(destination);

        if (sourceIndex == -1 || destinationIndex == -1) {
            return null;
        }

        return matrix[sourceIndex][destinationIndex];
    }

    /**
     * Returns the entire adjacency matrix.
     */
    public Edge[][] getMatrix() {
        return matrix;
    }

    @Override
    public void printGraph() {

        System.out.println("\n=========== ADJACENCY MATRIX ===========");

        System.out.print("\t");

        for (int i = 0; i < vertexCount; i++) {

            System.out.print(vertices[i].getId() + "\t");
        }

        System.out.println();

        for (int i = 0; i < vertexCount; i++) {

            System.out.print(vertices[i].getId() + "\t");

            for (int j = 0; j < vertexCount; j++) {

                if (matrix[i][j] == null) {

                    System.out.print("0\t");

                } else {

                    System.out.print(matrix[i][j].getDistanceMeters() + "\t");
                }
            }

            System.out.println();
        }
    }
}
