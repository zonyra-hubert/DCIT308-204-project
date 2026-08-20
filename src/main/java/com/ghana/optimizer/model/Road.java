package com.ghana.optimizer.model;

/**
 * Domain entity representing a road or pedestrian path segment on the University of Ghana campus.
 */
public class Road {

    private String id;                 // e.g. "RD-UG-001"
    private String sourceLocationId;   // e.g. "LOC-UG-21"
    private String targetLocationId;   // e.g. "LOC-UG-18"
    private double distanceM;          // Distance in meters
    private int travelTimeMins;        // Travel time in minutes
    private double conditionScore;     // 1.0 (very poor) to 5.0 (excellent)
    private double penaltyWeight;      // Parameter 1: Road Condition Penalty (default 59.0)

    public Road() {
        this.penaltyWeight = 59.0;
    }

    public Road(String id, String sourceLocationId, String targetLocationId, double distanceM,
                int travelTimeMins, double conditionScore, double penaltyWeight) {
        this.id = id;
        this.sourceLocationId = sourceLocationId;
        this.targetLocationId = targetLocationId;
        this.distanceM = distanceM;
        this.travelTimeMins = travelTimeMins;
        this.conditionScore = conditionScore;
        this.penaltyWeight = penaltyWeight;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSourceLocationId() { return sourceLocationId; }
    public void setSourceLocationId(String sourceLocationId) { this.sourceLocationId = sourceLocationId; }

    public String getTargetLocationId() { return targetLocationId; }
    public void setTargetLocationId(String targetLocationId) { this.targetLocationId = targetLocationId; }

    public double getDistanceM() { return distanceM; }
    public void setDistanceM(double distanceM) { this.distanceM = distanceM; }

    public int getTravelTimeMins() { return travelTimeMins; }
    public void setTravelTimeMins(int travelTimeMins) { this.travelTimeMins = travelTimeMins; }

    public double getConditionScore() { return conditionScore; }
    public void setConditionScore(double conditionScore) { this.conditionScore = conditionScore; }

    public double getPenaltyWeight() { return penaltyWeight; }
    public void setPenaltyWeight(double penaltyWeight) { this.penaltyWeight = penaltyWeight; }

    /**
     * Effective cost function calculated as:
     * effectiveCost = distanceM + penaltyWeight * (5.0 - conditionScore)
     */
    public double getEffectiveCost() {
        return distanceM + penaltyWeight * (5.0 - conditionScore);
    }

    @Override
    public String toString() {
        return "Road{id='" + id + "', " + sourceLocationId + " <-> " + targetLocationId
                + ", distance=" + distanceM + "m, condition=" + conditionScore
                + ", effCost=" + String.format("%.1f", getEffectiveCost()) + "}";
    }
}
