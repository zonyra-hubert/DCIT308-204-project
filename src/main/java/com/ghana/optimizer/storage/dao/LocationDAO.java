package com.ghana.optimizer.storage.dao;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.Location;
import com.ghana.optimizer.storage.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for campus Location entities.
 */
public class LocationDAO {

    public void insert(Location location) throws SQLException {
        String sql = "INSERT OR REPLACE INTO locations (id, name, region, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, location.getId());
            preparedStatement.setString(2, location.getName());
            preparedStatement.setString(3, location.getRegion());
            preparedStatement.setDouble(4, location.getLatitude());
            preparedStatement.setDouble(5, location.getLongitude());
            preparedStatement.executeUpdate();
        }
    }

    public Location findById(String locationId) throws SQLException {
        String sql = "SELECT id, name, region, latitude, longitude FROM locations WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, locationId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public DynamicArray<Location> findAll() throws SQLException {
        DynamicArray<Location> locationsList = new DynamicArray<>();
        String sql = "SELECT id, name, region, latitude, longitude FROM locations ORDER BY id";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                locationsList.add(mapRow(resultSet));
            }
        }
        return locationsList;
    }

    public DynamicArray<Location> findByRegion(String region) throws SQLException {
        DynamicArray<Location> locationsList = new DynamicArray<>();
        String sql = "SELECT id, name, region, latitude, longitude FROM locations WHERE region = ? ORDER BY id";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, region);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    locationsList.add(mapRow(resultSet));
                }
            }
        }
        return locationsList;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM locations";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public boolean delete(String locationId) throws SQLException {
        String sql = "DELETE FROM locations WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, locationId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public void clearAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM locations");
        }
    }

    private Location mapRow(ResultSet resultSet) throws SQLException {
        return new Location(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("region"),
                resultSet.getDouble("latitude"),
                resultSet.getDouble("longitude")
        );
    }
}
