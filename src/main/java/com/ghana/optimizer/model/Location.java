package com.ghana.optimizer.model;

/**
 * Domain entity representing a physical node or landmark on the University of Ghana, Legon Campus.
 */
public class Location {

    private String id;          // e.g. "LOC-UG-01"
    private String name;        // e.g. "Balme Library"
    private String region;      // e.g. "Main Campus Core", "Traditional Halls Zone"
    private double latitude;    // GPS latitude (e.g. 5.6505)
    private double longitude;   // GPS longitude (e.g. -0.1872)

    public Location() {}

    public Location(String id, String name, String region, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return "Location{id='" + id + "', name='" + name + "', region='" + region + "'}";
    }
}
