package com.ghana.optimizer.algorithm.scheduling;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.ds.queue.MyDeque;
import com.ghana.optimizer.ds.queue.PriorityQueue;
import com.ghana.optimizer.model.Resource;
import com.ghana.optimizer.model.ServiceRequest;

import java.util.Comparator;

/**
 * Priority Dispatch and Resource Scheduling Engine for the University of Ghana
 * Campus Service Operations Optimizer (UG-CSOO).
 *
 * Operational Responsibilities:
 *  1. Priority Queue Maintenance Dispatch:
 *     - Uses custom PriorityQueue (backed by BinaryHeap) to extract highest urgency
 *       facilities maintenance tickets (Urgency 5 -> 1) in O(log N) time.
 *  2. Emergency Deque Overrides:
 *     - Uses custom MyDeque to inject critical life-safety tickets to the front (addFront)
 *       or rear (addRear) for immediate O(1) override dispatch.
 *  3. Resource Matching & Allocation:
 *     - Matches pending requests to available personnel crews (plumbers, electricians, IT staff)
 *       and utility vehicles (shuttles, pickups, cranes).
 *  4. Real-Time Operational Auditing:
 *     - Records dispatch assignments, budget utilization, and operational resolution metrics.
 */
public class PriorityDispatchScheduler {

    /**
     * Dispatch Assignment Record capturing the binding of a ServiceRequest to a Resource.
     */
    public static class DispatchAssignment {
        private final ServiceRequest serviceRequest;
        private final Resource assignedResource;
        private final long dispatchTimestampMs;
        private final double estimatedCostGHS;
        private final String notes;

        public DispatchAssignment(ServiceRequest serviceRequest, Resource assignedResource,
                                  double estimatedCostGHS, String notes) {
            this.serviceRequest = serviceRequest;
            this.assignedResource = assignedResource;
            this.dispatchTimestampMs = System.currentTimeMillis();
            this.estimatedCostGHS = estimatedCostGHS;
            this.notes = notes;
        }

        public ServiceRequest getServiceRequest() { return serviceRequest; }
        public Resource getAssignedResource() { return assignedResource; }
        public long getDispatchTimestampMs() { return dispatchTimestampMs; }
        public double getEstimatedCostGHS() { return estimatedCostGHS; }
        public String getNotes() { return notes; }

        @Override
        public String toString() {
            String resourceName = assignedResource != null ? assignedResource.getName() : "UNASSIGNED";
            return String.format("Dispatch[Ticket=%s (Pri:%d) -> Resource=%s | Cost=GHS %.2f | Loc=%s]",
                    serviceRequest.getId(), serviceRequest.getPriorityLevel(), resourceName,
                    estimatedCostGHS, serviceRequest.getLocationId());
        }
    }

    // Default Priority Comparator: Highest Urgency (5 -> 1), tiebreak by lower request ID / earlier arrival
    public static final Comparator<ServiceRequest> DISPATCH_PRIORITY_COMPARATOR = (requestA, requestB) -> {
        if (requestA == null && requestB == null) return 0;
        if (requestA == null) return 1;
        if (requestB == null) return -1;

        // Primary: Urgency / Priority Level descending (5 down to 1)
        int priorityComparison = Integer.compare(requestB.getPriorityLevel(), requestA.getPriorityLevel());
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Secondary: Cost/Budget ascending (quicker/cheaper dispatch on equal priority)
        int costComparison = Double.compare(requestA.getBudgetRequired(), requestB.getBudgetRequired());
        if (costComparison != 0) {
            return costComparison;
        }

        // Tertiary: Request ID ascending (FIFO ordering for deterministic behavior)
        return Integer.compare(requestA.getRequestId(), requestB.getRequestId());
    };

    private final PriorityQueue<ServiceRequest> pendingRequestsQueue;
    private final MyDeque<ServiceRequest> emergencyOverrideDeque;
    private final DynamicArray<Resource> availableResources;
    private final DynamicArray<DispatchAssignment> dispatchHistory;
    private int totalDispatchesCompleted;
    private double cumulativeDispatchedBudgetGHS;
    private int cumulativePriorityPointsResolved;

    /**
     * Default constructor initializing scheduler with custom priority queue and emergency deque.
     */
    public PriorityDispatchScheduler() {
        this.pendingRequestsQueue = new PriorityQueue<>(DISPATCH_PRIORITY_COMPARATOR);
        this.emergencyOverrideDeque = new MyDeque<>();
        this.availableResources = new DynamicArray<>();
        this.dispatchHistory = new DynamicArray<>();
        this.totalDispatchesCompleted = 0;
        this.cumulativeDispatchedBudgetGHS = 0.0;
        this.cumulativePriorityPointsResolved = 0;
    }

    /**
     * Submits an emergency ticket directly to the FRONT of the dispatch pipeline
     * using MyDeque in O(1) time.
     *
     * @param emergencyRequest Emergency service ticket.
     */
    public void submitEmergencyRequest(ServiceRequest emergencyRequest) {
        if (emergencyRequest == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        emergencyOverrideDeque.addFront(emergencyRequest);
    }

    /**
     * Gets the emergency override deque.
     *
     * @return MyDeque instance for emergency requests.
     */
    public MyDeque<ServiceRequest> getEmergencyOverrideDeque() {
        return emergencyOverrideDeque;
    }

    /**
     * Enqueues a new campus service request into the standard priority dispatch queue in O(log N) time.
     *
     * @param request Campus service ticket to submit.
     */
    public void submitRequest(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        pendingRequestsQueue.enqueue(request);
    }

    /**
     * Bulk submits a collection of service requests into the dispatch queue.
     *
     * @param requestList Collection of service requests.
     */
    public void submitAll(DynamicArray<ServiceRequest> requestList) {
        if (requestList == null) return;
        for (int i = 0; i < requestList.size(); i++) {
            submitRequest(requestList.get(i));
        }
    }

    /**
     * Registers an operational resource (crew, vehicle, IT technician) into the resource pool.
     *
     * @param resource Resource unit to register.
     */
    public void registerResource(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null.");
        }
        availableResources.add(resource);
    }

    /**
     * Registers a list of operational resources into the resource pool.
     *
     * @param resourceList List of resources.
     */
    public void registerAllResources(DynamicArray<Resource> resourceList) {
        if (resourceList == null) return;
        for (int i = 0; i < resourceList.size(); i++) {
            registerResource(resourceList.get(i));
        }
    }

    /**
     * Dispatches the single highest priority pending request to the best matching available resource.
     *
     * @return DispatchAssignment record, or null if no requests are pending.
     */
    public DispatchAssignment dispatchNext() {
        ServiceRequest topPriorityRequest;
        boolean isEmergency = false;

        if (!emergencyOverrideDeque.isEmpty()) {
            topPriorityRequest = emergencyOverrideDeque.removeFront();
            isEmergency = true;
        } else if (!pendingRequestsQueue.isEmpty()) {
            topPriorityRequest = pendingRequestsQueue.dequeue();
        } else {
            return null;
        }

        Resource matchingResource = findAndAllocateBestResource(topPriorityRequest);

        double estimatedCost = topPriorityRequest.getBudgetRequired();
        if (matchingResource != null) {
            estimatedCost += matchingResource.getCostPerHour() * topPriorityRequest.getEstimatedDurationHrs();
        }

        topPriorityRequest.setStatus("DISPATCHED");

        String notes;
        if (isEmergency) {
            notes = matchingResource != null
                    ? "EMERGENCY OVERRIDE (MyDeque) dispatched with resource: " + matchingResource.getName()
                    : "EMERGENCY OVERRIDE (MyDeque) dispatched without dedicated resource";
        } else {
            notes = matchingResource != null
                    ? "Dispatched with resource: " + matchingResource.getName()
                    : "Dispatched without dedicated resource (Standard Queue)";
        }

        DispatchAssignment assignment = new DispatchAssignment(
                topPriorityRequest, matchingResource, estimatedCost, notes);

        dispatchHistory.add(assignment);
        totalDispatchesCompleted++;
        cumulativeDispatchedBudgetGHS += estimatedCost;
        cumulativePriorityPointsResolved += topPriorityRequest.getPriorityLevel();

        return assignment;
    }

    /**
     * Batches dispatches for all pending requests while matching available resources exist.
     *
     * @return DynamicArray of created DispatchAssignment records.
     */
    public DynamicArray<DispatchAssignment> dispatchAllAvailable() {
        DynamicArray<DispatchAssignment> batchResults = new DynamicArray<>();
        while (!pendingRequestsQueue.isEmpty()) {
            DispatchAssignment assignment = dispatchNext();
            if (assignment != null) {
                batchResults.add(assignment);
            }
        }
        return batchResults;
    }

    /**
     * Creates and dispatches a high-priority Campus Shuttle Queue Override for peak congestion.
     *
     * @param stopLocationId Campus location ID of transit stop (e.g. "LOC-UG-21" Main Gate Stop).
     * @param passengerDemand Estimated waiting passengers.
     * @return Created DispatchAssignment record.
     */
    public DispatchAssignment scheduleShuttlePeakOverride(String stopLocationId, int passengerDemand) {
        int overrideUrgency = passengerDemand >= 30 ? 5 : 4;
        String requestId = "SHUTTLE-OVR-" + (totalDispatchesCompleted + 1);

        ServiceRequest shuttleRequest = new ServiceRequest(
                requestId,
                stopLocationId,
                "Peak Shuttle Queue Override (" + passengerDemand + " waiting passengers)",
                overrideUrgency,
                150.00,
                0.75,
                "PENDING"
        );
        shuttleRequest.setCategory("Transit");

        submitRequest(shuttleRequest);
        return dispatchNext();
    }

    /**
     * Matches the request with the best suitable available resource in the pool.
     */
    private Resource findAndAllocateBestResource(ServiceRequest request) {
        String reqCategory = request.getCategory() != null ? request.getCategory().toLowerCase() : "";
        String reqDesc = request.getDescription() != null ? request.getDescription().toLowerCase() : "";

        Resource bestCandidate = null;

        for (int i = 0; i < availableResources.size(); i++) {
            Resource res = availableResources.get(i);
            if (!res.isAvailable()) continue;

            String resName = res.getName().toLowerCase();
            String resType = res.getType() != null ? res.getType().toLowerCase() : "";

            boolean isMatch = false;

            if ((reqCategory.contains("transit") || reqDesc.contains("shuttle")) &&
                    (resName.contains("shuttle") || resName.contains("bus") || resType.contains("vehicle"))) {
                isMatch = true;
            } else if ((reqCategory.contains("plumb") || reqDesc.contains("water") || reqDesc.contains("tank")) &&
                    resName.contains("plumb")) {
                isMatch = true;
            } else if ((reqCategory.contains("electr") || reqDesc.contains("power") || reqDesc.contains("circuit")) &&
                    resName.contains("electr")) {
                isMatch = true;
            } else if ((reqCategory.contains("it") || reqCategory.contains("ict") || reqDesc.contains("projector") || reqDesc.contains("network")) &&
                    (resName.contains("it") || resName.contains("ugcs") || resName.contains("technician"))) {
                isMatch = true;
            } else if (resType.contains("personnel") || resType.contains("crew")) {
                // Fallback general crew
                isMatch = true;
            }

            if (isMatch) {
                bestCandidate = res;
                res.setAvailable(false); // Mark allocated
                break;
            }
        }

        // If no category-specific match, grab any available general resource
        if (bestCandidate == null) {
            for (int i = 0; i < availableResources.size(); i++) {
                Resource res = availableResources.get(i);
                if (res.isAvailable()) {
                    bestCandidate = res;
                    res.setAvailable(false);
                    break;
                }
            }
        }

        return bestCandidate;
    }

    /**
     * Formats and returns an operational summary table of all dispatched assignments.
     */
    public String generateDispatchReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("         🏛️ UNIVERSITY OF GHANA CAMPUS PRIORITY DISPATCH SCHEDULE REPORT         \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("%-14s | %-4s | %-12s | %-32s | %-10s\n",
                "Ticket ID", "Pri", "Location", "Assigned Resource", "Cost (GHS)"));
        sb.append("----------------------------------------------------------------------------------------\n");

        for (int i = 0; i < dispatchHistory.size(); i++) {
            DispatchAssignment da = dispatchHistory.get(i);
            ServiceRequest sr = da.getServiceRequest();
            Resource res = da.getAssignedResource();
            String resLabel = res != null ? res.getName() : "General Dispatch";
            if (resLabel.length() > 32) resLabel = resLabel.substring(0, 29) + "...";

            sb.append(String.format("%-14s | %-4d | %-12s | %-32s | GHS %-8.2f\n",
                    sr.getId(), sr.getPriorityLevel(), sr.getLocationId(), resLabel, da.getEstimatedCostGHS()));
        }

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("Summary: Total Dispatched: %d | Priority Points Resolved: %d | Total Cost: GHS %.2f\n",
                totalDispatchesCompleted, cumulativePriorityPointsResolved, cumulativeDispatchedBudgetGHS));
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    // Getters for inspection and testing
    public int getPendingQueueSize() { return pendingRequestsQueue.size(); }
    public boolean hasPendingRequests() { return !pendingRequestsQueue.isEmpty(); }
    public int getTotalDispatchesCompleted() { return totalDispatchesCompleted; }
    public double getCumulativeDispatchedBudgetGHS() { return cumulativeDispatchedBudgetGHS; }
    public int getCumulativePriorityPointsResolved() { return cumulativePriorityPointsResolved; }
    public DynamicArray<DispatchAssignment> getDispatchHistory() { return dispatchHistory; }
}
