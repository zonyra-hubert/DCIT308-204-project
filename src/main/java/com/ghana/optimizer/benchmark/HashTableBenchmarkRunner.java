package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.ds.hash.CustomHashTable;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Empirical Benchmark Runner for CustomHashTable:
 * Evaluates collision metrics, insertion throughput, and lookup performance
 * across varying load factors and dataset sizes.
 */
public class HashTableBenchmarkRunner {

    private static final double[] LOAD_FACTORS = {0.25, 0.50, 0.75, 0.90, 0.95};
    private static final int[] ELEMENT_COUNTS = {500, 1_000, 5_000, 10_000};
    private static final String OUTPUT_PATH = "exports/benchmark_results/hashtable_benchmarks.csv";

    public static void main(String[] args) {
        runAndExport();
    }

    public static void runAndExport() {
        System.out.println("\n--- Running CustomHashTable Empirical Efficiency Benchmark Suite ---");
        File dir = new File("exports/benchmark_results");
        if (!dir.exists()) dir.mkdirs();

        AlgorithmRunDAO runDAO = new AlgorithmRunDAO();

        try (FileWriter csvWriter = new FileWriter(OUTPUT_PATH)) {
            csvWriter.write("loadFactorThreshold,elementCount,initialCapacity,finalCapacity,finalLoadFactor,insertTimeNanos,lookupTimeNanos,memoryUsedKb\n");

            for (double loadFactor : LOAD_FACTORS) {
                for (int count : ELEMENT_COUNTS) {
                    CustomHashTable<String, String> table = new CustomHashTable<>(761, loadFactor);

                    long startMem = getUsedMemoryKb();

                    // Measure Insertion
                    long insertStart = System.nanoTime();
                    for (int i = 0; i < count; i++) {
                        String key = "REQ-UG-" + String.format("%06d", i);
                        String value = "Ticket Description for item " + i;
                        table.put(key, value);
                    }
                    long insertTimeNanos = System.nanoTime() - insertStart;

                    // Measure Lookup
                    long lookupStart = System.nanoTime();
                    for (int i = 0; i < count; i++) {
                        String key = "REQ-UG-" + String.format("%06d", i);
                        String val = table.get(key);
                    }
                    long lookupTimeNanos = System.nanoTime() - lookupStart;
                    long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("%.2f,%d,%d,%d,%.4f,%d,%d,%d\n",
                            loadFactor, count, 761, table.getCapacity(),
                            table.getLoadFactor(), insertTimeNanos, lookupTimeNanos, memUsed));

                    // Persist to DB
                    try {
                        runDAO.insert(new AlgorithmRun(
                                UUID.randomUUID().toString(),
                                "CustomHashTable_LF_" + String.format("%.2f", loadFactor),
                                count,
                                insertTimeNanos + lookupTimeNanos,
                                memUsed,
                                761,
                                1089.0,
                                "{\"loadFactorThreshold\":" + loadFactor + ", \"finalCapacity\":" + table.getCapacity() + "}",
                                null
                        ));
                    } catch (SQLException ignored) {}
                }
                System.out.printf("  [Load Factor=%.2f] CustomHashTable benchmark completed.\n", loadFactor);
            }
            System.out.println("HashTable benchmarks written to: " + OUTPUT_PATH);
        } catch (IOException e) {
            System.err.println("Error running hash table benchmark: " + e.getMessage());
        }
    }

    private static long getUsedMemoryKb() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
