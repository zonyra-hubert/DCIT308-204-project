package com.ghana.optimizer.config;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static String dbUrl = "jdbc:sqlite:data/ghana_optimizer.db";
    private static String dbDriver = "org.sqlite.JDBC";

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                if (prop.getProperty("db.url") != null) {
                    dbUrl = prop.getProperty("db.url");
                }
                if (prop.getProperty("db.driver") != null) {
                    dbDriver = prop.getProperty("db.driver");
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load application.properties, using default SQLite config: " + e.getMessage());
        }
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getDbDriver() {
        return dbDriver;
    }
}
