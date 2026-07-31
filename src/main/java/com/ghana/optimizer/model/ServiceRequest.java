package com.ghana.optimizer.model;

/**
 * A service request in the system: a maintenance ticket, IT support
 * request, shuttle request, or lab-resource move.
 *
 * This is the entity type that LinearSearch.java searches over — by
 * requestId (linearSearchById) and by category (linearSearchByCategory).
 */
public class ServiceRequest {

    private int requestId;
    private int sourceId;
    private Integer destinationId;   // nullable — not every request has a destination
    private String category;        // e.g. "maintenance", "IT", "shuttle", "lab_move"
    private int urgency;            // 1 (low) to 5 (critical)
    private String timeSubmitted;   // ISO-8601 timestamp
    private String deadline;        // nullable
    private String status;          // pending, assigned, in_progress, done, cancelled

    public ServiceRequest() { }

    public ServiceRequest(int requestId, int sourceId, Integer destinationId, String category,
                        int urgency, String timeSubmitted, String deadline, String status) {
        this.requestId = requestId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }

    public Integer getDestinationId() { return destinationId; }
    public void setDestinationId(Integer destinationId) { this.destinationId = destinationId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getUrgency() { return urgency; }
    public void setUrgency(int urgency) { this.urgency = urgency; }

    public String getTimeSubmitted() { return timeSubmitted; }
    public void setTimeSubmitted(String timeSubmitted) { this.timeSubmitted = timeSubmitted; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "ServiceRequest{id=" + requestId + ", category='" + category
                + "', urgency=" + urgency + ", status='" + status + "'}";
    }

    public int getPriorityLevel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPriorityLevel'");
    }

    public Object getLocationId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLocationId'");
    }
}
