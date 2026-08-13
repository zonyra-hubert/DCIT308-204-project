package com.ghana.optimizer.algorithm.optimization;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

/**
 * 0/1 Knapsack Budget Optimizer for the hostel maintenance shift budget.
 * Maximizes total priority score while enforcing the hard budget cap of GHS 1,089.00.
 */
public class KnapsackOptimizer {

    public static final int BUDGET_LIMIT_GHS = 1089;

    public static DynamicArray<ServiceRequest> optimizeMaintenanceBudget(ServiceRequest[] requests) {
        return optimizeMaintenanceBudget(requests, BUDGET_LIMIT_GHS);
    }

    public static DynamicArray<ServiceRequest> optimizeMaintenanceBudget(ServiceRequest[] requests, int budgetLimitGHS) {
        if (requests == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        if (budgetLimitGHS < 0) {
            throw new IllegalArgumentException("budgetLimitGHS must be non-negative");
        }

        int n = requests.length;
        int[][] dp = new int[n + 1][budgetLimitGHS + 1];

        for (int i = 1; i <= n; i++) {
            ServiceRequest request = requests[i - 1];
            if (request == null) {
                continue;
            }

            int cost = (int) Math.ceil(request.getBudgetRequired());
            int priorityValue = request.getPriorityLevel();

            for (int w = 0; w <= budgetLimitGHS; w++) {
                if (cost <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - cost] + priorityValue);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return backtrackSelectedRequests(dp, requests, budgetLimitGHS);
    }

    public static DynamicArray<ServiceRequest> optimizeBudget(ServiceRequest[] requests) {
        return optimizeMaintenanceBudget(requests);
    }

    public static DynamicArray<ServiceRequest> backtrackSelectedRequests(int[][] dp, ServiceRequest[] requests, int budgetLimitGHS) {
        if (dp == null || requests == null) {
            throw new IllegalArgumentException("dp and requests must not be null");
        }
        if (budgetLimitGHS < 0) {
            throw new IllegalArgumentException("budgetLimitGHS must be non-negative");
        }

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();
        int remainingBudget = budgetLimitGHS;

        for (int i = requests.length; i > 0 && remainingBudget > 0; i--) {
            ServiceRequest request = requests[i - 1];
            if (request == null) {
                continue;
            }

            int cost = (int) Math.ceil(request.getBudgetRequired());
            if (dp[i][remainingBudget] != dp[i - 1][remainingBudget]) {
                selected.insert(request);
                remainingBudget -= cost;
            }
        }

        DynamicArray<ServiceRequest> orderedSelection = new DynamicArray<>();
        for (int i = selected.size() - 1; i >= 0; i--) {
            orderedSelection.insert(selected.get(i));
        }
        return orderedSelection;
    }

    public static void printDpTable(int[][] dp, ServiceRequest[] requests) {
        if (dp == null || requests == null) {
            throw new IllegalArgumentException("dp and requests must not be null");
        }

        System.out.println("\n0/1 Knapsack DP table (budget cap: GHS " + BUDGET_LIMIT_GHS + ")");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-8s | %-8s | %-8s | %-8s%n", "Item", "Cost", "Prio", "Total");
        System.out.println("--------------------------------------------------------------------------");

        for (int i = 0; i <= requests.length; i++) {
            int itemCost = 0;
            int itemPriority = 0;
            if (i > 0 && requests[i - 1] != null) {
                itemCost = (int) Math.ceil(requests[i - 1].getBudgetRequired());
                itemPriority = requests[i - 1].getPriorityLevel();
            }
            System.out.printf("%-8s | %-8d | %-8d | %-8d%n",
                    i == 0 ? "BASE" : requests[i - 1].getId(), itemCost, itemPriority, dp[i][BUDGET_LIMIT_GHS]);
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    public static String selectedRequestIds(DynamicArray<ServiceRequest> selectedRequests) {
        if (selectedRequests == null || selectedRequests.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < selectedRequests.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(selectedRequests.get(i).getId());
        }
        builder.append("]");
        return builder.toString();
    }
}
