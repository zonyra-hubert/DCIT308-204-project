package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.search.BinarySearch;
import com.ghana.optimizer.algorithm.search.ExponentialSearch;
import com.ghana.optimizer.algorithm.search.ExtendedLinearSearch;
import com.ghana.optimizer.algorithm.search.JumpSearch;
import com.ghana.optimizer.algorithm.search.LinearSearch;
import com.ghana.optimizer.ds.list.DynamicArray;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying generic functionality across all search algorithms.
 */
public class GenericSearchAlgorithmTest {

    @Test
    public void genericBinarySearch_findsElementInSortedStringArray() {
        String[] campusLocationsArray = {"Balme Library", "Computer Science Dept", "Main Gate", "Night Market", "Volta Hall"};

        int foundLocationIndex = BinarySearch.search(campusLocationsArray, "Night Market");

        assertEquals(3, foundLocationIndex);
    }

    @Test
    public void genericBinarySearch_supportsCustomComparator() {
        Integer[] numberArray = {100, 80, 60, 40, 20}; // Descending order
        Comparator<Integer> descendingComparator = (firstValue, secondValue) -> Integer.compare(secondValue, firstValue);

        int foundIndex = BinarySearch.search(numberArray, 60, descendingComparator);

        assertEquals(2, foundIndex);
    }

    @Test
    public void genericLinearSearch_findsElementInDynamicArray() {
        DynamicArray<String> hostelNamesList = new DynamicArray<>();
        hostelNamesList.insert("Akuafo Hall");
        hostelNamesList.insert("Commonwealth Hall");
        hostelNamesList.insert("Legon Hall");

        int matchIndex = LinearSearch.search(hostelNamesList, "Commonwealth Hall");

        assertEquals(1, matchIndex);
    }

    @Test
    public void genericJumpSearch_findsElementInSortedGenericArray() {
        Double[] doubleValuesArray = {1.5, 3.2, 4.8, 9.1, 12.4, 15.6, 18.9};

        int matchIndex = JumpSearch.jumpSearch(doubleValuesArray, 12.4);

        assertEquals(4, matchIndex);
    }

    @Test
    public void genericExponentialSearch_findsElementInSortedGenericArray() {
        String[] sortedDepartmentsArray = {"Chemistry", "Economics", "Law", "Mathematics", "Physics", "Statistics"};

        int foundIndex = ExponentialSearch.search(sortedDepartmentsArray, "Physics");

        assertEquals(4, foundIndex);
    }

    @Test
    public void genericExtendedLinearSearch_findsElementByProperty() {
        String[] hostelNamesArray = {"Volta Hall", "Mensah Sarbah Hall", "K.A. Busia Hall"};

        int foundIndex = ExtendedLinearSearch.searchByProperty(hostelNamesArray, String::length, 18); // "Mensah Sarbah Hall" length is 18

        assertEquals(1, foundIndex);
    }
}
