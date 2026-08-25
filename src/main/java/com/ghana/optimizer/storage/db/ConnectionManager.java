package com.ghana.optimizer.storage.db;

import com.ghana.optimizer.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static boolean driverAvailable = false;

    static {
        try {
            Class.forName(DatabaseConfig.getDbDriver());
            driverAvailable = true;
        } catch (ClassNotFoundException e) {
            driverAvailable = false;
        }
    }

    public static boolean isDriverAvailable() {
        return driverAvailable;
    }

    public static Connection getConnection() throws SQLException {
        if (!driverAvailable) {
            throw new SQLException("SQLite JDBC Driver not loaded on classpath (CSV Fallback Mode Active)");
        }
        return DriverManager.getConnection(DatabaseConfig.getDbUrl());
    }
}
