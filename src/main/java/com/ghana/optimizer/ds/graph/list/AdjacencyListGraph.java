package com.ghana.optimizer.ds.graph.list;

import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Graph;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.VertexIndex;

/**
 * Graph implementation using an Adjacency List.
 * Each vertex stores a linked list of neighbouring edges.
 */
public class AdjacencyListGraph extends Graph {

    private final ListNode[] adjacencyList;
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

        // Add edge to source list
        ListNode sourceNode = new ListNode(edge);
        sourceNode.setNext(adjacencyList[sourceIndex]);
        adjacencyList[sourceIndex] = sourceNode;

        // Since campus roads are bidirectional, add reverse edge
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
            if (current.getEdge().getDestination().getId().equals(destination.getId())) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public ListNode[] getAdjacencyList() {
        return adjacencyList;
    }

    public ListNode getNeighbors(int vertexIndex) {
        if (vertexIndex < 0 || vertexIndex >= vertices.length) {
            return null;
        }
        return adjacencyList[vertexIndex];
    }

    public VertexIndex getVertexIndex() {
        return vertexIndex;
    }

    @Override
    public void printGraph() {
        System.out.println("\n=========== ADJACENCY LIST ===========");
        for (int i = 0; i < vertexCount; i++) {
            System.out.print(vertices[i].getId() + " (" + vertices[i].getName() + ") -> ");
            ListNode current = adjacencyList[i];
            while (current != null) {
                Edge e = current.getEdge();
                System.out.print("[" + e.getDestination().getId() + " : " + e.getDistanceMeters() + "m] -> ");
                current = current.getNext();
            }
            System.out.println("null");
        }
        System.out.println("======================================\n");
    }
}
