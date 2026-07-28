package com.ghana.optimizer;

import com.ghana.optimizer.storage.db.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    // System Parameters
    public static final double ROAD_PENALTY_WEIGHT = 43.0;
    public static final int HASH_TABLE_CAPACITY = 547;
    public static final double BUDGET_CONSTRAINT_GHS = 1089.00;

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  University of Ghana Campus Service Operations Optimizer (UG-CSOO)  ");
        System.out.println("  Operational Domain: UG Legon Campus, Accra, Ghana                       ");
        System.out.println("==========================================================================");
        System.out.println("System Parameters Initialized:");
        System.out.println("  - Parameter 1 (Road Penalty Weight): " + ROAD_PENALTY_WEIGHT);
        System.out.println("  - Parameter 2 (Custom Hash Table Capacity): " + HASH_TABLE_CAPACITY);
        System.out.println("  - Parameter 3 (Budget Constraint): GHS " + BUDGET_CONSTRAINT_GHS);
        System.out.println("--------------------------------------------------------------------------");

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("[DB STATUS] Connecting to SQLite Database...");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM locations;");
            if (rs.next()) {
                System.out.println("  -> Campus Nodes/Locations Loaded: " + rs.getInt(1));
            }

            rs = stmt.executeQuery("SELECT COUNT(*) FROM roads;");
            if (rs.next()) {
                System.out.println("  -> Campus Road Segments Loaded: " + rs.getInt(1));
            }

            rs = stmt.executeQuery("SELECT COUNT(*) FROM service_requests;");
            if (rs.next()) {
                System.out.println("  -> Active Service Requests Loaded: " + rs.getInt(1));
            }

            rs = stmt.executeQuery("SELECT COUNT(*) FROM resources;");
            if (rs.next()) {
                System.out.println("  -> Campus Maintenance/IT Resources Loaded: " + rs.getInt(1));
            }

            System.out.println("--------------------------------------------------------------------------");
            System.out.println("UG-CSOO System Engine Initialized Successfully.");

        } catch (Exception e) {
            System.err.println("Error initializing UG-CSOO database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
