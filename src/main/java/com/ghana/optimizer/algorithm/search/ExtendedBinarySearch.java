package com.ghana.optimizer.algorithm.search;

import com.ghana.optimizer.model.ServiceRequest;

import java.util.Comparator;

/**
 * Generic Extended Binary Search algorithm for University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 * Validates preconditions via SortedPreconditionValidator and performs O(log n) binary search over generic arrays.
 *
 */
public final class ExtendedBinarySearch {

    private ExtendedBinarySearch() {
        // Utility class - private constructor prevents instantiation
    }

    /**
     * Generic binary search using natural ordering.
     */
    public static <T extends Comparable<T>> int search(T[] searchArray, T targetElement) {
        return search(searchArray, targetElement, Comparator.naturalOrder());
    }

    /**
     * Generic binary search using custom comparator.
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
        SortedPreconditionValidator.validate(searchArray, elementComparator);

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
        return BinarySearch.search(primitiveArray, targetValue);
    }

    /**
     * Verifies if a primitive array is sorted in ascending order.
     */
    public static boolean isSortedAscending(int[] primitiveArray) {
        return BinarySearch.isSortedAscending(primitiveArray);
    }

    private static final Comparator<ServiceRequest> BY_PRIORITY_LEVEL =
            Comparator.comparingInt(ServiceRequest::getPriorityLevel);

    private static final Comparator<ServiceRequest> BY_LOCATION_ID =
            Comparator.comparing(
                    (ServiceRequest requestItem) -> String.valueOf(requestItem.getLocationId()),
                    Comparator.nullsFirst(String::compareTo));

    /**
     * Binary search over requests sorted by priority level.
     */
    public static int searchByPriorityLevel(ServiceRequest[] requestArray, int priorityLevel) {
        if (requestArray == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        SortedPreconditionValidator.validate(requestArray, BY_PRIORITY_LEVEL);

        int lowerBoundaryIndex = 0;
        int upperBoundaryIndex = requestArray.length - 1;

        while (lowerBoundaryIndex <= upperBoundaryIndex) {
            int middleBoundaryIndex = lowerBoundaryIndex + (upperBoundaryIndex - lowerBoundaryIndex) / 2;
            int currentPriorityLevel = requestArray[middleBoundaryIndex].getPriorityLevel();

            if (currentPriorityLevel == priorityLevel) {
                return middleBoundaryIndex;
            } else if (currentPriorityLevel < priorityLevel) {
                lowerBoundaryIndex = middleBoundaryIndex + 1;
            } else {
                upperBoundaryIndex = middleBoundaryIndex - 1;
            }
        }
        return -1;
    }

    /**
     * Binary search over requests sorted by locationId.
     */
    public static int searchByLocationId(ServiceRequest[] requestArray, String targetLocationId) {
        if (requestArray == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        if (targetLocationId == null) {
            throw new IllegalArgumentException("locationId must not be null");
        }
        SortedPreconditionValidator.validate(requestArray, BY_LOCATION_ID);

        int lowerBoundaryIndex = 0;
        int upperBoundaryIndex = requestArray.length - 1;

        while (lowerBoundaryIndex <= upperBoundaryIndex) {
            int middleBoundaryIndex = lowerBoundaryIndex + (upperBoundaryIndex - lowerBoundaryIndex) / 2;
            String currentLocationId = String.valueOf(requestArray[middleBoundaryIndex].getLocationId());
            int comparisonResult = currentLocationId.compareTo(targetLocationId);

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
}
