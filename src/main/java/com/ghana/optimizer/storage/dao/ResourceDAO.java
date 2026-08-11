package com.ghana.optimizer.storage.dao;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.Resource;
import com.ghana.optimizer.storage.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for campus operational Resources (personnel, vehicles, equipment).
 */
public class ResourceDAO {

    public void insert(Resource resource) throws SQLException {
        String sql = "INSERT OR REPLACE INTO resources (id, name, type, capacity, cost_per_hour, current_location_id, is_available) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, resource.getId());
            preparedStatement.setString(2, resource.getName());
            preparedStatement.setString(3, resource.getType());
            preparedStatement.setDouble(4, resource.getCapacity());
            preparedStatement.setDouble(5, resource.getCostPerHour());
            preparedStatement.setString(6, resource.getCurrentLocationId());
            preparedStatement.setBoolean(7, resource.isAvailable());
            preparedStatement.executeUpdate();
        }
    }

    public Resource findById(String resourceId) throws SQLException {
        String sql = "SELECT id, name, type, capacity, cost_per_hour, current_location_id, is_available FROM resources WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, resourceId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public DynamicArray<Resource> findAll() throws SQLException {
        DynamicArray<Resource> resourcesList = new DynamicArray<>();
        String sql = "SELECT id, name, type, capacity, cost_per_hour, current_location_id, is_available FROM resources ORDER BY id";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                resourcesList.add(mapRow(resultSet));
            }
        }
        return resourcesList;
    }

    public DynamicArray<Resource> findAvailableByType(String type) throws SQLException {
        DynamicArray<Resource> resourcesList = new DynamicArray<>();
        String sql = "SELECT id, name, type, capacity, cost_per_hour, current_location_id, is_available FROM resources WHERE type = ? AND is_available = 1 ORDER BY id";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, type);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resourcesList.add(mapRow(resultSet));
                }
            }
        }
        return resourcesList;
    }

    public boolean updateAvailability(String resourceId, boolean isAvailable) throws SQLException {
        String sql = "UPDATE resources SET is_available = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setString(2, resourceId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM resources";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public boolean delete(String resourceId) throws SQLException {
        String sql = "DELETE FROM resources WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, resourceId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public void clearAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM resources");
        }
    }

    private Resource mapRow(ResultSet resultSet) throws SQLException {
        return new Resource(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("type"),
                resultSet.getDouble("capacity"),
                resultSet.getDouble("cost_per_hour"),
                resultSet.getString("current_location_id"),
                resultSet.getBoolean("is_available")
        );
    }
}
