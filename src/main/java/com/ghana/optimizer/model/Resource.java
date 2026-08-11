package com.ghana.optimizer.model;

/**
 * Domain entity representing an operational resource (personnel crew, vehicle, or equipment unit)
 * across the University of Ghana campus.
 */
public class Resource {

    private String id;                 // e.g. "RES-UG-01"
    private String name;               // e.g. "Legon Central Plumbing Rapid Response Team 1"
    private String type;               // PERSONNEL, VEHICLE, EQUIPMENT
    private double capacity;           // Capacity metric (crew size, passenger count, tonnage)
    private double costPerHour;        // Cost rate per hour in GHS
    private String currentLocationId;  // Location ID where currently stationed
    private boolean isAvailable;       // Availability status

    public Resource() {
        this.isAvailable = true;
    }

    public Resource(String id, String name, String type, double capacity,
                    double costPerHour, String currentLocationId, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.costPerHour = costPerHour;
        this.currentLocationId = currentLocationId;
        this.isAvailable = isAvailable;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getCapacity() { return capacity; }
    public void setCapacity(double capacity) { this.capacity = capacity; }

    public double getCostPerHour() { return costPerHour; }
    public void setCostPerHour(double costPerHour) { this.costPerHour = costPerHour; }

    public String getCurrentLocationId() { return currentLocationId; }
    public void setCurrentLocationId(String currentLocationId) { this.currentLocationId = currentLocationId; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Resource{id='" + id + "', name='" + name + "', type='" + type
                + "', location='" + currentLocationId + "', rate=GHS " + costPerHour + "/hr, available=" + isAvailable + "}";
    }
}
