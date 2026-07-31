package com.ghana.optimizer.algorithm.search;
/**
 * Binary search over a sorted array. Validates the sorted precondition
 * before searching, since binary search silently gives wrong answers
 * on unsorted input rather than failing loudly - O(log n) time.
 */
public final class BinarySearch {

    private BinarySearch() {
        // utility class - no instances
    }

    /**
     * @param array the array to search - must already be sorted ascending
     * @param target the value to find
     * @return the index of a matching element, or -1 if not found
     * @throws IllegalArgumentException if array is null or not sorted ascending
     */
    public static int search(int[] array, int target) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (!isSortedAscending(array)) {
            throw new IllegalArgumentException(
                "BinarySearch requires the array to be sorted in ascending order");
        }

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2; // avoids overflow vs (low + high) / 2
            if (array[mid] == target) {
                return mid;
            } else if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Checks whether {@code array} is sorted in non-decreasing order.
     * Public so callers can validate up front and handle the "not
     * sorted" case themselves instead of catching an exception.
     */
    public static boolean isSortedAscending(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }
}
