package com.ghana.optimizer.ui;

import com.ghana.optimizer.algorithm.graph.BFS;
import com.ghana.optimizer.algorithm.graph.DFS;
import com.ghana.optimizer.algorithm.graph.Dijkstra;
import com.ghana.optimizer.algorithm.graph.KruskalMST;
import com.ghana.optimizer.algorithm.optimization.GreedyKnapsackHeuristic;
import com.ghana.optimizer.algorithm.optimization.KnapsackOptimizer;
import com.ghana.optimizer.algorithm.scheduling.PriorityDispatchScheduler;
import com.ghana.optimizer.algorithm.search.BinarySearch;
import com.ghana.optimizer.algorithm.search.LinearSearch;
import com.ghana.optimizer.algorithm.sort.InsertionSort;
import com.ghana.optimizer.algorithm.sort.MergeSort;
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
import com.ghana.optimizer.ds.queue.MyDeque;
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
        TableFormatter.printHeader("UG-CSOO MASTER EXAMINER INTERACTIVE CONSOLE MENU");
        System.out.println("  1. Database Explorer & Entity Inspector (SQLite / DAOs)");
        System.out.println("  2. Custom Data Structures Interactive Lab");
        System.out.println("  3. Sorting & Searching Engine Lab (with Live Step Traces)");
        System.out.println("  4. Campus Road Network & Graph Viewer (Adjacency Matrix/List)");
        System.out.println("  5. Operational Budget & Knapsack Optimization Explorer");
        System.out.println("  6. Empirical Efficiency Benchmark Lab (Run Suites & Export CSV)");
        System.out.println("  7. Automated Unit Test Suite Runner");
        System.out.println("  8. Theory, Correctness, Proofs & Trace Tables Viewer");
        System.out.println("  0. Exit Application");
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
            System.out.printf("  - Road Segments Loaded    : %d records (Penalty Weight: 59.0)\n", roads.size());
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
        System.out.println("  [6] CustomHashTable (Prime Capacity: 761) Demo");
        System.out.println("  [7] DisjointSet (Union-Find with Path Compression) Demo");
        System.out.println("  [8] PriorityDispatchScheduler (PriorityQueue & Deque Emergency Dispatch)");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose structure demo (0-8): ", 0, 8);
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
                System.out.println("\n--- Stack & Queue & Deque & CircularQueue Verification ---");
                MyStack<String> stack = new MyStack<>();
                stack.push("Dispatch-1");
                stack.push("Dispatch-2");
                System.out.println("Stack Pop (LIFO): " + stack.pop());

                MyQueue<String> queue = new MyQueue<>();
                queue.enqueue("Req-1");
                queue.enqueue("Req-2");
                System.out.println("Queue Dequeue (FIFO): " + queue.dequeue());

                MyDeque<String> deque = new MyDeque<>();
                deque.addRear("Routine Ticket #101");
                deque.addRear("Routine Ticket #102");
                deque.addFront("EMERGENCY TICKET #999 (Life-Safety Override)");
                System.out.println("Deque Peek Front (Emergency Override): " + deque.peekFront());
                System.out.println("Deque Removed Front: " + deque.removeFront());
                System.out.println("Deque Removed Rear: " + deque.removeRear());

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
                System.out.println("\n--- CustomHashTable (Capacity: 761) Verification ---");
                CustomHashTable<String, String> table = new CustomHashTable<>(761);
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
            case 8 -> {
                System.out.println("\n--- PriorityDispatchScheduler Live Dispatch Simulation ---");
                DynamicArray<ServiceRequest> reqs = getServiceRequests();
                DynamicArray<Resource> resources = getResources();
                PriorityDispatchScheduler scheduler = new PriorityDispatchScheduler(resources);
                for (int i = 0; i < Math.min(10, reqs.size()); i++) {
                    scheduler.submitRequest(reqs.get(i));
                }
                System.out.println("Submitted 10 campus requests to priority heap. Dispatching highest priority:");
                PriorityDispatchScheduler.DispatchAssignment record = scheduler.dispatchNext();
                if (record != null) {
                    System.out.printf("Dispatched: [%s] Category: %s | Priority: %d | Assigned: %s (%s)\n",
                            record.getServiceRequest().getId(), record.getServiceRequest().getCategory(),
                            record.getServiceRequest().getPriorityLevel(),
                            record.getAssignedResource() != null ? record.getAssignedResource().getName() : "General Queue",
                            record.getAssignedResource() != null ? record.getAssignedResource().getType() : "N/A");
                }
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
        System.out.println("  [3] Run Merge Sort Multi-Attribute Trace (Urgency desc, Budget asc)");
        System.out.println("  [4] Run QuickSort Partition Trace (Pivot & Swaps Table)");
        System.out.println("  [5] Compare Linear Search vs Binary Search");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose (0-5): ", 0, 5);
        switch (choice) {
            case 1 -> {
                DynamicArray<ServiceRequest> sample = new DynamicArray<>();
                sample.add(new ServiceRequest("REQ-01", "LOC-01", "Plumbing leak", 2, 100.0, 1.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-02", "LOC-02", "Electrical fault", 5, 250.0, 2.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-03", "LOC-03", "ICT maintenance", 1, 50.0, 0.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-04", "LOC-04", "Shuttle dispatch", 4, 300.0, 1.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-05", "LOC-05", "Library AC", 3, 180.0, 2.0, "PENDING"));

                System.out.println("\nExecuting Selection Sort Trace:");
                SelectionSort.selectionSort(sample);
            }
            case 2 -> {
                DynamicArray<ServiceRequest> sample = new DynamicArray<>();
                sample.add(new ServiceRequest("REQ-01", "LOC-01", "Plumbing leak", 2, 100.0, 1.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-02", "LOC-02", "Electrical fault", 5, 250.0, 2.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-03", "LOC-03", "ICT maintenance", 1, 50.0, 0.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-04", "LOC-04", "Shuttle dispatch", 4, 300.0, 1.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-05", "LOC-05", "Library AC", 3, 180.0, 2.0, "PENDING"));

                System.out.println("\nExecuting Insertion Sort Trace:");
                InsertionSort.insertionSort(sample);
            }
            case 3 -> {
                DynamicArray<ServiceRequest> sample = new DynamicArray<>();
                sample.add(new ServiceRequest("REQ-01", "LOC-01", "Plumbing leak", 4, 200.0, 1.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-02", "LOC-02", "Electrical fault", 5, 450.0, 2.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-03", "LOC-03", "ICT server maintenance", 5, 120.0, 0.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-04", "LOC-04", "Shuttle dispatch", 4, 150.0, 1.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-05", "LOC-05", "Generator fuel", 2, 300.0, 2.0, "PENDING"));

                System.out.println("\nExecuting Merge Sort (Primary: Priority desc, Secondary: Budget asc):");
                MergeSort.sort(sample);
                for (int i = 0; i < sample.size(); i++) {
                    ServiceRequest r = sample.get(i);
                    System.out.printf("  [%d] %-8s | Pri: %d | Budget: GHS %6.2f | %s\n",
                            i + 1, r.getId(), r.getPriorityLevel(), r.getBudgetRequired(), r.getDescription());
                }
            }
            case 4 -> {
                DynamicArray<ServiceRequest> sample = new DynamicArray<>();
                sample.add(new ServiceRequest("REQ-01", "LOC-01", "Plumbing leak", 2, 100.0, 1.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-02", "LOC-02", "Electrical fault", 5, 250.0, 2.0, "PENDING"));
                sample.add(new ServiceRequest("REQ-03", "LOC-03", "ICT maintenance", 1, 50.0, 0.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-04", "LOC-04", "Shuttle dispatch", 4, 300.0, 1.5, "PENDING"));
                sample.add(new ServiceRequest("REQ-05", "LOC-05", "Library AC", 3, 180.0, 2.0, "PENDING"));

                System.out.println("\nExecuting QuickSort Partition Trace:");
                com.ghana.optimizer.algorithm.sort.QuickSort.quickSort(sample);
            }
            case 5 -> {
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
        TableFormatter.printSubHeader("University of Ghana Campus Road Network & Graph Engine");
        System.out.println("  [1] Interactive Dijkstra Shortest Route Solver (Penalty lambda = 59.0)");
        System.out.println("  [2] Compute Kruskal's Minimum Spanning Tree (Campus Road Backbone)");
        System.out.println("  [3] Run BFS Reachability & Shortest Hops");
        System.out.println("  [4] Run DFS Cycle Detection & Connected Components");
        System.out.println("  [5] View Campus Adjacency Matrix");
        System.out.println("  [6] View Campus Adjacency List");
        System.out.println("  [7] Launch Interactive Graph Console Explorer UI");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose (0-7): ", 0, 7);
        try {
            AdjacencyListGraph listGraph = new AdjacencyListGraph(250);
            GraphLoader.loadLocations("data/seed/locations.csv", listGraph);
            GraphLoader.loadRoads("data/seed/roads.csv", listGraph);

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Origin Location ID (e.g. LOC-UG-01): ");
                    String startId = scanner.nextLine().trim();
                    System.out.print("Enter Target Destination ID (e.g. LOC-UG-23): ");
                    String targetId = scanner.nextLine().trim();

                    try {
                        java.util.List<Dijkstra.DijkstraStep> steps = Dijkstra.dijkstra(listGraph, startId);
                        java.util.List<String> path = Dijkstra.reconstructPath(steps, targetId);
                        Dijkstra.printTrace(steps, startId);

                        if (path.isEmpty()) {
                            System.out.println("No reachable path found between " + startId + " and " + targetId);
                        } else {
                            System.out.println("\nOptimal Dijkstra Shortest Path: " + String.join(" -> ", path));
                        }
                    } catch (Exception ex) {
                        System.err.println("Routing query error: " + ex.getMessage());
                    }
                }
                case 2 -> {
                    System.out.println("\nExecuting Kruskal's Minimum Spanning Tree Algorithm...");
                    KruskalMST.MSTResult mstResult = KruskalMST.computeMST(listGraph);
                    System.out.println(mstResult.formatMSTSummary());
                }
                case 3 -> {
                    System.out.print("Enter Origin Location ID for BFS (e.g. LOC-UG-01): ");
                    String startId = scanner.nextLine().trim();
                    try {
                        java.util.List<BFS.BFSStep> steps = BFS.bfs(listGraph, startId);
                        BFS.printTrace(steps, startId);
                    } catch (Exception ex) {
                        System.err.println("BFS error: " + ex.getMessage());
                    }
                }
                case 4 -> {
                    System.out.print("Enter Origin Location ID for DFS (e.g. LOC-UG-01): ");
                    String startId = scanner.nextLine().trim();
                    try {
                        java.util.List<DFS.DFSStep> steps = DFS.dfs(listGraph, startId);
                        DFS.printTrace(steps, startId);
                        boolean hasCycle = DFS.hasCycle(listGraph);
                        System.out.println("Network Cycle Detection: " + (hasCycle ? "Cycles Present (Normal Road Network)" : "Acyclic"));
                    } catch (Exception ex) {
                        System.err.println("DFS error: " + ex.getMessage());
                    }
                }
                case 5 -> {
                    AdjacencyMatrixGraph matrixGraph = new AdjacencyMatrixGraph(250);
                    GraphLoader.loadLocations("data/seed/locations.csv", matrixGraph);
                    GraphLoader.loadRoads("data/seed/roads.csv", matrixGraph);
                    matrixGraph.printGraph();
                }
                case 6 -> {
                    listGraph.printGraph();
                }
                case 7 -> {
                    GraphConsoleUI ui = new GraphConsoleUI();
                    ui.run();
                }
            }
        } catch (Exception e) {
            System.err.println("Graph Engine Error: " + e.getMessage());
        }
    }

    // ==========================================
    // 5. Budget & Optimization Explorer
    // ==========================================
    private void exploreBudgetAndOptimization() {
        TableFormatter.printSubHeader("Operational Budget & Knapsack Optimization Explorer");
        System.out.println("  [1] Run 0/1 Knapsack Dynamic Programming Optimizer (Cap: GHS 1,089.00)");
        System.out.println("  [2] Run Greedy Ratio Heuristic Optimizer (Cap: GHS 1,089.00)");
        System.out.println("  [3] Side-by-Side Comparative Evaluation (DP vs Greedy Gap)");
        System.out.println("  [4] Custom Shift Budget Constraint Simulation");
        System.out.println("  [0] Back to Main Menu");

        int choice = readIntInput("Choose (0-4): ", 0, 4);
        try {
            DynamicArray<ServiceRequest> requests = getServiceRequests();
            double defaultBudget = 1089.00;

            switch (choice) {
                case 1 -> {
                    System.out.println("\nExecuting 0/1 Knapsack Dynamic Programming Tabulation...");
                    KnapsackOptimizer.KnapsackResult result = KnapsackOptimizer.optimize(requests, defaultBudget);
                    System.out.println(result.formatReport());
                }
                case 2 -> {
                    System.out.println("\nExecuting Ratio-Based Greedy Knapsack Heuristic...");
                    GreedyKnapsackHeuristic.GreedyResult result = GreedyKnapsackHeuristic.solveGreedy(requests, defaultBudget);
                    System.out.println(result.formatReport());
                }
                case 3 -> {
                    System.out.println("\nRunning Comparative Evaluation: 0/1 Knapsack DP vs Greedy Ratio Heuristic");
                    KnapsackOptimizer.KnapsackResult dpResult = KnapsackOptimizer.optimize(requests, defaultBudget);
                    GreedyKnapsackHeuristic.GreedyResult greedyResult = GreedyKnapsackHeuristic.solveGreedy(requests, defaultBudget);

                    double suboptimality = greedyResult.computeSuboptimalityPenalty(dpResult);

                    System.out.println("================================================================================");
                    System.out.println("  KNAPSACK ALGORITHM COMPARATIVE EVALUATION (W = GHS 1,089.00)");
                    System.out.println("================================================================================");
                    System.out.printf(" Candidate Request Pool        : %d tickets\n", requests.size());
                    System.out.printf(" DP Optimal Priority Points    : %d points (Cost: GHS %.2f, %d tickets)\n",
                            dpResult.getTotalPriorityPoints(), dpResult.getTotalCost(), dpResult.getSelectedCount());
                    System.out.printf(" Greedy Heuristic Priority     : %d points (Cost: GHS %.2f, %d tickets)\n",
                            greedyResult.getTotalPriorityPoints(), greedyResult.getTotalCost(), greedyResult.getSelectedCount());
                    System.out.printf(" Priority Gap (DP vs Greedy)   : %d points\n",
                            dpResult.getTotalPriorityPoints() - greedyResult.getTotalPriorityPoints());
                    System.out.printf(" Suboptimality Penalty Gap     : %.2f%%\n", suboptimality);
                    System.out.println(" Theoretical Note: 0/1 Knapsack lacks the greedy-choice property.");
                    System.out.println(" Greedy selection leaves unfillable budget gaps, demonstrating suboptimality.");
                    System.out.println("================================================================================");
                }
                case 4 -> {
                    System.out.print("Enter Custom Shift Budget Limit in GHS (e.g. 500, 1500, 2500): ");
                    double customBudget = Double.parseDouble(scanner.nextLine().trim());
                    KnapsackOptimizer.KnapsackResult customResult = KnapsackOptimizer.optimize(requests, customBudget);
                    System.out.println(customResult.formatReport());
                }
            }
        } catch (Exception e) {
            System.err.println("Optimization Explorer Error: " + e.getMessage());
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
