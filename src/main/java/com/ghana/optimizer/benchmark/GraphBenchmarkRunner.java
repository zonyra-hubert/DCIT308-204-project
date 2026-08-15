package com.ghana.optimizer.benchmark;

import com.ghana.optimizer.algorithm.graph.BFS;
import com.ghana.optimizer.algorithm.graph.DFS;
import com.ghana.optimizer.algorithm.graph.Dijkstra;
import com.ghana.optimizer.algorithm.graph.KruskalMST;
import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Empirical Benchmark Runner for Graph Algorithms:
 * Evaluates Dijkstra (effective-weight shortest path), BFS (hop reachability),
 * DFS (cycle detection), and Kruskal's MST across increasing campus network sizes.
 */
public class GraphBenchmarkRunner {

    private static final int[] GRAPH_SIZES = {25, 50, 100, 200};
    private static final int TRIALS = 5;
    private static final String OUTPUT_PATH = "exports/benchmark_results/graph_benchmarks.csv";

    public static void main(String[] args) {
        runAndExport();
    }

    public static void runAndExport() {
        System.out.println("\n--- Running Graph Algorithms Empirical Benchmark Suite ---");
        File dir = new File("exports/benchmark_results");
        if (!dir.exists()) dir.mkdirs();

        AlgorithmRunDAO runDAO = new AlgorithmRunDAO();

        try (FileWriter csvWriter = new FileWriter(OUTPUT_PATH)) {
            csvWriter.write("algorithm,vertexCount,edgeCount,trial,elapsedNanos,memoryUsedKb\n");

            for (int vCount : GRAPH_SIZES) {
                AdjacencyListGraph graph = buildSyntheticGraph(vCount);
                int edgeCount = vCount * 3;

                // 1. Benchmark Dijkstra
                for (int t = 0; t < TRIALS; t++) {
                    long startMem = getUsedMemoryKb();
                    long startTime = System.nanoTime();
                    Dijkstra.dijkstra(graph, "LOC-0");
                    long elapsed = System.nanoTime() - startTime;
                    long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("Dijkstra,%d,%d,%d,%d,%d\n", vCount, edgeCount, t + 1, elapsed, memUsed));
                }

                // 2. Benchmark BFS
                for (int t = 0; t < TRIALS; t++) {
                    long startMem = getUsedMemoryKb();
                    long startTime = System.nanoTime();
                    BFS.bfs(graph, "LOC-0");
                    long elapsed = System.nanoTime() - startTime;
                    long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("BFS,%d,%d,%d,%d,%d\n", vCount, edgeCount, t + 1, elapsed, memUsed));
                }

                // 3. Benchmark DFS
                for (int t = 0; t < TRIALS; t++) {
                    long startMem = getUsedMemoryKb();
                    long startTime = System.nanoTime();
                    DFS.dfs(graph, "LOC-0");
                    long elapsed = System.nanoTime() - startTime;
                    long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("DFS,%d,%d,%d,%d,%d\n", vCount, edgeCount, t + 1, elapsed, memUsed));
                }

                // 4. Benchmark Kruskal MST
                for (int t = 0; t < TRIALS; t++) {
                    long startMem = getUsedMemoryKb();
                    long startTime = System.nanoTime();
                    KruskalMST.computeMST(graph);
                    long elapsed = System.nanoTime() - startTime;
                    long memUsed = Math.max(0, getUsedMemoryKb() - startMem);

                    csvWriter.write(String.format("KruskalMST,%d,%d,%d,%d,%d\n", vCount, edgeCount, t + 1, elapsed, memUsed));
                }

                // Record summary to SQLite
                try {
                    AlgorithmRun run = new AlgorithmRun(
                            UUID.randomUUID().toString(),
                            "Dijkstra-Graph-Suite",
                            vCount,
                            System.nanoTime(),
                            getUsedMemoryKb(),
                            547,
                            1089.0,
                            "{\"algorithm\":\"Dijkstra\", \"vertexCount\":" + vCount + "}",
                            new java.sql.Timestamp(System.currentTimeMillis()).toString()
                    );
                    runDAO.insert(run);
                } catch (SQLException ignored) {}
            }

            System.out.println("Graph benchmarks complete! Exported to " + OUTPUT_PATH);

        } catch (IOException e) {
            System.err.println("Error writing graph benchmarks: " + e.getMessage());
        }
    }

    private static AdjacencyListGraph buildSyntheticGraph(int size) {
        AdjacencyListGraph graph = new AdjacencyListGraph(size + 10);
        Vertex[] vertices = new Vertex[size];

        for (int i = 0; i < size; i++) {
            vertices[i] = new Vertex("LOC-" + i, "Location-" + i, "UG-Campus", 5.65 + (i * 0.001), -0.18 + (i * 0.001));
            graph.addVertex(vertices[i]);
        }

        // Add ring backbone + cross edges
        for (int i = 0; i < size; i++) {
            int next = (i + 1) % size;
            graph.addEdge(new Edge(vertices[i], vertices[next], 150 + (i % 50), 2, 4.0, 43.0));

            if (i + 3 < size) {
                graph.addEdge(new Edge(vertices[i], vertices[i + 3], 300 + (i % 70), 4, 3.5, 43.0));
            }
        }

        return graph;
    }

    private static long getUsedMemoryKb() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / 1024;
    }
}
