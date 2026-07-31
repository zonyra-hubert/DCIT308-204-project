package com.ghana.optimizer.storage.db;

import com.ghana.optimizer.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    static {
        try {
            Class.forName(DatabaseConfig.getDbDriver());
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC Driver: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.getDbUrl());
    }
}
