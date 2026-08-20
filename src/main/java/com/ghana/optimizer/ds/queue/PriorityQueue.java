package com.ghana.optimizer.ds.queue;

import com.ghana.optimizer.ds.heap.BinaryHeap;

import java.util.Comparator;

/**
 * Priority Queue implementation located in the queue package for University of Ghana
 * Campus Service Operations Optimizer (UG-CSOO).
 *
 * Utilizes a custom BinaryHeap from com.ghana.optimizer.ds.heap as its underlying storage engine.
 *
 * @param <T> Element type contained in the priority queue.
 */
public class PriorityQueue<T> {

    private final BinaryHeap<T> underlyingBinaryHeap;

    /**
     * Default constructor initializing priority queue with default binary heap.
     */
    public PriorityQueue() {
        this.underlyingBinaryHeap = new BinaryHeap<>();
    }

    /**
     * Constructor with initial capacity.
     *
     * @param initialCapacity Starting capacity of underlying binary heap array.
     */
    public PriorityQueue(int initialCapacity) {
        this.underlyingBinaryHeap = new BinaryHeap<>(initialCapacity);
    }

    /**
     * Constructor with custom comparator.
     *
     * @param comparatorInstance Comparator for ordering elements.
     */
    public PriorityQueue(Comparator<T> comparatorInstance) {
        this.underlyingBinaryHeap = new BinaryHeap<>(comparatorInstance);
    }

    /**
     * Full constructor specifying initial capacity and custom comparator.
     *
     * @param initialCapacity Starting capacity of underlying binary heap array.
     * @param comparatorInstance Comparator for ordering elements.
     */
    public PriorityQueue(int initialCapacity, Comparator<T> comparatorInstance) {
        this.underlyingBinaryHeap = new BinaryHeap<>(initialCapacity, comparatorInstance);
    }

    /**
     * Enqueues an element into the priority queue.
     *
     * @param insertedElement Element to insert.
     */
    public void enqueue(T insertedElement) {
        underlyingBinaryHeap.insert(insertedElement);
    }

    /**
     * Alias for enqueue method (Queue API standard).
     *
     * @param insertedElement Element to insert.
     * @return true when element is successfully added.
     */
    public boolean offer(T insertedElement) {
        enqueue(insertedElement);
        return true;
    }

    /**
     * Removes and returns the highest priority element.
     *
     * @return Highest priority element, or null if empty.
     */
    public T dequeue() {
        return underlyingBinaryHeap.extractTop();
    }

    /**
     * Alias for dequeue method (Queue API standard).
     *
     * @return Highest priority element, or null if empty.
     */
    public T poll() {
        return dequeue();
    }

    /**
     * Returns the highest priority element without removing it.
     *
     * @return Highest priority element, or null if empty.
     */
    public T peek() {
        return underlyingBinaryHeap.peekTop();
    }

    /**
     * Checks if the target element exists in the priority queue.
     *
     * @param targetElement Element to check.
     * @return true if present, false otherwise.
     */
    public boolean contains(T targetElement) {
        return underlyingBinaryHeap.contains(targetElement);
    }

    /**
     * Clears all elements from the priority queue.
     */
    public void clear() {
        underlyingBinaryHeap.clear();
    }

    public int size() {
        return underlyingBinaryHeap.size();
    }

    public boolean isEmpty() {
        return underlyingBinaryHeap.isEmpty();
    }

    public int capacity() {
        return underlyingBinaryHeap.capacity();
    }
}
