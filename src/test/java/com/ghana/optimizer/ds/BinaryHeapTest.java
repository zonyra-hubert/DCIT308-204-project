package com.ghana.optimizer.ds;

import com.ghana.optimizer.ds.heap.BinaryHeap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BinaryHeap implementation.
 */
public class BinaryHeapTest {

    private BinaryHeap<Integer> integerBinaryHeap;

    @BeforeEach
    void initializeBinaryHeap() {
        integerBinaryHeap = new BinaryHeap<>();
    }

    @Test
    void defaultConstructor_createsEmptyHeap() {
        assertEquals(0, integerBinaryHeap.size());
        assertTrue(integerBinaryHeap.isEmpty());
        assertNull(integerBinaryHeap.peekTop());
    }

    @Test
    void insertAndExtractTop_maintainsMinHeapProperty() {
        integerBinaryHeap.insert(50);
        integerBinaryHeap.insert(20);
        integerBinaryHeap.insert(80);
        integerBinaryHeap.insert(10);
        integerBinaryHeap.insert(30);

        assertEquals(5, integerBinaryHeap.size());
        assertEquals(10, integerBinaryHeap.peekTop());

        assertEquals(10, integerBinaryHeap.extractTop());
        assertEquals(20, integerBinaryHeap.extractTop());
        assertEquals(30, integerBinaryHeap.extractMin());
        assertEquals(50, integerBinaryHeap.extractTop());
        assertEquals(80, integerBinaryHeap.extractTop());

        assertTrue(integerBinaryHeap.isEmpty());
        assertNull(integerBinaryHeap.extractTop());
    }

    @Test
    void customComparator_maintainsMaxHeapProperty() {
        Comparator<Integer> maxHeapComparator = (firstValue, secondValue) -> Integer.compare(secondValue, firstValue);
        BinaryHeap<Integer> maxBinaryHeap = new BinaryHeap<>(maxHeapComparator);

        maxBinaryHeap.insert(15);
        maxBinaryHeap.insert(99);
        maxBinaryHeap.insert(42);

        assertEquals(99, maxBinaryHeap.peekTop());
        assertEquals(99, maxBinaryHeap.extractTop());
        assertEquals(42, maxBinaryHeap.extractTop());
        assertEquals(15, maxBinaryHeap.extractTop());
    }

    @Test
    void resizeHeapArray_expandsArrayCapacity() {
        BinaryHeap<Integer> smallBinaryHeap = new BinaryHeap<>(2);

        smallBinaryHeap.insert(300);
        smallBinaryHeap.insert(100);
        smallBinaryHeap.insert(200);

        assertTrue(smallBinaryHeap.capacity() > 2);
        assertEquals(3, smallBinaryHeap.size());
        assertEquals(100, smallBinaryHeap.extractTop());
        assertEquals(200, smallBinaryHeap.extractTop());
        assertEquals(300, smallBinaryHeap.extractTop());
    }

    @Test
    void containsAndClear_resetsHeapState() {
        integerBinaryHeap.insert(77);
        integerBinaryHeap.insert(88);

        assertTrue(integerBinaryHeap.contains(77));
        assertFalse(integerBinaryHeap.contains(999));

        integerBinaryHeap.clear();

        assertEquals(0, integerBinaryHeap.size());
        assertTrue(integerBinaryHeap.isEmpty());
        assertFalse(integerBinaryHeap.contains(77));
    }
}
