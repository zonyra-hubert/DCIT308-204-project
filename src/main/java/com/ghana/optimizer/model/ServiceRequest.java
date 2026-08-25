package com.ghana.optimizer.model;

/**
 * Domain entity representing a University of Ghana campus service request,
 * facilities maintenance ticket, ICT dispatch, or shuttle request.
 */
public class ServiceRequest {

    private String id;
    private int requestId;
    private String title;
    private String locationId;
    private int sourceId;
    private Integer destinationId;
    private String description;
    private String category;
    private int priorityLevel; // 1 (Low) to 5 (Critical)
    private double budgetRequired; // in GHS
    private double estimatedDurationHrs;
    private String status; // PENDING, DISPATCHED, COMPLETED, CANCELLED
    private String timeSubmitted;
    private String deadline;
    private String createdAt;

    public ServiceRequest() {
        this.status = "PENDING";
        this.priorityLevel = 1;
        this.budgetRequired = 0.0;
        this.estimatedDurationHrs = 1.0;
    }

    public ServiceRequest(String id, String locationId, String description, int priorityLevel,
                          double budgetRequired, double estimatedDurationHrs, String status) {
        this.id = id;
        this.locationId = locationId;
        this.description = description;
        this.priorityLevel = priorityLevel;
        this.budgetRequired = budgetRequired;
        this.estimatedDurationHrs = estimatedDurationHrs;
        this.status = status;
        this.category = parseCategory(description);

        // Derive integer requestId from ID if pattern is REQ-UG-xxx
        try {
            if (id != null && id.contains("-")) {
                String[] parts = id.split("-");
                this.requestId = Integer.parseInt(parts[parts.length - 1]);
            }
        } catch (Exception ignored) {
            this.requestId = id != null ? id.hashCode() : 0;
        }
    }

    public ServiceRequest(int requestId, int sourceId, Integer destinationId, String category,
                          int urgency, String timeSubmitted, String deadline, String status) {
        this.requestId = requestId;
        this.id = "REQ-UG-" + String.format("%03d", requestId);
        this.sourceId = sourceId;
        this.locationId = "LOC-UG-" + String.format("%02d", sourceId);
        this.destinationId = destinationId;
        this.category = category;
        this.description = category + " Request #" + requestId;
        this.priorityLevel = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
        this.budgetRequired = 100.0 * urgency;
        this.estimatedDurationHrs = 2.0;
    }

    private static String parseCategory(String description) {
        if (description == null) return "General";
        String lower = description.toLowerCase();
        if (lower.contains("plumb") || lower.contains("tank") || lower.contains("water")) return "Plumbing";
        if (lower.contains("electr") || lower.contains("circuit") || lower.contains("power")) return "Electrical";
        if (lower.contains("ict") || lower.contains("projector") || lower.contains("audio") || lower.contains("network") || lower.contains("fiber") || lower.contains("air conditioner")) return "IT Support";
        if (lower.contains("shuttle") || lower.contains("transit") || lower.contains("bus")) return "Transit";
        if (lower.contains("book") || lower.contains("library") || lower.contains("journal")) return "Library Logistics";
        return "Maintenance";
    }

    // Getters and Setters
    public String getTitle() {
        return title != null ? title : description;
    }
    public void setTitle(String title) { this.title = title; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) {
        this.requestId = requestId;
        if (this.id == null || this.id.isEmpty()) {
            this.id = "REQ-UG-" + String.format("%03d", requestId);
        }
    }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }

    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }

    public Integer getDestinationId() { return destinationId; }
    public void setDestinationId(Integer destinationId) { this.destinationId = destinationId; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        if (this.category == null || this.category.isEmpty()) {
            this.category = parseCategory(description);
        }
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(int priorityLevel) { this.priorityLevel = priorityLevel; }

    public int getUrgency() { return priorityLevel; }
    public void setUrgency(int urgency) { this.priorityLevel = urgency; }

    public double getBudgetRequired() { return budgetRequired; }
    public void setBudgetRequired(double budgetRequired) { this.budgetRequired = budgetRequired; }

    public double getEstimatedDurationHrs() { return estimatedDurationHrs; }
    public void setEstimatedDurationHrs(double estimatedDurationHrs) { this.estimatedDurationHrs = estimatedDurationHrs; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimeSubmitted() { return timeSubmitted; }
    public void setTimeSubmitted(String timeSubmitted) { this.timeSubmitted = timeSubmitted; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    /** Ratio of priority points per GHS cost (used in Greedy knapsack heuristic) */
    public double getPriorityToCostRatio() {
        if (budgetRequired <= 0.0) return priorityLevel * 1000.0;
        return ((double) priorityLevel) / budgetRequired;
    }

    @Override
    public String toString() {
        return "ServiceRequest{id='" + id + "', location='" + locationId + "', priority=" + priorityLevel
                + ", budget=GHS " + String.format("%.2f", budgetRequired) + ", status='" + status + "'}";
    }
}
