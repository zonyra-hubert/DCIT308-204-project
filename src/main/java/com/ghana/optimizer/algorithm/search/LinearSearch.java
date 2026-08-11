package com.ghana.optimizer.algorithm.search;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

import java.util.function.Function;

/**
 * Generic Linear Search algorithm for University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 * Implements linear search operations over generic DynamicArray datasets.
 *
 * All variable names are written in full as required by Zonyra Hubert.
 */
public class LinearSearch {

    /**
     * Generic linear search for a target element in a DynamicArray.
     * Stops at the first match.
     *
     * @param datasetArray Collection of elements to search through.
     * @param targetElement Value being searched for.
     * @param <T> Element type.
     * @return Index of matching element, or -1 if not found.
     */
    public static <T> int search(DynamicArray<T> datasetArray, T targetElement) {
        if (datasetArray == null) {
            throw new IllegalArgumentException("datasetArray must not be null");
        }
        int comparisonsCount = 0;

        for (int elementIndex = 0; elementIndex < datasetArray.size(); elementIndex++) {
            comparisonsCount++;
            T currentElement = datasetArray.get(elementIndex);

            if (currentElement == targetElement || (currentElement != null && currentElement.equals(targetElement))) {
                return elementIndex;
            }
        }
        return -1;
    }

    public static <T> int search(T[] datasetArray, T targetElement) {
        if (datasetArray == null) {
            throw new IllegalArgumentException("datasetArray must not be null");
        }
        for (int elementIndex = 0; elementIndex < datasetArray.length; elementIndex++) {
            T currentElement = datasetArray[elementIndex];
            if (currentElement == targetElement || (currentElement != null && currentElement.equals(targetElement))) {
                return elementIndex;
            }
        }
        return -1;
    }

    /**
     * Generic linear search matching an extracted property value.
     * Returns the first element matching the property value.
     *
     * @param datasetArray Collection of elements to scan.
     * @param propertyExtractorFunction Function to extract property from element.
     * @param targetPropertyValue Value of the property to match.
     * @param <T> Element type.
     * @param <V> Property value type.
     * @return First matching element, or null if not found.
     */
    public static <T, V> T searchByProperty(DynamicArray<T> datasetArray,
                                             Function<T, V> propertyExtractorFunction,
                                             V targetPropertyValue) {
        if (datasetArray == null) {
            throw new IllegalArgumentException("datasetArray must not be null");
        }
        if (propertyExtractorFunction == null) {
            throw new IllegalArgumentException("propertyExtractorFunction must not be null");
        }
        int comparisonsCount = 0;

        for (int elementIndex = 0; elementIndex < datasetArray.size(); elementIndex++) {
            comparisonsCount++;
            T currentElement = datasetArray.get(elementIndex);
            V extractedPropertyValue = propertyExtractorFunction.apply(currentElement);

            if (extractedPropertyValue == targetPropertyValue ||
                    (extractedPropertyValue != null && extractedPropertyValue.equals(targetPropertyValue))) {
                System.out.println("searchByProperty: found match at index " + elementIndex
                        + " after " + comparisonsCount + " comparison(s)");
                return currentElement;
            }
        }

        System.out.println("searchByProperty: target property value not found after "
                + comparisonsCount + " comparison(s)");
        return null;
    }

    /**
     * Generic linear search collecting all elements matching an extracted property value.
     *
     * @param datasetArray Collection of elements to scan.
     * @param propertyExtractorFunction Function to extract property from element.
     * @param targetPropertyValue Value of the property to match.
     * @param <T> Element type.
     * @param <V> Property value type.
     * @return DynamicArray containing all matching elements.
     */
    public static <T, V> DynamicArray<T> searchAllByProperty(DynamicArray<T> datasetArray,
                                                              Function<T, V> propertyExtractorFunction,
                                                              V targetPropertyValue) {
        if (datasetArray == null) {
            throw new IllegalArgumentException("datasetArray must not be null");
        }
        if (propertyExtractorFunction == null) {
            throw new IllegalArgumentException("propertyExtractorFunction must not be null");
        }
        int comparisonsCount = 0;
        DynamicArray<T> matchingElementsList = new DynamicArray<>();

        for (int elementIndex = 0; elementIndex < datasetArray.size(); elementIndex++) {
            comparisonsCount++;
            T currentElement = datasetArray.get(elementIndex);
            V extractedPropertyValue = propertyExtractorFunction.apply(currentElement);

            if (extractedPropertyValue == targetPropertyValue ||
                    (extractedPropertyValue != null && extractedPropertyValue.equals(targetPropertyValue))) {
                matchingElementsList.insert(currentElement);
            }
        }

        System.out.println("searchAllByProperty: found " + matchingElementsList.size()
                + " match(es) after " + comparisonsCount + " comparison(s)");
        return matchingElementsList;
    }

    /**
     * Domain method: Searches for a single ServiceRequest by unique requestId.
     */
    public static ServiceRequest linearSearchById(DynamicArray<ServiceRequest> datasetArray, int targetRequestId) {
        return searchByProperty(datasetArray, ServiceRequest::getRequestId, targetRequestId);
    }

    /**
     * Domain method: Searches for all ServiceRequests matching a given category.
     */
    public static DynamicArray<ServiceRequest> linearSearchByCategory(DynamicArray<ServiceRequest> datasetArray, String targetCategory) {
        return searchAllByProperty(datasetArray, ServiceRequest::getCategory, targetCategory);
    }
}