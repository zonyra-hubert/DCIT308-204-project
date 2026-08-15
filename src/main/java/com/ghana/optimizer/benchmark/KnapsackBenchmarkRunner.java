package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.algorithm.optimization.GreedyKnapsackHeuristic;
import com.ghana.optimizer.algorithm.optimization.KnapsackOptimizer;
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
 * Empirical Benchmark Runner for Knapsack Optimization:
 * Compares 0/1 Knapsack 2D Dynamic Programming Tabulation vs Ratio-based Greedy Heuristic
 * measuring runtime, memory, total priority points, and suboptimality gap under W = GHS 1,089.00.
 */
public class KnapsackBenchmarkRunner {

    private static final int[] BATCH_SIZES = {25, 50, 100, 200, 300};
    private static final int TRIALS = 5;
    private static final double BUDGET_CAP = 1089.00;
    private static final String OUTPUT_PATH = "exports/benchmark_results/knapsack_benchmarks.csv";

    public static void main(String[] args) {
        runAndExport();
    }

    public static void runAndExport() {
        System.out.println("\n--- Running Knapsack Optimization Empirical Benchmark Suite ---");
        File dir = new File("exports/benchmark_results");
        if (!dir.exists()) dir.mkdirs();

        AlgorithmRunDAO runDAO = new AlgorithmRunDAO();
        Random rng = new Random(22167843); // System Parameter 4: Seed from Team Lead Index

        try (FileWriter csvWriter = new FileWriter(OUTPUT_PATH)) {
            csvWriter.write("algorithm,requestCount,budgetLimit,trial,priorityPoints,costUtilized,suboptimalityPct,elapsedNanos,memoryUsedKb\n");

            for (int n : BATCH_SIZES) {
                DynamicArray<ServiceRequest> sampleRequests = generateSyntheticRequests(n, rng);

                // 1. 0/1 Knapsack DP Tabulation
                for (int t = 0; t < TRIALS; t++) {
                    long startMem = getUsedMemoryKb();
                    long startTime = System.nanoTime();
                    KnapsackOptimizer.KnapsackResult dpResult = KnapsackOptimizer.optimize(sampleRequests, BUDGET_CAP);
                    long elapsed = System.nanoTime() - startTime;
                    long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("Knapsack_01_DP,%d,%.2f,%d,%d,%.2f,0.00,%d,%d\n",
                            n, BUDGET_CAP, t + 1, dpResult.getTotalPriorityPoints(), dpResult.getTotalCost(), elapsed, memUsed));
                }

                // 2. Greedy Ratio Heuristic
                for (int t = 0; t < TRIALS; t++) {
                    KnapsackOptimizer.KnapsackResult dpResult = KnapsackOptimizer.optimize(sampleRequests, BUDGET_CAP);

                    long startMem = getUsedMemoryKb();
                    long startTime = System.nanoTime();
                    GreedyKnapsackHeuristic.GreedyResult greedyResult = GreedyKnapsackHeuristic.solveGreedy(sampleRequests, BUDGET_CAP);
                    long elapsed = System.nanoTime() - startTime;
                    long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                    double suboptimality = greedyResult.computeSuboptimalityPenalty(dpResult);

                    csvWriter.write(String.format("Greedy_Ratio_Heuristic,%d,%.2f,%d,%d,%.2f,%.2f,%d,%d\n",
                            n, BUDGET_CAP, t + 1, greedyResult.getTotalPriorityPoints(), greedyResult.getTotalCost(), suboptimality, elapsed, memUsed));
                }

                // Record summary to SQLite
                try {
                    AlgorithmRun run = new AlgorithmRun(
                            UUID.randomUUID().toString(),
                            "Knapsack-DP-Suite",
                            n,
                            System.nanoTime(),
                            getUsedMemoryKb(),
                            547,
                            1089.0,
                            "{\"algorithm\":\"Knapsack01DP\", \"itemCount\":" + n + "}",
                            new java.sql.Timestamp(System.currentTimeMillis()).toString()
                    );
                    runDAO.insert(run);
                } catch (SQLException ignored) {}
            }

            System.out.println("Knapsack benchmarks complete! Exported to " + OUTPUT_PATH);

        } catch (IOException e) {
            System.err.println("Error writing knapsack benchmarks: " + e.getMessage());
        }
    }

    private static DynamicArray<ServiceRequest> generateSyntheticRequests(int count, Random rng) {
        DynamicArray<ServiceRequest> requests = new DynamicArray<>(count);
        String[] categories = {"Plumbing", "Electrical", "ICT", "HVAC", "Carpentry", "Shuttle"};

        for (int i = 1; i <= count; i++) {
            String id = String.format("REQ-SYN-%04d", i);
            String loc = String.format("LOC-UG-%02d", (i % 52) + 1);
            String cat = categories[i % categories.length];
            int priority = 1 + rng.nextInt(5);
            double cost = 50.0 + rng.nextDouble() * 350.0;
            double duration = 1.0 + rng.nextDouble() * 4.0;

            requests.add(new ServiceRequest(id, loc, cat + " maintenance request #" + i, priority, cost, duration, "PENDING"));
        }
        return requests;
    }

    private static long getUsedMemoryKb() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / 1024;
    }
}
