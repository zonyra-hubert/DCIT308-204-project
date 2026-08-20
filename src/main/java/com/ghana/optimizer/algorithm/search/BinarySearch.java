package com.ghana.optimizer.algorithm.search;

import java.util.Comparator;

/**
 * Generic Binary Search algorithm for University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 * Operates over sorted arrays using generic types (T extends Comparable<T> or custom Comparator<T>).
 *
 */
public final class BinarySearch {

    private BinarySearch() {
        // Utility class - private constructor prevents instantiation
    }

    /**
     * Generic binary search using natural ordering (Comparable).
     *
     * @param searchArray Sorted array of elements to search through.
     * @param targetElement Value being searched for.
     * @param <T> Element type implementing Comparable.
     * @return Index of matching element, or -1 if target is not found.
     */
    public static <T extends Comparable<T>> int search(T[] searchArray, T targetElement) {
        return search(searchArray, targetElement, Comparator.naturalOrder());
    }

    /**
     * Generic binary search using a custom Comparator.
     *
     * @param searchArray Sorted array of elements to search through.
     * @param targetElement Value being searched for.
     * @param elementComparator Comparator defining element ordering.
     * @param <T> Element type.
     * @return Index of matching element, or -1 if target is not found.
     */
    public static <T> int search(T[] searchArray, T targetElement, Comparator<T> elementComparator) {
        if (searchArray == null) {
            throw new IllegalArgumentException("searchArray must not be null");
        }
        if (targetElement == null) {
            throw new IllegalArgumentException("targetElement must not be null");
        }
        if (elementComparator == null) {
            throw new IllegalArgumentException("elementComparator must not be null");
        }
        if (!isSortedAscending(searchArray, elementComparator)) {
            throw new IllegalArgumentException("BinarySearch requires the array to be sorted in ascending order");
        }

        int lowerBoundaryIndex = 0;
        int upperBoundaryIndex = searchArray.length - 1;

        while (lowerBoundaryIndex <= upperBoundaryIndex) {
            int middleBoundaryIndex = lowerBoundaryIndex + (upperBoundaryIndex - lowerBoundaryIndex) / 2;
            T currentElement = searchArray[middleBoundaryIndex];
            int comparisonResult = elementComparator.compare(currentElement, targetElement);

            if (comparisonResult == 0) {
                return middleBoundaryIndex;
            } else if (comparisonResult < 0) {
                lowerBoundaryIndex = middleBoundaryIndex + 1;
            } else {
                upperBoundaryIndex = middleBoundaryIndex - 1;
            }
        }
        return -1;
    }

    /**
     * Primitive int[] overload for backward compatibility.
     */
    public static int search(int[] primitiveArray, int targetValue) {
        if (primitiveArray == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (!isSortedAscending(primitiveArray)) {
            throw new IllegalArgumentException("BinarySearch requires the array to be sorted in ascending order");
        }

        int lowerBoundaryIndex = 0;
        int upperBoundaryIndex = primitiveArray.length - 1;

        while (lowerBoundaryIndex <= upperBoundaryIndex) {
            int middleBoundaryIndex = lowerBoundaryIndex + (upperBoundaryIndex - lowerBoundaryIndex) / 2;
            int currentValue = primitiveArray[middleBoundaryIndex];

            if (currentValue == targetValue) {
                return middleBoundaryIndex;
            } else if (currentValue < targetValue) {
                lowerBoundaryIndex = middleBoundaryIndex + 1;
            } else {
                upperBoundaryIndex = middleBoundaryIndex - 1;
            }
        }
        return -1;
    }

    /**
     * Verifies if a generic array is sorted in ascending order using natural ordering.
     */
    public static <T extends Comparable<T>> boolean isSortedAscending(T[] searchArray) {
        return isSortedAscending(searchArray, Comparator.naturalOrder());
    }

    /**
     * Verifies if a generic array is sorted in ascending order using a custom Comparator.
     */
    public static <T> boolean isSortedAscending(T[] searchArray, Comparator<T> elementComparator) {
        if (searchArray == null) {
            throw new IllegalArgumentException("searchArray must not be null");
        }
        if (elementComparator == null) {
            throw new IllegalArgumentException("elementComparator must not be null");
        }
        for (int elementIndex = 1; elementIndex < searchArray.length; elementIndex++) {
            T previousElement = searchArray[elementIndex - 1];
            T currentElement = searchArray[elementIndex];
            if (elementComparator.compare(previousElement, currentElement) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if a primitive int array is sorted in ascending order.
     */
    public static boolean isSortedAscending(int[] primitiveArray) {
        if (primitiveArray == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        for (int elementIndex = 1; elementIndex < primitiveArray.length; elementIndex++) {
            if (primitiveArray[elementIndex - 1] > primitiveArray[elementIndex]) {
                return false;
            }
        }
        return true;
    }
}
