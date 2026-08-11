package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.algorithm.search.BinarySearch;
import com.ghana.optimizer.algorithm.search.LinearSearch;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Empirical Benchmark Runner for Search Algorithms:
 * Compares Linear Search O(N) vs Binary Search O(log N) across multiple dataset sizes.
 */
public class SearchBenchmarkRunner {

    private static final int[] SIZES = {100, 500, 1_000, 5_000, 10_000};
    private static final int TRIALS = 10;
    private static final String OUTPUT_PATH = "exports/benchmark_results/search_benchmarks.csv";

    public static void main(String[] args) {
        runAndExport();
    }

    public static void runAndExport() {
        System.out.println("\n--- Running Search Algorithms Empirical Benchmark Suite ---");
        File dir = new File("exports/benchmark_results");
        if (!dir.exists()) dir.mkdirs();

        AlgorithmRunDAO runDAO = new AlgorithmRunDAO();

        try (FileWriter csvWriter = new FileWriter(OUTPUT_PATH)) {
            csvWriter.write("algorithm,inputSize,targetPosition,trial,elapsedNanos,memoryUsedKb\n");

            for (int n : SIZES) {
                Integer[] sortedData = new Integer[n];
                for (int i = 0; i < n; i++) {
                    sortedData[i] = i * 2;
                }

                // Targets at: beginning (0%), middle (50%), end (100%), missing
                int[] targetIndices = {0, n / 2, n - 1};
                String[] positions = {"beginning", "middle", "end"};

                for (int posIdx = 0; posIdx < targetIndices.length; posIdx++) {
                    int targetValue = sortedData[targetIndices[posIdx]];
                    String positionName = positions[posIdx];

                    // Linear Search
                    long linearTotalTime = 0;
                    for (int t = 0; t < TRIALS; t++) {
                        long startMem = getUsedMemoryKb();
                        long startTime = System.nanoTime();
                        int idx = LinearSearch.search(sortedData, targetValue);
                        long elapsed = System.nanoTime() - startTime;
                        long memUsed = Math.max(0, getUsedMemoryKb() - startMem);
                        linearTotalTime += elapsed;

                        csvWriter.write(String.format("LinearSearch,%d,%s,%d,%d,%d\n",
                                n, positionName, t, elapsed, memUsed));
                    }
                    long avgLinearNanos = linearTotalTime / TRIALS;

                    // Binary Search
                    long binaryTotalTime = 0;
                    for (int t = 0; t < TRIALS; t++) {
                        long startMem = getUsedMemoryKb();
                        long startTime = System.nanoTime();
                        int idx = BinarySearch.search(sortedData, targetValue);
                        long elapsed = System.nanoTime() - startTime;
                        long memUsed = Math.max(0, getUsedMemoryKb() - startMem);
                        binaryTotalTime += elapsed;

                        csvWriter.write(String.format("BinarySearch,%d,%s,%d,%d,%d\n",
                                n, positionName, t, elapsed, memUsed));
                    }
                    long avgBinaryNanos = binaryTotalTime / TRIALS;

                    // Log average to DB
                    try {
                        runDAO.insert(new AlgorithmRun(
                                UUID.randomUUID().toString(),
                                "LinearSearch_" + positionName,
                                n,
                                avgLinearNanos,
                                getUsedMemoryKb(),
                                547,
                                1089.0,
                                "{\"targetPosition\":\"" + positionName + "\", \"trials\":" + TRIALS + "}",
                                null
                        ));
                        runDAO.insert(new AlgorithmRun(
                                UUID.randomUUID().toString(),
                                "BinarySearch_" + positionName,
                                n,
                                avgBinaryNanos,
                                getUsedMemoryKb(),
                                547,
                                1089.0,
                                "{\"targetPosition\":\"" + positionName + "\", \"trials\":" + TRIALS + "}",
                                null
                        ));
                    } catch (SQLException ignored) {}
                }
                System.out.printf("  [N=%-5d] Linear vs Binary Search benchmark completed.\n", n);
            }
            System.out.println("Search benchmarks written to: " + OUTPUT_PATH);
        } catch (IOException e) {
            System.err.println("Error running search benchmark: " + e.getMessage());
        }
    }

    private static long getUsedMemoryKb() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
