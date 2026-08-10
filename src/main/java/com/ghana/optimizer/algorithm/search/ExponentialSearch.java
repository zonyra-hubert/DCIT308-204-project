package com.ghana.optimizer.algorithm.search;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Generic Exponential Search algorithm for University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 * Finds range by doubling index boundaries, then narrows down using generic BinarySearch.
 *
 * All variable names are written in full as required by Zonyra Hubert.
 */
public final class ExponentialSearch {

    private ExponentialSearch() {
        // Utility class - private constructor prevents instantiation
    }

    /**
     * Generic exponential search using natural ordering.
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
     * Generic exponential search using a custom Comparator.
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
        if (!BinarySearch.isSortedAscending(searchArray, elementComparator)) {
            throw new IllegalArgumentException("ExponentialSearch requires the array to be sorted in ascending order");
        }
        if (searchArray.length == 0) {
            return -1;
        }

        RangeBoundaryResult rangeBoundary = findRangeBoundary(searchArray, targetElement, elementComparator);

        T[] searchWindowArray = Arrays.copyOfRange(searchArray, rangeBoundary.getLowerBoundaryIndex(), rangeBoundary.getUpperBoundaryIndex() + 1);
        int localWindowIndex = BinarySearch.search(searchWindowArray, targetElement, elementComparator);
        return localWindowIndex == -1 ? -1 : rangeBoundary.getLowerBoundaryIndex() + localWindowIndex;
    }

    /**
     * Finds range boundary for generic exponential search.
     */
    public static <T> RangeBoundaryResult findRangeBoundary(T[] searchArray, T targetElement, Comparator<T> elementComparator) {
        if (elementComparator.compare(searchArray[0], targetElement) == 0) {
            return new RangeBoundaryResult(0, 0);
        }

        int boundaryIndex = 1;
        while (boundaryIndex < searchArray.length &&
                elementComparator.compare(searchArray[boundaryIndex], targetElement) < 0) {
            boundaryIndex *= 2;
        }

        int lowerBoundaryIndex = boundaryIndex / 2;
        int upperBoundaryIndex = Math.min(boundaryIndex, searchArray.length - 1);
        return new RangeBoundaryResult(lowerBoundaryIndex, upperBoundaryIndex);
    }

    /**
     * Primitive int[] overload for backward compatibility.
     */
    public static int search(int[] primitiveArray, int targetValue) {
        if (primitiveArray == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (!BinarySearch.isSortedAscending(primitiveArray)) {
            throw new IllegalArgumentException("ExponentialSearch requires the array to be sorted in ascending order");
        }
        if (primitiveArray.length == 0) {
            return -1;
        }

        RangeBoundaryResult rangeBoundary = findPrimitiveRangeBoundary(primitiveArray, targetValue);

        int[] primitiveWindowArray = Arrays.copyOfRange(primitiveArray, rangeBoundary.getLowerBoundaryIndex(), rangeBoundary.getUpperBoundaryIndex() + 1);
        int localWindowIndex = BinarySearch.search(primitiveWindowArray, targetValue);
        return localWindowIndex == -1 ? -1 : rangeBoundary.getLowerBoundaryIndex() + localWindowIndex;
    }

    private static RangeBoundaryResult findPrimitiveRangeBoundary(int[] primitiveArray, int targetValue) {
        if (primitiveArray[0] == targetValue) {
            return new RangeBoundaryResult(0, 0);
        }

        int boundaryIndex = 1;
        while (boundaryIndex < primitiveArray.length && primitiveArray[boundaryIndex] < targetValue) {
            boundaryIndex *= 2;
        }

        int lowerBoundaryIndex = boundaryIndex / 2;
        int upperBoundaryIndex = Math.min(boundaryIndex, primitiveArray.length - 1);
        return new RangeBoundaryResult(lowerBoundaryIndex, upperBoundaryIndex);
    }

    /**
     * Class representing range boundaries.
     */
    public static final class RangeBoundaryResult {
        private final int lowerBoundaryIndex;
        private final int upperBoundaryIndex;

        public RangeBoundaryResult(int lowerBoundaryIndex, int upperBoundaryIndex) {
            this.lowerBoundaryIndex = lowerBoundaryIndex;
            this.upperBoundaryIndex = upperBoundaryIndex;
        }

        public int getLowerBoundaryIndex() {
            return lowerBoundaryIndex;
        }

        public int getUpperBoundaryIndex() {
            return upperBoundaryIndex;
        }
    }
}