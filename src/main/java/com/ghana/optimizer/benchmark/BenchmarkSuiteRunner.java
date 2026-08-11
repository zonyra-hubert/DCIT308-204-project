package com.ghana.optimizer.benchmark;

/**
 * Master Empirical Benchmark Suite Coordinator:
 * Executes Search, Sort, HashTable, Tree, and Heap performance benchmarks in sequence,
 * exporting CSV result files to exports/benchmark_results/ and recording runs to SQLite.
 */
public class BenchmarkSuiteRunner {

    public static void main(String[] args) {
        runAllBenchmarks();
    }

    public static void runAllBenchmarks() {
        System.out.println("==========================================================================");
        System.out.println("  🏛️ UG-CSOO EMPIRICAL EFFICIENCY & PERFORMANCE BENCHMARK SUITE");
        System.out.println("  Operational Domain: University of Ghana Legon Campus Operations");
        System.out.println("==========================================================================");

        long overallStart = System.currentTimeMillis();

        // 1. Search Benchmark
        SearchBenchmarkRunner.runAndExport();

        // 2. Sort Benchmark
        SortBenchmarkRunner.runAndExport();

        // 3. Hash Table Benchmark
        HashTableBenchmarkRunner.runAndExport();

        // 4. Tree Structures Benchmark
        TreeBenchmarkRunner.runAndExport();

        // 5. Binary Heap Benchmark
        HeapBenchmarkRunner.runAndExport();

        long totalElapsedMs = System.currentTimeMillis() - overallStart;
        System.out.println("\n==========================================================================");
        System.out.printf("  All Empirical Benchmark Suites Finished in %d ms (%.2f seconds).\n",
                totalElapsedMs, totalElapsedMs / 1000.0);
        System.out.println("  CSV Output Directory: exports/benchmark_results/");
        System.out.println("  Database Logs Table:  algorithm_runs");
        System.out.println("==========================================================================");
    }
}
