package com.ghana.optimizer.algorithm.search;

import java.util.Comparator;

/**
 * Generic Jump Search algorithm for University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 * Jump search on a sorted array using a fixed block step of floor(sqrt(n)).
 *
 */
public class JumpSearch {

    /**
     * Generic jump search using natural ordering.
     *
     * @param searchArray Sorted array of elements to search through.
     * @param targetElement Value being searched for.
     * @param <T> Element type implementing Comparable.
     * @return Index of matching element, or -1 if target is not found.
     */
    public static <T extends Comparable<T>> int jumpSearch(T[] searchArray, T targetElement) {
        return jumpSearch(searchArray, targetElement, Comparator.naturalOrder());
    }

    /**
     * Generic jump search using a custom Comparator.
     *
     * @param searchArray Sorted array of elements to search through.
     * @param targetElement Value being searched for.
     * @param elementComparator Comparator defining element ordering.
     * @param <T> Element type.
     * @return Index of matching element, or -1 if target is not found.
     */
    public static <T> int jumpSearch(T[] searchArray, T targetElement, Comparator<T> elementComparator) {
        if (searchArray == null) {
            throw new IllegalArgumentException("searchArray must not be null");
        }
        if (targetElement == null) {
            throw new IllegalArgumentException("targetElement must not be null");
        }
        if (elementComparator == null) {
            throw new IllegalArgumentException("elementComparator must not be null");
        }

        int totalElementCount = searchArray.length;
        if (totalElementCount == 0) {
            return -1;
        }

        int jumpBlockStepSize = (int) Math.floor(Math.sqrt(totalElementCount));
        int previousBlockIndex = 0;
        int currentBlockIndex = jumpBlockStepSize;

        // Phase 1: Jump ahead in fixed-size blocks
        while (currentBlockIndex < totalElementCount &&
                elementComparator.compare(searchArray[currentBlockIndex - 1], targetElement) < 0) {
            previousBlockIndex = currentBlockIndex;
            currentBlockIndex += jumpBlockStepSize;
        }

        // Phase 2: Linear scan within identified block
        int searchEndIndex = Math.min(currentBlockIndex, totalElementCount);
        for (int elementIndex = previousBlockIndex; elementIndex < searchEndIndex; elementIndex++) {
            if (elementComparator.compare(searchArray[elementIndex], targetElement) == 0) {
                return elementIndex;
            }
        }
        return -1;
    }

    /**
     * Primitive int[] overload for backward compatibility and benchmarking.
     */
    public static int jumpSearch(int[] primitiveArray, int targetValue) {
        if (primitiveArray == null) {
            throw new IllegalArgumentException("primitiveArray must not be null");
        }
        int totalElementCount = primitiveArray.length;
        if (totalElementCount == 0) {
            return -1;
        }

        int jumpBlockStepSize = (int) Math.floor(Math.sqrt(totalElementCount));
        int previousBlockIndex = 0;
        int currentBlockIndex = jumpBlockStepSize;

        while (currentBlockIndex < totalElementCount && primitiveArray[currentBlockIndex - 1] < targetValue) {
            previousBlockIndex = currentBlockIndex;
            currentBlockIndex += jumpBlockStepSize;
        }

        int searchEndIndex = Math.min(currentBlockIndex, totalElementCount);
        for (int elementIndex = previousBlockIndex; elementIndex < searchEndIndex; elementIndex++) {
            if (primitiveArray[elementIndex] == targetValue) {
                return elementIndex;
            }
        }
        return -1;
    }
}
