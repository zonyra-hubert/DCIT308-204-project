package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.ds.heap.BinaryHeap;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

/**
 * Empirical Benchmark Runner for BinaryHeap Priority Dispatch Engine:
 * Measures insert throughput and extractMin operation runtimes across various dataset sizes.
 */
public class HeapBenchmarkRunner {

    private static final int[] SIZES = {100, 500, 1_000, 5_000, 10_000};
    private static final String OUTPUT_PATH = "exports/benchmark_results/heap_benchmarks.csv";

    public static void main(String[] args) {
        runAndExport();
    }

    public static void runAndExport() {
        System.out.println("\n--- Running BinaryHeap Priority Queue Empirical Benchmark Suite ---");
        File dir = new File("exports/benchmark_results");
        if (!dir.exists()) dir.mkdirs();

        AlgorithmRunDAO runDAO = new AlgorithmRunDAO();

        try (FileWriter csvWriter = new FileWriter(OUTPUT_PATH)) {
            csvWriter.write("datasetSize,insertTotalNanos,insertPerOpNanos,extractMinTotalNanos,extractMinPerOpNanos,memoryUsedKb\n");

            Random random = new Random(42);

            for (int n : SIZES) {
                BinaryHeap<Integer> heap = new BinaryHeap<>(n);
                Integer[] randomValues = new Integer[n];
                for (int i = 0; i < n; i++) {
                    randomValues[i] = random.nextInt(n * 10);
                }

                long startMem = getUsedMemoryKb();

                // 1. Measure Insertion Throughput
                long insertStart = System.nanoTime();
                for (Integer val : randomValues) {
                    heap.insert(val);
                }
                long insertTotalNanos = System.nanoTime() - insertStart;
                double insertPerOp = (double) insertTotalNanos / n;

                // 2. Measure ExtractMin Throughput
                long extractStart = System.nanoTime();
                while (!heap.isEmpty()) {
                    heap.extractMin();
                }
                long extractTotalNanos = System.nanoTime() - extractStart;
                double extractPerOp = (double) extractTotalNanos / n;

                long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                csvWriter.write(String.format("%d,%d,%.2f,%d,%.2f,%d\n",
                        n, insertTotalNanos, insertPerOp, extractTotalNanos, extractPerOp, memUsed));

                // Persist to DB
                try {
                    runDAO.insert(new AlgorithmRun(
                            UUID.randomUUID().toString(),
                            "BinaryHeap_Operations",
                            n,
                            insertTotalNanos + extractTotalNanos,
                            memUsed,
                            761,
                            1089.0,
                            "{\"insertPerOpNanos\":" + String.format("%.2f", insertPerOp)
                                    + ", \"extractMinPerOpNanos\":" + String.format("%.2f", extractPerOp) + "}",
                            null
                    ));
                } catch (SQLException ignored) {}

                System.out.printf("  [N=%-5d] Heap insert: %.1f ns/op | extractMin: %.1f ns/op\n", n, insertPerOp, extractPerOp);
            }
            System.out.println("Heap benchmarks written to: " + OUTPUT_PATH);
        } catch (IOException e) {
            System.err.println("Error running heap benchmark: " + e.getMessage());
        }
    }

    private static long getUsedMemoryKb() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
