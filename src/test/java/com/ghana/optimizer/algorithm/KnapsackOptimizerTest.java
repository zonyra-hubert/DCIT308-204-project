package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.optimization.KnapsackOptimizer;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KnapsackOptimizerTest {

    @Test
    void optimizeMaintenanceBudget_shouldChooseBestFeasibleSubsetWithinShiftBudget() {
        ServiceRequest[] requests = new ServiceRequest[] {
                new ServiceRequest("REQ-UG-01", "LOC-UG-01", "Plumbing leak repair", 5, 600.0, 4.0, "PENDING"),
                new ServiceRequest("REQ-UG-02", "LOC-UG-02", "Electrical switchboard repair", 5, 450.0, 3.0, "PENDING"),
                new ServiceRequest("REQ-UG-06", "LOC-UG-06", "Water heater maintenance", 5, 120.0, 2.0, "PENDING"),
                new ServiceRequest("REQ-UG-14", "LOC-UG-14", "Circuit breaker reset", 5, 250.0, 2.0, "PENDING")
        };

        DynamicArray<ServiceRequest> selected = KnapsackOptimizer.optimizeMaintenanceBudget(requests);

        assertNotNull(selected);
        assertFalse(selected.isEmpty());
        assertEquals(3, selected.size());

        int totalCost = 0;
        int totalPriority = 0;
        for (int i = 0; i < selected.size(); i++) {
            ServiceRequest request = selected.get(i);
            totalCost += (int) Math.ceil(request.getBudgetRequired());
            totalPriority += request.getPriorityLevel();
        }

        assertTrue(totalCost <= KnapsackOptimizer.BUDGET_LIMIT_GHS);
        assertEquals(15, totalPriority);
        assertTrue(containsId(selected, "REQ-UG-02"));
        assertTrue(containsId(selected, "REQ-UG-06"));
        assertTrue(containsId(selected, "REQ-UG-14"));
    }

    private boolean containsId(DynamicArray<ServiceRequest> requests, String id) {
        for (int i = 0; i < requests.size(); i++) {
            if (id.equals(requests.get(i).getId())) {
                return true;
            }
        }
        return false;
    }
}
