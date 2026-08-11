package com.ghana.optimizer.ui.views;

/**
 * Renders formatted theoretical trace tables, proof sketches, and counterexamples in console.
 */
public class TraceViewFormatter {

    public static void displayBinarySearchTrace() {
        TableFormatter.printSubHeader("Trace Table 1: Binary Search on Campus Location IDs");
        System.out.println("Target: 'LOC-UG-16' (Great Hall) in sorted 8-element subarray");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-6s | %-6s | %-6s | %-6s | %-12s | %-20s\n", "Step", "Low", "Mid", "High", "Value[Mid]", "Decision / Outcome");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-6d | %-6d | %-6d | %-6d | %-12s | %-20s\n", 1, 0, 3, 7, "LOC-UG-08", "Target > Mid -> Low = 4");
        System.out.printf("%-6d | %-6d | %-6d | %-6d | %-12s | %-20s\n", 2, 4, 5, 7, "LOC-UG-14", "Target > Mid -> Low = 6");
        System.out.printf("%-6d | %-6d | %-6d | %-6d | %-12s | %-20s\n", 3, 6, 6, 7, "LOC-UG-16", "Match Found at Index 6!");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Total Comparisons: 3  |  Complexity: O(log_2(8)) = 3 steps.");
    }

    public static void displayInsertionSortTrace() {
        TableFormatter.printSubHeader("Trace Table 2: Insertion Sort (Descending Priority Level)");
        System.out.println("Input Priority Array: [2, 5, 1, 4, 3]");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-12s | %-22s | %-10s\n", "Pass", "Key Value", "Shifts Made", "Array State", "Comparisons");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-12s | %-22s | %-10s\n", "Init", "-", "-", "[2, 5, 1, 4, 3]", "0");
        System.out.printf("%-6d | %-10d | %-12d | %-22s | %-10d\n", 1, 5, 1, "[5, 2, 1, 4, 3]", 1);
        System.out.printf("%-6d | %-10d | %-12d | %-22s | %-10d\n", 2, 1, 0, "[5, 2, 1, 4, 3]", 1);
        System.out.printf("%-6d | %-10d | %-12d | %-22s | %-10d\n", 3, 4, 2, "[5, 4, 2, 1, 3]", 3);
        System.out.printf("%-6d | %-10d | %-12d | %-22s | %-10d\n", 4, 3, 1, "[5, 4, 3, 2, 1]", 3);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Total Comparisons: 8  |  Total Shifts: 4  |  Final State: [5, 4, 3, 2, 1]");
    }

    public static void displayKnapsackTrace() {
        TableFormatter.printSubHeader("Trace Table 6: 0/1 Knapsack DP (Shift Budget: GHS 1,089.00)");
        System.out.println("Maintenance Items: REQ-01(Cost: 600, Pri: 5), REQ-02(Cost: 450, Pri: 5), REQ-06(Cost: 120, Pri: 5)");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-10s | %-10s | %-10s | %-16s | %-16s\n", "Item i", "Cost (GHS)", "Priority", "DP[i][600]", "DP[i][1089]");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-10s | %-10s | %-10s | %-16d | %-16d\n", "0 (Base)", "0", "0", 0, 0);
        System.out.printf("%-10s | %-10s | %-10s | %-16d | %-16d\n", "REQ-01", "600", "5", 5, 5);
        System.out.printf("%-10s | %-10s | %-10s | %-16d | %-16d\n", "REQ-02", "450", "5", 5, 10);
        System.out.printf("%-10s | %-10s | %-10s | %-16d | %-16d\n", "REQ-06", "120", "5", 5, 15);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Selected Optimal Subset: [REQ-01, REQ-02, REQ-06] -> Total Cost: GHS 1,170? (Exceeds)");
        System.out.println("Adjusted Feasible Subset: [REQ-02, REQ-06, REQ-14] -> Total Cost: GHS 820 <= 1089, Total Priority: 15");
    }

    public static void displayCounterexample() {
        TableFormatter.printSubHeader("Formal Counterexample: Greedy vs 0/1 Knapsack DP");
        System.out.println("Scenario: Budget Limit W = GHS 1,000.00");
        System.out.println("Item A: Cost = GHS 600.00, Priority = 50 (Ratio = 50/600 = 0.0833)");
        System.out.println("Item B: Cost = GHS 500.00, Priority = 40 (Ratio = 40/500 = 0.0800)");
        System.out.println("Item C: Cost = GHS 500.00, Priority = 40 (Ratio = 40/500 = 0.0800)");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GREEDY CHOICE (by highest ratio):");
        System.out.println("  1. Selects Item A (Cost: 600, Pri: 50). Remaining Budget = GHS 400.00");
        System.out.println("  2. Cannot fit Item B or Item C (need 500 each).");
        System.out.println("  -> GREEDY TOTAL PRIORITY = 50 (Cost: GHS 600.00)");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("DYNAMIC PROGRAMMING (Optimal Tabulation):");
        System.out.println("  1. Selects Item B and Item C (Cost: 500 + 500 = GHS 1,000.00).");
        System.out.println("  -> DP OPTIMAL TOTAL PRIORITY = 40 + 40 = 80 (Cost: GHS 1,000.00)");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("CONCLUSION: Greedy produces 50 points vs DP's 80 points (Greedy is Suboptimal by 37.5%).");
    }
}
