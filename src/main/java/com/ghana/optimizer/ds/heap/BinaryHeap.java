package com.ghana.optimizer.ds.heap;

import java.util.Comparator;

/**

 *
 *
 * @param <T> Element type contained in the binary heap.
 */
public class BinaryHeap<T> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    private Object[] heapArray;
    private int currentElementCount;
    private final Comparator<T> comparatorInstance;

    /**
     * Default constructor initializing binary heap with default capacity (11)
     * and natural ordering.
     */
    public BinaryHeap() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * Constructor with initial capacity and natural ordering.
     *
     * @param initialCapacity Starting capacity of underlying array.
     */
    public BinaryHeap(int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * Constructor with custom comparator.
     *
     * @param comparatorInstance Comparator for ordering elements.
     */
    public BinaryHeap(Comparator<T> comparatorInstance) {
        this(DEFAULT_INITIAL_CAPACITY, comparatorInstance);
    }

    /**
     * Full constructor specifying initial capacity and custom comparator.
     *
     * @param initialCapacity Starting capacity of underlying array.
     * @param comparatorInstance Comparator for ordering elements.
     */
    public BinaryHeap(int initialCapacity, Comparator<T> comparatorInstance) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be greater than zero");
        }
        this.heapArray = new Object[initialCapacity];
        this.currentElementCount = 0;
        this.comparatorInstance = comparatorInstance;
    }

    /**
     * Inserts an element into the binary heap and sifts it up to maintain heap order.
     *
     * @param insertedElement Element to insert.
     */
    public void insert(T insertedElement) {
        if (insertedElement == null) {
            throw new IllegalArgumentException("Cannot insert null element into BinaryHeap");
        }
        if (currentElementCount == heapArray.length) {
            resizeHeapArray();
        }
        heapArray[currentElementCount] = insertedElement;
        siftUp(currentElementCount);
        currentElementCount++;
    }

    /**
     * Removes and returns the top (root) element of the binary heap.
     *
     * @return Top element, or null if empty.
     */
    @SuppressWarnings("unchecked")
    public T extractTop() {
        if (isEmpty()) {
            return null;
        }
        T rootElement = (T) heapArray[0];
        int lastElementIndex = currentElementCount - 1;
        T lastElement = (T) heapArray[lastElementIndex];

        heapArray[0] = lastElement;
        heapArray[lastElementIndex] = null;
        currentElementCount--;

        if (currentElementCount > 0) {
            siftDown(0);
        }
        return rootElement;
    }

    /**
     * Alias for extractTop method when used as min-heap.
     *
     * @return Root element, or null if empty.
     */
    public T extractMin() {
        return extractTop();
    }

    /**
     * Returns the root element without removing it.
     *
     * @return Root element, or null if empty.
     */
    @SuppressWarnings("unchecked")
    public T peekTop() {
        if (isEmpty()) {
            return null;
        }
        T rootElement = (T) heapArray[0];
        return rootElement;
    }

    /**
     * Restores heap property upward starting from startingElementIndex.
     */
    @SuppressWarnings("unchecked")
    private void siftUp(int startingElementIndex) {
        int currentElementIndex = startingElementIndex;
        T movedElement = (T) heapArray[currentElementIndex];

        while (currentElementIndex > 0) {
            int parentElementIndex = getParentIndex(currentElementIndex);
            T parentElement = (T) heapArray[parentElementIndex];

            if (compareElements(movedElement, parentElement) >= 0) {
                break;
            }
            heapArray[currentElementIndex] = parentElement;
            currentElementIndex = parentElementIndex;
        }
        heapArray[currentElementIndex] = movedElement;
    }

    /**
     * Restores heap property downward starting from startingElementIndex.
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int startingElementIndex) {
        int currentElementIndex = startingElementIndex;
        T movedElement = (T) heapArray[currentElementIndex];
        int halfSizeBoundary = currentElementCount / 2;

        while (currentElementIndex < halfSizeBoundary) {
            int leftChildIndex = getLeftChildIndex(currentElementIndex);
            int rightChildIndex = getRightChildIndex(currentElementIndex);
            int higherPriorityChildIndex = leftChildIndex;
            T higherPriorityChildElement = (T) heapArray[leftChildIndex];

            if (rightChildIndex < currentElementCount) {
                T rightChildElement = (T) heapArray[rightChildIndex];
                if (compareElements(rightChildElement, higherPriorityChildElement) < 0) {
                    higherPriorityChildIndex = rightChildIndex;
                    higherPriorityChildElement = rightChildElement;
                }
            }

            if (compareElements(movedElement, higherPriorityChildElement) <= 0) {
                break;
            }
            heapArray[currentElementIndex] = higherPriorityChildElement;
            currentElementIndex = higherPriorityChildIndex;
        }
        heapArray[currentElementIndex] = movedElement;
    }

    /**
     * Compares two elements using comparator if provided, or natural ordering.
     */
    @SuppressWarnings("unchecked")
    private int compareElements(T firstElement, T secondElement) {
        if (comparatorInstance != null) {
            int comparisonResult = comparatorInstance.compare(firstElement, secondElement);
            return comparisonResult;
        } else {
            Comparable<T> comparableFirstElement = (Comparable<T>) firstElement;
            int comparisonResult = comparableFirstElement.compareTo(secondElement);
            return comparisonResult;
        }
    }

    private int getParentIndex(int childIndex) {
        int parentIndex = (childIndex - 1) / 2;
        return parentIndex;
    }

    private int getLeftChildIndex(int parentIndex) {
        int leftChildIndex = (2 * parentIndex) + 1;
        return leftChildIndex;
    }

    private int getRightChildIndex(int parentIndex) {
        int rightChildIndex = (2 * parentIndex) + 2;
        return rightChildIndex;
    }

    /**
     * Doubles capacity of the underlying heap array when full.
     */
    private void resizeHeapArray() {
        int oldHeapCapacity = heapArray.length;
        int newHeapCapacity = oldHeapCapacity * 2;
        Object[] newHeapArray = new Object[newHeapCapacity];
        System.arraycopy(heapArray, 0, newHeapArray, 0, currentElementCount);
        this.heapArray = newHeapArray;
    }

    /**
     * Checks if the target element exists in the binary heap.
     *
     * @param targetElement Element to search for.
     * @return true if found, false otherwise.
     */
    public boolean contains(T targetElement) {
        if (targetElement == null) {
            return false;
        }
        for (int searchedElementIndex = 0; searchedElementIndex < currentElementCount; searchedElementIndex++) {
            if (targetElement.equals(heapArray[searchedElementIndex])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all elements from the binary heap.
     */
    public void clear() {
        for (int searchedElementIndex = 0; searchedElementIndex < currentElementCount; searchedElementIndex++) {
            heapArray[searchedElementIndex] = null;
        }
        this.currentElementCount = 0;
    }

    public int size() {
        return currentElementCount;
    }

    public boolean isEmpty() {
        return currentElementCount == 0;
    }

    public int capacity() {
        return heapArray.length;
    }
}
