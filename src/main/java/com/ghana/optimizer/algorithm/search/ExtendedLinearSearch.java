package com.ghana.optimizer.algorithm.search;

import com.ghana.optimizer.model.ServiceRequest;


public final class ExtendedLinearSearch {private ExtendedLinearSearch() {
        // utility class - no instances
    }

    /**
     * @param array the array to search (may be unsorted)
     * @param target the value to find
     * @return the index of the first matching element, or -1 if not found
     * @throws IllegalArgumentException if array is null
     */
    public static int search(int[] array, int target) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first request whose priority level equals {@code priorityLevel}.
     * Works on unsorted data - no precondition, unlike binary search.
     *
     * @return index of the first match, or -1 if none found
     * @throws IllegalArgumentException if requests is null
     */
    public static int searchByPriorityLevel(ServiceRequest[] requests, int priorityLevel) {
        if (requests == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        for (int i = 0; i < requests.length; i++) {
            if (requests[i] != null && requests[i].getPriorityLevel() == priorityLevel) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first request whose location id equals {@code locationId}.
     * Works on unsorted data - no precondition, unlike binary search.
     *
     * @return index of the first match, or -1 if none found
     * @throws IllegalArgumentException if requests or locationId is null
     */
    public static int searchByLocationId(ServiceRequest[] requests, String locationId) {
        if (requests == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        if (locationId == null) {
            throw new IllegalArgumentException("locationId must not be null");
        }
        for (int i = 0; i < requests.length; i++) {
            if (requests[i] != null && locationId.equals(requests[i].getLocationId())) {
                return i;
            }
        }
        return -1;
    }
}
