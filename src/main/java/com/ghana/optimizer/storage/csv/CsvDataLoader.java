package com.ghana.optimizer.storage.csv;

import com.ghana.optimizer.model.Location;
import com.ghana.optimizer.model.Resource;
import com.ghana.optimizer.model.Road;
import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.storage.dao.LocationDAO;
import com.ghana.optimizer.storage.dao.ResourceDAO;
import com.ghana.optimizer.storage.dao.RoadDAO;
import com.ghana.optimizer.storage.dao.ServiceRequestDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Loads campus seed CSV datasets into the SQLite database.
 */
public class CsvDataLoader {

    private final LocationDAO locationDAO;
    private final RoadDAO roadDAO;
    private final ServiceRequestDAO serviceRequestDAO;
    private final ResourceDAO resourceDAO;

    public CsvDataLoader() {
        this.locationDAO = new LocationDAO();
        this.roadDAO = new RoadDAO();
        this.serviceRequestDAO = new ServiceRequestDAO();
        this.resourceDAO = new ResourceDAO();
    }

    /**
     * Seeds the database from CSV files if any of the primary tables are empty.
     */
    public boolean seedDatabaseIfEmpty() {
        try {
            int locationCount = locationDAO.count();
            int roadCount = roadDAO.count();
            int requestCount = serviceRequestDAO.count();
            int resourceCount = resourceDAO.count();

            boolean needsSeeding = (locationCount < 50 || roadCount < 100 || requestCount < 150 || resourceCount < 30);
            if (needsSeeding) {
                System.out.println("[CSV LOADER] Seeding SQLite database from data/seed/ CSV datasets...");
                loadAllSeedData("data/seed");
                System.out.println("[CSV LOADER] Database seeding completed successfully.");
                return true;
            }
        } catch (Exception e) {
            System.err.println("[CSV LOADER ERROR] Error checking or seeding database: " + e.getMessage());
        }
        return false;
    }

    public void loadAllSeedData(String seedDirectoryPath) throws IOException, SQLException {
        File locationsFile = new File(seedDirectoryPath, "locations.csv");
        if (locationsFile.exists()) {
            loadLocations(locationsFile.getPath());
        }

        File roadsFile = new File(seedDirectoryPath, "roads.csv");
        if (roadsFile.exists()) {
            loadRoads(roadsFile.getPath());
        }

        File requestsFile = new File(seedDirectoryPath, "requests.csv");
        if (requestsFile.exists()) {
            loadServiceRequests(requestsFile.getPath());
        }

        File resourcesFile = new File(seedDirectoryPath, "resources.csv");
        if (resourcesFile.exists()) {
            loadResources(resourcesFile.getPath());
        }
    }

    public int loadLocations(String filePath) throws IOException, SQLException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Header: id,name,region,latitude,longitude
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Location loc = new Location(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            Double.parseDouble(parts[3].trim()),
                            Double.parseDouble(parts[4].trim())
                    );
                    locationDAO.insert(loc);
                    count++;
                }
            }
        }
        return count;
    }

    public int loadRoads(String filePath) throws IOException, SQLException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Header: id,source_location_id,target_location_id,distance_m,travel_time_mins,condition_score,penalty_weight
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Road road = new Road(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            Double.parseDouble(parts[3].trim()),
                            Integer.parseInt(parts[4].trim()),
                            Double.parseDouble(parts[5].trim()),
                            Double.parseDouble(parts[6].trim())
                    );
                    roadDAO.insert(road);
                    count++;
                }
            }
        }
        return count;
    }

    public int loadServiceRequests(String filePath) throws IOException, SQLException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Header: id,location_id,description,priority_level,budget_required,estimated_duration_hrs,status
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    ServiceRequest req = new ServiceRequest(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            Integer.parseInt(parts[3].trim()),
                            Double.parseDouble(parts[4].trim()),
                            Double.parseDouble(parts[5].trim()),
                            parts[6].trim()
                    );
                    serviceRequestDAO.insert(req);
                    count++;
                }
            }
        }
        return count;
    }

    public int loadResources(String filePath) throws IOException, SQLException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Header: id,name,type,capacity,cost_per_hour,current_location_id,is_available
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Resource res = new Resource(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            Double.parseDouble(parts[3].trim()),
                            Double.parseDouble(parts[4].trim()),
                            parts[5].trim(),
                            Boolean.parseBoolean(parts[6].trim())
                    );
                    resourceDAO.insert(res);
                    count++;
                }
            }
        }
        return count;
    }
}
