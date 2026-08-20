package com.ghana.optimizer.algorithm.search;

import java.util.Comparator;

/**
 * Generic Precondition Validator for Binary Search algorithms in UG-CSOO.
 * Validates that arrays are sorted according to a given Comparator before executing binary search.
 *
 */
public final class SortedPreconditionValidator {

    private SortedPreconditionValidator() {
        // Utility class - private constructor prevents instantiation
    }

    /**
     * Checks whether validatedArray is sorted in ascending order according to elementComparator.
     */
    public static <T> boolean isSorted(T[] validatedArray, Comparator<T> elementComparator) {
        requireNonNull(validatedArray, elementComparator);
        return firstViolationIndex(validatedArray, elementComparator) == -1;
    }

    /**
     * Precondition guard: Call at top of binary search methods requiring sorted input.
     * Throws an exception detailing the exact out-of-order pair if validation fails.
     */
    public static <T> void validate(T[] validatedArray, Comparator<T> elementComparator) {
        requireNonNull(validatedArray, elementComparator);
        int violationIndex = firstViolationIndex(validatedArray, elementComparator);
        if (violationIndex != -1) {
            throw new IllegalArgumentException(
                    "Precondition violated: array is not sorted ascending. "
                            + "First out-of-order pair at indices [" + (violationIndex - 1) + ", " + violationIndex + "]: "
                            + validatedArray[violationIndex - 1] + " should not come before " + validatedArray[violationIndex] + ".");
        }
    }

    /**
     * Finds the first adjacent pair that violates ascending order.
     */
    public static <T> Counterexample<T> findCounterexample(T[] validatedArray, Comparator<T> elementComparator) {
        requireNonNull(validatedArray, elementComparator);
        int violationIndex = firstViolationIndex(validatedArray, elementComparator);
        if (violationIndex == -1) {
            return null;
        }
        return new Counterexample<>(violationIndex - 1, violationIndex, validatedArray[violationIndex - 1], validatedArray[violationIndex]);
    }

    /**
     * Demonstrates binary search failure on unsorted input by comparing against linear search.
     */
    public static <T> FailureDemo<T> demonstrateBinarySearchFailure(
            T[] validatedArray, T targetElement, Comparator<T> elementComparator) {
        requireNonNull(validatedArray, elementComparator);
        if (targetElement == null) {
            throw new IllegalArgumentException("targetElement must not be null");
        }

        int trustedLinearIndex = linearSearch(validatedArray, targetElement);
        int uncheckedBinaryIndex = binarySearchNoPrecondition(validatedArray, targetElement, elementComparator);
        boolean searchResultsAgree = (trustedLinearIndex == uncheckedBinaryIndex);

        return new FailureDemo<>(validatedArray, targetElement, trustedLinearIndex, uncheckedBinaryIndex, searchResultsAgree);
    }

    private static <T> int firstViolationIndex(T[] validatedArray, Comparator<T> elementComparator) {
        for (int elementIndex = 1; elementIndex < validatedArray.length; elementIndex++) {
            if (elementComparator.compare(validatedArray[elementIndex - 1], validatedArray[elementIndex]) > 0) {
                return elementIndex;
            }
        }
        return -1;
    }

    private static <T> int linearSearch(T[] validatedArray, T targetElement) {
        for (int elementIndex = 0; elementIndex < validatedArray.length; elementIndex++) {
            if (validatedArray[elementIndex].equals(targetElement)) {
                return elementIndex;
            }
        }
        return -1;
    }

    private static <T> int binarySearchNoPrecondition(T[] validatedArray, T targetElement, Comparator<T> elementComparator) {
        int lowerBoundaryIndex = 0;
        int upperBoundaryIndex = validatedArray.length - 1;
        while (lowerBoundaryIndex <= upperBoundaryIndex) {
            int middleBoundaryIndex = lowerBoundaryIndex + (upperBoundaryIndex - lowerBoundaryIndex) / 2;
            int comparisonResult = elementComparator.compare(validatedArray[middleBoundaryIndex], targetElement);
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

    private static <T> void requireNonNull(T[] validatedArray, Comparator<T> elementComparator) {
        if (validatedArray == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (elementComparator == null) {
            throw new IllegalArgumentException("comparator must not be null");
        }
    }

    public static final class Counterexample<T> {
        private final int firstIndex;
        private final int secondIndex;
        private final T firstValue;
        private final T secondValue;

        Counterexample(int firstIndex, int secondIndex, T firstValue, T secondValue) {
            this.firstIndex = firstIndex;
            this.secondIndex = secondIndex;
            this.firstValue = firstValue;
            this.secondValue = secondValue;
        }

        public int getFirstIndex() {
            return firstIndex;
        }

        public int getSecondIndex() {
            return secondIndex;
        }

        public T getFirstValue() {
            return firstValue;
        }

        public T getSecondValue() {
            return secondValue;
        }

        @Override
        public String toString() {
            return "index " + firstIndex + " (" + firstValue + ") comes before index "
                    + secondIndex + " (" + secondValue + "), which breaks ascending order";
        }
    }

    public static final class FailureDemo<T> {
        private final T[] validatedArray;
        private final T targetElement;
        private final int trustedIndex;
        private final int uncheckedBinaryIndex;
        private final boolean searchResultsAgree;

        FailureDemo(T[] validatedArray, T targetElement, int trustedIndex, int uncheckedBinaryIndex, boolean searchResultsAgree) {
            this.validatedArray = validatedArray;
            this.targetElement = targetElement;
            this.trustedIndex = trustedIndex;
            this.uncheckedBinaryIndex = uncheckedBinaryIndex;
            this.searchResultsAgree = searchResultsAgree;
        }

        public boolean agree() {
            return searchResultsAgree;
        }

        public int getTrustedIndex() {
            return trustedIndex;
        }

        public int getUncheckedBinaryIndex() {
            return uncheckedBinaryIndex;
        }

        @Override
        public String toString() {
            if (searchResultsAgree) {
                return "Both agree on index " + trustedIndex + " for target " + targetElement
                        + " - but this is luck, not a guarantee, since the array is unsorted.";
            }
            return "MISMATCH for target " + targetElement + " in " + java.util.Arrays.toString(validatedArray)
                    + ": linear search (always correct) found it at index " + trustedIndex
                    + ", but unchecked binary search returned index " + uncheckedBinaryIndex
                    + ". This is exactly why BinarySearch must validate its precondition first.";
        }
    }
}
