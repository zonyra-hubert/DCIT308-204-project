package com.ghana.optimizer.ds;

import com.ghana.optimizer.ds.queue.PriorityQueue;
import com.ghana.optimizer.model.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriorityQueue implementation.
 */
public class PriorityQueueTest {

    private PriorityQueue<Integer> integerPriorityQueue;

    @BeforeEach
    void initializePriorityQueue() {
        integerPriorityQueue = new PriorityQueue<>();
    }

    @Test
    void defaultConstructor_createsEmptyMinHeap() {
        assertEquals(0, integerPriorityQueue.size());
        assertTrue(integerPriorityQueue.isEmpty());
        assertNull(integerPriorityQueue.peek());
    }

    @Test
    void enqueueAndDequeue_maintainsMinHeapOrderingForNaturalOrder() {
        integerPriorityQueue.enqueue(43);
        integerPriorityQueue.enqueue(10);
        integerPriorityQueue.enqueue(547);
        integerPriorityQueue.enqueue(5);
        integerPriorityQueue.enqueue(1089);

        assertEquals(5, integerPriorityQueue.size());
        assertEquals(5, integerPriorityQueue.peek());

        assertEquals(5, integerPriorityQueue.dequeue());
        assertEquals(10, integerPriorityQueue.dequeue());
        assertEquals(43, integerPriorityQueue.dequeue());
        assertEquals(547, integerPriorityQueue.dequeue());
        assertEquals(1089, integerPriorityQueue.dequeue());

        assertTrue(integerPriorityQueue.isEmpty());
        assertNull(integerPriorityQueue.dequeue());
    }

    @Test
    void customComparator_maintainsMaxHeapOrdering() {
        Comparator<Integer> maxHeapComparator = (firstValue, secondValue) -> Integer.compare(secondValue, firstValue);
        PriorityQueue<Integer> maxPriorityQueue = new PriorityQueue<>(maxHeapComparator);

        maxPriorityQueue.enqueue(15);
        maxPriorityQueue.enqueue(99);
        maxPriorityQueue.enqueue(42);
        maxPriorityQueue.enqueue(100);

        assertEquals(100, maxPriorityQueue.peek());
        assertEquals(100, maxPriorityQueue.dequeue());
        assertEquals(99, maxPriorityQueue.dequeue());
        assertEquals(42, maxPriorityQueue.dequeue());
        assertEquals(15, maxPriorityQueue.dequeue());
    }

    @Test
    void priorityQueueWithServiceRequests_ordersByUrgencyLevel() {
        Comparator<ServiceRequest> serviceRequestPriorityComparator = (firstRequest, secondRequest) ->
                Integer.compare(secondRequest.getUrgency(), firstRequest.getUrgency()); // Highest urgency first

        PriorityQueue<ServiceRequest> requestPriorityQueue = new PriorityQueue<>(serviceRequestPriorityComparator);

        ServiceRequest lowUrgencyRequest = new ServiceRequest(1, 101, null, "maintenance", 1, "2026-08-05T08:00:00", null, "pending");
        ServiceRequest criticalUrgencyRequest = new ServiceRequest(2, 102, null, "IT", 5, "2026-08-05T08:05:00", null, "pending");
        ServiceRequest mediumUrgencyRequest = new ServiceRequest(3, 103, null, "shuttle", 3, "2026-08-05T08:10:00", null, "pending");

        requestPriorityQueue.enqueue(lowUrgencyRequest);
        requestPriorityQueue.enqueue(criticalUrgencyRequest);
        requestPriorityQueue.enqueue(mediumUrgencyRequest);

        assertEquals(3, requestPriorityQueue.size());

        ServiceRequest highestPriorityExtracted = requestPriorityQueue.dequeue();
        assertEquals(5, highestPriorityExtracted.getUrgency());
        assertEquals(2, highestPriorityExtracted.getRequestId());

        ServiceRequest secondPriorityExtracted = requestPriorityQueue.dequeue();
        assertEquals(3, secondPriorityExtracted.getUrgency());
        assertEquals(3, secondPriorityExtracted.getRequestId());

        ServiceRequest thirdPriorityExtracted = requestPriorityQueue.dequeue();
        assertEquals(1, thirdPriorityExtracted.getUrgency());
        assertEquals(1, thirdPriorityExtracted.getRequestId());
    }

    @Test
    void resizeHeapArray_expandsCapacityWhenExceedingInitialLimit() {
        PriorityQueue<Integer> smallPriorityQueue = new PriorityQueue<>(2);

        smallPriorityQueue.enqueue(30);
        smallPriorityQueue.enqueue(20);
        smallPriorityQueue.enqueue(10); // Triggers resizing

        assertTrue(smallPriorityQueue.capacity() > 2);
        assertEquals(3, smallPriorityQueue.size());
        assertEquals(10, smallPriorityQueue.dequeue());
        assertEquals(20, smallPriorityQueue.dequeue());
        assertEquals(30, smallPriorityQueue.dequeue());
    }

    @Test
    void containsAndClear_operatesCorrectly() {
        integerPriorityQueue.enqueue(100);
        integerPriorityQueue.enqueue(200);

        assertTrue(integerPriorityQueue.contains(100));
        assertFalse(integerPriorityQueue.contains(999));

        integerPriorityQueue.clear();

        assertEquals(0, integerPriorityQueue.size());
        assertTrue(integerPriorityQueue.isEmpty());
        assertFalse(integerPriorityQueue.contains(100));
    }
}
