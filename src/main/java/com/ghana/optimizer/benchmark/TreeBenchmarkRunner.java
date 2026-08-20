package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.ds.tree.BTree;
import com.ghana.optimizer.ds.tree.BinarySearchTree;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

/**
 * Empirical Benchmark Runner for Tree Structures:
 * Compares standard Binary Search Tree (BST) vs balanced B-Tree (t=2) across
 * sorted and random input sequences, measuring tree height, insertion time, and search time.
 */
public class TreeBenchmarkRunner {

    private static final int[] SIZES = {100, 500, 1_000, 2_500, 5_000};
    private static final String[] INPUT_PATTERNS = {"sorted", "random"};
    private static final String OUTPUT_PATH = "exports/benchmark_results/tree_benchmarks.csv";

    public static void main(String[] args) {
        runAndExport();
    }

    public static void runAndExport() {
        System.out.println("\n--- Running Tree Data Structures (BST vs B-Tree) Benchmark Suite ---");
        File dir = new File("exports/benchmark_results");
        if (!dir.exists()) dir.mkdirs();

        AlgorithmRunDAO runDAO = new AlgorithmRunDAO();

        try (FileWriter csvWriter = new FileWriter(OUTPUT_PATH)) {
            csvWriter.write("treeType,inputPattern,elementCount,treeHeight,insertTimeNanos,searchTimeNanos,memoryUsedKb\n");

            for (int n : SIZES) {
                for (String pattern : INPUT_PATTERNS) {
                    Integer[] keys = generateKeys(n, pattern);

                    // 1. Binary Search Tree (BST)
                    BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
                    long startMem = getUsedMemoryKb();

                    long bstInsertStart = System.nanoTime();
                    for (Integer k : keys) {
                        bst.insert(k, "VAL-" + k);
                    }
                    long bstInsertNanos = System.nanoTime() - bstInsertStart;

                    long bstSearchStart = System.nanoTime();
                    for (Integer k : keys) {
                        bst.search(k);
                    }
                    long bstSearchNanos = System.nanoTime() - bstSearchStart;
                    int bstHeight = bst.height();
                    long bstMem = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("BST,%s,%d,%d,%d,%d,%d\n",
                            pattern, n, bstHeight, bstInsertNanos, bstSearchNanos, bstMem));

                    // 2. B-Tree (min degree t=2)
                    BTree<Integer, String> bTree = new BTree<>(2);
                    startMem = getUsedMemoryKb();

                    long btreeInsertStart = System.nanoTime();
                    for (Integer k : keys) {
                        bTree.insert(k, "VAL-" + k);
                    }
                    long btreeInsertNanos = System.nanoTime() - btreeInsertStart;

                    long btreeSearchStart = System.nanoTime();
                    for (Integer k : keys) {
                        bTree.search(k);
                    }
                    long btreeSearchNanos = System.nanoTime() - btreeSearchStart;
                    int btreeHeight = bTree.height();
                    long btreeMem = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("BTree,%s,%d,%d,%d,%d,%d\n",
                            pattern, n, btreeHeight, btreeInsertNanos, btreeSearchNanos, btreeMem));

                    // Persist to DB
                    try {
                        runDAO.insert(new AlgorithmRun(
                                UUID.randomUUID().toString(),
                                "BST_" + pattern,
                                n,
                                bstInsertNanos + bstSearchNanos,
                                bstMem,
                                761,
                                1089.0,
                                "{\"height\":" + bstHeight + ", \"pattern\":\"" + pattern + "\"}",
                                null
                        ));
                        runDAO.insert(new AlgorithmRun(
                                UUID.randomUUID().toString(),
                                "BTree_" + pattern,
                                n,
                                btreeInsertNanos + btreeSearchNanos,
                                btreeMem,
                                761,
                                1089.0,
                                "{\"height\":" + btreeHeight + ", \"pattern\":\"" + pattern + "\"}",
                                null
                        ));
                    } catch (SQLException ignored) {}
                }
                System.out.printf("  [N=%-5d] BST vs BTree height & search benchmark completed.\n", n);
            }
            System.out.println("Tree benchmarks written to: " + OUTPUT_PATH);
        } catch (IOException e) {
            System.err.println("Error running tree benchmark: " + e.getMessage());
        }
    }

    private static Integer[] generateKeys(int n, String pattern) {
        Integer[] keys = new Integer[n];
        if (pattern.equals("sorted")) {
            for (int i = 0; i < n; i++) keys[i] = i;
        } else {
            Random random = new Random(42);
            for (int i = 0; i < n; i++) keys[i] = random.nextInt(n * 10);
        }
        return keys;
    }

    private static long getUsedMemoryKb() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
