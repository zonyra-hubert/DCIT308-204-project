package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.search.BinarySearch;
import com.ghana.optimizer.algorithm.search.LinearSearch;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstrates linearSearchById() and linearSearchByCategory() against
 * best-case, worst-case, and not-found scenarios. Correctness is
 * checked on the returned value directly; the O(1)/O(n) comparison
 * count is checked by capturing what each method prints to the
 * console (both methods print "... after N comparison(s)").
 */
public class SearchingTest {

    private final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void captureConsoleOutput() {
        System.setOut(new PrintStream(capturedOutput));
    }

    @AfterEach
    void restoreConsoleOutput() {
        System.setOut(originalOut);
    }

    /** Pulls the comparison count out of the line each method prints, e.g. "...after 5 comparison(s)". */
    private int extractComparisonCount() {
        String printed = capturedOutput.toString();
        String marker = "after ";
        int start = printed.lastIndexOf(marker) + marker.length();
        int end = printed.indexOf(" comparison", start);
        return Integer.parseInt(printed.substring(start, end).trim());
    }

    /** Builds n requests with sequential IDs 1..n and a rotating category, all "unsorted" by design. */
    private DynamicArray<ServiceRequest> buildUnsortedRequests(int n) {
        DynamicArray<ServiceRequest> data = new DynamicArray<>();
        String[] categories = {"maintenance", "IT", "shuttle", "lab_move"};
        for (int i = 1; i <= n; i++) {
            String category = categories[i % categories.length];
            data.insert(new ServiceRequest(i, 1, null, category, (i % 5) + 1,
                    "2026-07-30T08:00:00", null, "pending"));
        }
        return data;
    }

    // ---------------- linearSearchById ----------------

    @Test
    void linearSearchById_bestCase_firstElement_O1() {
        DynamicArray<ServiceRequest> data = buildUnsortedRequests(1000);

        ServiceRequest found = LinearSearch.linearSearchById(data, 1); // first element

        assertNotNull(found);
        assertEquals(1, found.getRequestId());
        assertEquals(1, extractComparisonCount(), "Best case must take exactly 1 comparison, regardless of n");
    }

    @Test
    void linearSearchById_worstCase_lastElement_On() {
        int n = 1000;
        DynamicArray<ServiceRequest> data = buildUnsortedRequests(n);

        ServiceRequest found = LinearSearch.linearSearchById(data, n); // last element

        assertNotNull(found);
        assertEquals(n, found.getRequestId());
        assertEquals(n, extractComparisonCount(), "Worst-found case must scan all n elements");
    }

    @Test
    void linearSearchById_notFound_On() {
        int n = 1000;
        DynamicArray<ServiceRequest> data = buildUnsortedRequests(n);

        ServiceRequest found = LinearSearch.linearSearchById(data, 999999); // absent id

        assertNull(found);
        assertEquals(n, extractComparisonCount(), "Not-found case must scan all n elements");
    }

    @Test
    void linearSearchById_emptyDataset_boundaryCase() {
        DynamicArray<ServiceRequest> empty = new DynamicArray<>();

        ServiceRequest found = LinearSearch.linearSearchById(empty, 1);

        assertNull(found);
        assertEquals(0, extractComparisonCount());
    }

    @Test
    void linearSearchById_comparisonsScaleLinearlyWithPosition() {
        DynamicArray<ServiceRequest> data = buildUnsortedRequests(100);

        LinearSearch.linearSearchById(data, 10);
        int early = extractComparisonCount();

        LinearSearch.linearSearchById(data, 90);
        int late = extractComparisonCount();

        assertEquals(10, early);
        assertEquals(90, late);
        assertTrue(late > early, "Comparisons should grow with the target's position, evidencing O(n) behavior");
    }

    // ---------------- binarySearch ----------------

    @Test
    void binarySearch_findsExistingElement_inSortedArray() {
        int[] array = {1, 3, 5, 7, 9, 11};

        int index = BinarySearch.search(array, 7);

        assertEquals(3, index);
    }

    @Test
    void binarySearch_returnsMinusOne_whenElementIsMissing() {
        int[] array = {1, 3, 5, 7, 9, 11};

        int index = BinarySearch.search(array, 4);

        assertEquals(-1, index);
    }

    @Test
    void binarySearch_rejectsUnsortedInput() {
        int[] array = {5, 1, 9, 3};

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> BinarySearch.search(array, 9)
        );

        assertEquals("BinarySearch requires the array to be sorted in ascending order", exception.getMessage());
    }

    // ---------------- linearSearchByCategory ----------------

    @Test
    void linearSearchByCategory_findsAllMatches_normalCase() {
        DynamicArray<ServiceRequest> data = buildUnsortedRequests(100);

        DynamicArray<ServiceRequest> matches = LinearSearch.linearSearchByCategory(data, "IT");

        assertTrue(matches.size() > 0);
        for (int i = 0; i < matches.size(); i++) {
            assertEquals("IT", matches.get(i).getCategory());
        }
    }

    @Test
    void linearSearchByCategory_alwaysScansFullArray_noEarlyExit() {
        // Even if the FIRST element matches, category search must still inspect
        // every remaining element, because category is not a unique key.
        int n = 500;
        DynamicArray<ServiceRequest> data = new DynamicArray<>();
        data.insert(new ServiceRequest(1, 1, null, "maintenance", 3, "t", null, "pending")); // match at index 0
        for (int i = 2; i <= n; i++) {
            data.insert(new ServiceRequest(i, 1, null, "IT", 3, "t", null, "pending")); // non-matches
        }

        DynamicArray<ServiceRequest> matches = LinearSearch.linearSearchByCategory(data, "maintenance");

        assertEquals(1, matches.size());
        assertEquals(n, extractComparisonCount(), "Category search has no early exit — always O(n)");
    }

    @Test
    void linearSearchByCategory_noMatches_edgeCase() {
        DynamicArray<ServiceRequest> data = buildUnsortedRequests(50);

        DynamicArray<ServiceRequest> matches = LinearSearch.linearSearchByCategory(data, "nonexistent_category");

        assertEquals(0, matches.size());
        assertEquals(50, extractComparisonCount());
    }

    @Test
    void linearSearchByCategory_emptyDataset_boundaryCase() {
        DynamicArray<ServiceRequest> empty = new DynamicArray<>();

        DynamicArray<ServiceRequest> matches = LinearSearch.linearSearchByCategory(empty, "IT");

        assertEquals(0, matches.size());
        assertEquals(0, extractComparisonCount());
    }
}