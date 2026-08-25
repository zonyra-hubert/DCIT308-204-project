# University of Ghana Campus Service Operations Optimizer (UG-CSOO)
## Comprehensive Technical Guide: Data Structures, Algorithms & Group Derived Constants

---

### 📌 Executive Overview
This document provides a complete reference for the **University of Ghana Campus Service Operations Optimizer (UG-CSOO)** software system. It details:
1. The **3 Group Derived System Constants** and the step-by-step mathematical calculations used to derive them from our 15-member student ID index sum.
2. All **13 Custom Data Structures (DS)** implemented from scratch with zero `java.util` collection dependencies in core logic.
3. All **12 Core Algorithms** spanning graph routing, dynamic programming budget optimization, multi-strategy dispatches, and sorting/searching engine labs.

---

# 🔢 Section 1: Group Derived System Constants (15-Member Group)

Our project group consists of **15 members**. Summing all 15 student ID numbers yields the group index sum:

$$\text{Group Index Sum} = 333,907,258$$

- **Minimum Student ID in Group ($\text{Min ID}$)**: `10948210`
- **Maximum Student ID in Group ($\text{Max ID}$)**: `22404243`

---

### 🧮 1. Parameter 1 — Road Degradation Penalty Multiplier ($\lambda = 59.0$)

* **Mathematical Formula**:
  $$\text{Parameter 1} = (\text{Index Sum} \pmod{100}) + 1$$

* **Step-by-Step Derivation**:
  $$333,907,258 \bmod 100 = 58$$
  $$58 + 1 = \mathbf{59.0}$$

* **Operational Purpose & Usage in Code**:
  Used inside `Dijkstra.java` and `Road.java` to compute the effective weight of campus road segments based on pavement degradation:
  $$\text{effectiveCost} = \text{distance\_m} + 59.0 \times (5.0 - \text{condition\_score})$$
  - **Physical Interpretation**: For a severe pothole road ($\text{condition}=1.0$), the formula adds a virtual penalty of $59.0 \times 4.0 = 236.0\text{ meters}$. This steers heavy maintenance trucks and shuttle buses away from damaged roads onto smooth asphalt ($5.0$), minimizing vehicle wear and transit delay.

---

### 🧮 2. Parameter 2 — Prime Hash Table Capacity ($M = 761$)

* **Mathematical Formula**:
  $$\text{Parameter 2} = \text{First Prime Number } \ge (\text{Index Sum} \pmod{500}) + 501$$

* **Step-by-Step Derivation**:
  $$333,907,258 \bmod 500 = 258$$
  $$258 + 501 = 759$$
  - Evaluate integers starting from $759$:
    - $759 = 3 \times 11 \times 23$ (Composite)
    - $760 = 2 \times 380$ (Even / Composite)
    - $761$ has no divisors other than $1$ and $761$ $\implies$ **`761` is Prime!**

* **Operational Purpose & Usage in Code**:
  Used as the default capacity $M = 761$ in `CustomHashTable.java`.
  - **Hash Function**: $\text{hash}(k) = (|k.\text{hashCode}()| \bmod 761)$.
  - **Physical Interpretation**: A prime capacity guarantees minimal bucket collision and zero clustering when hashing campus location IDs (`"LOC-UG-01"`), road IDs, and service ticket keys into separate-chaining linked list buckets.

---

### 🧮 3. Parameter 3 — Operational Shift Budget Limit ($W = \text{GHS } 1,089.00$)

* **Mathematical Formula**:
  $$\text{Parameter 3} = ((\text{Max Student ID} - \text{Min Student ID}) \pmod{1000}) + 100$$

* **Step-by-Step Derivation**:
  $$\text{Max ID} = 22404243,\quad \text{Min ID} = 10948210$$
  $$22404243 - 10948210 = 11,456,033$$
  $$11,456,033 \bmod 1000 = 989$$
  $$989 + 100 = \mathbf{1089.00} \implies \mathbf{\text{GHS } 1,089.00}$$

* **Operational Purpose & Usage in Code**:
  Used as the budget capacity constraint $W = 1089$ in `KnapsackOptimizer.java`.
  - **Physical Interpretation**: The University Physical Development Department operates under a strict shift budget ceiling of $\text{GHS } 1,089.00$. The 0/1 Knapsack Dynamic Programming solver selects the optimal subset of discrete maintenance tickets that maximizes total priority points without exceeding $\text{GHS } 1,089.00$.

---

# 📦 Section 2: Custom Data Structures (DS) Reference

| Data Structure Class | Package Location | Key Operations & Time Complexity | Primary Operational Usage in UG-CSOO |
| :--- | :--- | :--- | :--- |
| **`DynamicArray<T>`** | `ds.list` | `add` $O(1)$ amortized, `get` $O(1)$, `remove` $O(N)$ | Resizable 1D array container used across all DAOs, graph adjacency lists, shortest path node traces, and sorting input buffers. |
| **`MyLinkedList<T>`** | `ds.list` | `addFirst`/`addLast` $O(1)$, `remove` $O(N)$ | Doubly-linked node list ADT serving as the underlying storage structure for deques and sequential ticket lists. |
| **`MyStack<T>`** | `ds.stack` | `push` $O(1)$, `pop` $O(1)$, `peek` $O(1)$ | Last-In, First-Out (LIFO) stack used for Depth-First Search (DFS) graph traversal, call-stack logging, and 2D Knapsack DP decision backtracking. |
| **`MyQueue<E>`** | `ds.queue` | `enqueue` $O(1)$, `dequeue` $O(1)$, `peek` $O(1)$ | First-In, First-Out (FIFO) queue used for Breadth-First Search (BFS) graph traversal and chronological service ticket dispatches. |
| **`MyDeque<T>`** | `ds.queue` | `addFront`/`addRear` $O(1)$, `removeFront`/`removeRear` $O(1)$ | Double-ended queue linked directly to `PriorityDispatchScheduler` as the **Life-Safety Emergency Override Queue**, allowing Level 5 emergency tickets to bypass routine queues. |
| **`CircularQueue<E>`** | `ds.queue` | `enqueue` $O(1)$, `dequeue` $O(1)$ (Modulo Ring Buffer) | Array-based fixed-capacity ring buffer using `(rear + 1) % cap` pointer math. Used for equitable **Round-Robin shuttle bus & maintenance crew dispatches** with zero element shifting. |
| **`CustomHashTable<K,V>`** | `ds.hash` | `put` $O(1)$ avg, `get` $O(1)$ avg, `remove` $O(1)$ avg | Separate chaining hash table with prime capacity $761$, providing fast lookup for campus locations, roads, resources, and service tickets by String ID. |
| **`BinaryHeap<T>`** | `ds.heap` | `insert` $O(\log N)$, `extractMin` $O(\log N)$, `peek` $O(1)$ | 1D array complete binary min-heap used inside **Dijkstra's Algorithm** ($O((V+E)\log V)$) and inside **PriorityDispatchScheduler** for urgency-first dispatches. |
| **`BinarySearchTree<K,V>`**| `ds.tree` | `insert` $O(\log N)$, `search` $O(\log N)$, `height` $O(N)$ worst | Ordered binary search tree used for range queries, location hierarchy lookups, and empirical tree height degradation experiments. |
| **`BTree<K,V>`** | `ds.tree` | `insert` $O(\log N)$, `search` $O(\log N)$ | Multi-way 2-3-4 balanced search tree ($t=2$) that guarantees $O(\log N)$ search height even under strictly sorted inputs, preventing BST degradation (100x speedup). |
| **`DisjointSet`** | `ds.disjoint` | `find` $O(\alpha(N))$, `union` $O(\alpha(N))$ | Disjoint-set forest with **Path Compression** and **Union by Rank**. Used inside **Kruskal's MST Algorithm** for near-constant cycle detection when building campus utility networks. |
| **`AdjacencyMatrixGraph`**| `ds.graph.matrix` | `addEdge` $O(1)$, `getWeight` $O(1)$, Space $O(V^2)$ | $200 \times 200$ 2D weighted matrix representing campus road connections, enabling $O(1)$ edge existence and distance queries. |
| **`AdjacencyListGraph`**  | `ds.graph.list`   | `addVertex` $O(1)$, `addEdge` $O(1)$, Space $O(V+E)$ | Linked list array graph model providing space-efficient storage for sparse road graph traversals (Dijkstra, BFS, DFS, Kruskal). |

---

# ⚙️ Section 3: Custom Algorithms Reference

### 🗺️ A. Graph Routing & Network Algorithms

1. **Dijkstra's Shortest Path Algorithm** (`com.ghana.optimizer.algorithm.graph.Dijkstra`)
   - **Purpose**: Computes min-cost routing for emergency maintenance vehicles across Legon Campus.
   - **Formula**: $\text{cost} = \text{distance\_m} + 59.0 \times (5.0 - \text{condition\_score})$
   - **Complexity**: $O((V + E) \log V)$ using `BinaryHeap` priority queue.

2. **Kruskal's Minimum Spanning Tree (MST)** (`com.ghana.optimizer.algorithm.graph.KruskalMST`)
   - **Purpose**: Connects all campus buildings into a minimum-distance utility backbone (fiber optic network / water mains) without loops.
   - **Complexity**: $O(E \log E)$ edge sorting $+ O(E \cdot \alpha(V))$ using `DisjointSet`.

3. **Prim's Minimum Spanning Tree (MST)** (`com.ghana.optimizer.algorithm.graph.PrimMST`)
   - **Purpose**: Alternative MST algorithm that grows the utility network outward from central campus landmarks.
   - **Complexity**: $O(V^2)$ on Adjacency Matrix or $O((V+E) \log V)$ using Min-Heap.

4. **Breadth-First Search (BFS)** (`com.ghana.optimizer.algorithm.graph.BFS`)
   - **Purpose**: Level-order traversal computing unweighted hop-count distances between campus landmarks.
   - **Complexity**: $O(V + E)$ using `MyQueue`.

5. **Depth-First Search (DFS)** (`com.ghana.optimizer.algorithm.graph.DFS`)
   - **Purpose**: Recursive/Stack-based traversal for cycle detection and discovering disconnected campus clusters.
   - **Complexity**: $O(V + E)$ using `MyStack`.

---

### 💰 B. Optimization & Scheduling Algorithms

1. **0/1 Knapsack Dynamic Programming Solver** (`com.ghana.optimizer.algorithm.optimization.KnapsackOptimizer`)
   - **Purpose**: Maximizes total priority points for facilities tickets under the $\text{GHS } 1,089.00$ shift budget constraint.
   - **Recurrence**: $DP[i][w] = \max(DP[i-1][w],\, DP[i-1][w - c_i] + p_i)$
   - **Complexity**: $O(N \cdot W)$ time and space, enabling exact 2D table backtracking.

2. **Greedy Knapsack Ratio Heuristic** (`com.ghana.optimizer.algorithm.optimization.GreedyKnapsackHeuristic`)
   - **Purpose**: Sorts tickets by priority-to-cost ratio ($p_i / c_i$). Used in trace tables to demonstrate why greedy heuristics fail on discrete items (37.5% penalty vs DP).
   - **Complexity**: $O(N \log N)$.

3. **Multi-Strategy Priority Dispatch Scheduler** (`com.ghana.optimizer.algorithm.scheduling.PriorityDispatchScheduler`)
   - **Purpose**: Orchestrates vehicle dispatches using FIFO (`MyQueue`), Round-Robin (`CircularQueue`), Urgency Min-Heap (`BinaryHeap`), and Emergency Override (`MyDeque`).

---

### 🔎 C. Search & Sorting Engine Algorithms

1. **Binary Search** (`com.ghana.optimizer.algorithm.search.BinarySearch`)
   - **Purpose**: Divide-and-conquer search on sorted arrays ($\le 3$ steps for 8 items, $\le 10$ steps for 1,000 locations).
   - **Complexity**: $O(\log N)$.

2. **Linear Search** (`com.ghana.optimizer.algorithm.search.LinearSearch`)
   - **Purpose**: Sequential scan for unstructured queries and multi-match category filtering.
   - **Complexity**: $O(N)$.

3. **Merge Sort** (`com.ghana.optimizer.algorithm.sort.MergeSort`)
   - **Purpose**: Stable multi-attribute sorting for service tickets (Urgency descending, Budget ascending).
   - **Complexity**: $O(N \log N)$ worst/average/best case.

4. **QuickSort** (`com.ghana.optimizer.algorithm.sort.QuickSort`)
   - **Purpose**: In-place divide-and-conquer sorting using 2-way element partitioning.
   - **Complexity**: $O(N \log N)$ average case.

5. **Selection Sort** (`com.ghana.optimizer.algorithm.sort.SelectionSort`)
   - **Purpose**: $O(N^2)$ sort minimizing physical element swaps ($O(N)$ swaps).

6. **Insertion Sort** (`com.ghana.optimizer.algorithm.sort.InsertionSort`)
   - **Purpose**: $O(N^2)$ sort with $O(N)$ best-case performance for nearly-sorted ticket queues.

---

### 📄 Google Docs Copy-Paste Instructions
1. Open [Google Docs](https://docs.google.com).
2. Create a **New Blank Document**.
3. Copy the markdown content above and paste it directly into Google Docs (Google Docs automatically formats Markdown headings, tables, bold text, and math equations!).
