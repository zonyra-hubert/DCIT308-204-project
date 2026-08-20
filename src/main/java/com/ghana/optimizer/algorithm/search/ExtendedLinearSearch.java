package com.ghana.optimizer.algorithm.search;

import com.ghana.optimizer.model.ServiceRequest;

import java.util.function.Function;

/**
 * Generic Extended Linear Search algorithm for University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 * Operates over generic arrays and domain objects.
 *
 */
public final class ExtendedLinearSearch {

    private ExtendedLinearSearch() {
        // Utility class - private constructor prevents instantiation
    }

    /**
     * Generic linear search over an array of elements.
     *
     * @param searchArray Array of elements to search through.
     * @param targetElement Value being searched for.
     * @param <T> Element type.
     * @return Index of matching element, or -1 if not found.
     */
    public static <T> int search(T[] searchArray, T targetElement) {
        if (searchArray == null) {
            throw new IllegalArgumentException("searchArray must not be null");
        }
        for (int elementIndex = 0; elementIndex < searchArray.length; elementIndex++) {
            T currentElement = searchArray[elementIndex];
            if (currentElement == targetElement || (currentElement != null && currentElement.equals(targetElement))) {
                return elementIndex;
            }
        }
        return -1;
    }

    /**
     * Generic linear search by extracted property value over an array.
     */
    public static <T, V> int searchByProperty(T[] searchArray, Function<T, V> propertyExtractorFunction, V targetPropertyValue) {
        if (searchArray == null) {
            throw new IllegalArgumentException("searchArray must not be null");
        }
        if (propertyExtractorFunction == null) {
            throw new IllegalArgumentException("propertyExtractorFunction must not be null");
        }
        for (int elementIndex = 0; elementIndex < searchArray.length; elementIndex++) {
            T currentElement = searchArray[elementIndex];
            if (currentElement != null) {
                V extractedPropertyValue = propertyExtractorFunction.apply(currentElement);
                if (extractedPropertyValue == targetPropertyValue ||
                        (extractedPropertyValue != null && extractedPropertyValue.equals(targetPropertyValue))) {
                    return elementIndex;
                }
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
        for (int elementIndex = 0; elementIndex < primitiveArray.length; elementIndex++) {
            if (primitiveArray[elementIndex] == targetValue) {
                return elementIndex;
            }
        }
        return -1;
    }

    /**
     * Finds the first request whose priority level equals priorityLevel.
     */
    public static int searchByPriorityLevel(ServiceRequest[] requestArray, int priorityLevel) {
        return searchByProperty(requestArray, ServiceRequest::getPriorityLevel, priorityLevel);
    }

    /**
     * Finds the first request whose location id equals locationId.
     */
    public static int searchByLocationId(ServiceRequest[] requestArray, String locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("locationId must not be null");
        }
        return searchByProperty(requestArray, (requestItem) -> String.valueOf(requestItem.getLocationId()), locationId);
    }
}
