# 🏛️ University of Ghana Campus Service Operations Optimizer (UG-CSOO)
## Module M1: Problem Specification, Domain Context & System Invariants

---

### 1. Executive Summary & Problem Domain
The **University of Ghana (UG), Legon Campus** represents a dynamic educational micro-city spanning approximately 13 square kilometers with over 40,000 students and staff. Daily operational logistics present complex multi-objective optimization challenges:
1. **Facilities & Infrastructure Maintenance**: Scheduling over 300+ urgent plumbing, electrical, and HVAC tickets across traditional halls (Akuafo, Volta, Legon, Commonwealth, Sarbah) and diaspora hostels (Jean Nelson Aka, Hilla Limann, Alexander Kwapong, Elizabeth Sey).
2. **Campus Transit & Shuttle Dispatch**: Optimizing campus shuttle frequency and route schedules across high-congestion nodes (Main Gate Stop, Diaspora Terminal, Balme Library, CC Halls).
3. **IT & Academic Logistics**: Deploying UGCS technicians and equipment transfers across academic faculties (Computer Science, Mathematics, Physics, Chemistry, Law, Engineering).

To ensure deterministic, high-efficiency execution, **UG-CSOO** models the entire campus road and facility network using custom-engineered data structures (avoiding standard `java.util` collections for core state) and rigorous algorithmic engines.

---

### 2. Explicit System Parameters & Constraints

| Parameter Identifier | Metric / Constraint | Exact Value | Domain Rationale & Technical Application |
| :--- | :--- | :--- | :--- |
| **System Parameter 1** | Road Condition Penalty ($\lambda$) | **`43.0`** | Penalty factor applied to deteriorated or speed-bumped campus roads (e.g. Annie Jiagge Rd, Guggisberg Ave). Effective edge weight: $w(e) = \text{distance\_m} + 43.0 \times (5.0 - \text{condition\_score})$. |
| **System Parameter 2** | Custom Hash Table Initial Prime Capacity ($M$) | **`547`** | Prime number bucket capacity for `CustomHashTable` ensuring low collision frequency ($\alpha < 0.60$) when indexing 300+ active campus service requests in $O(1)$ expected time. |
| **System Parameter 3** | Operational Shift Budget Limit ($W$) | **`GHS 1,089.00`** | Hard financial cap per crew shift for batch maintenance optimization via the 0/1 Knapsack Dynamic Programming solver. |

---

### 3. Formal Input-Output (I/O) Specifications

#### A. Campus Graph Route Solver (Dijkstra)
- **Input**: 
  - Graph $G = (V, E)$ where $V = \{\text{LOC-UG-01}, \dots, \text{LOC-UG-52}\}$ and $E$ comprises 105 bidirectional road segments.
  - Source vertex $s \in V$ (e.g., Physical Development Yard `LOC-UG-43`).
  - Target vertex $t \in V$ (e.g., Commonwealth Hall `LOC-UG-09`).
- **Pre-conditions**:
  - $s, t \in V$ are valid existing nodes.
  - All effective edge weights $w(u, v) = d(u,v) + 43.0 \times (5.0 - c(u,v)) \ge 0$ (non-negative costs).
- **Post-conditions**:
  - Returns the minimum effective cost $D[t]$.
  - Returns the optimal sequence of vertices $\langle s, v_1, v_2, \dots, t \rangle$.
- **Asymptotic Complexity**: $O((|V| + |E|) \log |V|)$ using custom `BinaryHeap`.

#### B. 0/1 Knapsack Budget Optimization Solver
- **Input**:
  - Array of $n$ candidate service requests $R = \langle r_1, r_2, \dots, r_n \rangle$.
  - Each request $r_i$ has integer priority points $p_i \in [1, 5]$ and integer ceiling cost $c_i = \lceil\text{budget\_required}\rceil \in \mathbb{Z}^+$.
  - Budget limit $W = 1089$ GHS.
- **Pre-conditions**:
  - $W \ge 0$, $\forall i: c_i > 0, p_i > 0$.
- **Post-conditions**:
  - Returns maximum total priority $P^* = \max \sum_{i \in S} p_i$ subject to $\sum_{i \in S} c_i \le W$.
  - Returns the exact subset $S^* \subseteq R$ through 2D tabulation backtracking.
- **Asymptotic Complexity**: $O(n \cdot W)$ time and space.

#### C. Custom Hash Table Operations (`CustomHashTable`)
- **Input**: Ticket ID string key $k \in \Sigma^*$ (e.g., `"REQ-UG-001"`), entity value $v$.
- **Pre-conditions**: Key $k \ne \text{null}$.
- **Post-conditions**:
  - `put(k, v)`: Associates key $k$ with $v$. If load factor $\alpha = \frac{N}{M} > 0.75$, doubles table capacity to next prime and rehashes all elements.
  - `get(k)`: Returns matching value $v$ in $O(1)$ expected time or null if absent.
- **Asymptotic Complexity**: Expected $O(1)$ search/insert; worst-case $O(N)$ on degenerate hash collisions.

---

### 4. Data Entity Invariants

1. **Location Entity Invariant**: Each location has a unique primary key `LOC-UG-xx` and valid GPS coordinates ($5.60 \le \text{lat} \le 5.70, -0.25 \le \text{lon} \le -0.10$).
2. **Road Entity Invariant**: Every road segment connects two existing valid locations. Road condition score is strictly bounded: $1.0 \le \text{condition\_score} \le 5.0$.
3. **Service Request Invariant**: Urgency / Priority level is strictly bounded: $1 \le \text{priority\_level} \le 5$. Status belongs to $\{\text{PENDING}, \text{DISPATCHED}, \text{COMPLETED}, \text{CANCELLED}\}$.
