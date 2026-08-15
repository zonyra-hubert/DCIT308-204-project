package com.ghana.optimizer.algorithm.sort;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Divide-and-Conquer Merge Sort implementation for the University of Ghana
 * Campus Service Operations Optimizer (UG-CSOO).
 *
 * Capabilities:
 *  1. Multi-attribute sorting on DynamicArray<ServiceRequest>:
 *     - Primary Key   : Priority / Urgency (descending: highest urgency 5 -> 1)
 *     - Secondary Key : Budget Required (ascending: lower budget first on tie)
 *  2. Generic array sorting with custom Comparators (T[]).
 *  3. In-place sorting of DynamicArray collections.
 *  4. Strict stability: items with identical keys maintain their original relative order.
 *  5. Asymptotic Complexity: O(N log N) time in best, average, and worst cases; O(N) auxiliary space.
 */
public class MergeSort {

    /**
     * Default Multi-Attribute Comparator for Campus Service Requests:
     * Primary: Urgency / Priority Level descending (5 -> 1)
     * Secondary: Budget Required ascending (cheaper tasks prioritized on tie)
     */
    public static final Comparator<ServiceRequest> DEFAULT_SERVICE_REQUEST_COMPARATOR = (firstRequest, secondRequest) -> {
        if (firstRequest == null && secondRequest == null) return 0;
        if (firstRequest == null) return 1;
        if (secondRequest == null) return -1;

        // Primary: Urgency descending (5 down to 1)
        int priorityComparison = Integer.compare(secondRequest.getPriorityLevel(), firstRequest.getPriorityLevel());
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Secondary: Budget required ascending (lower budget first)
        return Double.compare(firstRequest.getBudgetRequired(), secondRequest.getBudgetRequired());
    };

    /**
     * Sorts a DynamicArray of ServiceRequests in-place using default multi-attribute ordering
     * (Urgency descending, Budget ascending).
     *
     * @param requestList DynamicArray of ServiceRequest items to sort.
     */
    public static void sort(DynamicArray<ServiceRequest> requestList) {
        sort(requestList, DEFAULT_SERVICE_REQUEST_COMPARATOR);
    }

    /**
     * Sorts a DynamicArray of ServiceRequests in-place using a custom comparator.
     *
     * @param requestList DynamicArray of ServiceRequest items.
     * @param comparator  Comparator governing item order.
     */
    public static void sort(DynamicArray<ServiceRequest> requestList, Comparator<ServiceRequest> comparator) {
        if (requestList == null || requestList.size() <= 1) {
            return;
        }

        int totalCount = requestList.size();
        ServiceRequest[] workingArray = new ServiceRequest[totalCount];
        for (int index = 0; index < totalCount; index++) {
            workingArray[index] = requestList.get(index);
        }

        ServiceRequest[] sortedArray = mergeSortArray(workingArray, comparator);

        for (int index = 0; index < totalCount; index++) {
            requestList.set(index, sortedArray[index]);
        }
    }

    /**
     * Generic merge sort on typed arrays with custom Comparator.
     *
     * @param inputArray Array of elements to sort.
     * @param comparator Comparator defining element ordering.
     * @param <T>        Type of array elements.
     * @return New sorted array containing elements in sorted order.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] sort(T[] inputArray, Comparator<T> comparator) {
        if (inputArray == null || inputArray.length <= 1) {
            return inputArray;
        }
        return mergeSortArray(inputArray, comparator);
    }

    /**
     * Recursive Divide-and-Conquer Merge Sort helper.
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] mergeSortArray(T[] arraySlice, Comparator<T> comparator) {
        if (arraySlice.length <= 1) {
            return arraySlice;
        }

        int midpoint = arraySlice.length / 2;
        T[] leftSubarray = Arrays.copyOfRange(arraySlice, 0, midpoint);
        T[] rightSubarray = Arrays.copyOfRange(arraySlice, midpoint, arraySlice.length);

        T[] sortedLeft = mergeSortArray(leftSubarray, comparator);
        T[] sortedRight = mergeSortArray(rightSubarray, comparator);

        return mergeSubarrays(sortedLeft, sortedRight, comparator);
    }

    /**
     * Merges two sorted subarrays stably.
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] mergeSubarrays(T[] leftSubarray, T[] rightSubarray, Comparator<T> comparator) {
        int leftLength = leftSubarray.length;
        int rightLength = rightSubarray.length;
        T[] mergedResult = (T[]) new Object[leftLength + rightLength];

        int leftPointer = 0;
        int rightPointer = 0;
        int resultPointer = 0;

        while (leftPointer < leftLength && rightPointer < rightLength) {
            int comparisonResult = comparator.compare(leftSubarray[leftPointer], rightSubarray[rightPointer]);
            // Stable merge: when equal (comparisonResult <= 0), take left element first
            if (comparisonResult <= 0) {
                mergedResult[resultPointer++] = leftSubarray[leftPointer++];
            } else {
                mergedResult[resultPointer++] = rightSubarray[rightPointer++];
            }
        }

        while (leftPointer < leftLength) {
            mergedResult[resultPointer++] = leftSubarray[leftPointer++];
        }

        while (rightPointer < rightLength) {
            mergedResult[resultPointer++] = rightSubarray[rightPointer++];
        }

        // Copy into properly typed array
        T[] typedResult = (T[]) java.lang.reflect.Array.newInstance(
                leftSubarray.getClass().getComponentType(), leftLength + rightLength);
        System.arraycopy(mergedResult, 0, typedResult, 0, leftLength + rightLength);
        return typedResult;
    }

    // =========================================================================
    // Legacy Task Data Model (Kept for backwards compatibility with test harnesses)
    // =========================================================================

    public static class Task {
        public String name;
        public int priority;
        public int budget;

        public Task(String name, int priority, int budget) {
            this.name = name;
            this.priority = priority;
            this.budget = budget;
        }

        @Override
        public String toString() {
            return name + "(" + priority + "," + budget + ")";
        }
    }

    public static int compare(Task firstTask, Task secondTask) {
        if (firstTask.priority != secondTask.priority) {
            return firstTask.priority - secondTask.priority;
        }
        return firstTask.budget - secondTask.budget;
    }

    public static Task[] sort(Task[] taskArray) {
        if (taskArray == null || taskArray.length <= 1) {
            return taskArray;
        }
        int midpoint = taskArray.length / 2;
        Task[] leftSubarray = Arrays.copyOfRange(taskArray, 0, midpoint);
        Task[] rightSubarray = Arrays.copyOfRange(taskArray, midpoint, taskArray.length);

        Task[] sortedLeft = sort(leftSubarray);
        Task[] sortedRight = sort(rightSubarray);

        return merge(sortedLeft, sortedRight);
    }

    public static Task[] merge(Task[] leftSubarray, Task[] rightSubarray) {
        Task[] result = new Task[leftSubarray.length + rightSubarray.length];
        int leftIndex = 0, rightIndex = 0, targetIndex = 0;

        while (leftIndex < leftSubarray.length && rightIndex < rightSubarray.length) {
            if (compare(leftSubarray[leftIndex], rightSubarray[rightIndex]) <= 0) {
                result[targetIndex++] = leftSubarray[leftIndex++];
            } else {
                result[targetIndex++] = rightSubarray[rightIndex++];
            }
        }
        while (leftIndex < leftSubarray.length) {
            result[targetIndex++] = leftSubarray[leftIndex++];
        }
        while (rightIndex < rightSubarray.length) {
            result[targetIndex++] = rightSubarray[rightIndex++];
        }
        return result;
    }
}
