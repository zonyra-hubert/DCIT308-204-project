package com.ghana.optimizer.graph.list;

import com.ghana.optimizer.graph.Edge;

/**
 * Represents one node in an adjacency list.
 */
public class ListNode {

    private Edge edge;

    private ListNode next;

    public ListNode(Edge edge) {
        this.edge = edge;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }
}