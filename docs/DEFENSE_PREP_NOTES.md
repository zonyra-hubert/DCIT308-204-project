# 🎓 Oral Defense Preparation Guide & Contribution Notes (Module M11 / Section 11)

This defense guide equips each team member to defend a specific custom data structure and algorithmic engine during the final project viva.

---

### Member 1 Defense Topic: `CustomHashTable` & `BinarySearch`
- **Data Structure**: `CustomHashTable<K, V>` with separate chaining.
  - **Invariants**: Initial prime capacity of `547` (System Parameter 2), load factor threshold $\alpha \le 0.75$. Prime modulo hashing: $\text{index} = |\text{hashCode}| \bmod M$.
  - **Complexity**: $O(1)$ expected time for `put`, `get`, and `remove`.
  - **Defensive Question**: *Why did we choose 547 as the initial capacity?*
    - *Answer*: 547 is a prime number that reduces clustering from common multiplier patterns in ticket IDs (e.g., `REQ-UG-001`), ensuring uniform hash bucket distribution across 300+ campus tickets without premature resizing.
- **Algorithm**: `BinarySearch`
  - **Complexity**: Time: $O(\log n)$, Space: $O(1)$.
  - **Invariant**: Array must satisfy strict ascending sorted order. Precondition checked using `SortedPreconditionValidator`.

---

### Member 2 Defense Topic: `BinaryHeap` & `PriorityQueue` Dispatch
- **Data Structure**: `BinaryHeap<T>` (Array-backed min-heap).
  - **Invariants**: Complete binary tree property; parent at $\lfloor\frac{i-1}{2}\rfloor$, children at $2i+1$ and $2i+2$. Min-heap ordering property: $\text{heap}[i] \le \text{heap}[2i+1]$ and $\text{heap}[i] \le \text{heap}[2i+2]$.
  - **Complexity**: `insert(item)` in $O(\log n)$, `extractMin()` in $O(\log n)$, `peek()` in $O(1)$.
  - **Defensive Question**: *How does PriorityQueue dispatch urgent tickets?*
    - *Answer*: Critical maintenance tickets (Urgency 5) bubble to the root of the heap using sift-up operations, enabling $O(\log n)$ urgent dispatch without scanning through all 300+ requests.

---

### Member 3 Defense Topic: `AdjacencyMatrixGraph` vs `AdjacencyListGraph` & Dijkstra's Algorithm
- **Data Structure**: Campus Road Graph representations.
  - **Matrix**: $V \times V$ matrix of `Edge` objects ($O(V^2)$ memory, $O(1)$ edge query).
  - **List**: Array of `ListNode` linked chains ($O(V + E)$ memory, $O(\text{deg}(v))$ traversal).
- **Algorithm**: Dijkstra Shortest Path with Road Penalty Factor `43.0`.
  - **Weight Function**: $w(u,v) = \text{distance\_m} + 43.0 \times (5.0 - \text{condition\_score})$.
  - **Complexity**: $O((V + E) \log V)$ using custom min-heap.
  - **Defensive Question**: *Why is road condition penalized by 43.0?*
    - *Answer*: UG Legon campus roads have uneven conditions and speed humps (e.g. Annie Jiagge Rd vs University Ave). Deteriorated segments (condition 1.0) add $43 \times 4.0 = 172\text{ meters}$ equivalent delay, rerouting repair trucks onto smoother roads.

---

### Member 4 Defense Topic: 0/1 Knapsack Dynamic Programming & Greedy Heuristics
- **Algorithm**: 0/1 Knapsack Budget Optimization (Shift limit: GHS 1,089.00).
  - **Complexity**: $O(n \cdot W)$ time and space, where $n = 304$ tickets, $W = 1089$ GHS.
  - **Optimal Substructure**: $\text{DP}[i][w] = \max(\text{DP}[i-1][w], \text{DP}[i-1][w-c_i] + p_i)$.
  - **Defensive Question**: *Why can't we just use a Greedy ratio solver for maintenance requests?*
    - *Answer*: Discrete 0/1 Knapsack does not have the greedy-choice property. As proven in our counterexample, greedy picking leaves budget gaps that produce suboptimal total priority points (37.5% penalty) compared to 2D DP tabulation.

---

### Member 5 Defense Topic: `BTree`, `DisjointSet` & Kruskal's MST
- **Data Structure**: `DisjointSet` (Union-Find) with path compression and union by rank.
  - **Complexity**: $O(\alpha(n))$ per operation (inverse Ackermann function).
  - **Role**: Cycle detection in Kruskal's Minimum Spanning Tree algorithm.
- **Tree Structure**: `BTree` (Balanced $B$-tree of minimum degree $t=2$).
  - **Complexity**: $O(t \log_t n)$ search and insertion depth, guaranteeing $O(\log n)$ height even under sequentially sorted insertions where standard BST degenerates to $O(n)$.
