package com.ghana.optimizer.algorithm.sort;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

/**
 * Selection Sort — Wisdom Nunakpor (@wnunakpor001)
 *
 * Sorts service requests by priority level, from scratch, in place, over
 * a DynamicArray. No built-in sort (Collections.sort, Arrays.sort,
 * .stream().sorted(), etc.) is used anywhere in this class.
 *
 * ---------------------------------------------------------------------
 * WHICH FIELD: urgency, standing in for "priority level"
 * ---------------------------------------------------------------------
 * The task board says "sorts requests by budget required or priority
 * level." ServiceRequest has no `budget` field, so this sorts on the
 * existing `urgency` field (1-5) as the priority-level proxy, rather
 * than adding a new field that every other file already built against
 * ServiceRequest's current constructor would need to account for.
 *
 * ---------------------------------------------------------------------
 * ORDER: descending (highest urgency first)
 * ---------------------------------------------------------------------
 * Sorted so the most urgent request ends up at index 0 — the operationally
 * useful order for "which request should we look at first," rather than
 * an arbitrary ascending sort.
 */


public class SelectionSort {

    /**
     * Sorts `data` in place, descending by urgency (highest priority
     * first), and returns a human-readable trace table showing exactly
     * what happened on each outer-loop pass: which position was being
     * filled, which element was selected, and whether a swap occurred.
     * Also prints the table and the final comparison/swap totals.
     *
     * @param data the DynamicArray to sort in place
     * @return a formatted multi-line trace table (also printed to console)
     */
    public static String selectionSort(DynamicArray<ServiceRequest> data) {
        int n = data.size();
        int comparisons = 0;
        int swaps = 0;

        StringBuilder trace = new StringBuilder();
        trace.append(String.format("%-4s %-8s %-14s %-18s %-18s %-8s%n",
                "Pass", "Fill@i", "SelectedIdx", "Id@i (before)", "Id@Selected (before)", "Swapped?"));
        trace.append("-".repeat(78)).append(System.lineSeparator());

        for (int i = 0; i < n - 1; i++) {
            int selectedIndex = i;

            for (int j = i + 1; j < n; j++) {
                comparisons++; // one comparison per element inspected in the unsorted remainder
                if (data.get(j).getUrgency() > data.get(selectedIndex).getUrgency()) {
                    selectedIndex = j;
                }
            }

            int idAtI = data.get(i).getRequestId();
            int idAtSelected = data.get(selectedIndex).getRequestId();
            boolean swapped = selectedIndex != i;

            if (swapped) {
                swap(data, i, selectedIndex);
                swaps++;
            }

            trace.append(String.format("%-4d %-8d %-14d %-18d %-18d %-8s%n",
                    i, i, selectedIndex, idAtI, idAtSelected, swapped ? "YES" : "no"));
        }

        trace.append("-".repeat(78)).append(System.lineSeparator());
        trace.append("Total comparisons: ").append(comparisons)
                .append(" (expected n(n-1)/2 = ").append(n * (n - 1) / 2).append(")")
                .append(System.lineSeparator());
        trace.append("Total swaps: ").append(swaps).append(" (max possible: ").append(n - 1).append(")")
                .append(System.lineSeparator());

        String result = trace.toString();
        if (n <= 20) {
            System.out.println(result);
        }
        return result;
    }

    public static String selectionSort(DynamicArray<ServiceRequest> data, boolean printTrace) {
        int n = data.size();
        int comparisons = 0;
        int swaps = 0;

        StringBuilder trace = new StringBuilder();
        if (printTrace) {
            trace.append(String.format("%-4s %-8s %-14s %-18s %-18s %-8s%n",
                    "Pass", "Fill@i", "SelectedIdx", "Id@i (before)", "Id@Selected (before)", "Swapped?"));
            trace.append("-".repeat(78)).append(System.lineSeparator());
        }

        for (int i = 0; i < n - 1; i++) {
            int selectedIndex = i;

            for (int j = i + 1; j < n; j++) {
                comparisons++;
                if (data.get(j).getUrgency() > data.get(selectedIndex).getUrgency()) {
                    selectedIndex = j;
                }
            }

            int idAtI = data.get(i).getRequestId();
            int idAtSelected = data.get(selectedIndex).getRequestId();
            boolean swapped = selectedIndex != i;

            if (swapped) {
                swap(data, i, selectedIndex);
                swaps++;
            }

            if (printTrace) {
                trace.append(String.format("%-4d %-8d %-14d %-18d %-18d %-8s%n",
                        i, i, selectedIndex, idAtI, idAtSelected, swapped ? "YES" : "no"));
            }
        }

        if (printTrace) {
            trace.append("-".repeat(78)).append(System.lineSeparator());
            trace.append("Total comparisons: ").append(comparisons)
                    .append(" (expected n(n-1)/2 = ").append(n * (n - 1) / 2).append(")")
                    .append(System.lineSeparator());
            trace.append("Total swaps: ").append(swaps).append(" (max possible: ").append(n - 1).append(")")
                    .append(System.lineSeparator());
            String result = trace.toString();
            System.out.println(result);
            return result;
        }
        return trace.toString();
    }

    private static void swap(DynamicArray<ServiceRequest> data, int i, int j) {
        ServiceRequest temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }
}
