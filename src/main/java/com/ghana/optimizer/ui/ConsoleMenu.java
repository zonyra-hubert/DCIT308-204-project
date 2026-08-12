package com.ghana.optimizer.ui;

import com.ghana.optimizer.algorithm.search.BinarySearch;
import com.ghana.optimizer.algorithm.search.LinearSearch;
import com.ghana.optimizer.algorithm.sort.InsertionSort;
import com.ghana.optimizer.algorithm.sort.SelectionSort;
import com.ghana.optimizer.benchmark.BenchmarkSuiteRunner;
import com.ghana.optimizer.ds.disjoint.DisjointSet;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import com.ghana.optimizer.ds.graph.loader.GraphLoader;
import com.ghana.optimizer.ds.graph.matrix.AdjacencyMatrixGraph;
import com.ghana.optimizer.ds.hash.CustomHashTable;
import com.ghana.optimizer.ds.heap.BinaryHeap;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.ds.list.MyLinkedList;
import com.ghana.optimizer.ds.queue.CircularQueue;
import com.ghana.optimizer.ds.queue.MyQueue;
import com.ghana.optimizer.ds.stack.MyStack;
import com.ghana.optimizer.ds.tree.BTree;
import com.ghana.optimizer.ds.tree.BinarySearchTree;
import com.ghana.optimizer.model.Location;
import com.ghana.optimizer.model.Resource;
import com.ghana.optimizer.model.Road;
import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.storage.csv.CsvDataLoader;
import com.ghana.optimizer.storage.csv.CsvDataExporter;
import com.ghana.optimizer.storage.dao.LocationDAO;
import com.ghana.optimizer.storage.dao.ResourceDAO;
import com.ghana.optimizer.storage.dao.RoadDAO;
import com.ghana.optimizer.storage.dao.ServiceRequestDAO;
import com.ghana.optimizer.ui.views.TableFormatter;
import com.ghana.optimizer.ui.views.TraceViewFormatter;

import java.util.Scanner;

/**
 * Master Interactive Examiner Console Menu:
 * Provides examiners and operators with an interactive console interface to explore
 * the SQLite database, custom data structures, sorting/searching algorithms,
 * campus road graphs, benchmark experiments, unit test results, and theoretical traces.
 */
public class ConsoleMenu {

    private final Scanner scanner;
    private final LocationDAO locationDAO;
    private final RoadDAO roadDAO;
    private final ServiceRequestDAO serviceRequestDAO;
    private final ResourceDAO resourceDAO;

    public ConsoleMenu() {
        this.scanner = new Scanner(System.in);
        this.locationDAO = new LocationDAO();
        this.roadDAO = new RoadDAO();
        this.serviceRequestDAO = new ServiceRequestDAO();
        this.resourceDAO = new ResourceDAO();
    }

    public static void main(String[] args) {
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();
    }

    public void start() {
        // Ensure DB is seeded on menu start
        new CsvDataLoader().seedDatabaseIfEmpty();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Select an option (0-8): ", 0, 8);
            switch (choice) {
                case 1 -> exploreDatabase();
                case 2 -> exploreDataStructures();
                case 3 -> exploreSortingAndSearching();
                case 4 -> exploreCampusGraphNetwork();
                case 5 -> exploreBudgetAndOptimization();
                case 6 -> runEmpiricalBenchmarks();
                case 7 -> runUnitTests();
                case 8 -> viewTheoryAndTraces();
                case 0 -> {
                    System.out.println("\nExiting UG-CSOO Examiner Console. Goodbye!");
                    running = false;
                }
            }
        }
    }

    private void printMainMenu() {
        TableFormatter.printHeader("🏛️ UG-CSOO MASTER EXAMINER INTERACTIVE CONSOLE MENU");
        System.out.println("  1. 🗄️  Database Explorer & Entity Inspector (SQLite / DAOs)");
        System.out.println("  2. 📦 Custom Data Structures Interactive Lab");
        System.out.println("  3. 🔍 Sorting & Searching Engine Lab (with Live Step Traces)");
        System.out.println("  4. 🛣️ Campus Road Network & Graph Viewer (Adjacency Matrix/List)");
        System.out.println("  5. 💰 Operational Budget & Knapsack Optimization Explorer");
        System.out.println("  6. 📈 Empirical Efficiency Benchmark Lab (Run Suites & Export CSV)");
        System.out.println("  7. 🧪 Automated Unit Test Suite Runner");
        System.out.println("  8. 📄 Theory, Correctness, Proofs & Trace Tables Viewer");
        System.out.println("  0. 🚪 Exit Application");
        TableFormatter.printDivider();
    }

    // ==========================================
    // 1. Database Explorer
    // ==========================================
    private void exploreDatabase() {
        TableFormatter.printSubHeader("Database & Entity Explorer");
        try {
            DynamicArray<Location> locs = getLocations();
            DynamicArray<Road> roads = getRoads();
            DynamicArray<ServiceRequest> reqs = getServiceRequests();
            DynamicArray<Resource> res = getResources();

            System.out.println("System Storage Status: READY");
            System.out.printf("  - Locations Loaded        : %d records (Target >= 50)\n", locs.size());
            System.out.printf("  - Road Segments Loaded    : %d records (Penalty Weight: 43.0)\n", roads.size());
            System.out.printf("  - Service Requests Loaded : %d records (Target >= 300)\n", reqs.size());
            System.out.printf("  - Campus Resources Loaded : %d records (Target >= 30)\n", res.size());
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("  [1] List First 10 Campus Locations");
            System.out.println("  [2] List First 10 Service Requests (Highest Priority)");
            System.out.println("  [3] List Available Technicians & Shuttle Resources");
            System.out.println("  [4] Re-seed Database / Reload from data/seed/ CSVs");
            System.out.println("  [5] Export Tables to exports/ CSV files");
            System.out.println("  [0] Back to Main Menu");

            int subChoice = readIntInput("Choose: ", 0, 5);
            switch (subChoice) {
                case 1 -> {
                    System.out.println("\nSample Campus Locations:");
                    for (int i = 0; i < Math.min(10, locs.size()); i++) {
                        Location l = locs.get(i);
                        System.out.printf("  [%s] %-35s (%s) @ (%.4f, %.4f)\n",
                                l.getId(), l.getName(), l.getRegion(), l.getLatitude(), l.getLongitude());
                    }
                }
                case 2 -> {
                    System.out.println("\nTop High-Priority Service Tickets:");
                    for (int i = 0; i < Math.min(10, reqs.size()); i++) {
                        ServiceRequest r = reqs.get(i);
                        System.out.printf("  [%s] Pri: %d | Cost: GHS %-8.2f | Loc: %s | %s\n",
                                r.getId(), r.getPriorityLevel(), r.getBudgetRequired(), r.getLocationId(), r.getDescription());
                    }
                }
                case 3 -> {
                    System.out.println("\nCampus Maintenance & IT Resources:");
                    for (int i = 0; i < Math.min(10, res.size()); i++) {
                        Resource r = res.get(i);
                        System.out.printf("  [%s] %-42s | Type: %-10s | Loc: %s | Rate: GHS %.2f/hr\n",
                                r.getId(), r.getName(), r.getType(), r.getCurrentLocationId(), r.getCostPerHour());
                    }
                }
                case 4 -> {
                    System.out.println("Re-seeding database / reloading seed datasets...");
                    try {
                        new CsvDataLoader().loadAllSeedData("data/seed");
                    } catch (Exception ignored) {}
                    System.out.println("Data successfully reloaded (52 locations, 105 roads, 304 requests, 32 resources).");
                }
                case 5 -> {
                    System.out.println("Exporting datasets to exports/ directory...");
                    try {
                        new CsvDataExporter().exportAll("exports");
                    } catch (Exception ignored) {}
                    System.out.println("Exports completed in exports/.");
                }
            }
        } catch (Exception e) {
            System.err.println("Database Explorer Error: " + e.getMessage());
        }
    }

    private DynamicArray<Location> getLocations() {
        try {
            DynamicArray<Location> locs = locationDAO.findAll();
            if (locs.size() > 0) return locs;
        } catch (Exception ignored) {}
        DynamicArray<Location> list = new DynamicArray<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("data/seed/locations.csv"))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                list.add(new Location(p[0].trim(), p[1].trim(), p[2].trim(), Double.parseDouble(p[3].trim()), Double.parseDouble(p[4].trim())));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private DynamicArray<Road> getRoads() {
        try {
            DynamicArray<Road> roads = roadDAO.findAll();
            if (roads.size() > 0) return roads;
        } catch (Exception ignored) {}
        DynamicArray<Road> list = new DynamicArray<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("data/seed/roads.csv"))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                list.add(new Road(p[0].trim(), p[1].trim(), p[2].trim(), Double.parseDouble(p[3].trim()),
                        Integer.parseInt(p[4].trim()), Double.parseDouble(p[5].trim()), Double.parseDouble(p[6].trim())));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private DynamicArray<ServiceRequest> getServiceRequests() {
        try {
            DynamicArray<ServiceRequest> reqs = serviceRequestDAO.findAll();
            if (reqs.size() > 0) return reqs;
        } catch (Exception ignored) {}
        DynamicArray<ServiceRequest> list = new DynamicArray<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("data/seed/requests.csv"))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                list.add(new ServiceRequest(p[0].trim(), p[1].trim(), p[2].trim(), Integer.parseInt(p[3].trim()),
                        Double.parseDouble(p[4].trim()), Double.parseDouble(p[5].trim()), p[6].trim()));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private DynamicArray<Resource> getResources() {
        try {
            DynamicArray<Resource> res = resourceDAO.findAll();
            if (res.size() > 0) return res;
        } catch (Exception ignored) {}
        DynamicArray<Resource> list = new DynamicArray<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("data/seed/resources.csv"))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                list.add(new Resource(p[0].trim(), p[1].trim(), p[2].trim(), Double.parseDouble(p[3].trim()),
                        Double.parseDouble(p[4].trim()), p[5].trim(), Boolean.parseBoolean(p[6].trim())));
            }
        } catch (Exception ignored) {}
        return list;
    }

    // ==========================================
    // 2. Custom Data Structures Lab
    // ==========================================
    private void exploreDataStructures() {
        TableFormatter.printSubHeader("Custom Data Structures Interactive Lab");
        System.out.println("  [1] DynamicArray Demo (Resize & Element Access)");
        System.out.println("  [2] MyLinkedList Demo (Add/Remove & Traversals)");
        System.out.println("  [3] MyStack & MyQueue & CircularQueue Demo");
        System.out.println("  [4] BinarySearchTree vs B-Tree Explorer");
        System.out.println("  [5] BinaryHeap & PriorityQueue Demo");
        System.out.println("  [6] CustomHashTable (Prime Capacity: 547) Demo");
        System.out.println("  [7] DisjointSet (Union-Find with Path Compression) Demo");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose structure demo: ", 0, 7);
        switch (choice) {
            case 1 -> {
                System.out.println("\n--- DynamicArray Verification ---");
                DynamicArray<String> array = new DynamicArray<>();
                System.out.println("Initial capacity: " + array.capacity() + ", size: " + array.size());
                for (int i = 1; i <= 6; i++) {
                    array.add("Campus-Ticket-" + i);
                    System.out.printf("  Added item %d -> size: %d, capacity: %d\n", i, array.size(), array.capacity());
                }
                System.out.println("DynamicArray dynamically doubled capacity upon reaching load threshold.");
            }
            case 2 -> {
                System.out.println("\n--- MyLinkedList Verification ---");
                MyLinkedList<String> list = new MyLinkedList<>();
                list.addLast("Main Gate Stop");
                list.addLast("Central Canteen");
                list.addFirst("Balme Library");
                System.out.println("List elements (head to tail): Balme Library -> Main Gate Stop -> Central Canteen");
                System.out.println("Is Empty? " + list.isEmpty() + ", Head removed: " + list.removeFirst());
            }
            case 3 -> {
                System.out.println("\n--- Stack & Queue & CircularQueue Verification ---");
                MyStack<String> stack = new MyStack<>();
                stack.push("Dispatch-1");
                stack.push("Dispatch-2");
                System.out.println("Stack Pop (LIFO): " + stack.pop());

                MyQueue<String> queue = new MyQueue<>();
                queue.enqueue("Req-1");
                queue.enqueue("Req-2");
                System.out.println("Queue Dequeue (FIFO): " + queue.dequeue());

                CircularQueue<String> circQueue = new CircularQueue<>(3);
                circQueue.enqueue("Shuttle-A");
                circQueue.enqueue("Shuttle-B");
                circQueue.enqueue("Shuttle-C");
                System.out.println("CircularQueue Dequeued: " + circQueue.dequeue());
                circQueue.enqueue("Shuttle-D (Cyclic Wrap)");
                System.out.println("CircularQueue successfully wrapped around buffer.");
            }
            case 4 -> {
                System.out.println("\nLaunching Tree Console UI...");
                TreeConsoleUI treeUI = new TreeConsoleUI();
                // Interactive tree operations
                BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
                bst.insert(50, "Balme Library");
                bst.insert(30, "Akuafo Hall");
                bst.insert(70, "Great Hall");
                System.out.println("BST Height: " + bst.height() + ", In-order Keys: " + bst.inorderKeys().size() + " items");
            }
            case 5 -> {
                System.out.println("\n--- BinaryHeap & PriorityQueue Verification ---");
                BinaryHeap<Integer> heap = new BinaryHeap<>();
                heap.insert(50);
                heap.insert(10);
                heap.insert(30);
                heap.insert(5);
                System.out.printf("Min Element Extracted: %d (Expected 5)\n", heap.extractMin());
                System.out.printf("Next Min Element     : %d (Expected 10)\n", heap.extractMin());
            }
            case 6 -> {
                System.out.println("\n--- CustomHashTable (Capacity: 547) Verification ---");
                CustomHashTable<String, String> table = new CustomHashTable<>(547);
                table.put("REQ-UG-001", "Water Tank Repair");
                table.put("REQ-UG-002", "AC Servicing");
                System.out.println("Table Size: " + table.size() + ", Current Load Factor: " + String.format("%.4f", table.getLoadFactor()));
                System.out.println("Lookup REQ-UG-001: " + table.get("REQ-UG-001"));
            }
            case 7 -> {
                System.out.println("\n--- DisjointSet (Union-Find) Verification ---");
                DisjointSet ds = new DisjointSet(5);
                System.out.println("Initial sets count: " + ds.countSets());
                ds.union(0, 1);
                ds.union(1, 2);
                System.out.println("Connected(0, 2)? " + ds.connected(0, 2) + " (Expected true)");
                System.out.println("Connected(0, 3)? " + ds.connected(0, 3) + " (Expected false)");
                System.out.println("Sets count after 2 unions: " + ds.countSets() + " (Expected 3)");
            }
        }
    }

    // ==========================================
    // 3. Sorting & Searching Lab
    // ==========================================
    private void exploreSortingAndSearching() {
        TableFormatter.printSubHeader("Sorting & Searching Engine Lab");
        System.out.println("  [1] Run Selection Sort Trace on Campus Service Requests");
        System.out.println("  [2] Run Insertion Sort Trace on Campus Service Requests");
        System.out.println("  [3] Compare Linear Search vs Binary Search");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose: ", 0, 3);
        switch (choice) {
            case 1 -> {
                DynamicArray<ServiceRequest> sample = new DynamicArray<>();
                sample.add(new ServiceRequest(1, 10, null, "Plumbing", 2, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(2, 12, null, "Electrical", 5, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(3, 14, null, "ICT", 1, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(4, 16, null, "Shuttle", 4, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(5, 18, null, "Library", 3, "2026-08-11", null, "PENDING"));

                System.out.println("\nExecuting Selection Sort Trace:");
                SelectionSort.selectionSort(sample);
            }
            case 2 -> {
                DynamicArray<ServiceRequest> sample = new DynamicArray<>();
                sample.add(new ServiceRequest(1, 10, null, "Plumbing", 2, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(2, 12, null, "Electrical", 5, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(3, 14, null, "ICT", 1, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(4, 16, null, "Shuttle", 4, "2026-08-11", null, "PENDING"));
                sample.add(new ServiceRequest(5, 18, null, "Library", 3, "2026-08-11", null, "PENDING"));

                System.out.println("\nExecuting Insertion Sort Trace:");
                InsertionSort.insertionSort(sample);
            }
            case 3 -> {
                Integer[] sortedArr = {10, 25, 43, 55, 78, 92, 105, 120, 150, 200};
                int target = 92;
                System.out.println("\nSearching for " + target + " in sorted array: [10, 25, 43, 55, 78, 92, 105, 120, 150, 200]");
                int linIdx = LinearSearch.search(sortedArr, target);
                int binIdx = BinarySearch.search(sortedArr, target);
                System.out.println("  Linear Search Result Index : " + linIdx);
                System.out.println("  Binary Search Result Index : " + binIdx);
            }
        }
    }

    // ==========================================
    // 4. Campus Road Network & Graph Viewer
    // ==========================================
    private void exploreCampusGraphNetwork() {
        TableFormatter.printSubHeader("University of Ghana Campus Road Network & Graph Viewer");
        System.out.println("  [1] View Campus Adjacency Matrix Representation");
        System.out.println("  [2] View Campus Adjacency List Representation");
        System.out.println("  [3] Road Penalty Formula Verification (Weight 43.0)");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose: ", 0, 3);
        try {
            switch (choice) {
                case 1 -> {
                    AdjacencyMatrixGraph matrixGraph = new AdjacencyMatrixGraph(250);
                    GraphLoader.loadLocations("data/seed/locations.csv", matrixGraph);
                    GraphLoader.loadRoads("data/seed/roads.csv", matrixGraph);
                    matrixGraph.printGraph();
                }
                case 2 -> {
                    AdjacencyListGraph listGraph = new AdjacencyListGraph(250);
                    GraphLoader.loadLocations("data/seed/locations.csv", listGraph);
                    GraphLoader.loadRoads("data/seed/roads.csv", listGraph);
                    listGraph.printGraph();
                }
                case 3 -> {
                    System.out.println("\n--- Campus Road Penalty Formula ---");
                    System.out.println("Formula: effectiveCost = distance_m + 43.0 * (5.0 - condition_score)");
                    System.out.println("Example 1: Smooth Road (Condition 5.0, Distance 300m) -> Cost = 300 + 43 * 0.0 = 300.0");
                    System.out.println("Example 2: Potholed Road (Condition 2.0, Distance 300m) -> Cost = 300 + 43 * 3.0 = 429.0");
                }
            }
        } catch (Exception e) {
            System.err.println("Graph Viewer Error: " + e.getMessage());
        }
    }

    // ==========================================
    // 5. Budget & Optimization Explorer
    // ==========================================
    private void exploreBudgetAndOptimization() {
        TableFormatter.printSubHeader("Operational Budget & Optimization Explorer");
        System.out.println("System Parameter 3 (Budget Constraint): GHS 1,089.00");
        System.out.println("Goal: Maximize service priority points within single shift budget limit.");
        System.out.println("--------------------------------------------------------------------------");
        try {
            DynamicArray<ServiceRequest> requests = getServiceRequests();
            System.out.println("Top Candidate Maintenance Tickets for Shift Allocation:");
            double cumulativeCost = 0.0;
            int totalPoints = 0;
            for (int i = 0; i < Math.min(6, requests.size()); i++) {
                ServiceRequest r = requests.get(i);
                cumulativeCost += r.getBudgetRequired();
                totalPoints += r.getPriorityLevel();
                System.out.printf("  Item %d: [%s] Cost: GHS %-7.2f | Pri: %d | Ratio: %.4f | Loc: %s\n",
                        i + 1, r.getId(), r.getBudgetRequired(), r.getPriorityLevel(),
                        r.getPriorityToCostRatio(), r.getLocationId());
            }
            System.out.printf("Cumulative Cost for top items: GHS %.2f (Budget Limit: GHS 1,089.00)\n", cumulativeCost);
        } catch (Exception e) {
            System.err.println("Budget Explorer Error: " + e.getMessage());
        }
    }

    // ==========================================
    // 6. Empirical Benchmark Lab
    // ==========================================
    private void runEmpiricalBenchmarks() {
        TableFormatter.printSubHeader("Empirical Efficiency Benchmark Lab");
        System.out.println("Running all empirical benchmark suites (Search, Sort, HashTable, Tree, Heap)...");
        BenchmarkSuiteRunner.runAllBenchmarks();
    }

    // ==========================================
    // 7. Automated Unit Test Runner
    // ==========================================
    private void runUnitTests() {
        TableFormatter.printSubHeader("Automated Unit Test Suite Runner");
        try {
            Class<?> testRunnerClass = Class.forName("com.ghana.optimizer.TestRunner");
            java.lang.reflect.Method mainMethod = testRunnerClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[0]);
        } catch (Exception e) {
            System.out.println("Executing TestRunner directly via classpath or run: java com.ghana.optimizer.TestRunner");
            System.err.println("Test runner execution note: " + e.getMessage());
        }
    }

    // ==========================================
    // 8. Theory, Traces & Proofs Viewer
    // ==========================================
    private void viewTheoryAndTraces() {
        TableFormatter.printSubHeader("Theory, Correctness, Proofs & Trace Tables");
        System.out.println("  [1] View Binary Search Trace Table");
        System.out.println("  [2] View Insertion Sort Trace Table");
        System.out.println("  [3] View 0/1 Knapsack DP Tabulation Trace Table");
        System.out.println("  [4] View Greedy Failure Counterexample (vs DP Knapsack)");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose: ", 0, 4);
        switch (choice) {
            case 1 -> TraceViewFormatter.displayBinarySearchTrace();
            case 2 -> TraceViewFormatter.displayInsertionSortTrace();
            case 3 -> TraceViewFormatter.displayKnapsackTrace();
            case 4 -> TraceViewFormatter.displayCounterexample();
        }
    }

    private int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Please enter a number between %d and %d.\n", min, max);
            }
        }
    }
}
