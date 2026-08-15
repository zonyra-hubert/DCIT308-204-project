package com.ghana.optimizer.algorithm.optimization;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

/**
 * 0/1 Knapsack Dynamic Programming Tabulation Solver.
 *
 * Maximizes total resolved priority points for campus maintenance tickets
 * subject to System Parameter 3: Operational Shift Budget Limit W = GHS 1,089.00.
 *
 * Recurrence:
 *     DP[i][w] = max(DP[i-1][w], DP[i-1][w - cost[i]] + priority[i])
 *
 * Time Complexity: O(N * W) where N is number of candidate tickets, W = 1089.
 * Space Complexity: O(N * W) for 2D tabulation table enabling exact subset backtracking.
 */
public class KnapsackOptimizer {

    public static final double DEFAULT_BUDGET_LIMIT = 1089.00;
    public static final int BUDGET_LIMIT_GHS = 1089;

    /**
     * Solves knapsack budget optimization over an array of ServiceRequest objects.
     */
    public static DynamicArray<ServiceRequest> optimizeMaintenanceBudget(ServiceRequest[] requests) {
        if (requests == null || requests.length == 0) {
            return new DynamicArray<>();
        }
        DynamicArray<ServiceRequest> arr = new DynamicArray<>(requests.length);
        for (ServiceRequest r : requests) {
            arr.add(r);
        }
        return optimize(arr, BUDGET_LIMIT_GHS).getSelectedRequests();
    }

    /**
     * Solves knapsack budget optimization over a DynamicArray of ServiceRequest objects.
     */
    public static DynamicArray<ServiceRequest> optimizeMaintenanceBudget(DynamicArray<ServiceRequest> requests) {
        if (requests == null || requests.size() == 0) {
            return new DynamicArray<>();
        }
        return optimize(requests, BUDGET_LIMIT_GHS).getSelectedRequests();
    }

    /**
     * Immutable container holding Knapsack optimization results.
     */
    public static class KnapsackResult {
        private final DynamicArray<ServiceRequest> selectedRequests;
        private final int totalPriorityPoints;
        private final double totalCost;
        private final double budgetLimit;
        private final int candidatePoolSize;
        private final int[][] dpTable;

        public KnapsackResult(DynamicArray<ServiceRequest> selectedRequests,
                              int totalPriorityPoints,
                              double totalCost,
                              double budgetLimit,
                              int candidatePoolSize,
                              int[][] dpTable) {
            this.selectedRequests = selectedRequests;
            this.totalPriorityPoints = totalPriorityPoints;
            this.totalCost = totalCost;
            this.budgetLimit = budgetLimit;
            this.candidatePoolSize = candidatePoolSize;
            this.dpTable = dpTable;
        }

        public DynamicArray<ServiceRequest> getSelectedRequests() {
            return selectedRequests;
        }

        public int getTotalPriorityPoints() {
            return totalPriorityPoints;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public double getBudgetLimit() {
            return budgetLimit;
        }

        public double getBudgetRemaining() {
            return budgetLimit - totalCost;
        }

        public int getCandidatePoolSize() {
            return candidatePoolSize;
        }

        public int getSelectedCount() {
            return selectedRequests.size();
        }

        public int[][] getDpTable() {
            return dpTable;
        }

        public String formatReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("================================================================================\n");
            sb.append("  0/1 KNAPSACK DYNAMIC PROGRAMMING OPTIMIZATION REPORT\n");
            sb.append("================================================================================\n");
            sb.append(String.format(" Operational Budget Cap   : GHS %.2f\n", budgetLimit));
            sb.append(String.format(" Candidate Request Pool   : %d tickets\n", candidatePoolSize));
            sb.append(String.format(" Selected For Shift       : %d tickets\n", selectedRequests.size()));
            sb.append(String.format(" Total Priority Resolved  : %d priority points\n", totalPriorityPoints));
            sb.append(String.format(" Total Budget Utilized    : GHS %.2f\n", totalCost));
            sb.append(String.format(" Remaining Unspent Budget : GHS %.2f\n", getBudgetRemaining()));
            sb.append("--------------------------------------------------------------------------------\n");
            sb.append(" Backtracked Optimal Shift Dispatch List:\n");
            sb.append(String.format(" %-12s | %-10s | %-8s | %-12s | %-25s\n", "REQUEST ID", "LOCATION", "URGENCY", "BUDGET REQ", "CATEGORY / DESC"));
            sb.append("--------------------------------------------------------------------------------\n");

            for (int i = 0; i < selectedRequests.size(); i++) {
                ServiceRequest req = selectedRequests.get(i);
                String desc = req.getDescription() != null ? req.getDescription() : req.getCategory();
                if (desc.length() > 25) desc = desc.substring(0, 22) + "...";
                sb.append(String.format(" %-12s | %-10s | %-8d | GHS %8.2f | %-25s\n",
                        req.getId(), req.getLocationId(), req.getPriorityLevel(), req.getBudgetRequired(), desc));
            }
            sb.append("================================================================================\n");
            return sb.toString();
        }
    }

    /**
     * Solves 0/1 Knapsack optimization using default budget limit W = GHS 1,089.00.
     */
    public static KnapsackResult optimize(DynamicArray<ServiceRequest> requests) {
        return optimize(requests, DEFAULT_BUDGET_LIMIT);
    }

    /**
     * Solves 0/1 Knapsack optimization using 2D DP tabulation and backtracking.
     *
     * @param requests Candidate pool of campus service requests.
     * @param budgetLimit Financial cap W in GHS.
     * @return KnapsackResult containing optimal selection and DP table metrics.
     */
    public static KnapsackResult optimize(DynamicArray<ServiceRequest> requests, double budgetLimit) {
        if (requests == null || requests.size() == 0 || budgetLimit <= 0) {
            return new KnapsackResult(new DynamicArray<>(), 0, 0.0, budgetLimit, requests != null ? requests.size() : 0, new int[1][1]);
        }

        int n = requests.size();
        int W = (int) Math.floor(budgetLimit);

        // dp[i][w] = max priority achievable with first i items and budget limit w
        int[][] dp = new int[n + 1][W + 1];
        int[][] minCost = new int[n + 1][W + 1];

        // Integer cost and value arrays (1-indexed for DP table)
        int[] weights = new int[n + 1];
        int[] values = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            ServiceRequest req = requests.get(i - 1);
            weights[i] = Math.max(1, (int) Math.ceil(req.getBudgetRequired()));
            values[i] = req.getPriorityLevel();
        }

        // Fill 2D DP Table with secondary cost minimization tie-breaker
        for (int i = 1; i <= n; i++) {
            int wt = weights[i];
            int val = values[i];

            for (int w = 0; w <= W; w++) {
                int withoutVal = dp[i - 1][w];
                int withoutCost = minCost[i - 1][w];

                if (wt <= w) {
                    int withVal = dp[i - 1][w - wt] + val;
                    int withCost = minCost[i - 1][w - wt] + wt;

                    if (withVal > withoutVal || (withVal == withoutVal && withCost < withoutCost)) {
                        dp[i][w] = withVal;
                        minCost[i][w] = withCost;
                    } else {
                        dp[i][w] = withoutVal;
                        minCost[i][w] = withoutCost;
                    }
                } else {
                    dp[i][w] = withoutVal;
                    minCost[i][w] = withoutCost;
                }
            }
        }

        int maxPriority = dp[n][W];

        // Backtracking to find exact subset
        DynamicArray<ServiceRequest> selected = new DynamicArray<>();
        int currW = W;
        double totalActualCost = 0.0;

        for (int i = n; i >= 1; i--) {
            int wt = weights[i];
            int val = values[i];

            if (currW >= wt && dp[i][currW] == dp[i - 1][currW - wt] + val && minCost[i][currW] == minCost[i - 1][currW - wt] + wt) {
                if (dp[i][currW] != dp[i - 1][currW] || minCost[i][currW] < minCost[i - 1][currW]) {
                    ServiceRequest req = requests.get(i - 1);
                    selected.add(req);
                    totalActualCost += req.getBudgetRequired();
                    currW -= wt;
                }
            }
        }

        // Reverse selected list so it appears in natural order
        DynamicArray<ServiceRequest> orderedSelected = new DynamicArray<>();
        for (int i = selected.size() - 1; i >= 0; i--) {
            orderedSelected.add(selected.get(i));
        }

        return new KnapsackResult(orderedSelected, maxPriority, totalActualCost, budgetLimit, n, dp);
    }
}
