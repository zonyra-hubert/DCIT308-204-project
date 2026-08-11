package com.ghana.optimizer;

import com.ghana.optimizer.storage.csv.CsvDataLoader;
import com.ghana.optimizer.storage.dao.LocationDAO;
import com.ghana.optimizer.storage.dao.ResourceDAO;
import com.ghana.optimizer.storage.dao.RoadDAO;
import com.ghana.optimizer.storage.dao.ServiceRequestDAO;
import com.ghana.optimizer.ui.ConsoleMenu;

/**
 * Master entry point for the University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 */
public class Main {

    // Explicit System Parameters as defined in the project specification
    public static final double ROAD_PENALTY_WEIGHT = 43.0;
    public static final int HASH_TABLE_CAPACITY = 547;
    public static final double BUDGET_CONSTRAINT_GHS = 1089.00;

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  🏛️ University of Ghana Campus Service Operations Optimizer (UG-CSOO)");
        System.out.println("  Operational Domain: UG Legon Campus, Accra, Ghana                       ");
        System.out.println("==========================================================================");
        System.out.println("System Parameters Initialized:");
        System.out.println("  - Parameter 1 (Road Penalty Weight): " + ROAD_PENALTY_WEIGHT);
        System.out.println("  - Parameter 2 (Custom Hash Table Capacity): " + HASH_TABLE_CAPACITY);
        System.out.println("  - Parameter 3 (Operational Budget Limit): GHS " + String.format("%.2f", BUDGET_CONSTRAINT_GHS));
        System.out.println("--------------------------------------------------------------------------");

        try {
            System.out.println("[DB STATUS] Connecting to SQLite Database...");
            CsvDataLoader dataLoader = new CsvDataLoader();
            dataLoader.seedDatabaseIfEmpty();

            LocationDAO locationDAO = new LocationDAO();
            RoadDAO roadDAO = new RoadDAO();
            ServiceRequestDAO requestDAO = new ServiceRequestDAO();
            ResourceDAO resourceDAO = new ResourceDAO();

            int locationCount = locationDAO.count();
            int roadCount = roadDAO.count();
            int requestCount = requestDAO.count();
            int resourceCount = resourceDAO.count();

            System.out.println("  -> Campus Nodes/Locations Loaded: " + locationCount + " (Target >= 50)");
            System.out.println("  -> Campus Road Segments Loaded: " + roadCount + " (Target >= 100)");
            System.out.println("  -> Active Service Requests Loaded: " + requestCount + " (Target >= 300)");
            System.out.println("  -> Campus Maintenance/IT Resources Loaded: " + resourceCount + " (Target >= 30)");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("UG-CSOO System Engine Initialized Successfully.");

            // If command-line argument "--cli" or no non-interactive flag, launch Console Menu
            if (args.length > 0 && args[0].equals("--cli")) {
                ConsoleMenu.main(args);
            } else if (args.length == 0 && System.console() != null) {
                ConsoleMenu.main(args);
            } else {
                System.out.println("Tip: Run with '--cli' or execute com.ghana.optimizer.ui.ConsoleMenu to launch the Examiner Interactive Menu.");
            }

        } catch (Exception e) {
            System.err.println("Error initializing UG-CSOO database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
