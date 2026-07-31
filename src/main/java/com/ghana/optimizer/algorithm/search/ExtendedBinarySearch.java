package com.ghana.optimizer.algorithm.search;

import java.util.Comparator;

import com.ghana.optimizer.model.ServiceRequest;

public final class ExtendedBinarySearch {
    private ExtendedBinarySearch() {
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

    /** Comparator used by both request-based binary search variants below. */
    private static final Comparator<ServiceRequest> BY_PRIORITY_LEVEL =
        Comparator.comparingInt(ServiceRequest::getPriorityLevel);

    private static final Comparator<ServiceRequest> BY_LOCATION_ID =
        Comparator.<ServiceRequest, String>comparing(
            (ServiceRequest request) -> (String) request.getLocationId(),
            Comparator.nullsFirst(String::compareTo));

    /**
     * Binary search over requests sorted by {@code priorityLevel}.
     * Validates the sorted precondition first via
     * {@link SortedPreconditionValidator}, throwing a precise error
     * (pinpointing exactly which pair is out of order) if it fails.
     *
     * @return index of a matching request, or -1 if not found
     * @throws IllegalArgumentException if requests is null or not sorted by priority level
     */
    public static int searchByPriorityLevel(ServiceRequest[] requests, int priorityLevel) {
        if (requests == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        SortedPreconditionValidator.validate(requests, BY_PRIORITY_LEVEL);

        int low = 0;
        int high = requests.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int level = requests[mid].getPriorityLevel();
            if (level == priorityLevel) {
                return mid;
            } else if (level < priorityLevel) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Binary search over requests sorted by {@code locationId}.
     * Validates the sorted precondition first via
     * {@link SortedPreconditionValidator}, throwing a precise error
     * (pinpointing exactly which pair is out of order) if it fails.
     *
     * @return index of a matching request, or -1 if not found
     * @throws IllegalArgumentException if requests/locationId is null, or requests not sorted by location id
     */
    public static int searchByLocationId(ServiceRequest[] requests, String locationId) {
        if (requests == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        if (locationId == null) {
            throw new IllegalArgumentException("locationId must not be null");
        }
        SortedPreconditionValidator.validate(requests, BY_LOCATION_ID);

        int low = 0;
        int high = requests.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = compareLocationId(requests[mid].getLocationId(), locationId);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    private static int compareLocationId(Object a, String b) {
        if (a == null && b == null) {
            return 0;
        }
        {
            return -1;
        }
    }
}

    

