package com.ghana.optimizer;

import com.ghana.optimizer.benchmark.BenchmarkSuiteRunner;
import com.ghana.optimizer.ui.ConsoleMenu;
import com.ghana.optimizer.ui.TreeConsoleUI;
import com.ghana.optimizer.ui.views.TableFormatter;
import com.ghana.optimizer.ui.views.TraceViewFormatter;

import java.util.Scanner;

/**
 * Master entry point and selection launcher for the University of Ghana
 * Campus Service Operations Optimizer (UG-CSOO).
 */
public class Main {

    // Explicit System Parameters as defined in the project specification (Recalculated for 15 Members)
    public static final double ROAD_PENALTY_WEIGHT = 59.0;
    public static final int HASH_TABLE_CAPACITY = 761;
    public static final double BUDGET_CONSTRAINT_GHS = 1089.00;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printSystemBanner();
            printActionSelectionMenu();

            System.out.print("Enter your choice (0-8): ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (0-8).\n");
                continue;
            }

            System.out.println();
            switch (choice) {
                case 1 -> {
                    // Launch Interactive Examiner Console Menu
                    ConsoleMenu menu = new ConsoleMenu();
                    menu.start();
                }
                case 2 -> {
                    // Run Automated Test Suite
                    TableFormatter.printHeader("RUNNING AUTOMATED UNIT TEST SUITE");
                    runTestRunner();
                }
                case 3 -> {
                    // Run Empirical Benchmark Suite
                    TableFormatter.printHeader("RUNNING EMPIRICAL EFFICIENCY BENCHMARKS");
                    BenchmarkSuiteRunner.runAllBenchmarks();
                }
                case 4 -> {
                    // Launch Interactive Tree Console UI
                    TableFormatter.printHeader("LAUNCHING TREE CONSOLE EXPLORER");
                    TreeConsoleUI treeUI = new TreeConsoleUI();
                    treeUI.run();
                }
                case 5 -> {
                    // Run Sorting & Searching Traces
                    TableFormatter.printHeader("SORTING & SEARCHING LIVE TRACE LAB");
                    TraceViewFormatter.displayBinarySearchTrace();
                    TraceViewFormatter.displayInsertionSortTrace();
                }
                case 6 -> {
                    // View Campus Road Graph
                    TableFormatter.printHeader("CAMPUS ROAD NETWORK & GRAPH ENGINE");
                    System.out.println("Formula: effectiveCost = distance_m + 59.0 * (5.0 - condition_score)");
                    System.out.println("Loaded: 200 Campus Locations & 200 Campus Road Segments.");
                    System.out.println("Use Option 1 (Examiner Console -> Graph Viewer) for full Matrix/List display.");
                }
                case 7 -> {
                    // View Theory, Proofs & Counterexamples
                    TableFormatter.printHeader("THEORY, INVARIANTS, PROOFS & COUNTEREXAMPLES");
                    TraceViewFormatter.displayKnapsackTrace();
                    TraceViewFormatter.displayCounterexample();
                    System.out.println("\nDocumentation available at:");
                    System.out.println("  - docs/SYSTEM_SPECIFICATION.md");
                    System.out.println("  - docs/TRACE_TABLES.md");
                    System.out.println("  - docs/PROOF_SKETCHES.md");
                    System.out.println("  - docs/COUNTEREXAMPLES.md");
                    System.out.println("  - docs/DEFENSE_PREP_NOTES.md");
                }
                case 8 -> {
                    // Submit Custom Service Request & Run Performance Analyzer
                    ConsoleMenu menu = new ConsoleMenu();
                    menu.handleInteractiveServiceRequest();
                }
                case 0 -> {
                    System.out.println("Exiting UG-CSOO System. Have a great day!");
                    running = false;
                }
                default -> System.out.println("Please select a number between 0 and 8.");
            }

            if (running && choice != 1 && choice != 4) {
                System.out.println("\nPress ENTER to return to the selection menu...");
                scanner.nextLine();
            }
        }
    }

    private static void printSystemBanner() {
        System.out.println("==========================================================================");
        System.out.println("  University of Ghana Campus Service Operations Optimizer (UG-CSOO)");
        System.out.println("  Operational Domain: UG Legon Campus, Accra, Ghana");
        System.out.println("==========================================================================");
        System.out.println("System Parameters:");
        System.out.println("  - Road Penalty Weight : " + ROAD_PENALTY_WEIGHT);
        System.out.println("  - Hash Prime Capacity : " + HASH_TABLE_CAPACITY);
        System.out.println("  - Shift Budget Limit  : GHS " + String.format("%.2f", BUDGET_CONSTRAINT_GHS));
        System.out.println("  - Real Datasets       : 200 Locations | 200 Roads | 200 Requests | 200 Resources");
        System.out.println("--------------------------------------------------------------------------");
    }

    private static void printActionSelectionMenu() {
        System.out.println("Select what you would like to do:");
        System.out.println("  [1] Launch Interactive Examiner Console Menu");
        System.out.println("  [2] Run Automated Verification & Test Suite");
        System.out.println("  [3] Run Empirical Performance Benchmark Suite (Export CSVs)");
        System.out.println("  [4] Launch Interactive BST & B-Tree Explorer (TreeConsoleUI)");
        System.out.println("  [5] Run Sorting & Searching Live Step Traces");
        System.out.println("  [6] View Campus Road Network & Graph Representations");
        System.out.println("  [7] View Theory, Invariants, Proofs & Counterexamples");
        System.out.println("  [8] Submit Custom Service Request & Run Live Performance Analyzer");
        System.out.println("  [0] Exit System");
        System.out.println("--------------------------------------------------------------------------");
    }

    private static void runTestRunner() {
        try {
            Class<?> testRunnerClass = Class.forName("com.ghana.optimizer.TestRunner");
            java.lang.reflect.Method mainMethod = testRunnerClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[0]);
        } catch (Exception e) {
            System.out.println("Executing test suite directly or run: java -cp bin com.ghana.optimizer.TestRunner");
            System.err.println("Test note: " + e.getMessage());
        }
    }
}
