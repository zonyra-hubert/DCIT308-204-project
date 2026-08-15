package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.sort.SelectionSort;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SelectionSort.selectionSort() — sorts ServiceRequests
 * descending by urgency (priority level).
 */
public class SelectionSortTest {

    private ServiceRequest request(int id, int urgency) {
        return new ServiceRequest(id, 1, null, "maintenance", urgency, "t", null, "pending");
    }

    private DynamicArray<ServiceRequest> toDynamicArray(int[] urgencies) {
        DynamicArray<ServiceRequest> data = new DynamicArray<>();
        for (int i = 0; i < urgencies.length; i++) {
            data.insert(request(i + 1, urgencies[i]));
        }
        return data;
    }

    private boolean isSortedDescendingByUrgency(DynamicArray<ServiceRequest> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i).getUrgency() < data.get(i + 1).getUrgency()) return false;
        }
        return true;
    }

    // ---------------- correctness ----------------

    @Test
    void selectionSort_sortsDescendingByUrgency_normalCase() {
        DynamicArray<ServiceRequest> data = toDynamicArray(new int[]{3, 1, 5, 2, 4});

        SelectionSort.selectionSort(data);

        assertTrue(isSortedDescendingByUrgency(data));
        assertEquals(5, data.get(0).getUrgency());
        assertEquals(1, data.get(4).getUrgency());
    }

    @Test
    void selectionSort_alreadySortedDescending_normalCase() {
        DynamicArray<ServiceRequest> data = toDynamicArray(new int[]{5, 4, 3, 2, 1});

        SelectionSort.selectionSort(data);

        assertTrue(isSortedDescendingByUrgency(data));
    }

    @Test
    void selectionSort_reverseSorted_worstCaseSwaps() {
        DynamicArray<ServiceRequest> data = toDynamicArray(new int[]{1, 2, 3, 4, 5}); // ascending = worst case for descending target

        SelectionSort.selectionSort(data);

        assertTrue(isSortedDescendingByUrgency(data));
    }

    @Test
    void selectionSort_duplicateUrgencies_edgeCase() {
        DynamicArray<ServiceRequest> data = toDynamicArray(new int[]{3, 3, 3, 3});

        SelectionSort.selectionSort(data);

        assertTrue(isSortedDescendingByUrgency(data));
    }

    // ---------------- boundary cases ----------------

    @Test
    void selectionSort_emptyDataset_boundaryCase() {
        DynamicArray<ServiceRequest> empty = new DynamicArray<>();

        String trace = SelectionSort.selectionSort(empty);

        assertEquals(0, empty.size());
        assertTrue(trace.contains("Total comparisons: 0"));
        assertTrue(trace.contains("Total swaps: 0"));
    }

    @Test
    void selectionSort_singleElement_boundaryCase() {
        DynamicArray<ServiceRequest> single = toDynamicArray(new int[]{3});

        String trace = SelectionSort.selectionSort(single);

        assertEquals(1, single.size());
        assertTrue(trace.contains("Total comparisons: 0"));
    }

    // ---------------- complexity proof: comparisons ALWAYS n(n-1)/2 ----------------

    @Test
    void selectionSort_comparisonCountIsAlwaysNChooseTwo_regardlessOfInputOrder() {
        int n = 20;
        int expectedComparisons = n * (n - 1) / 2; // 190

        int[] alreadySorted = new int[n];
        int[] reverseSorted = new int[n];
        for (int i = 0; i < n; i++) {
            alreadySorted[i] = n - i; // already descending — the target order
            reverseSorted[i] = i + 1; // ascending — the opposite of target order
        }

        String traceAlreadySorted = SelectionSort.selectionSort(toDynamicArray(alreadySorted));
        String traceReverseSorted = SelectionSort.selectionSort(toDynamicArray(reverseSorted));

        assertTrue(traceAlreadySorted.contains("Total comparisons: " + expectedComparisons),
                "Comparisons must be n(n-1)/2 = " + expectedComparisons + " even when already sorted");
        assertTrue(traceReverseSorted.contains("Total comparisons: " + expectedComparisons),
                "Comparisons must be n(n-1)/2 = " + expectedComparisons + " regardless of input order — "
                        + "this is what separates selection sort from insertion sort");
    }

    // ---------------- complexity proof: swaps DO vary with input order ----------------

    @Test
    void selectionSort_swapCountVariesWithInputOrder_zeroWhenAlreadyCorrect() {
        DynamicArray<ServiceRequest> alreadyDescending = toDynamicArray(new int[]{5, 4, 3, 2, 1});

        String trace = SelectionSort.selectionSort(alreadyDescending);

        assertTrue(trace.contains("Total swaps: 0"),
                "No swaps should be needed when the input is already in the target order");
    }

    @Test
    void selectionSort_swapCountVariesWithInputOrder_nonZeroWhenReversed() {
        DynamicArray<ServiceRequest> ascending = toDynamicArray(new int[]{1, 2, 3, 4, 5}); // opposite of target order

        String trace = SelectionSort.selectionSort(ascending);

        assertFalse(trace.contains("Total swaps: 0"),
                "Swaps should be needed when the input is the reverse of the target order");
    }

    // ---------------- trace table shape ----------------

    @Test
    void selectionSort_traceTableHasOneRowPerPass() {
        int n = 6;
        DynamicArray<ServiceRequest> data = toDynamicArray(new int[]{3, 1, 4, 1, 5, 9});

        String trace = SelectionSort.selectionSort(data);

        // n elements -> n-1 outer-loop passes -> n-1 data rows, plus header/separator/summary lines
        long dataRowCount = trace.lines()
                .filter(line -> line.matches("^\\d+\\s+\\d+\\s+\\d+.*"))
                .count();
        assertEquals(n - 1, dataRowCount, "Trace table must have exactly n-1 rows for n elements");
    }
}
