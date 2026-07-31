package com.ghana.optimizer.algorithm.search;

import java.util.Comparator;

/**
 * Binary search only works if its input is already sorted by the same
 * key it searches on - that is its <b>precondition</b>. Give it
 * unsorted data and it does not necessarily throw or crash; it can
 * silently return the wrong index, or -1 for a value that is actually
 * present. That failure mode is worse than a crash because nothing
 * tells you it happened.
 *
 * <p>This class does two jobs:
 * <ol>
 *   <li><b>Validate</b> - check whether an array is sorted with
 *       respect to a given {@link Comparator}, before a caller trusts
 *       it to binary search.</li>
 *   <li><b>Explain</b> - when it is not sorted, produce a concrete
 *       {@link Counterexample}: the exact adjacent pair that breaks
 *       the ordering, and (optionally) a live demonstration of binary
 *       search returning a wrong answer on that data.</li>
 * </ol>
 *
 * <p>It is generic (any {@code T} + {@code Comparator<T>}) so the same
 * class backs both the raw {@code int[]} checks in {@link BinarySearch}
 * and the {@code ServiceRequest[]} multi-attribute searches.
 */
public final class SortedPreconditionValidator {

    private SortedPreconditionValidator() {
        // utility class - no instances
    }

    /**
     * @return true if {@code array} is sorted in non-decreasing order
     *         according to {@code comparator}
     * @throws IllegalArgumentException if array or comparator is null
     */
    public static <T> boolean isSorted(T[] array, Comparator<T> comparator) {
        requireNonNull(array, comparator);
        return firstViolationIndex(array, comparator) == -1;
    }

    /**
     * Precondition guard: call this at the top of any binary search
     * that requires sorted input. Throws with a message that pinpoints
     * exactly where the ordering breaks, instead of a generic
     * "not sorted" message - so the caller can see and fix the real
     * problem immediately.
     *
     * @throws IllegalArgumentException if array/comparator is null, or
     *         if the array is not sorted according to comparator
     */
    public static <T> void validate(T[] array, Comparator<T> comparator) {
        requireNonNull(array, comparator);
        int i = firstViolationIndex(array, comparator);
        if (i != -1) {
            throw new IllegalArgumentException(
                "Precondition violated: array is not sorted ascending. "
                + "First out-of-order pair at indices [" + (i - 1) + ", " + i + "]: "
                + array[i - 1] + " should not come before " + array[i] + ".");
        }
    }

    /**
     * Finds the first adjacent pair that violates ascending order, or
     * returns {@code null} if the array is already sorted. This is the
     * "counterexample generator": rather than just saying *that* the
     * data is unsorted, it points at concrete proof.
     */
    public static <T> Counterexample<T> findCounterexample(T[] array, Comparator<T> comparator) {
        requireNonNull(array, comparator);
        int i = firstViolationIndex(array, comparator);
        if (i == -1) {
            return null;
        }
        return new Counterexample<>(i - 1, i, array[i - 1], array[i]);
    }

    /**
     * Demonstrates - by actually running both algorithms, not just by
     * assertion - why binary search cannot be trusted on unsorted data.
     * Runs {@link LinearSearch} (which has no sorted precondition) and
     * a raw, precondition-free binary search over the same unsorted
     * array for the same target, and reports whether they disagree.
     *
     * <p>Linear search is the "ground truth" here because it checks
     * every element, so it is correct regardless of ordering. If binary
     * search's unchecked result differs from it, that is direct proof
     * of the failure - not merely an appeal to the precondition rule.
     */
    public static <T> FailureDemo<T> demonstrateBinarySearchFailure(
            T[] array, T target, Comparator<T> comparator) {
        requireNonNull(array, comparator);
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }

        int trustedIndex = linearSearch(array, target);
        int uncheckedIndex = binarySearchNoPrecondition(array, target, comparator);
        boolean agree = trustedIndex == uncheckedIndex;

        return new FailureDemo<>(array, target, trustedIndex, uncheckedIndex, agree);
    }

    // ---- internals ----

    private static <T> int firstViolationIndex(T[] array, Comparator<T> comparator) {
        for (int i = 1; i < array.length; i++) {
            if (comparator.compare(array[i - 1], array[i]) > 0) {
                return i;
            }
        }
        return -1;
    }

    private static <T> int linearSearch(T[] array, T target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Deliberately skips the precondition check - this exists only so
     * {@link #demonstrateBinarySearchFailure} can show what "real"
     * binary search (without a guard) does on bad input. Never expose
     * this outside this class; production code should always go
     * through a checked path like {@link BinarySearch}.
     */
    private static <T> int binarySearchNoPrecondition(T[] array, T target, Comparator<T> comparator) {
        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = comparator.compare(array[mid], target);
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

    private static <T> void requireNonNull(T[] array, Comparator<T> comparator) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("comparator must not be null");
        }
    }

    /** A concrete, adjacent out-of-order pair proving an array is not sorted. */
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

    /** Result of a live linear-vs-unchecked-binary-search comparison on unsorted data. */
    public static final class FailureDemo<T> {
        private final T[] array;
        private final T target;
        private final int trustedIndex;
        private final int uncheckedBinaryIndex;
        private final boolean agree;

        FailureDemo(T[] array, T target, int trustedIndex, int uncheckedBinaryIndex, boolean agree) {
            this.array = array;
            this.target = target;
            this.trustedIndex = trustedIndex;
            this.uncheckedBinaryIndex = uncheckedBinaryIndex;
            this.agree = agree;
        }

        /** True if binary search happened to get lucky and agree with linear search anyway. */
        public boolean agree() {
            return agree;
        }

        public int getTrustedIndex() {
            return trustedIndex;
        }

        public int getUncheckedBinaryIndex() {
            return uncheckedBinaryIndex;
        }

        @Override
        public String toString() {
            if (agree) {
                return "Both agree on index " + trustedIndex + " for target " + target
                    + " - but this is luck, not a guarantee, since the array is unsorted.";
            }
            return "MISMATCH for target " + target + " in " + java.util.Arrays.toString(array)
                + ": linear search (always correct) found it at index " + trustedIndex
                + ", but unchecked binary search returned index " + uncheckedBinaryIndex
                + ". This is exactly why BinarySearch must validate its precondition first.";
        }
    }
}
