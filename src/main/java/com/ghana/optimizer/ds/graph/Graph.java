package com.ghana.optimizer.ds.graph;

public abstract class Graph {

    protected Vertex[] vertices;
    protected int vertexCount;

    public Graph(int maxVertices) {
        this.vertices = new Vertex[maxVertices];
        this.vertexCount = 0;
    }

    /**
     * Adds a vertex to the graph.
     */
    public abstract void addVertex(Vertex vertex);

    /**
     * Adds an edge between two vertices.
     */
    public abstract void addEdge(Edge edge);

    /**
     * Returns true if two vertices are connected.
     */
    public abstract boolean hasEdge(Vertex source, Vertex destination);

    /**
     * Prints the graph representation.
     */
    public abstract void printGraph();

    /**
     * Returns all vertices.
     */
    public Vertex[] getVertices() {
        return vertices;
    }

    /**
     * Returns number of vertices currently stored.
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Finds a vertex by its ID.
     */
    public Vertex findVertexById(String id) {
        for (int i = 0; i < vertexCount; i++) {
            if (vertices[i] != null && vertices[i].getId().equals(id)) {
                return vertices[i];
            }
        }
        return null;
    }
}
