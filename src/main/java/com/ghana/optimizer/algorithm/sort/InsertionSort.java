package com.ghana.optimizer.algorithm.sort;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

/**
 * Insertion Sort — stable, in-place, from scratch (no Arrays.sort /
 * Collections.sort / .stream().sorted() anywhere in this class).
 *
 * Matches SelectionSort's shape exactly: operates directly on
 * DynamicArray<ServiceRequest>, sorts descending by urgency (highest
 * priority first), and returns/prints a pass-by-pass trace table.
 *
 * ---------------------------------------------------------------------
 * WHICH FIELD / ORDER: same convention as SelectionSort — urgency
 * (1-5) as the priority-level proxy, descending so the most urgent
 * request ends up at index 0.
 * ---------------------------------------------------------------------
 *
 * Complexity
 *   Time  : best  O(n)      input already sorted -> inner while never runs
 *           worst/avg O(n^2)  reverse-sorted / random input
 *   Space : O(1) extra — in-place; only 'key' and 'j' beyond the array
 *
 * Stability
 *   An element is shifted only when it is STRICTLY less urgent than the
 *   key (i.e. its urgency is less than key's urgency, since we sort
 *   descending). Equal-urgency requests are never shifted past one
 *   another, so they keep their original relative order.
 *
 * Note: the original version of this file was generic (T[] + Comparator)
 * with a reusable [low..high] partition overload, for potential reuse as
 * MergeSort/QuickSort's small-partition base case. Standardizing on
 * SelectionSort's DynamicArray<ServiceRequest> shape drops both of those
 * — flag it to the team if MergeSort/QuickSort end up wanting a shared
 * generic sort utility later.
 */
public class InsertionSort {

    /**
     * Sorts `data` in place, descending by urgency (highest priority
     * first), and returns a human-readable trace table showing exactly
     * what happened on each outer-loop pass: which key was being
     * inserted, how many elements it shifted past, and where it landed.
     * Also prints the table and the final comparison/shift totals.
     *
     * @param data the DynamicArray to sort in place
     * @return a formatted multi-line trace table (also printed to console)
     */
    public static String insertionSort(DynamicArray<ServiceRequest> data) {
        int n = data.size();
        int comparisons = 0;
        int shifts = 0;

        StringBuilder trace = new StringBuilder();
        trace.append(String.format("%-4s %-10s %-14s %-10s %-10s%n",
                "Pass", "Key(Id)", "KeyUrgency", "Shifts", "Insert@"));
        trace.append("-".repeat(56)).append(System.lineSeparator());

        for (int i = 1; i < n; i++) {
            ServiceRequest key = data.get(i);
            int keyId = key.getRequestId();
            int keyUrgency = key.getUrgency();
            int j = i - 1;
            int passShifts = 0;

            while (j >= 0) {
                comparisons++; // one comparison per element inspected while shifting
                if (data.get(j).getUrgency() < keyUrgency) {
                    data.set(j + 1, data.get(j));
                    j--;
                    passShifts++;
                } else {
                    break;
                }
            }
            data.set(j + 1, key);
            shifts += passShifts;

            trace.append(String.format("%-4d %-10d %-14d %-10d %-10d%n",
                    i, keyId, keyUrgency, passShifts, j + 1));
        }

        trace.append("-".repeat(56)).append(System.lineSeparator());
        trace.append("Total comparisons: ").append(comparisons)
                .append(" (best case n-1 = ").append(n - 1)
                .append(", worst case n(n-1)/2 = ").append(n * (n - 1) / 2).append(")")
                .append(System.lineSeparator());
        trace.append("Total shifts: ").append(shifts).append(System.lineSeparator());

        String result = trace.toString();
        System.out.println(result);
        return result;
    }
}