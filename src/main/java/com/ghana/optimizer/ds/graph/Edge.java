package com.ghana.optimizer.ds.graph;

public class Edge {

    private final Vertex source;
    private final Vertex destination;

    private final int distanceMeters;
    private final int travelTimeMinutes;

    private final double conditionScore;
    private final double penaltyWeight;

    public Edge(Vertex source,
                Vertex destination,
                int distanceMeters,
                int travelTimeMinutes,
                double conditionScore,
                double penaltyWeight) {

        this.source = source;
        this.destination = destination;
        this.distanceMeters = distanceMeters;
        this.travelTimeMinutes = travelTimeMinutes;
        this.conditionScore = conditionScore;
        this.penaltyWeight = penaltyWeight;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getDistanceMeters() {
        return distanceMeters;
    }

    public int getTravelTimeMinutes() {
        return travelTimeMinutes;
    }

    public double getConditionScore() {
        return conditionScore;
    }

    public double getPenaltyWeight() {
        return penaltyWeight;
    }

    /**
     * Effective weight using the formula:
     * effectiveWeight = distanceMeters + penaltyWeight * (5.0 - conditionScore)
     */
    public double getEffectiveWeight() {
        return distanceMeters + penaltyWeight * (5.0 - conditionScore);
    }

    @Override
    public String toString() {
        return source.getName()
                + " -> "
                + destination.getName()
                + " ("
                + distanceMeters
                + "m, effCost="
                + String.format("%.1f", getEffectiveWeight())
                + ")";
    }
}
