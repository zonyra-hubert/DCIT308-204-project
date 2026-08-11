package com.ghana.optimizer.storage.csv;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.model.AuditEvent;
import com.ghana.optimizer.model.Location;
import com.ghana.optimizer.model.Resource;
import com.ghana.optimizer.model.Road;
import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;
import com.ghana.optimizer.storage.dao.AuditEventDAO;
import com.ghana.optimizer.storage.dao.LocationDAO;
import com.ghana.optimizer.storage.dao.ResourceDAO;
import com.ghana.optimizer.storage.dao.RoadDAO;
import com.ghana.optimizer.storage.dao.ServiceRequestDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Exports SQLite database tables and operational reports to CSV files in exports/ directory.
 */
public class CsvDataExporter {

    private final LocationDAO locationDAO = new LocationDAO();
    private final RoadDAO roadDAO = new RoadDAO();
    private final ServiceRequestDAO serviceRequestDAO = new ServiceRequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final AlgorithmRunDAO algorithmRunDAO = new AlgorithmRunDAO();
    private final AuditEventDAO auditEventDAO = new AuditEventDAO();

    public void exportAll(String exportDirectoryPath) throws IOException, SQLException {
        File dir = new File(exportDirectoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        exportLocations(new File(dir, "exported_locations.csv").getPath());
        exportRoads(new File(dir, "exported_roads.csv").getPath());
        exportServiceRequests(new File(dir, "exported_requests.csv").getPath());
        exportResources(new File(dir, "exported_resources.csv").getPath());
        exportAlgorithmRuns(new File(dir, "exported_algorithm_runs.csv").getPath());
        exportAuditEvents(new File(dir, "exported_audit_events.csv").getPath());
    }

    public void exportLocations(String filePath) throws IOException, SQLException {
        DynamicArray<Location> list = locationDAO.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,name,region,latitude,longitude\n");
            for (int i = 0; i < list.size(); i++) {
                Location loc = list.get(i);
                writer.write(String.format("%s,%s,%s,%.6f,%.6f\n",
                        loc.getId(), loc.getName(), loc.getRegion(), loc.getLatitude(), loc.getLongitude()));
            }
        }
    }

    public void exportRoads(String filePath) throws IOException, SQLException {
        DynamicArray<Road> list = roadDAO.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,source_location_id,target_location_id,distance_m,travel_time_mins,condition_score,penalty_weight\n");
            for (int i = 0; i < list.size(); i++) {
                Road road = list.get(i);
                writer.write(String.format("%s,%s,%s,%.1f,%d,%.1f,%.1f\n",
                        road.getId(), road.getSourceLocationId(), road.getTargetLocationId(),
                        road.getDistanceM(), road.getTravelTimeMins(), road.getConditionScore(), road.getPenaltyWeight()));
            }
        }
    }

    public void exportServiceRequests(String filePath) throws IOException, SQLException {
        DynamicArray<ServiceRequest> list = serviceRequestDAO.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,location_id,description,priority_level,budget_required,estimated_duration_hrs,status,created_at\n");
            for (int i = 0; i < list.size(); i++) {
                ServiceRequest req = list.get(i);
                writer.write(String.format("%s,%s,\"%s\",%d,%.2f,%.2f,%s,%s\n",
                        req.getId(), req.getLocationId(), req.getDescription().replace("\"", "\"\""),
                        req.getPriorityLevel(), req.getBudgetRequired(), req.getEstimatedDurationHrs(),
                        req.getStatus(), req.getCreatedAt() != null ? req.getCreatedAt() : ""));
            }
        }
    }

    public void exportResources(String filePath) throws IOException, SQLException {
        DynamicArray<Resource> list = resourceDAO.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,name,type,capacity,cost_per_hour,current_location_id,is_available\n");
            for (int i = 0; i < list.size(); i++) {
                Resource res = list.get(i);
                writer.write(String.format("%s,%s,%s,%.1f,%.2f,%s,%b\n",
                        res.getId(), res.getName(), res.getType(),
                        res.getCapacity(), res.getCostPerHour(), res.getCurrentLocationId(), res.isAvailable()));
            }
        }
    }

    public void exportAlgorithmRuns(String filePath) throws IOException, SQLException {
        DynamicArray<AlgorithmRun> list = algorithmRunDAO.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,algorithm_name,dataset_size,execution_time_ns,memory_used_kb,hash_capacity,budget_limit,parameters_json,executed_at\n");
            for (int i = 0; i < list.size(); i++) {
                AlgorithmRun run = list.get(i);
                writer.write(String.format("%s,%s,%d,%d,%d,%d,%.2f,\"%s\",%s\n",
                        run.getId(), run.getAlgorithmName(), run.getDatasetSize(),
                        run.getExecutionTimeNs(), run.getMemoryUsedKb(), run.getHashCapacity(),
                        run.getBudgetLimit(), run.getParametersJson() != null ? run.getParametersJson().replace("\"", "\"\"") : "",
                        run.getExecutedAt() != null ? run.getExecutedAt() : ""));
            }
        }
    }

    public void exportAuditEvents(String filePath) throws IOException, SQLException {
        DynamicArray<AuditEvent> list = auditEventDAO.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,action_type,entity_name,entity_id,details,timestamp\n");
            for (int i = 0; i < list.size(); i++) {
                AuditEvent evt = list.get(i);
                writer.write(String.format("%s,%s,%s,%s,\"%s\",%s\n",
                        evt.getId(), evt.getActionType(), evt.getEntityName(),
                        evt.getEntityId(), evt.getDetails() != null ? evt.getDetails().replace("\"", "\"\"") : "",
                        evt.getTimestamp() != null ? evt.getTimestamp() : ""));
            }
        }
    }
}
