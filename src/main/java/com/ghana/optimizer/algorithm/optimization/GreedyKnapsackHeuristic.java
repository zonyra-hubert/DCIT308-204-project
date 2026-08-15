package com.ghana.optimizer.algorithm.optimization;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

/**
 * Greedy Heuristic baseline solver for service request budget allocation.
 *
 * Sorts tickets by efficiency ratio (priority / cost) in descending order,
 * greedily taking items while remaining budget permits.
 *
 * Demonstrates the 37.5% suboptimality penalty against 0/1 Knapsack DP
 * (documented in docs/COUNTEREXAMPLES.md).
 *
 * Time Complexity: O(N log N) for ratio sorting + O(N) linear selection.
 * Space Complexity: O(N) auxiliary.
 */
public class GreedyKnapsackHeuristic {

    public static final double DEFAULT_BUDGET_LIMIT = 1089.00;

    /**
     * Immutable result container for Greedy selection.
     */
    public static class GreedyResult {
        private final DynamicArray<ServiceRequest> selectedRequests;
        private final int totalPriorityPoints;
        private final double totalCost;
        private final double budgetLimit;
        private final int candidatePoolSize;

        public GreedyResult(DynamicArray<ServiceRequest> selectedRequests,
                            int totalPriorityPoints,
                            double totalCost,
                            double budgetLimit,
                            int candidatePoolSize) {
            this.selectedRequests = selectedRequests;
            this.totalPriorityPoints = totalPriorityPoints;
            this.totalCost = totalCost;
            this.budgetLimit = budgetLimit;
            this.candidatePoolSize = candidatePoolSize;
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

        /**
         * Computes the suboptimality penalty percentage compared to the true DP optimal result.
         */
        public double computeSuboptimalityPenalty(KnapsackOptimizer.KnapsackResult dpResult) {
            if (dpResult == null || dpResult.getTotalPriorityPoints() == 0) {
                return 0.0;
            }
            int dpPoints = dpResult.getTotalPriorityPoints();
            int greedyPoints = this.totalPriorityPoints;
            return ((double) (dpPoints - greedyPoints) / dpPoints) * 100.0;
        }

        public String formatReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("================================================================================\n");
            sb.append("  GREEDY RATIO-BASED KNAPSACK HEURISTIC REPORT\n");
            sb.append("================================================================================\n");
            sb.append(String.format(" Operational Budget Cap   : GHS %.2f\n", budgetLimit));
            sb.append(String.format(" Candidate Request Pool   : %d tickets\n", candidatePoolSize));
            sb.append(String.format(" Selected For Shift       : %d tickets\n", selectedRequests.size()));
            sb.append(String.format(" Total Priority Resolved  : %d priority points\n", totalPriorityPoints));
            sb.append(String.format(" Total Budget Utilized    : GHS %.2f\n", totalCost));
            sb.append(String.format(" Remaining Unspent Budget : GHS %.2f\n", getBudgetRemaining()));
            sb.append("================================================================================\n");
            return sb.toString();
        }
    }

    /**
     * Solves budget allocation greedily using default budget limit W = GHS 1,089.00.
     */
    public static GreedyResult solveGreedy(DynamicArray<ServiceRequest> requests) {
        return solveGreedy(requests, DEFAULT_BUDGET_LIMIT);
    }

    /**
     * Solves budget allocation using greedy ratio ordering (priority / cost).
     *
     * @param requests Candidate tickets.
     * @param budgetLimit Budget constraint W in GHS.
     * @return GreedyResult.
     */
    public static GreedyResult solveGreedy(DynamicArray<ServiceRequest> requests, double budgetLimit) {
        if (requests == null || requests.size() == 0 || budgetLimit <= 0) {
            return new GreedyResult(new DynamicArray<>(), 0, 0.0, budgetLimit, requests != null ? requests.size() : 0);
        }

        int n = requests.size();
        DynamicArray<ServiceRequest> copy = new DynamicArray<>(n);
        for (int i = 0; i < n; i++) {
            copy.add(requests.get(i));
        }

        // Sort by priority/cost ratio descending
        sortRequestsByRatio(copy);

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();
        double currentSpent = 0.0;
        int totalPriority = 0;

        for (int i = 0; i < copy.size(); i++) {
            ServiceRequest req = copy.get(i);
            double cost = req.getBudgetRequired();
            if (currentSpent + cost <= budgetLimit) {
                selected.add(req);
                currentSpent += cost;
                totalPriority += req.getPriorityLevel();
            }
        }

        return new GreedyResult(selected, totalPriority, currentSpent, budgetLimit, n);
    }

    private static void sortRequestsByRatio(DynamicArray<ServiceRequest> array) {
        int n = array.size();
        if (n <= 1) return;

        ServiceRequest[] temp = new ServiceRequest[n];
        mergeSortRatio(array, temp, 0, n - 1);
    }

    private static void mergeSortRatio(DynamicArray<ServiceRequest> array, ServiceRequest[] temp, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSortRatio(array, temp, left, mid);
        mergeSortRatio(array, temp, mid + 1, right);
        mergeRatio(array, temp, left, mid, right);
    }

    private static void mergeRatio(DynamicArray<ServiceRequest> array, ServiceRequest[] temp, int left, int mid, int right) {
        for (int i = left; i <= right; i++) {
            temp[i] = array.get(i);
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            double ratioI = temp[i].getPriorityToCostRatio();
            double ratioJ = temp[j].getPriorityToCostRatio();

            // Descending ratio sort
            if (ratioI >= ratioJ) {
                array.set(k++, temp[i++]);
            } else {
                array.set(k++, temp[j++]);
            }
        }

        while (i <= mid) {
            array.set(k++, temp[i++]);
        }
        while (j <= right) {
            array.set(k++, temp[j++]);
        }
    }
}
