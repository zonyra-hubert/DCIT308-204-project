package com.ghana.optimizer.graph.list;

import com.ghana.optimizer.graph.Edge;
import com.ghana.optimizer.graph.Graph;
import com.ghana.optimizer.graph.Vertex;
import com.ghana.optimizer.graph.VertexIndex;



/**
 * Graph implementation using an Adjacency List.
 * Each vertex stores a linked list of neighbouring edges.
 */
public class AdjacencyListGraph extends Graph {

    /**
     * Head node of each adjacency list.
     */
    private final ListNode[] adjacencyList;

    /**
     * Converts a vertex into its array index.
     */
    private final VertexIndex vertexIndex;

    public AdjacencyListGraph(int maxVertices) {

        super(maxVertices);

        adjacencyList = new ListNode[maxVertices];

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

        /*
         * Add edge to source list
         */
        ListNode sourceNode = new ListNode(edge);

        sourceNode.setNext(adjacencyList[sourceIndex]);

        adjacencyList[sourceIndex] = sourceNode;

        /*
         * Since roads are undirected,
         * create the reverse edge.
         */
        Edge reverseEdge = new Edge(
                edge.getDestination(),
                edge.getSource(),
                edge.getDistanceMeters(),
                edge.getTravelTimeMinutes(),
                edge.getConditionScore(),
                edge.getPenaltyWeight()
        );

        ListNode destinationNode = new ListNode(reverseEdge);

        destinationNode.setNext(adjacencyList[destinationIndex]);

        adjacencyList[destinationIndex] = destinationNode;
    }

    @Override
    public boolean hasEdge(Vertex source, Vertex destination) {

        int sourceIndex = vertexIndex.indexOf(source);

        if (sourceIndex == -1) {
            return false;
        }

        ListNode current = adjacencyList[sourceIndex];

        while (current != null) {

            if (current.getEdge()
                    .getDestination()
                    .getId()
                    .equals(destination.getId())) {

                return true;
            }

            current = current.getNext();
        }

        return false;
    }

    /**
     * Returns the head node of a vertex's adjacency list.
     */
    public ListNode getNeighbours(Vertex vertex) {

        int index = vertexIndex.indexOf(vertex);

        if (index == -1) {
            return null;
        }

        return adjacencyList[index];
    }

    /**
     * Returns the complete adjacency list.
     */
    public ListNode[] getAdjacencyList() {
        return adjacencyList;
    }

    @Override
    public void printGraph() {

        System.out.println("\n========== ADJACENCY LIST ==========");

        for (int i = 0; i < vertexCount; i++) {

            System.out.print(vertices[i].getId() + " -> ");

            ListNode current = adjacencyList[i];

            while (current != null) {

                Edge edge = current.getEdge();

                System.out.print(
                        edge.getDestination().getId()
                                + "("
                                + edge.getDistanceMeters()
                                + "m)"
                );

                current = current.getNext();

                if (current != null) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }
}
