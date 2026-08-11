package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.sort.InsertionSort;
import com.ghana.optimizer.algorithm.sort.SelectionSort;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortAlgorithmTest {

    @Test
    public void testSelectionSortDescendingOrder() {
        DynamicArray<ServiceRequest> data = new DynamicArray<>();
        data.add(new ServiceRequest(1, 1, null, "Maint", 2, "2026-08-11", null, "PENDING"));
        data.add(new ServiceRequest(2, 2, null, "Maint", 5, "2026-08-11", null, "PENDING"));
        data.add(new ServiceRequest(3, 3, null, "Maint", 1, "2026-08-11", null, "PENDING"));
        data.add(new ServiceRequest(4, 4, null, "Maint", 4, "2026-08-11", null, "PENDING"));

        SelectionSort.selectionSort(data);

        assertEquals(5, data.get(0).getUrgency());
        assertEquals(4, data.get(1).getUrgency());
        assertEquals(2, data.get(2).getUrgency());
        assertEquals(1, data.get(3).getUrgency());
    }

    @Test
    public void testInsertionSortDescendingOrder() {
        DynamicArray<ServiceRequest> data = new DynamicArray<>();
        data.add(new ServiceRequest(1, 1, null, "Maint", 1, "2026-08-11", null, "PENDING"));
        data.add(new ServiceRequest(2, 2, null, "Maint", 3, "2026-08-11", null, "PENDING"));
        data.add(new ServiceRequest(3, 3, null, "Maint", 5, "2026-08-11", null, "PENDING"));
        data.add(new ServiceRequest(4, 4, null, "Maint", 2, "2026-08-11", null, "PENDING"));

        InsertionSort.insertionSort(data);

        assertEquals(5, data.get(0).getUrgency());
        assertEquals(3, data.get(1).getUrgency());
        assertEquals(2, data.get(2).getUrgency());
        assertEquals(1, data.get(3).getUrgency());
    }

    @Test
    public void testSortEmptyAndSingleElement() {
        DynamicArray<ServiceRequest> empty = new DynamicArray<>();
        SelectionSort.selectionSort(empty);
        assertEquals(0, empty.size());

        DynamicArray<ServiceRequest> single = new DynamicArray<>();
        single.add(new ServiceRequest(1, 1, null, "Maint", 4, "2026-08-11", null, "PENDING"));
        InsertionSort.insertionSort(single);
        assertEquals(1, single.size());
        assertEquals(4, single.get(0).getUrgency());
    }
}
