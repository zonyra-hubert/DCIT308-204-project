package com.ghana.optimizer.algorithm.sort;
 
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;
 

public class QuickSort {
 
    /**
     * Sorts `data` in place, descending by urgency (highest priority
     * first), and returns a human-readable trace table showing exactly
     * what happened on each partition call: the subrange being
     * partitioned, the pivot chosen, where it finally landed, and how
     * many swaps that call performed. Also prints the table and the
     * final comparison/swap totals.
     *
     * @param data the DynamicArray to sort in place
     * @return a formatted multi-line trace table (also printed to console)
     */
    public static String quickSort(DynamicArray<ServiceRequest> data) {
        int n = data.size();
 
        StringBuilder trace = new StringBuilder();
        trace.append(String.format("%-5s %-12s %-10s %-14s %-14s %-8s%n",
                "Call", "Range[lo,hi]", "PivotId", "PivotUrgency", "FinalPivotIdx", "Swaps"));
        trace.append("-".repeat(70)).append(System.lineSeparator());
 
        Counters counters = new Counters();
        quickSortHelper(data, 0, n - 1, trace, counters);
 
        trace.append("-".repeat(70)).append(System.lineSeparator());
        trace.append("Total comparisons: ").append(counters.comparisons)
                .append(" (best/avg n log n, worst n(n-1)/2 = ").append(n * (n - 1) / 2).append(")")
                .append(System.lineSeparator());
        trace.append("Total swaps: ").append(counters.swaps)
                .append(" (includes the final pivot-into-place swap on each partition call)")
                .append(System.lineSeparator());
        trace.append("Partition calls: ").append(counters.calls).append(System.lineSeparator());
 
        String result = trace.toString();
        System.out.println(result);
        return result;
    }
 
    /**
     * Recursively sorts data[lo..hi] in place. Base case: a range of
     * zero or one elements is trivially sorted, so lo >= hi does nothing.
     */
    private static void quickSortHelper(DynamicArray<ServiceRequest> data, int lo, int hi,
                                         StringBuilder trace, Counters counters) {
        if (lo >= hi) {
            return;
        }
 
        int pivotIndex = partition(data, lo, hi, trace, counters);
 
        quickSortHelper(data, lo, pivotIndex - 1, trace, counters);
        quickSortHelper(data, pivotIndex + 1, hi, trace, counters);
    }
 
    /**
     * Lomuto partition, descending by urgency: everything strictly more
     * urgent than the pivot ends up to its left, everything else to its
     * right, and the pivot lands at its final sorted position.
     *
     * @return the index the pivot ended up at
     */
    private static int partition(DynamicArray<ServiceRequest> data, int lo, int hi,
                                  StringBuilder trace, Counters counters) {
        ServiceRequest pivot = data.get(hi);
        int pivotId = pivot.getRequestId();
        int pivotUrgency = pivot.getUrgency();
 
        int callSwaps = 0;
        int i = lo - 1; // boundary of the "more urgent than pivot" region
 
        for (int j = lo; j < hi; j++) {
            counters.comparisons++; // one comparison per element inspected in [lo, hi)
            if (data.get(j).getUrgency() > pivotUrgency) {
                i++;
                if (i != j) {
                    swap(data, i, j);
                    counters.swaps++;
                    callSwaps++;
                }
            }
        }
 
        swap(data, i + 1, hi); // move pivot into its final position
        counters.swaps++;
        callSwaps++;
 
        int finalPivotIndex = i + 1;
        counters.calls++;
        trace.append(String.format("%-5d %-12s %-10d %-14d %-14d %-8d%n",
                counters.calls, "[" + lo + "," + hi + "]", pivotId, pivotUrgency, finalPivotIndex, callSwaps));
 
        return finalPivotIndex;
    }
 
    private static void swap(DynamicArray<ServiceRequest> data, int i, int j) {
        ServiceRequest temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }
 
    /** Mutable running totals threaded through the recursive calls. */
    private static class Counters {
        int comparisons = 0;
        int swaps = 0;
        int calls = 0;
    }
}
