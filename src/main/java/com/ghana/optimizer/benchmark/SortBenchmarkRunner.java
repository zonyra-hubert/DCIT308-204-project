package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.algorithm.sort.InsertionSort;
import com.ghana.optimizer.algorithm.sort.SelectionSort;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

/**
 * Sorting Empirical Runtime Benchmark Engine:
 * Measures nanosecond runtimes for SelectionSort and InsertionSort across
 * n = 100 ... 5,000, exporting results to exports/benchmark_results/sorting_benchmarks.csv
 * and persisting metrics to the SQLite algorithm_runs table.
 */
public class SortBenchmarkRunner {

    private static final int[] SIZES = {100, 500, 1_000, 2_500, 5_000};
    private static final String[] INPUT_TYPES = {"random", "sorted", "reverse_sorted"};
    private static final int TRIALS_PER_CONFIG = 5;
    private static final int WARMUP_TRIALS = 2;
    private static final String OUTPUT_PATH = "exports/benchmark_results/sorting_benchmarks.csv";

    public static void main(String[] args) {
        runAndExport();
    }

    public static void runAndExport() {
        System.out.println("\n--- Running Sorting Empirical Runtime Benchmark Suite ---");
        File dir = new File("exports/benchmark_results");
        if (!dir.exists()) dir.mkdirs();

        AlgorithmRunDAO runDAO = new AlgorithmRunDAO();

        try (FileWriter csvWriter = new FileWriter(OUTPUT_PATH)) {
            csvWriter.write("algorithm,inputSize,inputType,trial,elapsedNanos,memoryUsedKb\n");

            for (int n : SIZES) {
                for (String inputType : INPUT_TYPES) {
                    ServiceRequest[] baseData = generateRequests(n, inputType);

                    // Selection Sort
                    long selectionTotalNanos = 0;
                    for (int w = 0; w < WARMUP_TRIALS; w++) {
                        SelectionSort.selectionSort(toDynamicArray(baseData));
                    }
                    for (int t = 0; t < TRIALS_PER_CONFIG; t++) {
                        DynamicArray<ServiceRequest> data = toDynamicArray(baseData);
                        long startMem = getUsedMemoryKb();
                        long startTime = System.nanoTime();
                        SelectionSort.selectionSort(data);
                        long elapsedNanos = System.nanoTime() - startTime;
                        long memUsed = Math.max(0, getUsedMemoryKb() - startMem);
                        selectionTotalNanos += elapsedNanos;

                        csvWriter.write(String.format("SelectionSort,%d,%s,%d,%d,%d\n",
                                n, inputType, t, elapsedNanos, memUsed));
                    }
                    long avgSelectionNanos = selectionTotalNanos / TRIALS_PER_CONFIG;

                    // Insertion Sort
                    long insertionTotalNanos = 0;
                    for (int w = 0; w < WARMUP_TRIALS; w++) {
                        InsertionSort.insertionSort(toDynamicArray(baseData));
                    }
                    for (int t = 0; t < TRIALS_PER_CONFIG; t++) {
                        DynamicArray<ServiceRequest> data = toDynamicArray(baseData);
                        long startMem = getUsedMemoryKb();
                        long startTime = System.nanoTime();
                        InsertionSort.insertionSort(data);
                        long elapsedNanos = System.nanoTime() - startTime;
                        long memUsed = Math.max(0, getUsedMemoryKb() - startMem);
                        insertionTotalNanos += elapsedNanos;

                        csvWriter.write(String.format("InsertionSort,%d,%s,%d,%d,%d\n",
                                n, inputType, t, elapsedNanos, memUsed));
                    }
                    long avgInsertionNanos = insertionTotalNanos / TRIALS_PER_CONFIG;

                    // Persist average runs to SQLite algorithm_runs table
                    try {
                        runDAO.insert(new AlgorithmRun(
                                UUID.randomUUID().toString(),
                                "SelectionSort_" + inputType,
                                n,
                                avgSelectionNanos,
                                getUsedMemoryKb(),
                                761,
                                1089.0,
                                "{\"inputType\":\"" + inputType + "\", \"trials\":" + TRIALS_PER_CONFIG + "}",
                                null
                        ));
                        runDAO.insert(new AlgorithmRun(
                                UUID.randomUUID().toString(),
                                "InsertionSort_" + inputType,
                                n,
                                avgInsertionNanos,
                                getUsedMemoryKb(),
                                761,
                                1089.0,
                                "{\"inputType\":\"" + inputType + "\", \"trials\":" + TRIALS_PER_CONFIG + "}",
                                null
                        ));
                    } catch (SQLException ignored) {}
                }
                System.out.printf("  [N=%-5d] Sorting comparison (Selection vs Insertion) completed.\n", n);
            }
            System.out.println("Sorting benchmarks written to: " + OUTPUT_PATH);
        } catch (IOException e) {
            System.err.println("Error running sorting benchmark: " + e.getMessage());
        }
    }

    private static DynamicArray<ServiceRequest> toDynamicArray(ServiceRequest[] source) {
        DynamicArray<ServiceRequest> dynamicArray = new DynamicArray<>();
        for (ServiceRequest request : source) {
            dynamicArray.add(new ServiceRequest(
                    request.getRequestId(),
                    request.getSourceId(),
                    request.getDestinationId(),
                    request.getCategory(),
                    request.getUrgency(),
                    request.getTimeSubmitted(),
                    request.getDeadline(),
                    request.getStatus()
            ));
        }
        return dynamicArray;
    }

    private static ServiceRequest[] generateRequests(int n, String inputType) {
        ServiceRequest[] data = new ServiceRequest[n];
        Random random = new Random(42);

        for (int i = 0; i < n; i++) {
            int urgency;
            if (inputType.equals("sorted")) {
                // Descending urgency for descending sort
                urgency = 5 - (int) (((long) i * 5) / n);
                if (urgency < 1) urgency = 1;
            } else if (inputType.equals("reverse_sorted")) {
                // Ascending urgency (worst case for descending sort)
                urgency = 1 + (int) (((long) i * 5) / n);
                if (urgency > 5) urgency = 5;
            } else {
                urgency = 1 + random.nextInt(5);
            }

            data[i] = new ServiceRequest(
                    i + 1,
                    (i % 50) + 1,
                    ((i + 5) % 50) + 1,
                    "Maintenance",
                    urgency,
                    "2026-08-11T08:00:00Z",
                    null,
                    "PENDING"
            );
        }
        return data;
    }

    private static long getUsedMemoryKb() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
