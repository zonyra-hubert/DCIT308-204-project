# University of Ghana • Department of Computer Science
## DCIT 204 / DCIT 308 — Data Structures and Algorithms
### Comprehensive Final Project Technical Report

---

# 1. Cover Page

* **Project Title**: Campus Service Operations Optimizer (UG-CSOO)
* **Local Ghana Context & Selected Organisation**: Physical Development and Municipal Services Directorate (PDMSD), University of Ghana, Legon Campus, Accra, Ghana (1,200 Hectares)
* **Course**: DCIT 204 / DCIT 308 Data Structures and Algorithms
* **Department**: Department of Computer Science, University of Ghana
* **Team Project Group**: 15-Member Project Team
* **Group Index Sum**: $333,907,258$
* **Derived System Constants**:
  - **Parameter 1 (Road Penalty Weight $\lambda$)**: **`59.0`** $\implies (333,907,258 \bmod 100) + 1 = 59.0$
  - **Parameter 2 (Prime Hash Capacity $M$)**: **`761`** $\implies$ First prime $\ge (333,907,258 \bmod 500) + 501 = 761$
  - **Parameter 3 (Shift Budget Limit $W$)**: **`GHS 1,089.00`** $\implies ((22404243 - 10948210) \bmod 1000) + 100 = 1089.00$

---

# 2. Problem Statement, Assumptions, Input-Output Definitions and System Boundaries

### 2.1 Local Ghana Context & Problem Statement
The University of Ghana Legon Campus spans over 1,200 hectares, serving over 60,000 students, faculty, and administrative staff across 200 physical buildings, hostels, and departments connected by 200 main and feeder road segments. The Physical Development and Municipal Services Directorate (PDMSD) manages hundreds of daily maintenance service tickets—including electrical power outages, plumbing pipe bursts, ICT network dispatches, and shuttle transit operations.

Manual, ad-hoc dispatching faces three major operational bottlenecks:
1. **Pavement Degradation & Road Quality Disparities**: Road quality varies from smooth asphalt ($\text{condition}=5.0$) to degraded pothole feeder roads ($\text{condition}=1.0$). Standard shortest-distance routing causes vehicle damage and severe transit delays.
2. **Shift Budget Ceiling**: PDMSD operates under a hard operational shift budget constraint of $\text{GHS } 1,089.00$. Selecting maintenance tickets greedily leaves budget gaps and fails to maximize total priority points resolved.
3. **Life-Safety Emergency Override**: Routine tickets often clog queues, delaying urgent emergency dispatches (e.g. high-voltage transformer sparks).

### 2.2 System Assumptions
1. **Network Topology**: Legon campus road network is modeled as a weighted graph $G = (V, E)$ with $V = 200$ locations and $E = 200$ bidirectional road edges.
2. **Road Degradation Weighting**: Effective road travel cost incorporates distance and pavement condition:
   $$\text{effectiveCost} = \text{distance\_m} + 59.0 \times (5.0 - \text{condition\_score})$$
3. **Shift Budget Ceiling**: Total expenditure on chosen service tickets cannot exceed $\text{GHS } 1,089.00$.

### 2.3 Input-Output Definitions
- **Inputs**:
  - `locations.csv` (200 records): Location ID, Name, Region, Latitude, Longitude.
  - `roads.csv` (200 records): Road ID, Source Location ID, Destination Location ID, Distance (m), Speed Limit, Condition Score (1.0..5.0), Penalty Factor (59.0).
  - `requests.csv` (200 records): Request ID, Location ID, Description, Priority Level (1..5), Budget Required (GHS), Estimated Duration (hrs), Status.
  - `resources.csv` (200 records): Resource ID, Name, Type, Capacity, Hourly Rate, Location ID, Availability.
- **Outputs**:
  - **Optimal Dijkstra Route**: Shortest penalty-weighted path sequence and total effective cost.
  - **Optimal Knapsack Subset**: 0/1 DP subset of tickets maximizing total priority points under GHS 1,089.00.
  - **Dispatch Resolution Records**: Resource assignment, attendance speed, cost, and notes.

### 2.4 System Boundaries
The core system logic operates entirely with **100% custom data structures** (zero `java.util` collections). Data persistence is provided by SQLite (`data/ghana_optimizer.db`) with an automatic CSV direct fallback engine.

---

# 3. Dataset Description, Data Dictionary and Database Schema

### 3.1 Dataset Overview
The project incorporates **800 authentic campus records** across 4 primary CSV datasets:

```
===================================================================================
Dataset File        Record Count   Entity Type          Primary Keys & Key Fields
===================================================================================
data/seed/locations.csv  200       Campus Locations     id (LOC-UG-001..200), name, GPS
data/seed/roads.csv      200       Campus Road Segments id (RD-UG-001..200), distance, cond
data/seed/requests.csv   200       Service Tickets      id (REQ-UG-001..200), priority, budget
data/seed/resources.csv  200       Maintenance Units    id (RES-UG-001..200), type, rate
===================================================================================
```

### 3.2 Relational Database Schema (`data/sql/schema.sql`)
```sql
CREATE TABLE IF NOT EXISTS locations (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    region TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS roads (
    id TEXT PRIMARY KEY,
    source_id TEXT NOT NULL,
    destination_id TEXT NOT NULL,
    distance_m REAL NOT NULL,
    speed_limit_kmh INTEGER NOT NULL,
    condition_score REAL NOT NULL,
    penalty_weight REAL DEFAULT 59.0,
    FOREIGN KEY (source_id) REFERENCES locations(id),
    FOREIGN KEY (destination_id) REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS service_requests (
    id TEXT PRIMARY KEY,
    location_id TEXT NOT NULL,
    description TEXT NOT NULL,
    priority_level INTEGER NOT NULL,
    budget_required REAL NOT NULL,
    estimated_duration_hrs REAL NOT NULL,
    status TEXT DEFAULT 'PENDING',
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS resources (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    capacity REAL NOT NULL,
    hourly_rate_ghs REAL NOT NULL,
    location_id TEXT NOT NULL,
    available INTEGER DEFAULT 1,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);
```

---

# 4. System Architecture and Module Design

```
+-----------------------------------------------------------------------------------+
|                            UG-CSOO MAIN APPLICATION ENGINE                        |
|                                (com.ghana.optimizer.Main)                         |
+-----------------------------------------------------------------------------------+
                                         |
     +-----------------------------------+-----------------------------------+
     |                                   |                                   |
     v                                   v                                   v
+-----------------------+   +-----------------------+   +-----------------------+
|  EXAMINER CONSOLE UI  |   |   AUTOMATED TESTS     |   | BENCHMARK SUITE RUNNER|
| (ConsoleMenu / TreeUI)|   |     (TestRunner)      |   | (BenchmarkSuiteRunner)|
+-----------------------+   +-----------------------+   +-----------------------+
     |                                   |                                   |
     +-----------------------------------+-----------------------------------+
                                         |
                                         v
+-----------------------------------------------------------------------------------+
|                               ALGORITHM ENGINE LAYER                              |
|  - Graph: Dijkstra Shortest Path, Kruskal MST, Prim MST, BFS, DFS                 |
|  - Optimization: 0/1 Knapsack DP Tabulation Solver, Greedy Ratio Heuristic        |
|  - Scheduling: Multi-Strategy PriorityDispatchScheduler (FIFO, Round-Robin, Heap) |
|  - Sort & Search: BinarySearch, LinearSearch, MergeSort, QuickSort, SelectionSort |
+-----------------------------------------------------------------------------------+
                                         |
                                         v
+-----------------------------------------------------------------------------------+
|                            CUSTOM DATA STRUCTURES LAYER                           |
|  DynamicArray, MyLinkedList, MyStack, MyQueue, MyDeque, CircularQueue,            |
|  CustomHashTable (M=761), BinaryHeap, BinarySearchTree, BTree (t=2), DisjointSet  |
+-----------------------------------------------------------------------------------+
                                         |
                                         v
+-----------------------------------------------------------------------------------+
|                              PERSISTENCE & DATA LAYER                             |
|       SQLite DB (data/ghana_optimizer.db)  <--->  CSV Direct Fallback Loader     |
|            (LocationDAO, RoadDAO, ServiceRequestDAO, ResourceDAO)                 |
+-----------------------------------------------------------------------------------+
```

---

# 5. Data-Structure Implementation Section with Diagrams and Explanations

### 5.1 Custom Data Structures Specification Table

```
========================================================================================================
Data Structure Class   Package          Storage Mechanism         Key Operations & Time Complexity
========================================================================================================
DynamicArray<T>        ds.list          Dynamic 1D Array (2x)     add O(1) amortized, get O(1)
MyLinkedList<T>        ds.list          Doubly-Linked Nodes       addFirst/addLast O(1), remove O(N)
MyStack<T>             ds.stack         LIFO Nodes / Array        push O(1), pop O(1), peek O(1)
MyQueue<E>             ds.queue         FIFO Nodes / Array        enqueue O(1), dequeue O(1)
MyDeque<T>             ds.queue         Doubly-Linked Deque       addFront/addRear O(1), remove O(1)
CircularQueue<E>       ds.queue         Array Modulo Ring Buffer  enqueue O(1), dequeue O(1)
CustomHashTable<K,V>   ds.hash          Separate Chaining (M=761) put O(1) avg, get O(1) avg
BinaryHeap<T>          ds.heap          1D Array Complete Tree    insert O(log N), extractMin O(log N)
BinarySearchTree<K,V>  ds.tree          Binary Search Tree        insert O(log N), height O(N) worst
BTree<K,V>             ds.tree          Multi-way 2-3-4 Tree(t=2) insert O(log N), height O(log N) guaranteed
DisjointSet            ds.disjoint      Rank & Path Compression   find O(alpha(N)), union O(alpha(N))
AdjacencyMatrixGraph   ds.graph.matrix  200x200 2D Double Array   addEdge O(1), getWeight O(1)
AdjacencyListGraph     ds.graph.list    Array of DynamicArrays    addVertex O(1), addEdge O(1)
========================================================================================================
```

### 5.2 Key Structure Explanations & Diagrams

1. **`CircularQueue<E>` (Modulo Ring Buffer)**:
   - Uses `rear = (rear + 1) % capacity` pointer arithmetic.
   - **Diagram**:
     ```
     [ Shuttle-D (Wrapped) ]  [ Shuttle-B ]  [ Shuttle-C ]
             ^                     ^
             |                     |
          Rear=0                Front=1
     ```
   - **Advantage**: Reuses freed array slots at index 0 in $O(1)$ time with **zero element shifting**.

2. **`MyDeque<T>` (Emergency Override Deque)**:
   - Doubly-linked node list supporting $O(1)$ front and rear operations.
   - Level 5 emergency tickets call `addFront()`, immediately bypassing routine tickets waiting at the rear.

3. **`CustomHashTable<K,V>` (Prime Capacity $761$)**:
   - Separate chaining hash table using prime initial capacity $M = 761$.
   - Index formula: `index = (|key.hashCode()| % 761)`. Provides zero-clustering $O(1)$ expected lookup for campus entity String IDs.

---

# 6. Algorithm Implementation Section with Pseudocode and Java Snippets

### 6.1 Dijkstra Shortest Path Algorithm

**Pseudocode**:
```text
Algorithm Dijkstra(Graph G, Source s, PenaltyWeight λ):
    Initialize dist[v] = ∞ for all v ∈ V, dist[s] = 0
    Initialize minHeap = PriorityQueue()
    minHeap.insert(s, 0)

    while minHeap is not empty:
        u = minHeap.extractMin()
        for each neighbor v of u:
            effectiveWeight = weight(u,v) + λ * (5.0 - condition(u,v))
            if dist[u] + effectiveWeight < dist[v]:
                dist[v] = dist[u] + effectiveWeight
                prev[v] = u
                minHeap.insert(v, dist[v])
```

**Selected Java Snippet (`Dijkstra.java`)**:
```java
public static ShortestPathResult computeShortestPath(AdjacencyListGraph graph, String sourceId, String targetId) {
    DynamicArray<Vertex> vertices = graph.getAllVertices();
    CustomHashTable<String, Double> dist = new CustomHashTable<>(761);
    CustomHashTable<String, String> prev = new CustomHashTable<>(761);
    BinaryHeap<PathNode> minHeap = new BinaryHeap<>();

    for (int i = 0; i < vertices.size(); i++) {
        dist.put(vertices.get(i).getId(), Double.MAX_VALUE);
    }
    dist.put(sourceId, 0.0);
    minHeap.insert(new PathNode(sourceId, 0.0));

    while (!minHeap.isEmpty()) {
        PathNode curr = minHeap.extractMin();
        if (curr.getId().equals(targetId)) break;

        DynamicArray<Edge> neighbors = graph.getNeighbors(curr.getId());
        for (int i = 0; i < neighbors.size(); i++) {
            Edge e = neighbors.get(i);
            double effectiveCost = e.getDistanceM() + 59.0 * (5.0 - e.getConditionScore());
            double newDist = dist.get(curr.getId()) + effectiveCost;
            if (newDist < dist.get(e.getDestinationId())) {
                dist.put(e.getDestinationId(), newDist);
                prev.put(e.getDestinationId(), curr.getId());
                minHeap.insert(new PathNode(e.getDestinationId(), newDist));
            }
        }
    }
    return reconstructPath(sourceId, targetId, dist, prev);
}
```

### 6.2 0/1 Knapsack Dynamic Programming Solver

**Pseudocode**:
```text
Algorithm KnapsackDP(Tickets R, ShiftBudget W):
    N = length(R)
    Initialize 2D array DP[N+1][W+1] = 0

    for i = 1 to N:
        c_i = cost(R[i]), p_i = priority(R[i])
        for w = 0 to W:
            if c_i <= w:
                DP[i][w] = max(DP[i-1][w], DP[i-1][w - c_i] + p_i)
            else:
                DP[i][w] = DP[i-1][w]

    Reconstruct chosen tickets by backtracking from DP[N][W]
```

**Selected Java Snippet (`KnapsackOptimizer.java`)**:
```java
public static KnapsackResult optimize(DynamicArray<ServiceRequest> requests, int budgetLimitGHS) {
    int n = requests.size();
    int W = budgetLimitGHS;
    int[][] dp = new int[n + 1][W + 1];

    for (int i = 1; i <= n; i++) {
        ServiceRequest req = requests.get(i - 1);
        int cost = (int) Math.ceil(req.getBudgetRequired());
        int priority = req.getPriorityLevel();

        for (int w = 0; w <= W; w++) {
            if (cost <= w) {
                dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - cost] + priority);
            } else {
                dp[i][w] = dp[i - 1][w];
            }
        }
    }

    DynamicArray<ServiceRequest> selected = backtrackSelectedItems(dp, requests, W);
    return new KnapsackResult(selected, dp[n][W], calculateTotalCost(selected), W, n, dp);
}
```

---

# 7. Correctness Evidence: Trace Tables, Invariants, Proof Sketches and Edge-Case Tests

### 7.1 Insertion Sort Loop Invariant Proof Sketch

* **Invariant**: At the start of each iteration $i$ of the outer loop ($1 \le i \le N-1$), the subarray $A[0 \dots i-1]$ consists of the elements originally in $A[0 \dots i-1]$, but in sorted order.

1. **Initialization ($i = 1$)**:
   Before the first iteration, $A[0 \dots 0]$ contains a single element, which is trivially sorted. The invariant holds.

2. **Maintenance**:
   During iteration $i$, the key $A[i]$ is saved. The inner loop shifts elements $A[i-1], A[i-2], \dots$ that are greater than $A[i]$ one position to the right. Inserting $A[i]$ into the resulting slot ensures $A[0 \dots i]$ is sorted. The invariant holds for $i + 1$.

3. **Termination ($i = N$)**:
   The loop terminates when $i = N$. By the invariant, $A[0 \dots N-1]$ contains all original elements in sorted order. $\blacksquare$

### 7.2 Binary Search Trace Table (`LOC-UG-16` Great Hall in 8-Element Array)

```
========================================================================================
Step   Low    Mid    High   Value[Mid]   Decision / Outcome
========================================================================================
1      0      3      7      LOC-UG-08    Target > Mid -> Low = 4
2      4      5      7      LOC-UG-14    Target > Mid -> Low = 6
3      6      6      7      LOC-UG-16    Match Found at Index 6!
========================================================================================
Total Comparisons: 3  |  Complexity: O(log_2(8)) = 3 steps.
```

---

# 8. Performance Analysis: Method, Input Sizes, Raw Results Tables, Graphs and Interpretation

### 8.1 Empirical Benchmark Methodology
Benchmarking executed via `BenchmarkSuiteRunner.java` across input sizes $N \in \{100, 500, 1000, 2000, 5000, 10000\}$ using nanosecond precision (`System.nanoTime()`).

### 8.2 Empirical Results Tables

#### **1. Tree Search Benchmark (Standard BST vs B-Tree $t=2$ on Sorted Input, $N=5,000$)**:
```
===================================================================================
Structure Type         Node Count   Tree Height   Search Time (ms)   Speedup Factor
===================================================================================
BinarySearchTree (BST) 5,000        4,999 (Degenerate) 73.00 ms       1.0x (Baseline)
BTree (t=2)            5,000        11 (Balanced)       0.72 ms       101.4x Speedup
===================================================================================
```
*Interpretation*: Under strictly sorted inputs, standard BST degenerates into a $O(N)$ linked list (height 4,999). B-Tree ($t=2$) maintains multi-way balancing (height 11), delivering over **100x execution speedup**.

#### **2. Search Engine Benchmark ($N=10,000$)**:
```
===================================================================================
Search Algorithm       Dataset Size   Worst-Case Comparisons   Execution Time (ms)
===================================================================================
LinearSearch           10,000         10,000 comparisons        2.45 ms
BinarySearch           10,000         14 comparisons           0.012 ms
===================================================================================
```

---

# 9. Database Integration Evidence: Schema, Sample Records, Screenshots and Run Logs

### 9.1 Database Verification Run Log (`DatabaseDAOTest.java`)
```text
Running Test Class: com.ghana.optimizer.storage.DatabaseDAOTest
--------------------------------------------------------------------------
 [PASS] testLocationDAOCRUD
 [PASS] testRoadDAOCRUD
 [PASS] testServiceRequestDAOCRUD
 [PASS] testResourceDAOCRUD
 [PASS] testAlgorithmRunDAOCreation
 [PASS] testAuditEventDAOCreation
 [PASS] testCsvDataLoaderSeedCounts
```

### 9.2 Complete Unit Test Suite Summary (`TestRunner.java`)
```text
==========================================================================
 Test Summary Result:
   Total Tests Executed : 122
   Total Tests Passed   : 122
   Total Tests Failed   : 0
==========================================================================
```

---

# 10. Responsible Algorithm Selection: When Chosen Algorithm is Appropriate and When It Is Not

```
========================================================================================================
Algorithm Choice     Appropriate Operating Conditions              Inappropriate / Failure Conditions
========================================================================================================
0/1 Knapsack DP      Discrete tickets with hard budget W <= 2000.  Continuous fractional items, or
                     Guarantees global optimality.                 large budget W > 1,000,000 (space growth).

Greedy Ratio         Fractional knapsack or quick estimation.       Discrete 0/1 tickets (leaves budget
                     Fast O(N log N) runtime.                       gaps causing 37.5% priority loss).

Dijkstra Routing     Graphs with non-negative edge weights.         Graphs with negative edge weights or
                     Includes road quality penalty factor 59.0.     negative weight cycles (use Bellman-Ford).

Binary Search        Sorted arrays with O(1) random access.         Unsorted arrays or linked lists without
                     Achieves O(log N) search speed.                random access.

B-Tree (t=2)         Large datasets, dynamic insertions, or        Small datasets (N < 20) where node split
                     strictly sorted inputs. Guarantees O(log N).   overhead exceeds binary tree traversal.
========================================================================================================
```

---

# 11. Individual Contribution Statement and Oral-Defense Preparation Notes

### 11.1 Team Division of Work Matrix (15 Members)
```
========================================================================================
Role / Specialized Module        Primary Responsibilities & Code Modules
========================================================================================
Hash Tables & Searching          CustomHashTable (M=761), BinarySearch, LinearSearch
Heaps & Priority Scheduling      BinaryHeap, PriorityDispatchScheduler, MyDeque Emergency
Graph Engine & Routing           Dijkstra, KruskalMST, PrimMST, AdjacencyMatrix/List Graph
Optimization & Budget DP         KnapsackOptimizer (0/1 DP), GreedyKnapsackHeuristic
Trees & Disjoint Sets            BinarySearchTree, BTree (t=2), DisjointSet (Union-Find)
Database & CSV Data Layer        SQLite DB, DAOs, CsvDataLoader, CsvDataExporter
Unit Testing & Benchmarks        TestRunner (122 tests), BenchmarkSuiteRunner
Console UI & User Experience     Main launcher, ConsoleMenu, TreeConsoleUI, TraceView
========================================================================================
```

### 11.2 Oral-Defense Flashcard Notes:
- **Q: Why is Parameter 1 set to 59.0?**  
  *A*: Derived from index sum $(333,907,258 \bmod 100) + 1 = 59.0$. Adds virtual penalty of up to 236m for degraded roads ($1.0$), steering trucks onto smooth pavement.
- **Q: Why use 0/1 Knapsack DP over Greedy selection?**  
  *A*: Tickets are discrete. Greedy ratio selection leaves unused budget gaps, achieving only 25 priority points vs 40 points optimal DP (37.5% loss).
- **Q: How does `MyDeque` handle life-safety emergencies?**  
  *A*: Level 5 emergency tickets invoke `addFront()` on `MyDeque` in $O(1)$ time, allowing life-safety dispatches to bypass routine queues.

---

# 12. References and Appendices

### 12.1 References
1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
2. Sedgewick, R., & Wayne, K. (2011). *Algorithms* (4th ed.). Addison-Wesley Professional.
3. SQLite Development Team. (2024). *SQLite Database Engine Documentation*. https://www.sqlite.org/

### 12.2 Appendices: Source Files Index
- **Main Launcher**: `src/main/java/com/ghana/optimizer/Main.java`
- **Test Runner**: `src/main/java/com/ghana/optimizer/TestRunner.java`
- **Benchmark Suite**: `src/main/java/com/ghana/optimizer/benchmark/BenchmarkSuiteRunner.java`
- **Data Structures**: `src/main/java/com/ghana/optimizer/ds/`
- **Algorithms**: `src/main/java/com/ghana/optimizer/algorithm/`
- **Storage & DAOs**: `src/main/java/com/ghana/optimizer/storage/`
