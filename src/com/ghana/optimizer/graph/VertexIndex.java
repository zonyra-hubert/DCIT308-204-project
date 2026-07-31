package com.ghana.optimizer.graph;

public class VertexIndex {

    private final Vertex[] vertices;

    public VertexIndex(Vertex[] vertices) {
        this.vertices = vertices;
    }

    /**
     * Returns the array index of a vertex.
     */
    public int indexOf(Vertex vertex) {

        if (vertex == null) {
            return -1;
        }

        for (int i = 0; i < vertices.length; i++) {

            if (vertices[i] == null) {
                continue;
            }

            if (vertices[i].getId().equals(vertex.getId())) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the array index using vertex ID.
     */
    public int indexOf(String vertexId) {

        for (int i = 0; i < vertices.length; i++) {

            if (vertices[i] == null) {
                continue;
            }

            if (vertices[i].getId().equals(vertexId)) {
                return i;
            }
        }

        return -1;
    }
}

