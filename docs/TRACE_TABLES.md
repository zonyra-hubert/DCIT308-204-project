# 📊 Execution Trace Tables (Module M9 / Section 10)

This document provides complete, rigorous execution trace tables across 6 fundamental algorithms within the **UG-CSOO** engine.

---

### Trace Table 1: Binary Search on Campus Location IDs
- **Problem**: Locate target `"LOC-UG-16"` (Great Hall) within a sorted array of 8 campus nodes.
- **Input Array**: `["LOC-UG-01", "LOC-UG-06", "LOC-UG-08", "LOC-UG-10", "LOC-UG-12", "LOC-UG-14", "LOC-UG-16", "LOC-UG-21"]` ($N = 8$)

| Iteration | `low` | `high` | `mid` | `array[mid]` | Comparison Result | Action / Pointer Shift |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **1** | 0 | 7 | 3 | `"LOC-UG-10"` | `"LOC-UG-16" > "LOC-UG-10"` | `low = mid + 1 = 4` |
| **2** | 4 | 7 | 5 | `"LOC-UG-14"` | `"LOC-UG-16" > "LOC-UG-14"` | `low = mid + 1 = 6` |
| **3** | 6 | 7 | 6 | `"LOC-UG-16"` | `"LOC-UG-16" == "LOC-UG-16"` | **Match Found at Index 6!** |

- **Metrics**: Total comparisons = 3. Search completed in $\lceil \log_2(8) \rceil = 3$ iterations.

---

### Trace Table 2: Insertion Sort (Descending Priority Level)
- **Problem**: Sort campus service requests in descending priority order ($5 \to 1$).
- **Input Array**: `[2, 5, 1, 4, 3]` ($N = 5$)

| Pass $i$ | `key` | Initial State | Elements Shifted | Final State After Pass | Total Shifts |
| :---: | :---: | :---: | :---: | :---: | :---: |
| **Init** | - | `[2, 5, 1, 4, 3]` | - | `[2, 5, 1, 4, 3]` | 0 |
| **1** | 5 | `[2, 5, 1, 4, 3]` | `2 < 5` (shift right) | `[5, 2, 1, 4, 3]` | 1 |
| **2** | 1 | `[5, 2, 1, 4, 3]` | `2 > 1` (no shift) | `[5, 2, 1, 4, 3]` | 0 |
| **3** | 4 | `[5, 2, 1, 4, 3]` | `1 < 4`, `2 < 4` | `[5, 4, 2, 1, 3]` | 2 |
| **4** | 3 | `[5, 4, 2, 1, 3]` | `1 < 3`, `2 < 3` | `[5, 4, 3, 2, 1]` | 2 |

- **Metrics**: Total shifts = 5, Total comparisons = 8. Final sorted array: `[5, 4, 3, 2, 1]`.

---

### Trace Table 3: Merge Sort Divide-and-Conquer Trace
- **Problem**: Sort 6 maintenance ticket budgets: `[600, 260, 450, 120, 350, 150]`

```
                        [600, 260, 450, 120, 350, 150]
                         /                           \
               [600, 260, 450]                   [120, 350, 150]
                /          \                      /          \
            [600]       [260, 450]            [120]       [350, 150]
                        /       \                         /       \
                     [260]     [450]                    [350]    [150]
```

| Step | Subproblem $L$ | Subproblem $R$ | Merge Comparisons | Merged Result |
| :---: | :---: | :---: | :---: | :---: |
| **1** | `[260]` | `[450]` | $260 \le 450$ (1) | `[260, 450]` |
| **2** | `[600]` | `[260, 450]` | $600 > 260$, $600 > 450$ (2) | `[260, 450, 600]` |
| **3** | `[350]` | `[150]` | $350 > 150$ (1) | `[150, 350]` |
| **4** | `[120]` | `[150, 350]` | $120 \le 150$ (1) | `[120, 150, 350]` |
| **5** | `[260, 450, 600]` | `[120, 150, 350]` | $260>120, 260>150, 260\le 350, 450>350$ (4) | `[120, 150, 260, 350, 450, 600]` |

- **Metrics**: Total divide levels = 3, Total merge comparisons = 9. Space = $O(N)$.

---

### Trace Table 4: Dijkstra Shortest Path (with Road Penalty Weight 43.0)
- **Source**: Physical Development (`LOC-UG-43`) $\to$ **Target**: Commonwealth Hall (`LOC-UG-09`)
- **Formula**: $w(u,v) = \text{distance\_m} + 43.0 \times (5.0 - \text{condition\_score})$

| Step | Extracted Node $u$ | $D[u]$ | Adjacent Neighbor $v$ | Edge Distance ($m$) / Cond | Effective Cost $w(u,v)$ | Relaxed $D[v]$ | Predecessor $P[v]$ |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **1** | `LOC-UG-43` | 0.0 | `LOC-UG-06` (Akuafo) | 350m / 4.2 | $350 + 43(0.8) = 384.4$ | **384.4** | `LOC-UG-43` |
| **2** | `LOC-UG-43` | 0.0 | `LOC-UG-21` (Main Gate) | 600m / 4.8 | $600 + 43(0.2) = 608.6$ | **608.6** | `LOC-UG-43` |
| **3** | `LOC-UG-06` | 384.4 | `LOC-UG-01` (Balme Lib) | 350m / 4.2 | $350 + 43(0.8) = 384.4$ | $384.4 + 384.4 = \mathbf{768.8}$ | `LOC-UG-06` |
| **4** | `LOC-UG-01` | 768.8 | `LOC-UG-16` (Great Hall)| 500m / 3.8 | $500 + 43(1.2) = 551.6$ | $768.8 + 551.6 = \mathbf{1320.4}$| `LOC-UG-01` |
| **5** | `LOC-UG-16` | 1320.4| `LOC-UG-09` (Commonwealth)| 200m / 4.6 | $200 + 43(0.4) = 217.2$ | $1320.4 + 217.2 = \mathbf{1537.6}$| `LOC-UG-16` |

- **Optimal Shortest Path**: `LOC-UG-43` $\to$ `LOC-UG-06` $\to$ `LOC-UG-01` $\to$ `LOC-UG-16` $\to$ `LOC-UG-09`.
- **Total Effective Weight**: **`1,537.60`**

---

### Trace Table 5: Kruskal Minimum Spanning Tree with Disjoint Set
- **Nodes**: 5 Campus Hubs ($V_0 \dots V_4$), Sorted Candidate Edges:

| Edge $e = (u, v)$ | Distance ($m$) | `find(u)` | `find(v)` | Cycle Detected? | DisjointSet Action | Edge in MST? |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| $(V_2, V_3)$ | 90 | 2 | 3 | No | `union(2, 3)` | **YES** |
| $(V_1, V_2)$ | 120 | 1 | 2 | No | `union(1, 2)` | **YES** |
| $(V_0, V_1)$ | 180 | 0 | 1 | No | `union(0, 1)` | **YES** |
| $(V_0, V_2)$ | 210 | 0 | 0 | **YES (Cycle!)** | *Rejected* | **NO** |
| $(V_3, V_4)$ | 240 | 0 | 4 | No | `union(0, 4)` | **YES** |

- **MST Total Cost**: $90 + 120 + 180 + 240 = \mathbf{630\text{ m}}$ (Spanning all 5 vertices with 4 edges).

---

### Trace Table 6: 0/1 Knapsack DP (Shift Budget Limit: GHS 1,089.00)
- **Shift Budget Cap $W$**: **`GHS 1,089.00`**
- **Candidate Requests**:
  - $r_1$: `REQ-006` (Electrical Circuit) $\to$ Cost: GHS 120.00, Priority: 5 (Ratio: 0.0417)
  - $r_2$: `REQ-011` (ICT Projector) $\to$ Cost: GHS 150.00, Priority: 3 (Ratio: 0.0200)
  - $r_3$: `REQ-014` (Electrical Circuit) $\to$ Cost: GHS 250.00, Priority: 5 (Ratio: 0.0200)
  - $r_4$: `REQ-005` (Water Tank Valve) $\to$ Cost: GHS 450.00, Priority: 4 (Ratio: 0.0089)

| Item $i$ | Cost ($c_i$) | Priority ($p_i$) | DP[$i$][$150$] | DP[$i$][$270$] | DP[$i$][$520$] | DP[$i$][$970$] | DP[$i$][$1089$] |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **0 (Base)** | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| **1 ($r_1$)** | 120 | 5 | 5 | 5 | 5 | 5 | **5** |
| **2 ($r_2$)** | 150 | 3 | 5 | 8 ($120+150$) | 8 | 8 | **8** |
| **3 ($r_3$)** | 250 | 5 | 5 | 8 | 13 ($r_1+r_2+r_3$) | 13 | **13** |
| **4 ($r_4$)** | 450 | 4 | 5 | 8 | 13 | 17 ($r_1+r_2+r_3+r_4$) | **17** |

- **Backtracking Reconstruction**:
  - $\text{DP}[4][1089] = 17 \ne \text{DP}[3][1089] \implies$ **Include $r_4$** (Cost: 450, Rem: 639)
  - $\text{DP}[3][639] = 13 \ne \text{DP}[2][639] \implies$ **Include $r_3$** (Cost: 250, Rem: 389)
  - $\text{DP}[2][389] = 8 \ne \text{DP}[1][389] \implies$ **Include $r_2$** (Cost: 150, Rem: 239)
  - $\text{DP}[1][239] = 5 \ne \text{DP}[0][239] \implies$ **Include $r_1$** (Cost: 120, Rem: 119)
- **Selected Optimal Tickets**: $\{r_1, r_2, r_3, r_4\}$
- **Total Allocated Cost**: $120 + 150 + 250 + 450 = \mathbf{\text{GHS } 970.00 \le 1,089.00}$
- **Total Priority Points**: $5 + 3 + 5 + 4 = \mathbf{17\text{ points}}$ (100% budget compliance).
