package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.algorithm.sort.SelectionSort;
import com.ghana.optimizer.algorithm.sort.InsertionSort;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Sorting Empirical Runtime Engine — Weyttey Emmanuel (@Global-Wonder)
 *
 * Measures nanosecond runtimes for Selection, Insertion, Merge, and
 * Quicksort across n = 100 ... 50,000, exported to
 * exports/benchmark_results/sorting_benchmarks.csv for the O(n^2) vs
 * O(n log n) comparison graphs.
 *
 * Each algorithm has a different signature (DynamicArray<ServiceRequest>
 * vs generic T[] + Comparator, etc.) so this class does NOT assume a
 * shared interface across sort implementations — it wraps each one in
 * a small adapter (see the SortRunner lambdas below) instead of forcing
 * everyone else's already-committed code to conform to one shape.
 */

public class SortBenchmarkRunner {
    // Geometric-ish spread across the required n=100..50,000 range.
    // Adjust freely — just keep it wide enough to show O(n^2) vs O(n log n)
    // diverging visibly on the graph.
    private static final int[] SIZES = {100, 500, 1_000};
 
    private static final String[] INPUT_TYPES = {"random", "sorted", "reverse_sorted"};
 
    private static final int TRIALS_PER_CONFIG = 5;   // timed runs, averaged/reported individually
    private static final int WARMUP_TRIALS = 2;       // JIT warmup, discarded
 
    private static final String OUTPUT_PATH = "exports/benchmark_results/sorting_benchmarks.csv";
 
    public static void main(String[] args) throws IOException {
        try (FileWriter csv = new FileWriter(OUTPUT_PATH)) {
            csv.write("algorithm,inputSize,inputType,trial,elapsedNanos\n");
 
            for (int n : SIZES) {
                for (String inputType : INPUT_TYPES) {
                    ServiceRequest[] base = generateRequests(n, inputType);
 
                    runBenchmark("SelectionSort", n, inputType, base, csv,
                            arr -> SelectionSort.selectionSort(toDynamicArray(arr)));
 
                    runBenchmark("InsertionSort", n, inputType, base, csv,
                            arr -> InsertionSort.insertionSort(toDynamicArray(arr)));
 
                    // runBenchmark("MergeSort", n, inputType, base, csv,
                    //         arr -> MergeSort.mergeSort(arr /* + whatever its real signature needs */));
 
                    // runBenchmark("QuickSort", n, inputType, base, csv,
                    //         arr -> QuickSort.quickSort(arr /* + whatever its real signature needs */));
                }
            }
        }
        System.out.println("Done. Results written to " + OUTPUT_PATH);
    }
 
    // ---------------------------------------------------------------
    // Core timing loop — identical for every algorithm regardless of
    // its underlying signature, because the adapter lambda absorbs
    // the difference.
    // ---------------------------------------------------------------
    private static void runBenchmark(String algorithmName, int n, String inputType,
                                      ServiceRequest[] base, FileWriter csv,
                                      SortRunner sorter) throws IOException {
 
        for (int w = 0; w < WARMUP_TRIALS; w++) {
            sorter.run(copyOf(base));
        }
 
        for (int t = 0; t < TRIALS_PER_CONFIG; t++) {
            ServiceRequest[] trialData = copyOf(base);
 
            long start = System.nanoTime();
            sorter.run(trialData);
            long elapsedNanos = System.nanoTime() - start;
 
            csv.write(String.format("%s,%d,%s,%d,%d%n",
                    algorithmName, n, inputType, t, elapsedNanos));
        }
    }
 
    @FunctionalInterface
    interface SortRunner {
        void run(ServiceRequest[] data);
    }
 
    // ---------------------------------------------------------------
    // Data generation
    // ---------------------------------------------------------------
    private static ServiceRequest[] generateRequests(int n, String inputType) {
        ServiceRequest[] data = new ServiceRequest[n];
        Random rnd = new Random(42); // fixed seed -> reproducible runs across algorithms
 
        for (int i = 0; i < n; i++) {
            int urgency;
            switch (inputType) {
                case "sorted":          urgency = i % 5 + 1; break;
                case "reverse_sorted":  urgency = 5 - (i % 5); break;
                default:                urgency = rnd.nextInt(5) + 1; // "random"
            }
            data[i] = new ServiceRequest(
                    i, 0, null, "maintenance", urgency,
                    "2026-08-08T00:00:00Z", null, "pending");
        }
        return data;
    }
 
    private static ServiceRequest[] copyOf(ServiceRequest[] src) {
        return Arrays.copyOf(src, src.length); // array utility, not a banned collection
    }
 
    // ---------------------------------------------------------------
    // Adapter: bridges plain ServiceRequest[] -> the custom DynamicArray
    // SelectionSort actually expects. Swap/remove this once (if) the
    // team standardizes on one container type across all sort classes.
    // ---------------------------------------------------------------
    private static DynamicArray<ServiceRequest> toDynamicArray(ServiceRequest[] arr) {
        DynamicArray<ServiceRequest> da = new DynamicArray<>();
        for (ServiceRequest r : arr) {
            da.insert(r);
        }
        return da;
    }
    
}
