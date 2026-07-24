# 🇬🇭 Ghana Smart Service Operations Optimizer

An enterprise-grade Java console application and empirical algorithm laboratory designed to model, route, schedule, and optimize public service operations, utility maintenance, and resource allocation across Ghana's national road and infrastructure network.

---

## 📌 Project Architecture & Tech Stack

* **Core Language & Runtime:** Java (JDK 17+)
* **Database & Persistence:** JDBC with SQLite / MySQL / PostgreSQL support
* **Testing Framework:** JUnit 5 (Jupiter) with 40+ Unit Tests
* **Core Rule:** **Zero Java Collections Framework (`java.util.ArrayList`, `HashMap`, `PriorityQueue`, etc.) for core logic.** All data structures are implemented custom from scratch using raw arrays and node pointers.

---

## 📂 Project Directory Structure

```
DCIT308-204-project/
├── pom.xml                                  # Maven build & dependency configuration
├── README.md                                # Project documentation & directory guide
├── .gitignore                               # Git ignore rules
├── data/
│   ├── seed/                                # Seed CSV datasets for bootstrapping system
│   │   ├── locations.csv                    # Major Ghanaian cities & GPS coordinates
│   │   ├── roads.csv                        # Inter-city highway network & road quality
│   │   ├── requests.csv                     # Municipal service requests & priority levels
│   │   └── resources.csv                    # Field maintenance crews, trucks & equipment
│   └── sql/
│       ├── schema.sql                       # Database DDL for entity tables
│       └── seed_data.sql                    # SQL insert bootstrap scripts
├── exports/
│   ├── reports/                             # Exported operational summary reports
│   └── benchmark_results/                 # Exported empirical lab metrics (CSV & logs)
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── ghana/
    │   │           └── optimizer/
    │   │               ├── Main.java                        # Main application launcher
    │   │               ├── config/
    │   │               │   └── DatabaseConfig.java          # JDBC database connection setup
    │   │               │
    │   │               ├── model/                           # Domain Entities
    │   │               │   ├── Location.java                # Graph node (City/Station)
    │   │               │   ├── Road.java                    # Graph edge (Highway/Road link)
    │   │               │   ├── ServiceRequest.java          # Utility maintenance request
    │   │               │   ├── Resource.java                # Service team/vehicle resource
    │   │               │   ├── AlgorithmRun.java            # Empirical benchmark run log
    │   │               │   └── AuditEvent.java              # Undo/Redo operation audit record
    │   │               │
    │   │               ├── ds/                              # Custom Data Structures (Zero Built-in Collections)
    │   │               │   ├── list/
    │   │               │   │   ├── DynamicArray.java        # Resizable array implementation
    │   │               │   │   ├── SinglyLinkedList.java    # Custom singly-linked list with Iterator
    │   │               │   │   └── DoublyLinkedList.java    # Custom doubly-linked list with Iterator
    │   │               │   ├── stack/
    │   │               │   │   └── CustomStack.java         # LIFO stack for undo/redo audit logs
    │   │               │   ├── queue/
    │   │               │   │   ├── FifoQueue.java           # Standard FIFO queue
    │   │               │   │   ├── CircularQueue.java       # Ring-buffer circular queue
    │   │               │   │   └── CustomDeque.java         # Double-ended queue for emergency dispatch
    │   │               │   ├── heap/
    │   │               │   │   └── BinaryHeapPriorityQueue.java # Priority queue dispatch heap
    │   │               │   ├── hash/
    │   │               │   │   ├── CustomHashTable.java     # Chaining/probing hash map
    │   │               │   │   └── CollisionMetrics.java    # Hash collision & load factor tracker
    │   │               │   ├── tree/
    │   │               │   │   ├── BinarySearchTree.java    # Unbalanced BST indexing
    │   │               │   │   └── RedBlackTree.java        # Self-balancing BST / B-Tree
    │   │               │   ├── disjoint/
    │   │               │   │   └── DisjointSetUnion.java    # Path Compression & Union by Rank
    │   │               │   └── graph/
    │   │               │       ├── GraphAdjacencyList.java   # Sparse graph representation
    │   │               │       └── GraphAdjacencyMatrix.java # Dense graph representation
    │   │               │
    │   │               ├── algorithm/                       # Algorithms & Routing Layer
    │   │               │   ├── search/
    │   │               │   │   ├── LinearSearch.java        # O(N) array search
    │   │               │   │   └── BinarySearch.java        # O(log N) sorted search
    │   │               │   ├── sort/
    │   │               │   │   ├── SelectionSort.java       # O(N^2) selection sort
    │   │               │   │   ├── InsertionSort.java       # O(N^2) insertion sort
    │   │               │   │   ├── MergeSort.java           # O(N log N) stable sort
    │   │               │   │   └── QuickSort.java           # O(N log N) in-place sort
    │   │               │   ├── scheduling/
    │   │               │   │   ├── FifoScheduler.java       # FCFS request dispatcher
    │   │               │   │   ├── PriorityScheduler.java   # Heap priority-based dispatcher
    │   │               │   │   └── UrgentDequeScheduler.java# Deque front-insertion override
    │   │               │   ├── graph/
    │   │               │   │   ├── BreadthFirstSearch.java  # BFS graph traversal
    │   │               │   │   ├── DepthFirstSearch.java    # DFS graph traversal & cycles
    │   │               │   │   ├── DijkstraAlgorithm.java   # Shortest path routing
    │   │               │   │   ├── PrimAlgorithm.java       # MST (Adjacency Matrix)
    │   │               │   │   └── KruskalAlgorithm.java    # MST (Disjoint Set)
    │   │               │   └── optimization/
    │   │               │       ├── GreedyResourceAllocator.java # Greedy allocation & counterexample trace
    │   │               │       └── KnapsackBudgetOptimizer.java # 0/1 Dynamic Programming solver
    │   │               │
    │   │               ├── storage/                         # Storage & Persistence Layer
    │   │               │   ├── db/
    │   │               │   │   └── ConnectionManager.java   # JDBC SQLite/MySQL connection pool
    │   │               │   ├── dao/                         # Data Access Objects (CRUD)
    │   │               │   │   ├── LocationDao.java
    │   │               │   │   ├── RoadDao.java
    │   │               │   │   ├── ServiceRequestDao.java
    │   │               │   │   ├── ResourceDao.java
    │   │               │   │   ├── AlgorithmRunDao.java
    │   │               │   │   └── AuditEventDao.java
    │   │               │   └── csv/
    │   │               │       └── CsvSeedReader.java       # Seed data parser
    │   │               │
    │   │               ├── benchmark/                       # Empirical Experimentation Lab
    │   │               │   ├── Benchmarker.java             # Execution time (ns) & Memory (KB) metric engine
    │   │               │   ├── BenchmarkResult.java         # Metric data container
    │   │               │   └── BenchmarkExporter.java       # CSV & Database logger for test runs
    │   │               │
    │   │               └── ui/                              # Application & UI Layer
    │   │                   ├── ConsoleMenu.java             # Interactive CLI menu handler
    │   │                   ├── CliHandler.java              # CLI command-line arguments router
    │   │                   └── views/
    │   │                       ├── DispatchView.java        # Service request & dispatch terminal view
    │   │                       ├── RoutingView.java         # Graph routing & MST terminal view
    │   │                       └── BenchmarkView.java       # Empirical lab runner & viewer
    │   └── resources/
    │       ├── application.properties                   # Configuration file
    │       └── db/
    │           └── schema.sql                           # Database DDL schema file
    │
    └── test/                                                # Testing & Benchmarking Layer
        └── java/
            └── com/
                └── ghana/
                    └── optimizer/
                        ├── ds/                              # Unit tests for custom data structures
                        │   ├── DynamicArrayTest.java
                        │   ├── LinkedListTest.java
                        │   ├── CustomStackTest.java
                        │   ├── QueueAndDequeTest.java
                        │   ├── BinaryHeapTest.java
                        │   ├── CustomHashTableTest.java
                        │   ├── BinarySearchTreeTest.java
                        │   ├── RedBlackTreeTest.java
                        │   ├── DisjointSetTest.java
                        │   └── GraphTest.java
                        ├── algorithm/                       # Unit tests for algorithms & optimization
                        │   ├── SearchingTest.java
                        │   ├── SortingTest.java
                        │   ├── SchedulingEngineTest.java
                        │   ├── GraphAlgorithmsTest.java
                        │   └── OptimizationEngineTest.java
                        ├── storage/                         # Unit tests for DAOs & DB access
                        │   └── DaoTest.java
                        └── benchmark/                       # Unit tests for benchmarking lab
                            └── BenchmarkerTest.java
```

---

## 📑 Detailed Architectural Package Guide

### 1. Domain Models (`com.ghana.optimizer.model`)
* `Location`: Node entity representing Ghanaian cities/hubs with GPS coordinates.
* `Road`: Edge entity connecting locations with distance, travel time, and quality score.
* `ServiceRequest`: Customer maintenance request with priority level (1–5), budget required, and status.
* `Resource`: Operational asset (personnel crew, heavy vehicle, machinery) with hourly rate and location.
* `AlgorithmRun`: Performance tracking entity recording algorithm execution time (ns) and memory overhead (KB).
* `AuditEvent`: Operation audit record used for multi-level undo/redo operations.

### 2. Custom Data Structures (`com.ghana.optimizer.ds`)
Implemented completely without Java Collections (`java.util.*`):
* **`list`**: Dynamic resizable arrays, singly linked lists, and doubly linked lists with custom `Iterator` implementations.
* **`stack`**: LIFO Stack supporting the system-wide Audit/Undo event log.
* **`queue`**: FIFO Queue, Circular Queue (ring buffer), and Deque for priority and urgent dispatch overrides.
* **`heap`**: Min/Max Binary Heap powering priority-driven request dispatching.
* **`hash`**: Custom Hash Table tracking collisions, probing counts, and dynamic load factor re-hashing metrics.
* **`tree`**: Binary Search Tree (BST) and Red-Black Tree for logarithmic indexing and fast searches.
* **`disjoint`**: Disjoint Set Union (DSU) featuring **Path Compression** and **Union by Rank**.
* **`graph`**: Adjacency Matrix and Adjacency List representations of Ghana's road network.

### 3. Algorithms & Optimization Engine (`com.ghana.optimizer.algorithm`)
* **Searching & Sorting**: Linear Search, Binary Search, Selection Sort, Insertion Sort, Merge Sort, and Quick Sort.
* **Scheduling Engine**: FIFO Dispatcher, Priority Heap Dispatcher, and Urgent Deque Override Handler.
* **Graph Engine**: BFS, DFS, Dijkstra Shortest Path, Prim MST, and Kruskal MST.
* **Optimization Engine**: 
  * `GreedyResourceAllocator`: Greedy allocation logic accompanied by a counterexample trace highlighting greedy limitations.
  * `KnapsackBudgetOptimizer`: 0/1 Dynamic Programming solver maximizing project utility under constrained municipal budgets.

### 4. Storage & Persistence (`com.ghana.optimizer.storage`)
* **`db/ConnectionManager`**: Handles JDBC connection lifecycle and database driver initialization.
* **`dao/*`**: Data Access Objects executing CRUD operations for all entities using standard JDBC `PreparedStatement`.
* **`csv/CsvSeedReader`**: Parses seed CSV files (`data/seed/*.csv`) to bootstrap database tables and memory graphs.

### 5. Empirical Benchmarking Lab (`com.ghana.optimizer.benchmark`)
* **`Benchmarker`**: Automated benchmarking lab testing data structure & algorithm operations across input sizes ($N = 100 \dots 50,000$). Measures execution time ($\text{ns}$) and memory usage ($\text{KB}$).
* **`BenchmarkExporter`**: Logs empirical experimentation results into CSV files under `exports/benchmark_results/` and persists metrics to the `algorithm_runs` database table.

### 6. Interactive CLI & UI Layer (`com.ghana.optimizer.ui`)
* **`ConsoleMenu`**: Menu-driven interactive terminal application allowing users to view map networks, dispatch service teams, run graph routing algorithms, trigger optimization solvers, and launch benchmarking experiments.

---

## 🛠️ How to Build and Run

### Prerequisites
* Java Development Kit (JDK 17 or higher)
* Apache Maven 3.8+

### 1. Compile the Project
```bash
mvn clean compile
```

### 2. Run All Unit Tests (40+ Tests)
```bash
mvn test
```

### 3. Build & Package Executable JAR
```bash
mvn package
```

### 4. Run the Application
```bash
java -jar target/smart-service-optimizer-1.0.0-SNAPSHOT.jar
```
