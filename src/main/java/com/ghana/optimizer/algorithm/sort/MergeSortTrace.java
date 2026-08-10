package com.ghana.optimizer.algorithm.sort;

import java.util.*;


public class MergeSortTrace {

    static class Task {
        String name;
        int priority;
        int budget;

        Task(String name, int priority, int budget) {
            this.name = name;
            this.priority = priority;
            this.budget = budget;
        }

        @Override
        public String toString() {
            return name + "(p=" + priority + ", b=" + budget + ")";
        }
    }

    static class TaskComparator {
        static int compare(Task a, Task b) {
            if (a.priority != b.priority) {
                return Integer.compare(a.priority, b.priority);
            }
            if (a.budget != b.budget) {
                return Integer.compare(a.budget, b.budget);
            }
            return 0;
        }
    }

    // ---------- Divide ----------
    static Task[] mergeSort(Task[] arr, int depth) {
        if (arr.length <= 1) {
            log(depth, "BASE", "[" + describe(arr) + "]", "-", "size <= 1, already sorted");
            return arr;
        }

        int mid = arr.length / 2;
        Task[] left = Arrays.copyOfRange(arr, 0, mid);
        Task[] right = Arrays.copyOfRange(arr, mid, arr.length);

        log(depth, "SPLIT", "[" + describe(arr) + "]",
                "[" + describe(left) + "] | [" + describe(right) + "]", "-");

        Task[] sortedLeft = mergeSort(left, depth + 1);
        Task[] sortedRight = mergeSort(right, depth + 1);

        return merge(sortedLeft, sortedRight, depth);
    }

    // ---------- Conquer + Combine ----------
    static Task[] merge(Task[] left, Task[] right, int depth) {
        Task[] result = new Task[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            Task a = left[i];
            Task b = right[j];
            int cmp = TaskComparator.compare(a, b);

            String rule;
            Task winner;
            if (cmp < 0) {
                rule = "priority " + a.priority + " < " + b.priority;
                winner = a;
            } else if (cmp > 0 && a.priority != b.priority) {
                rule = "priority " + b.priority + " < " + a.priority;
                winner = b;
            } else if (cmp != 0) {
                // priorities tied, budget decides
                rule = a.budget < b.budget
                        ? "priority tie -> budget " + a.budget + " < " + b.budget
                        : "priority tie -> budget " + b.budget + " < " + a.budget;
                winner = a.budget < b.budget ? a : b;
            } else {
                rule = "full tie -> left wins (stability)";
                winner = a; // cmp == 0: take left to preserve original order
            }

            if (winner == a) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
            log(depth, "MERGE", a + " vs " + b, "take " + result[k - 1], rule);
        }

        while (i < left.length) {
            log(depth, "MERGE", left[i] + " vs (right empty)", "take " + left[i], "append remainder");
            result[k++] = left[i++];
        }
        while (j < right.length) {
            log(depth, "MERGE", "(left empty) vs " + right[j], "take " + right[j], "append remainder");
            result[k++] = right[j++];
        }

        log(depth, "RESULT", "-", "[" + describe(result) + "]", "-");
        return result;
    }

    // ---------- Trace table printing ----------
    static void log(int depth, String phase, String comparing, String take, String rule) {
        String indent = "  ".repeat(depth);
        System.out.printf("%-6s d%d %-45s -> %-45s %s%n",
                phase, depth, indent + comparing, take, rule);
    }

    static String describe(Task[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < arr.length; idx++) {
            sb.append(arr[idx]);
            if (idx < arr.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    // ---------- Demo ----------
    public static void main(String[] args) {
        Task[] tasks = {
            new Task("T1", 3, 500),
            new Task("T2", 1, 800),
            new Task("T3", 2, 300),
            new Task("T4", 1, 200),
            new Task("T5", 3, 100),
            new Task("T6", 2, 250),
            new Task("T7", 4, 50),
            new Task("T8", 1, 800)
        };

        System.out.println("Input:  " + describe(tasks));
        System.out.println("--------------------------------------------------------------------------------");

        Task[] sorted = mergeSort(tasks, 0);

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Sorted: " + describe(sorted));
    }
}
