package com.ghana.optimizer.algorithm.search;

/**
 * Interpolation Search
 * -----------------------------------------------------------------------
 * UG-CSOO System — Search Algorithms Module
 *
 * Rationale for use in this system:
 *   Service ticket IDs (service_requests.id) and budget values
 *   (service_requests.budget_required) are effectively uniformly
 *   distributed over their ranges once sorted — ticket IDs are
 *   sequential/near-sequential integers, and budgets are drawn from a
 *   roughly even spread bounded by the operational constraint of
 *   GHS 1,089.00. Interpolation search exploits this uniformity to
 *   achieve an average-case time complexity of O(log log n), compared
 *   to binary search's guaranteed O(log n), by "guessing" a probe
 *   position proportional to where the target should sit in the value
 *   range rather than always checking the midpoint.
 *
 * Notes:
 *   - No java.util collection types are used (arrays only), consistent
 *     with the rest of the UG-CSOO codebase.
 *   - Worst case degrades to O(n) on non-uniform / skewed data — the
 *     accompanying benchmark demonstrates this trade-off directly
 *     against binary search.
 * -----------------------------------------------------------------------
 */
public final class InterpolationSearch {

    private InterpolationSearch() {
        // static utility class — no instances
    }

    /**
     * Searches a sorted (ascending) array of ticket IDs.
     *
     * @param sortedIds ascending-sorted array of IDs (e.g. service_requests.id)
     * @param targetId  ID to locate
     * @return index of targetId in sortedIds, or -1 if not found
     */
    public static int search(long[] sortedIds, long targetId) {
        if (sortedIds == null || sortedIds.length == 0) {
            return -1;
        }

        int low = 0;
        int high = sortedIds.length - 1;

        while (low <= high && targetId >= sortedIds[low] && targetId <= sortedIds[high]) {

            if (low == high) {
                return sortedIds[low] == targetId ? low : -1;
            }

            long range = sortedIds[high] - sortedIds[low];
            if (range == 0) {
                return sortedIds[low] == targetId ? low : -1;
            }

            // Interpolation probe: estimate position proportionally to value
            int pos = low + (int) (((double) (targetId - sortedIds[low]) * (high - low)) / range);

            // Defensive clamp — guards against floating point drift at the
            // edges of the search window
            if (pos < low) pos = low;
            if (pos > high) pos = high;

            if (sortedIds[pos] == targetId) {
                return pos;
            } else if (sortedIds[pos] < targetId) {
                low = pos + 1;
            } else {
                high = pos - 1;
            }
        }

        return -1;
    }

    /**
     * Searches a sorted (ascending) array of budget values.
     * Budgets are doubles (currency amounts), so exact equality is
     * checked within a small epsilon tolerance to avoid floating point
     * comparison errors.
     *
     * @param sortedBudgets ascending-sorted array of budget_required values
     * @param targetBudget  budget value to locate
     * @param epsilon       tolerance for floating point equality (e.g. 0.001)
     * @return index of targetBudget in sortedBudgets, or -1 if not found
     */
    public static int search(double[] sortedBudgets, double targetBudget, double epsilon) {
        if (sortedBudgets == null || sortedBudgets.length == 0) {
            return -1;
        }

        int low = 0;
        int high = sortedBudgets.length - 1;

        while (low <= high
                && targetBudget >= sortedBudgets[low] - epsilon
                && targetBudget <= sortedBudgets[high] + epsilon) {

            if (low == high) {
                return Math.abs(sortedBudgets[low] - targetBudget) <= epsilon ? low : -1;
            }

            double range = sortedBudgets[high] - sortedBudgets[low];
            if (range <= 0) {
                return Math.abs(sortedBudgets[low] - targetBudget) <= epsilon ? low : -1;
            }

            int pos = low + (int) (((targetBudget - sortedBudgets[low]) * (high - low)) / range);

            if (pos < low) pos = low;
            if (pos > high) pos = high;

            double diff = sortedBudgets[pos] - targetBudget;
            if (Math.abs(diff) <= epsilon) {
                return pos;
            } else if (diff < 0) {
                low = pos + 1;
            } else {
                high = pos - 1;
            }
        }

        return -1;
    }
}
