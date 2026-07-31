package com.ghana.optimizer.algorithm.search;

/**
 * Exponential search over a sorted array — O(log n). Designed for
 * unbounded/growing lists where you don't want to assume you know
 * the array's length up front: it finds a bracketing range by
 * doubling an index (1, 2, 4, 8, ...), then delegates the final
 * narrow-down to BinarySearch.search() within that range.
 */
public final class ExponentialSearch {

    private ExponentialSearch() {
        // utility class - no instances
    }

    public static int search(int[] array, int target) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (!BinarySearch.isSortedAscending(array)) {
            throw new IllegalArgumentException(
                "ExponentialSearch requires the array to be sorted in ascending order");
        }
        if (array.length == 0) {
            return -1;
        }

        RangeResult range = findRange(array, target);

        int[] window = java.util.Arrays.copyOfRange(array, range.low, range.high + 1);
        int localIndex = BinarySearch.search(window, target);
        return localIndex == -1 ? -1 : range.low + localIndex;
    }

    public static RangeResult findRange(int[] array, int target) {
        if (array[0] == target) {
            return new RangeResult(0, 0);
        }

        int bound = 1;
        while (bound < array.length && array[bound] < target) {
            bound *= 2;
        }

        int low = bound / 2;
        int high = Math.min(bound, array.length - 1);
        return new RangeResult(low, high);
    }

    public static final class RangeResult {
        public final int low;
        public final int high;

        public RangeResult(int low, int high) {
            this.low = low;
            this.high = high;
        }
    }
}