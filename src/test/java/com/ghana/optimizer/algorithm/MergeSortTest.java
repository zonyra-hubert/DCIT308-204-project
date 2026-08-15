package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.sort.MergeSort;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying MergeSort algorithm:
 *  1. Multi-attribute sorting on DynamicArray<ServiceRequest> (Urgency descending, Budget ascending).
 *  2. Stability (preserving initial relative order on full tie).
 *  3. Generic array merge sort with custom comparator.
 *  4. Boundary cases (empty array, single element).
 *  5. Legacy Task model compatibility.
 */
public class MergeSortTest {

    @Test
    public void testMergeSortDynamicArrayMultiAttribute() {
        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        // Priority 2, budget 500
        requests.add(new ServiceRequest("REQ-01", "LOC-01", "Plumbing", 2, 500.0, 1.0, "PENDING"));
        // Priority 5, budget 300
        requests.add(new ServiceRequest("REQ-02", "LOC-02", "Electrical", 5, 300.0, 1.0, "PENDING"));
        // Priority 5, budget 100 (tie on priority 5 with REQ-02, lower budget should come first)
        requests.add(new ServiceRequest("REQ-03", "LOC-03", "ICT", 5, 100.0, 1.0, "PENDING"));
        // Priority 1, budget 50
        requests.add(new ServiceRequest("REQ-04", "LOC-04", "Shuttle", 1, 50.0, 1.0, "PENDING"));
        // Priority 3, budget 200
        requests.add(new ServiceRequest("REQ-05", "LOC-05", "HVAC", 3, 200.0, 1.0, "PENDING"));

        MergeSort.sort(requests);

        // Expected order:
        // 1. REQ-03 (Pri: 5, Budget: 100)
        // 2. REQ-02 (Pri: 5, Budget: 300)
        // 3. REQ-05 (Pri: 3, Budget: 200)
        // 4. REQ-01 (Pri: 2, Budget: 500)
        // 5. REQ-04 (Pri: 1, Budget: 50)
        assertEquals("REQ-03", requests.get(0).getId());
        assertEquals(5, requests.get(0).getPriorityLevel());
        assertEquals(100.0, requests.get(0).getBudgetRequired());

        assertEquals("REQ-02", requests.get(1).getId());
        assertEquals(5, requests.get(1).getPriorityLevel());
        assertEquals(300.0, requests.get(1).getBudgetRequired());

        assertEquals("REQ-05", requests.get(2).getId());
        assertEquals(3, requests.get(2).getPriorityLevel());

        assertEquals("REQ-01", requests.get(3).getId());
        assertEquals(2, requests.get(3).getPriorityLevel());

        assertEquals("REQ-04", requests.get(4).getId());
        assertEquals(1, requests.get(4).getPriorityLevel());
    }

    @Test
    public void testMergeSortStabilityOnFullTie() {
        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        // Two requests with identical priority (4) and budget (250)
        ServiceRequest reqA = new ServiceRequest("REQ-A", "LOC-01", "Plumbing", 4, 250.0, 1.0, "PENDING");
        ServiceRequest reqB = new ServiceRequest("REQ-B", "LOC-02", "Electrical", 4, 250.0, 1.0, "PENDING");
        requests.add(reqA);
        requests.add(reqB);

        MergeSort.sort(requests);

        // Stability ensures reqA remains at index 0 and reqB at index 1
        assertEquals("REQ-A", requests.get(0).getId());
        assertEquals("REQ-B", requests.get(1).getId());
    }

    @Test
    public void testMergeSortBoundaryConditions() {
        DynamicArray<ServiceRequest> empty = new DynamicArray<>();
        MergeSort.sort(empty);
        assertEquals(0, empty.size());

        DynamicArray<ServiceRequest> single = new DynamicArray<>();
        single.add(new ServiceRequest("REQ-01", "LOC-01", "Plumbing", 3, 100.0, 1.0, "PENDING"));
        MergeSort.sort(single);
        assertEquals(1, single.size());
        assertEquals("REQ-01", single.get(0).getId());
    }

    @Test
    public void testGenericArrayMergeSort() {
        Integer[] numbers = {45, 12, 88, 23, 70, 5, 99};
        Integer[] sorted = MergeSort.sort(numbers, Comparator.naturalOrder());

        assertArrayEquals(new Integer[]{5, 12, 23, 45, 70, 88, 99}, sorted);
    }

    @Test
    public void testLegacyTaskMergeSort() {
        MergeSort.Task[] tasks = {
                new MergeSort.Task("T1", 3, 500),
                new MergeSort.Task("T2", 1, 800),
                new MergeSort.Task("T3", 2, 300),
                new MergeSort.Task("T4", 1, 200)
        };

        MergeSort.Task[] sorted = MergeSort.sort(tasks);

        assertEquals("T4", sorted[0].name);
        assertEquals(1, sorted[0].priority);
        assertEquals(200, sorted[0].budget);

        assertEquals("T2", sorted[1].name);
        assertEquals(1, sorted[1].priority);
        assertEquals(800, sorted[1].budget);
    }
}
