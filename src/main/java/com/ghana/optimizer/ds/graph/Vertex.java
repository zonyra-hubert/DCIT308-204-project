package com.ghana.optimizer.ds.graph;

public class Vertex {

    private final String id;
    private final String name;
    private final String region;
    private final double latitude;
    private final double longitude;

    public Vertex(String id,
                  String name,
                  String region,
                  double latitude,
                  double longitude) {

        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
