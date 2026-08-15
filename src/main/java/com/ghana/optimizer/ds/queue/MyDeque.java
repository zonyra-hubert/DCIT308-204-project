package com.ghana.optimizer.ds.queue;

import com.ghana.optimizer.ds.list.Node;

/**
 * A custom generic double-ended queue implemented using a doubly linked list.
 * This is suited for simulation and operational workflows where tasks may need
 * to be processed from either end, such as urgent campus service requests or
 * dispatch activities.
 *
 * @param <T> the type of element stored in the deque
 */
public class MyDeque<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Creates an empty deque.
     */
    public MyDeque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Adds an element to the front of the deque.
     *
     * @param value the element to add
     */
    public void addFront(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }

        size++;
    }

    /**
     * Adds an element to the rear of the deque.
     *
     * @param value the element to add
     */
    public void addRear(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }

        size++;
    }

    /**
     * Removes and returns the element at the front of the deque.
     *
     * @return the front element, or null if the deque is empty
     */
    public T removeFront() {
        if (isEmpty()) {
            return null;
        }

        T value = head.getData();

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrevious(null);
        }

        size--;
        return value;
    }

    /**
     * Removes and returns the element at the rear of the deque.
     *
     * @return the rear element, or null if the deque is empty
     */
    public T removeRear() {
        if (isEmpty()) {
            return null;
        }

        T value = tail.getData();

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }

        size--;
        return value;
    }

    /**
     * Returns the front element without removing it.
     *
     * @return the front element, or null if the deque is empty
     */
    public T peekFront() {
        if (isEmpty()) {
            return null;
        }

        return head.getData();
    }

    /**
     * Returns the rear element without removing it.
     *
     * @return the rear element, or null if the deque is empty
     */
    public T peekRear() {
        if (isEmpty()) {
            return null;
        }

        return tail.getData();
    }

    /**
     * Checks whether the deque is empty.
     *
     * @return true if empty; false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements currently in the deque.
     *
     * @return the deque size
     */
    public int size() {
        return size;
    }

    /**
     * Removes all elements from the deque.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}
