package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.scheduling.PriorityDispatchScheduler;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.Resource;
import com.ghana.optimizer.model.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying PriorityDispatchScheduler:
 *  1. Priority Queue dispatch ordering (highest urgency first).
 *  2. Resource matching (Plumbing -> Plumber, Shuttle -> Vehicle, IT -> Technician).
 *  3. Shuttle peak override creation and elevation.
 *  4. Batch dispatching of requests.
 *  5. Dispatch report generation and metric accuracy.
 */
public class PriorityDispatchSchedulerTest {

    private PriorityDispatchScheduler scheduler;

    @BeforeEach
    public void setUp() {
        scheduler = new PriorityDispatchScheduler();
    }

    @Test
    public void testPriorityQueueOrdersByUrgencyLevel() {
        ServiceRequest lowPri = new ServiceRequest("REQ-01", "LOC-01", "Routine painting", 1, 50.0, 1.0, "PENDING");
        ServiceRequest critPri = new ServiceRequest("REQ-02", "LOC-02", "Major water valve burst", 5, 450.0, 2.0, "PENDING");
        ServiceRequest medPri = new ServiceRequest("REQ-03", "LOC-03", "AC filter cleaning", 3, 150.0, 1.0, "PENDING");

        scheduler.submitRequest(lowPri);
        scheduler.submitRequest(critPri);
        scheduler.submitRequest(medPri);

        assertEquals(3, scheduler.getPendingQueueSize());

        // First dispatch must be highest urgency (Urgency 5)
        PriorityDispatchScheduler.DispatchAssignment firstDispatch = scheduler.dispatchNext();
        assertNotNull(firstDispatch);
        assertEquals("REQ-02", firstDispatch.getServiceRequest().getId());
        assertEquals(5, firstDispatch.getServiceRequest().getPriorityLevel());

        // Second dispatch must be urgency 3
        PriorityDispatchScheduler.DispatchAssignment secondDispatch = scheduler.dispatchNext();
        assertNotNull(secondDispatch);
        assertEquals("REQ-03", secondDispatch.getServiceRequest().getId());
        assertEquals(3, secondDispatch.getServiceRequest().getPriorityLevel());

        // Third dispatch must be urgency 1
        PriorityDispatchScheduler.DispatchAssignment thirdDispatch = scheduler.dispatchNext();
        assertNotNull(thirdDispatch);
        assertEquals("REQ-01", thirdDispatch.getServiceRequest().getId());
        assertEquals(1, thirdDispatch.getServiceRequest().getPriorityLevel());

        assertNull(scheduler.dispatchNext(), "No pending requests should remain");
    }

    @Test
    public void testResourceMatchingAndAllocation() {
        Resource plumbingCrew = new Resource("RES-01", "Legon Plumbing Response Team", "PERSONNEL", 4.0, 50.0, "LOC-43", true);
        Resource shuttleBus = new Resource("RES-02", "Campus Shuttle 30-Seater", "VEHICLE", 30.0, 80.0, "LOC-21", true);
        Resource itTech = new Resource("RES-03", "UGCS Senior Network Technician", "PERSONNEL", 1.0, 45.0, "LOC-44", true);

        scheduler.registerResource(plumbingCrew);
        scheduler.registerResource(shuttleBus);
        scheduler.registerResource(itTech);

        ServiceRequest plumbingReq = new ServiceRequest("REQ-P", "LOC-06", "Akuafo Hall pipe leak repair", 5, 200.0, 2.0, "PENDING");
        plumbingReq.setCategory("Plumbing");

        scheduler.submitRequest(plumbingReq);
        PriorityDispatchScheduler.DispatchAssignment assignment = scheduler.dispatchNext();

        assertNotNull(assignment);
        assertNotNull(assignment.getAssignedResource());
        assertEquals("RES-01", assignment.getAssignedResource().getId());
        assertFalse(plumbingCrew.isAvailable(), "Allocated resource must become unavailable");
    }

    @Test
    public void testShuttlePeakOverride() {
        Resource shuttleBus = new Resource("RES-BUS-01", "30-Seater Shuttle Van", "VEHICLE", 30.0, 60.0, "LOC-21", true);
        scheduler.registerResource(shuttleBus);

        PriorityDispatchScheduler.DispatchAssignment overrideDispatch =
                scheduler.scheduleShuttlePeakOverride("LOC-UG-21", 45);

        assertNotNull(overrideDispatch);
        assertEquals(5, overrideDispatch.getServiceRequest().getPriorityLevel());
        assertTrue(overrideDispatch.getServiceRequest().getDescription().contains("Peak Shuttle Queue Override"));
        assertEquals("LOC-UG-21", overrideDispatch.getServiceRequest().getLocationId());
    }

    @Test
    public void testBatchDispatchAllAvailable() {
        for (int i = 1; i <= 5; i++) {
            scheduler.registerResource(new Resource("RES-" + i, "General Crew " + i, "PERSONNEL", 2.0, 30.0, "LOC-01", true));
            scheduler.submitRequest(new ServiceRequest("REQ-" + i, "LOC-0" + i, "Maintenance " + i, (i % 5) + 1, 100.0, 1.0, "PENDING"));
        }

        DynamicArray<PriorityDispatchScheduler.DispatchAssignment> batch = scheduler.dispatchAllAvailable();

        assertEquals(5, batch.size());
        assertEquals(0, scheduler.getPendingQueueSize());
        assertEquals(5, scheduler.getTotalDispatchesCompleted());

        String report = scheduler.generateDispatchReport();
        assertTrue(report.contains("CAMPUS PRIORITY DISPATCH SCHEDULE REPORT"));
        assertTrue(report.contains("Total Dispatched: 5"));
    }

    @Test
    public void testMyDequeEmergencyOverrideDispatch() {
        ServiceRequest standardHigh = new ServiceRequest("REQ-STD", "LOC-01", "Routine high priority", 4, 100.0, 1.0, "PENDING");
        scheduler.submitRequest(standardHigh);

        ServiceRequest emergencyReq = new ServiceRequest("REQ-EMERGENCY", "LOC-02", "Transformer Fire Emergency", 5, 500.0, 2.0, "PENDING");
        scheduler.submitEmergencyRequest(emergencyReq);

        assertEquals(1, scheduler.getEmergencyOverrideDeque().size());

        // Emergency request submitted to Deque must be dispatched FIRST, before the standard high priority queue
        PriorityDispatchScheduler.DispatchAssignment first = scheduler.dispatchNext();
        assertNotNull(first);
        assertEquals("REQ-EMERGENCY", first.getServiceRequest().getId());
        assertTrue(first.getNotes().contains("EMERGENCY OVERRIDE (MyDeque)"));

        // Next must be the standard request
        PriorityDispatchScheduler.DispatchAssignment second = scheduler.dispatchNext();
        assertNotNull(second);
        assertEquals("REQ-STD", second.getServiceRequest().getId());
    }
}
