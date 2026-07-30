package com.ghana.optimizer.algorithm.search;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;

/**
 * Linear Search (Core) — Wisdom Nunakpor (@wnunakpor001)
 *
 * Implements linearSearchById() and linearSearchByCategory() from scratch
 * over a DynamicArray of ServiceRequest objects. No built-in search
 * (Collections.binarySearch, stream().filter(), etc.) is used anywhere
 * in this class — every element is inspected by hand in a loop.
 *
 * ---------------------------------------------------------------------
 * WHY LINEAR SEARCH WORKS HERE: UNSORTED DATA
 * ---------------------------------------------------------------------
 * Linear search makes no assumption about ordering — it simply inspects
 * elements one at a time from the start of the array until it finds a
 * match or runs out of elements. That's exactly why it fits
 * service_requests data: requests arrive and get stored in submission
 * order, NOT sorted by ID or category. Binary search would require
 * sorting first — an unnecessary O(n log n) cost if you only search
 * occasionally.
 */


public class LinearSearch {

    /**
     * Searches for a single ServiceRequest by its unique requestId.
     * requestId is unique (it's the database's primary key), so this
     * stops and returns the moment it finds a match — that early exit
     * is exactly what keeps the best case O(1).
     *
     * @param data     the unsorted collection of service requests to scan
     * @param targetId the requestId being searched for
     * @return the matching ServiceRequest, or null if none was found
     */


    /**
     * -------------------------------------------------------------------
     *                  LinearSearch Method By ID
     ---------------------------------------------------------------------
     */
    public static ServiceRequest linearSearchById(DynamicArray<ServiceRequest> data, int targetId) {
        int comparisons = 0;

        for (int i = 0; i < data.size(); i++) {
            comparisons++; // one comparison per element inspected
            ServiceRequest current = data.get(i);

            if (current.getRequestId() == targetId) {
                System.out.println("linearSearchById: found id=" + targetId
                        + " at index " + i + " after " + comparisons + " comparison(s)");
                return current; // early exit — id is unique, no need to keep scanning
            }
        }

        System.out.println("linearSearchById: id=" + targetId + " not found after "
                + comparisons + " comparison(s)");
        return null;
    }


    /**
     * -------------------------------------------------------------------
     *                 LinearSearch Method By Category
     ---------------------------------------------------------------------
     */

    /**
     * Searches for ALL ServiceRequests matching a given category. Unlike
     * requestId, category is NOT unique — many requests can share
     * "maintenance", "IT", "shuttle", etc. — so this method can never
     * stop early; it must inspect every element exactly once no matter
     * how many matches it already found. That makes it always O(n),
     * even in the best case — a direct contrast with linearSearchById.
     *
     * @param data the unsorted collection of service requests to scan
     * @param targetCategory the category being searched for (exact match)
     * @return a DynamicArray of every matching ServiceRequest (empty if none found)
     */
    public static DynamicArray<ServiceRequest> linearSearchByCategory(DynamicArray<ServiceRequest> data, String targetCategory) {
        int comparisons = 0;
        DynamicArray<ServiceRequest> matches = new DynamicArray<>();

        for (int i = 0; i < data.size(); i++) {
            comparisons++; // one comparison per element inspected
            ServiceRequest current = data.get(i);

            if (current.getCategory().equals(targetCategory)) {
                matches.insert(current); // no early exit — category isn't unique
            }
        }

        System.out.println("linearSearchByCategory: found " + matches.size()
                + " match(es) for '" + targetCategory + "' after " + comparisons + " comparison(s)");
        return matches;
    }
}