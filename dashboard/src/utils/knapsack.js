/**
 * Performs 0/1 Knapsack Dynamic Programming resource allocation
 * maximizing priority value achieved under budget constraint GHS.
 */
export function solve01Knapsack(requests, budgetLimitGHS) {
  const n = requests.length;
  const W = Math.floor(budgetLimitGHS);
  
  // dp[i][w] stores max value using first i items and weight limit w
  const dp = Array.from({ length: n + 1 }, () => new Array(W + 1).fill(0));

  for (let i = 1; i <= n; i++) {
    const cost = Math.ceil(requests[i - 1].budget);
    const value = requests[i - 1].priority;

    for (let w = 0; w <= W; w++) {
      if (cost <= w) {
        dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - cost] + value);
      } else {
        dp[i][w] = dp[i - 1][w];
      }
    }
  }

  // Backtrack to find selected items
  let w = W;
  const selected = [];
  for (let i = n; i > 0 && w > 0; i--) {
    if (dp[i][w] !== dp[i - 1][w]) {
      selected.push(requests[i - 1]);
      w -= Math.ceil(requests[i - 1].budget);
    }
  }

  const totalValue = dp[n][W];
  const totalCost = selected.reduce((sum, r) => sum + r.budget, 0);

  return {
    selectedRequests: selected,
    totalValue,
    totalCost,
    remainingBudget: budgetLimitGHS - totalCost
  };
}

/**
 * Performs Greedy Allocation based on Highest Priority-to-Cost Ratio
 */
export function solveGreedyAllocation(requests, budgetLimitGHS) {
  // Sort requests by priority / budget ratio descending
  const sorted = [...requests].sort((a, b) => (b.priority / b.budget) - (a.priority / a.budget));
  
  let currentCost = 0;
  let totalValue = 0;
  const selected = [];

  for (const req of sorted) {
    if (currentCost + req.budget <= budgetLimitGHS) {
      selected.push(req);
      currentCost += req.budget;
      totalValue += req.priority;
    }
  }

  return {
    selectedRequests: selected,
    totalValue,
    totalCost: currentCost,
    remainingBudget: budgetLimitGHS - currentCost
  };
}
