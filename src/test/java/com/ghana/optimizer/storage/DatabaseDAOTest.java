package com.ghana.optimizer.storage;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.model.AuditEvent;
import com.ghana.optimizer.model.Location;
import com.ghana.optimizer.model.Resource;
import com.ghana.optimizer.model.Road;
import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.storage.csv.CsvDataLoader;
import com.ghana.optimizer.storage.dao.AlgorithmRunDAO;
import com.ghana.optimizer.storage.dao.AuditEventDAO;
import com.ghana.optimizer.storage.dao.LocationDAO;
import com.ghana.optimizer.storage.dao.ResourceDAO;
import com.ghana.optimizer.storage.dao.RoadDAO;
import com.ghana.optimizer.storage.dao.ServiceRequestDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseDAOTest {

    private LocationDAO locationDAO;
    private RoadDAO roadDAO;
    private ServiceRequestDAO serviceRequestDAO;
    private ResourceDAO resourceDAO;
    private AlgorithmRunDAO algorithmRunDAO;
    private AuditEventDAO auditEventDAO;

    @BeforeEach
    public void setUp() {
        locationDAO = new LocationDAO();
        roadDAO = new RoadDAO();
        serviceRequestDAO = new ServiceRequestDAO();
        resourceDAO = new ResourceDAO();
        algorithmRunDAO = new AlgorithmRunDAO();
        auditEventDAO = new AuditEventDAO();
    }

    @Test
    public void testLocationDAOCRUD() throws SQLException {
        Location loc = new Location("LOC-TEST-01", "Test Hall", "Traditional Zone", 5.6500, -0.1870);
        locationDAO.insert(loc);

        Location fetched = locationDAO.findById("LOC-TEST-01");
        assertNotNull(fetched, "Fetched location should not be null");
        assertEquals("Test Hall", fetched.getName());
        assertEquals("Traditional Zone", fetched.getRegion());

        boolean deleted = locationDAO.delete("LOC-TEST-01");
        assertTrue(deleted, "Location deletion should succeed");
        assertNull(locationDAO.findById("LOC-TEST-01"), "Deleted location should not exist");
    }

    @Test
    public void testRoadDAOCRUD() throws SQLException {
        Road road = new Road("RD-TEST-01", "LOC-UG-01", "LOC-UG-02", 350.0, 3, 4.5, 43.0);
        roadDAO.insert(road);

        Road fetched = roadDAO.findById("RD-TEST-01");
        assertNotNull(fetched, "Fetched road should not be null");
        assertEquals(350.0, fetched.getDistanceM(), 0.001);
        assertEquals(43.0, fetched.getPenaltyWeight(), 0.001);
        assertEquals(350.0 + 43.0 * (5.0 - 4.5), fetched.getEffectiveCost(), 0.001);

        roadDAO.delete("RD-TEST-01");
    }

    @Test
    public void testServiceRequestDAOCRUD() throws SQLException {
        ServiceRequest req = new ServiceRequest("REQ-TEST-01", "LOC-UG-01", "Test Water Pipe Fix", 5, 250.0, 2.0, "PENDING");
        serviceRequestDAO.insert(req);

        ServiceRequest fetched = serviceRequestDAO.findById("REQ-TEST-01");
        assertNotNull(fetched, "Fetched request should not be null");
        assertEquals(5, fetched.getPriorityLevel());
        assertEquals(250.0, fetched.getBudgetRequired(), 0.001);

        serviceRequestDAO.updateStatus("REQ-TEST-01", "COMPLETED");
        ServiceRequest updated = serviceRequestDAO.findById("REQ-TEST-01");
        assertEquals("COMPLETED", updated.getStatus());

        serviceRequestDAO.delete("REQ-TEST-01");
    }

    @Test
    public void testResourceDAOCRUD() throws SQLException {
        Resource res = new Resource("RES-TEST-01", "Plumbing Rapid Team Test", "PERSONNEL", 4.0, 85.0, "LOC-UG-01", true);
        resourceDAO.insert(res);

        Resource fetched = resourceDAO.findById("RES-TEST-01");
        assertNotNull(fetched);
        assertTrue(fetched.isAvailable());

        resourceDAO.updateAvailability("RES-TEST-01", false);
        Resource updated = resourceDAO.findById("RES-TEST-01");
        assertFalse(updated.isAvailable());

        resourceDAO.delete("RES-TEST-01");
    }

    @Test
    public void testAlgorithmRunDAOCreation() throws SQLException {
        AlgorithmRun run = new AlgorithmRun(
                UUID.randomUUID().toString(),
                "Dijkstra_Benchmark_Test",
                100,
                1500000L,
                1024L,
                547,
                1089.0,
                "{\"test\":true}",
                null
        );
        algorithmRunDAO.insert(run);

        DynamicArray<AlgorithmRun> runs = algorithmRunDAO.findByAlgorithm("Dijkstra_Benchmark_Test");
        assertTrue(runs.size() >= 1, "Algorithm runs should contain logged test run");
    }

    @Test
    public void testAuditEventDAOCreation() throws SQLException {
        AuditEvent event = new AuditEvent(
                UUID.randomUUID().toString(),
                "DISPATCH",
                "ServiceRequest",
                "REQ-UG-001",
                "Dispatched plumbing team to Sarbah Hall",
                null
        );
        auditEventDAO.insert(event);

        DynamicArray<AuditEvent> events = auditEventDAO.findAll();
        assertTrue(events.size() >= 1, "Audit events should contain logged dispatch event");
    }

    @Test
    public void testCsvDataLoaderSeedCounts() throws SQLException {
        CsvDataLoader loader = new CsvDataLoader();
        loader.seedDatabaseIfEmpty();

        assertTrue(locationDAO.count() >= 50, "Locations count should be >= 50");
        assertTrue(roadDAO.count() >= 100, "Roads count should be >= 100");
        assertTrue(serviceRequestDAO.count() >= 300, "Service requests count should be >= 300");
        assertTrue(resourceDAO.count() >= 30, "Resources count should be >= 30");
    }
}
