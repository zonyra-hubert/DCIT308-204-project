# 📐 Mathematical Proof Sketches (Module M9 / Section 10)

---

### Proof Sketch 1: Loop Invariant for Insertion Sort
**Theorem**: Insertion sort correctly sorts an array $A[0 \dots n-1]$ of $n$ elements in descending order.

**Loop Invariant**: At the start of each iteration of the outer `for` loop indexed by $i$ (where $1 \le i \le n-1$), the subarray $A[0 \dots i-1]$ consists of the elements originally in $A[0 \dots i-1]$, but sorted in descending order ($A[k] \ge A[k+1]$ for $0 \le k < i-1$).

1. **Initialization (Base Case)**:
   - Prior to the first iteration ($i = 1$), the subarray consists of the single element $A[0]$.
   - A single-element subarray is vacuously sorted in descending order. Hence, the invariant holds before the loop starts.

2. **Maintenance (Inductive Step)**:
   - The body of the loop extracts `key = A[i]`.
   - The inner `while` loop shifts elements $A[j]$ (where $j = i-1, i-2, \dots$) that are strictly less than `key` one position to the right ($A[j+1] = A[j]$).
   - Once an element $A[j] \ge \text{key}$ is found (or $j < 0$), `key` is placed at $A[j+1]$.
   - Because $A[0 \dots i-1]$ was already sorted descending, placing `key` in its correct relative position results in $A[0 \dots i]$ containing the original elements of $A[0 \dots i]$ in sorted descending order.
   - Incrementing $i$ preserves the invariant for the next pass.

3. **Termination**:
   - The loop terminates when $i = n$.
   - Substituting $i = n$ into the invariant yields that the subarray $A[0 \dots n-1]$ contains all original elements of the array in sorted descending order.
   - $\blacksquare$

---

### Proof Sketch 2: Mathematical Induction for Merge Sort Correctness
**Theorem**: The recursive `mergeSort(A, 0, n-1)` procedure returns a permutation of $A[0 \dots n-1]$ that is sorted for all $n \ge 1$.

**Inductive Hypothesis $P(n)$**: `mergeSort` correctly sorts any array slice of length $n$.

1. **Base Case ($n = 1$)**:
   - When $n = 1$, `low == high`. The algorithm immediately returns the array unchanged.
   - An array of length 1 is already sorted. Thus, $P(1)$ holds.

2. **Inductive Step**:
   - Assume $P(k)$ holds for all $1 \le k < n$. We must prove $P(n)$ holds for an array of length $n > 1$.
   - The algorithm computes $\text{mid} = \lfloor \frac{\text{low} + \text{high}}{2} \rfloor$ and splits $A$ into:
     - Left subproblem $L$ of size $n_1 = \text{mid} - \text{low} + 1 < n$.
     - Right subproblem $R$ of size $n_2 = \text{high} - \text{mid} < n$.
   - By inductive hypothesis, `mergeSort(L)` produces a sorted array $L'$, and `mergeSort(R)` produces a sorted array $R'$.
   - The `merge(L', R')` routine selects the smallest available element from $L'$ and $R'$ at each step. By the two-pointer merging property, all elements in the merged array are placed in monotonically non-decreasing order.
   - Thus, $P(n)$ is true. By mathematical induction, Merge Sort is correct for all $n \ge 1$.
   - $\blacksquare$

---

### Proof Sketch 3: Optimal Substructure for 0/1 Knapsack DP
**Theorem**: The 0/1 Knapsack problem satisfies the **Principle of Optimality (Optimal Substructure)**.

**Problem Formulation**: Let $S_n \subseteq \{1, 2, \dots, n\}$ be the optimal subset of items selected under weight/budget limit $W$, yielding total value $V(S_n)$.

1. **Case 1 (Item $n \notin S_n$)**:
   - If item $n$ is not included in the optimal solution, then $S_n = S_{n-1}$, where $S_{n-1}$ is a feasible subset of the first $n-1$ items under budget $W$.
   - **Claim**: $S_{n-1}$ must be an optimal solution for the subproblem $\{1, \dots, n-1\}$ with budget $W$.
   - *Proof by contradiction*: If there existed a feasible subset $S'_{n-1} \subseteq \{1, \dots, n-1\}$ with weight $\le W$ such that $V(S'_{n-1}) > V(S_{n-1})$, then $S'_{n-1}$ would also be feasible for $\{1, \dots, n\}$ with value $V(S'_{n-1}) > V(S_n)$, contradicting the optimality of $S_n$.

2. **Case 2 (Item $n \in S_n$)**:
   - If item $n$ with cost $c_n$ and priority $p_n$ is included, then $S_n = S_{n-1}^* \cup \{n\}$, with remaining budget $W - c_n$.
   - **Claim**: $S_{n-1}^*$ must be an optimal solution for the subproblem $\{1, \dots, n-1\}$ with budget $W - c_n$.
   - *Proof by contradiction*: If there existed $S'' \subseteq \{1, \dots, n-1\}$ with $\text{cost}(S'') \le W - c_n$ and $V(S'') > V(S_{n-1}^*)$, then the set $S'' \cup \{n\}$ would have cost $\le (W - c_n) + c_n = W$ and total value $V(S'') + p_n > V(S_{n-1}^*) + p_n = V(S_n)$, contradicting the optimality of $S_n$.

3. **Recurrence Relation**:
   $$\text{DP}[i][w] = \begin{cases} 
   0 & \text{if } i = 0 \text{ or } w = 0 \\ 
   \text{DP}[i-1][w] & \text{if } c_i > w \\ 
   \max(\text{DP}[i-1][w], \text{DP}[i-1][w - c_i] + p_i) & \text{if } c_i \le w 
   \end{cases}$$
   Because subproblems overlap and optimal solutions to subproblems combine to form global optimal solutions, Dynamic Programming guarantees the global maximum.
   - $\blacksquare$
