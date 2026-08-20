package com.ghana.optimizer.ds;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.ds.tree.BTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BTree implementation.
 */
public class BTreeTest {

    private BTree<Integer, String> bTree;

    @BeforeEach
    void initializeBTree() {
        bTree = new BTree<>(2);
    }

    @Test
    void defaultConstructor_createsEmptyBTree() {
        assertEquals(0, bTree.size());
        assertTrue(bTree.isEmpty());
        assertFalse(bTree.contains(5));
    }

    @Test
    void insertAndSearch_storesAndRetrievesEntries() {
        bTree.insert(10, "Ten");
        bTree.insert(20, "Twenty");
        bTree.insert(5, "Five");

        assertEquals(3, bTree.size());
        assertEquals("Ten", bTree.search(10));
        assertEquals("Twenty", bTree.search(20));
        assertEquals("Five", bTree.search(5));
    }

    @Test
    void contains_returnsTrueWhenKeyExists() {
        bTree.insert(15, "Fifteen");
        assertTrue(bTree.contains(15));
        assertFalse(bTree.contains(99));
    }

    @Test
    void insertDuplicateKey_overwritesExistingValue() {
        bTree.insert(7, "Seven");
        bTree.insert(7, "Seven Updated");

        assertEquals(1, bTree.size());
        assertEquals("Seven Updated", bTree.search(7));
    }

    @Test
    void inorderKeys_returnsKeysInSortedOrder() {
        bTree.insert(30, "Thirty");
        bTree.insert(10, "Ten");
        bTree.insert(20, "Twenty");
        bTree.insert(5, "Five");
        bTree.insert(25, "Twenty-Five");

        DynamicArray<Integer> keys = bTree.inorderKeys();

        assertEquals(5, keys.size());
        assertEquals(5, keys.get(0));
        assertEquals(10, keys.get(1));
        assertEquals(20, keys.get(2));
        assertEquals(25, keys.get(3));
        assertEquals(30, keys.get(4));
    }

    @Test
    void height_returnsZeroForSingleNodeAndPositiveForMultipleLevels() {
        assertEquals(-1, bTree.height());

        bTree.insert(1, "One");
        assertEquals(0, bTree.height());

        for (int key = 2; key <= 12; key++) {
            bTree.insert(key, "Value " + key);
        }

        assertTrue(bTree.height() >= 1);
    }

    @Test
    void clear_resetsTreeState() {
        bTree.insert(50, "Fifty");
        bTree.clear();

        assertEquals(0, bTree.size());
        assertTrue(bTree.isEmpty());
        assertNull(bTree.search(50));
    }
}
