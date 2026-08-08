package com.ghana.optimizer.algorithm.sort;
import java.util.Arrays;

/**
 * Divide-and-conquer merge sort with multi-attribute ordering:
 *   1. priority ascending (primary key)
 *   2. budget ascending   (secondary key — only used when priority ties)
 *
 * Stable: on a full tie (priority AND budget equal), the item that was
 * on the left keeps its original relative position, because merge()
 * uses "<=" and always favors the left side when compare() == 0.
 */
public class MergeSort {

    // ---------- Data model ----------
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
            return name + "(" + priority + "," + budget + ")";
        }
    }

    // ---------- The comparator: the ONLY place ordering rules live ----------
    static int compare(Task a, Task b) {
        if (a.priority != b.priority) {
            return a.priority - b.priority;   // primary key
        }
        return a.budget - b.budget;           // secondary key (tiebreak)
    }

    // ---------- Divide ----------
    static Task[] sort(Task[] arr) {
        if (arr.length <= 1) {
            return arr; // base case: a single element is trivially sorted
        }

        int mid = arr.length / 2;
        Task[] left = Arrays.copyOfRange(arr, 0, mid);
        Task[] right = Arrays.copyOfRange(arr, mid, arr.length);

        Task[] sortedLeft = sort(left);
        Task[] sortedRight = sort(right);

        return merge(sortedLeft, sortedRight);
    }

    // ---------- Conquer + Combine ----------
    static Task[] merge(Task[] left, Task[] right) {
        Task[] result = new Task[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (compare(left[i], right[j]) <= 0) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
        return result;
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

        System.out.println("Input:  " + Arrays.toString(tasks));
        Task[] sorted = sort(tasks);
        System.out.println("Sorted: " + Arrays.toString(sorted));
    }
}

    

