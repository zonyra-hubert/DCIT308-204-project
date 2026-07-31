package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.search.BinarySearch;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTest {

    @Test
    void findsElementInSortedArray() {
        int[] array = {1, 3, 5, 7, 9, 11};
        assertEquals(3, BinarySearch.search(array, 7));
    }

    @Test
    void returnsMinusOneWhenElementMissing() {
        int[] array = {1, 3, 5, 7, 9, 11};
        assertEquals(-1, BinarySearch.search(array, 4));
    }

    @Test
    void rejectsUnsortedInput() {
        int[] array = {5, 1, 9, 3};
        assertThrows(IllegalArgumentException.class, () -> BinarySearch.search(array, 9));
    }

    @Test
    void isSortedAscendingDetectsUnsortedArray() {
        assertFalse(BinarySearch.isSortedAscending(new int[]{3, 1, 2}));
        assertTrue(BinarySearch.isSortedAscending(new int[]{1, 2, 3}));
    }
}
