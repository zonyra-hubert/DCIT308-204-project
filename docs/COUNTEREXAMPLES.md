# ❌ Counterexamples & Failure Mode Analysis (Module M9 / Section 10)

---

### Counterexample 1: Greedy Heuristic Failure vs. 0/1 Knapsack DP
**Context**: A maintenance crew on the University of Ghana Legon campus has an operational budget limit of **$W = \text{GHS } 1,000.00$** for the shift.

**Candidate Service Requests**:
1. **Request A** (`REQ-UG-A`): Roof Header Tank Valve Overhaul
   - Cost $c_A = \text{GHS } 600.00$
   - Priority $p_A = 60$ points
   - Density / Ratio $\rho_A = \frac{60}{600} = \mathbf{0.1000\text{ pts/GHS}}$
2. **Request B** (`REQ-UG-B`): Emergency Electrical Circuit Fault
   - Cost $c_B = \text{GHS } 500.00$
   - Priority $p_B = 48$ points
   - Density / Ratio $\rho_B = \frac{48}{500} = \mathbf{0.0960\text{ pts/GHS}}$
3. **Request C** (`REQ-UG-C`): Network Fiber Switch Splicing
   - Cost $c_C = \text{GHS } 500.00$
   - Priority $p_C = 48$ points
   - Density / Ratio $\rho_C = \frac{48}{500} = \mathbf{0.0960\text{ pts/GHS}}$

#### 1. Greedy Strategy Execution (Highest Priority-to-Cost Ratio)
- Sorts items by ratio descending: $\rho_A (0.1000) > \rho_B (0.0960) \ge \rho_C (0.0960)$.
- **Step 1**: Pick Request A (Cost: GHS 600.00, Priority: 60).
  - Remaining Budget = $1000 - 600 = \text{GHS } 400.00$.
- **Step 2**: Evaluate Request B (Cost: GHS 500.00) $\to$ **Cannot fit** ($500 > 400$).
- **Step 3**: Evaluate Request C (Cost: GHS 500.00) $\to$ **Cannot fit** ($500 > 400$).
- **Greedy Output**: $\{A\}$
  - **Total Cost**: GHS 600.00 (Leaves GHS 400.00 unutilized)
  - **Total Priority Points**: **`60 points`**

#### 2. Dynamic Programming Optimal Execution
- Considers all combinations via 2D DP table.
- Feasible set 1: $\{A\} \to \text{Cost: } 600 \le 1000, \text{Points: } 60$
- Feasible set 2: $\{B, C\} \to \text{Cost: } 500 + 500 = 1000 \le 1000, \text{Points: } 48 + 48 = \mathbf{96\text{ points}}$
- **DP Output**: $\{B, C\}$
  - **Total Cost**: GHS 1,000.00
  - **Total Priority Points**: **`96 points`**

#### Mathematical Conclusion
$$\text{Greedy Solution} (60\text{ pts}) < \text{Optimal DP Solution} (96\text{ pts})$$
Greedy strategy achieves only $\frac{60}{96} = 62.5\%$ of the optimal value (**37.5% suboptimality**). This proves that the Greedy choice property fails for discrete 0/1 Knapsack when items are indivisible.

---

### Counterexample 2: Precondition Violation in Binary Search
**Theorem**: Binary Search requires the input array to satisfy the precondition of monotonic ordering:
$$\forall i \in [0, n-2]: A[i] \le A[i+1]$$

**Unsorted Counterexample Array**:
$$A = [45, 12, 88, 23, 70, 5, 99]$$
**Target**: $T = 23$ (Notice $23$ definitely exists at index 3 in the array).

#### Binary Search Step-by-Step Execution on Unsorted Array:
1. `low = 0`, `high = 6`, `mid = 3`.
   - Element at `mid`: $A[3] = 23$.
   - By chance, matches at index 3.
2. Now search for $T = 12$ (exists at index 1):
   - `low = 0`, `high = 6`, `mid = 3`, $A[3] = 23$.
   - Comparison: $12 < 23 \implies \text{search left half: } \text{high} = 2$.
   - Next `mid = 1`, $A[1] = 12 \implies$ Found.
3. Now search for $T = 5$ (exists at index 5 in the right half!):
   - `low = 0`, `high = 6`, `mid = 3`, $A[3] = 23$.
   - Comparison: $5 < 23$.
   - Binary Search algorithm assumes all elements $< 23$ lie in the left half ($A[0 \dots 2]$).
   - Algorithm sets $\text{high} = \text{mid} - 1 = 2$ (completely pruning the right half $A[4 \dots 6]$ where 5 actually resides!).
   - Search space becomes $A[0 \dots 2] = [45, 12, 88]$.
   - `mid = 1`, $A[1] = 12$. $5 < 12 \implies \text{high} = 0$.
   - `mid = 0`, $A[0] = 45$. $5 < 45 \implies \text{high} = -1$.
   - **Result**: Returns `-1` (**False Negative: Failed to find element 5 that is present in the array!**).

#### Conclusion & Defensive Measure
Running Binary Search without verifying the sorting precondition leads to silent algorithmic correctness failure. The UG-CSOO `SortedPreconditionValidator` guards against this by asserting array monotonicity before dispatching search routines.
