package com.ghana.optimizer.ds;

import com.ghana.optimizer.ds.hash.CustomHashTable;
import com.ghana.optimizer.ds.list.DynamicArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CustomHashTable implementation.
 */
public class CustomHashTableTest {

    private CustomHashTable<String, Integer> campusLocationTable;

    @BeforeEach
    void initializeHashTable() {
        campusLocationTable = new CustomHashTable<>();
    }

    @Test
    void defaultConstructor_initializesWithParameterTwoCapacity() {
        assertEquals(761, campusLocationTable.getCapacity(), "Default initial capacity should equal System Parameter 2 (761)");
        assertEquals(0, campusLocationTable.size());
        assertTrue(campusLocationTable.isEmpty());
    }

    @Test
    void putAndGet_storesAndRetrievesEntriesCorrectly() {
        campusLocationTable.put("LOC-UG-01", 100);
        campusLocationTable.put("LOC-UG-02", 200);
        campusLocationTable.put("LOC-UG-03", 300);

        assertEquals(3, campusLocationTable.size());
        assertFalse(campusLocationTable.isEmpty());
        assertEquals(100, campusLocationTable.get("LOC-UG-01"));
        assertEquals(200, campusLocationTable.get("LOC-UG-02"));
        assertEquals(300, campusLocationTable.get("LOC-UG-03"));
    }

    @Test
    void put_updatesExistingValueForDuplicateKey() {
        campusLocationTable.put("LOC-UG-06", 50);
        assertEquals(50, campusLocationTable.get("LOC-UG-06"));
        assertEquals(1, campusLocationTable.size());

        campusLocationTable.put("LOC-UG-06", 99);
        assertEquals(99, campusLocationTable.get("LOC-UG-06"));
        assertEquals(1, campusLocationTable.size(), "Size should not increase when updating existing key");
    }

    @Test
    void remove_deletesSpecifiedEntryAndReturnsValue() {
        campusLocationTable.put("LOC-UG-09", 400);
        campusLocationTable.put("LOC-UG-10", 500);

        Integer removedValue = campusLocationTable.remove("LOC-UG-09");

        assertEquals(400, removedValue);
        assertNull(campusLocationTable.get("LOC-UG-09"));
        assertEquals(1, campusLocationTable.size());
        assertFalse(campusLocationTable.containsKey("LOC-UG-09"));
        assertTrue(campusLocationTable.containsKey("LOC-UG-10"));
    }

    @Test
    void remove_returnsNullForNonExistentKey() {
        campusLocationTable.put("LOC-UG-01", 100);

        Integer nonexistentRemovedValue = campusLocationTable.remove("NON-EXISTENT");

        assertNull(nonexistentRemovedValue);
        assertEquals(1, campusLocationTable.size());
    }

    @Test
    void containsKeyAndContainsValue_verifiesExistence() {
        campusLocationTable.put("Balme Library", 1);
        campusLocationTable.put("Computer Science Dept", 2);

        assertTrue(campusLocationTable.containsKey("Balme Library"));
        assertFalse(campusLocationTable.containsKey("Night Market"));

        assertTrue(campusLocationTable.containsValue(1));
        assertFalse(campusLocationTable.containsValue(999));
    }

    @Test
    void resizeAndRehashTable_triggersResizingWhenLoadFactorExceeded() {
        CustomHashTable<Integer, String> smallHashTable = new CustomHashTable<>(4, 0.75);

        smallHashTable.put(1, "Ticket Alpha");
        smallHashTable.put(2, "Ticket Beta");
        smallHashTable.put(3, "Ticket Gamma");
        smallHashTable.put(4, "Ticket Delta");

        assertTrue(smallHashTable.getCapacity() > 4, "Table capacity should double and expand upon exceeding load factor threshold");
        assertEquals(4, smallHashTable.size());
        assertEquals("Ticket Alpha", smallHashTable.get(1));
        assertEquals("Ticket Beta", smallHashTable.get(2));
        assertEquals("Ticket Gamma", smallHashTable.get(3));
        assertEquals("Ticket Delta", smallHashTable.get(4));
    }

    @Test
    void keysAndValues_returnsAllInsertedElements() {
        campusLocationTable.put("Key-One", 11);
        campusLocationTable.put("Key-Two", 22);

        DynamicArray<String> keyDynamicArray = campusLocationTable.keys();
        DynamicArray<Integer> valueDynamicArray = campusLocationTable.values();

        assertEquals(2, keyDynamicArray.size());
        assertEquals(2, valueDynamicArray.size());
    }

    @Test
    void clear_removesAllEntriesFromTable() {
        campusLocationTable.put("A", 1);
        campusLocationTable.put("B", 2);
        campusLocationTable.clear();

        assertEquals(0, campusLocationTable.size());
        assertTrue(campusLocationTable.isEmpty());
        assertNull(campusLocationTable.get("A"));
    }
}
