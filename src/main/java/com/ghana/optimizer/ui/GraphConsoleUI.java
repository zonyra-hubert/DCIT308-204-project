package com.ghana.optimizer.ui;

import com.ghana.optimizer.algorithm.graph.BFS;
import com.ghana.optimizer.algorithm.graph.DFS;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import com.ghana.optimizer.ds.graph.loader.GraphLoader;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive console UI for graph traversal operations (BFS/DFS) over
 * the campus road network. Mirrors TreeConsoleUI's menu structure and
 * input-handling conventions so the two feel like one coherent app.
 *
 * NOTE for the team: this is intentionally a SEPARATE class from
 * TreeConsoleUI rather than bolted onto it — each already has a single,
 * clear responsibility (trees vs. graph). Per the project brief's
 * Module 9 requirement, these should eventually be tied together under
 * ONE true top-level menu (likely from Main.java) so an examiner can
 * reach every algorithm from a single entry point without knowing
 * which class to run — that unification is a separate integration
 * task, not done here.
 */
public class GraphConsoleUI {

    private static final String LOCATIONS_CSV = "data/seed/locations.csv";
    private static final String ROADS_CSV = "data/seed/roads.csv";
    private static final int MAX_VERTICES = 100; // seed data has 52 locations; headroom for growth

    private final Scanner scanner;
    private AdjacencyListGraph graph;

    public GraphConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.graph = new AdjacencyListGraph(MAX_VERTICES);
        loadCampusGraph();
    }

    public static void main(String[] args) {
        GraphConsoleUI consoleUI = new GraphConsoleUI();
        consoleUI.run();
    }

    private void loadCampusGraph() {
        try {
            GraphLoader.loadLocations(LOCATIONS_CSV, graph);
            GraphLoader.loadRoads(ROADS_CSV, graph);
            println("Loaded campus graph: " + graph.getVertexCount() + " locations.");
        } catch (IOException e) {
            println("WARNING: Could not load campus graph data (" + e.getMessage() + ").");
            println("Run this from the project root directory so data/seed/*.csv can be found.");
        }
    }

    public void run() {
        printWelcome();
        while (true) {
            printPrimaryMenu();
            int selection = readInteger("Choose an option:");
            switch (selection) {
                case 1 -> runBfsTraversal();
                case 2 -> runDfsTraversal();
                case 3 -> showConnectedComponents();
                case 4 -> checkForCycles();
                case 5 -> showShortestPath();
                case 6 -> graph.printGraph();
                case 0 -> exitProgram();
                default -> println("Invalid selection. Please enter a valid option.");
            }
        }
    }

    private void printWelcome() {
        println("==========================================================================");
        println("  UG-CSOO Campus Graph Explorer");
        println("  Interactive console UI for BFS / DFS operations on the road network");
        println("==========================================================================");
        println("");
    }

    private void printPrimaryMenu() {
        println("Graph Menu:");
        println("  1. Run BFS from a location");
        println("  2. Run DFS from a location");
        println("  3. Show connected components (connectivity check)");
        println("  4. Check for cycles in the road network");
        println("  5. Find shortest path (fewest hops) between two locations");
        println("  6. Print full adjacency list");
        println("  0. Exit");
    }

    private void runBfsTraversal() {
        String startId = readString("Enter start location ID (e.g. LOC-UG-01):");
        try {
            List<BFS.BFSStep> result = BFS.bfs(graph, startId);
            BFS.printTrace(result, startId);
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        }
    }

    private void runDfsTraversal() {
        String startId = readString("Enter start location ID (e.g. LOC-UG-01):");
        try {
            List<DFS.DFSStep> result = DFS.dfs(graph, startId);
            DFS.printTrace(result, startId);
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        }
    }

    private void showConnectedComponents() {
        List<List<String>> components = DFS.findConnectedComponents(graph);
        println("Connected components found: " + components.size());
        for (int i = 0; i < components.size(); i++) {
            println("  Component " + (i + 1) + " (" + components.get(i).size() + " locations): " + components.get(i));
        }
        if (components.size() > 1) {
            println("NOTE: the campus network is NOT fully connected — " + components.size() + " separate clusters exist.");
        } else {
            println("The campus network is fully connected.");
        }
    }

    private void checkForCycles() {
        boolean hasCycle = DFS.hasCycle(graph);
        println(hasCycle
                ? "Cycle detected: the road network contains at least one cycle (expected for a real road network)."
                : "No cycle detected: the road network is tree-shaped (unusual for a real road network — check your data).");
    }

    private void showShortestPath() {
        String startId = readString("Enter start location ID:");
        String targetId = readString("Enter destination location ID:");
        try {
            List<BFS.BFSStep> result = BFS.bfs(graph, startId);
            List<String> path = BFS.reconstructPath(result, targetId);
            if (path.isEmpty()) {
                println("No path found from " + startId + " to " + targetId + " (unreachable or invalid ID).");
            } else {
                println("Shortest path (" + (path.size() - 1) + " hops): " + String.join(" -> ", path));
            }
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        }
    }

    private int readInteger(String prompt) {
        while (true) {
            try {
                print(prompt + " ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                println("Invalid number. Please enter an integer.");
            }
        }
    }

    private String readString(String prompt) {
        print(prompt + " ");
        return scanner.nextLine().trim();
    }

    private void print(String message) {
        System.out.print(message);
    }

    private void println(String message) {
        System.out.println(message);
    }

    private void exitProgram() {
        println("\nExiting the graph explorer. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}