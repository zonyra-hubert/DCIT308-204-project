package com.ghana.optimizer.storage.dao;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.ServiceRequest;
import com.ghana.optimizer.storage.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for campus Service Requests.
 */
public class ServiceRequestDAO {

    public void insert(ServiceRequest request) throws SQLException {
        String sql = "INSERT OR REPLACE INTO service_requests (id, location_id, description, priority_level, budget_required, estimated_duration_hrs, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, request.getId());
            preparedStatement.setString(2, request.getLocationId());
            preparedStatement.setString(3, request.getDescription());
            preparedStatement.setInt(4, request.getPriorityLevel());
            preparedStatement.setDouble(5, request.getBudgetRequired());
            preparedStatement.setDouble(6, request.getEstimatedDurationHrs());
            preparedStatement.setString(7, request.getStatus());
            preparedStatement.executeUpdate();
        }
    }

    public ServiceRequest findById(String requestId) throws SQLException {
        String sql = "SELECT id, location_id, description, priority_level, budget_required, estimated_duration_hrs, status, created_at FROM service_requests WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, requestId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public DynamicArray<ServiceRequest> findAll() throws SQLException {
        DynamicArray<ServiceRequest> requestsList = new DynamicArray<>();
        String sql = "SELECT id, location_id, description, priority_level, budget_required, estimated_duration_hrs, status, created_at FROM service_requests ORDER BY priority_level DESC, id ASC";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                requestsList.add(mapRow(resultSet));
            }
        }
        return requestsList;
    }

    public DynamicArray<ServiceRequest> findByStatus(String status) throws SQLException {
        DynamicArray<ServiceRequest> requestsList = new DynamicArray<>();
        String sql = "SELECT id, location_id, description, priority_level, budget_required, estimated_duration_hrs, status, created_at FROM service_requests WHERE status = ? ORDER BY priority_level DESC, id ASC";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    requestsList.add(mapRow(resultSet));
                }
            }
        }
        return requestsList;
    }

    public DynamicArray<ServiceRequest> findByPriority(int minPriority) throws SQLException {
        DynamicArray<ServiceRequest> requestsList = new DynamicArray<>();
        String sql = "SELECT id, location_id, description, priority_level, budget_required, estimated_duration_hrs, status, created_at FROM service_requests WHERE priority_level >= ? ORDER BY priority_level DESC";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, minPriority);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    requestsList.add(mapRow(resultSet));
                }
            }
        }
        return requestsList;
    }

    public boolean updateStatus(String requestId, String newStatus) throws SQLException {
        String sql = "UPDATE service_requests SET status = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, requestId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public boolean delete(String requestId) throws SQLException {
        String sql = "DELETE FROM service_requests WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, requestId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public void clearAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM service_requests");
        }
    }

    private ServiceRequest mapRow(ResultSet resultSet) throws SQLException {
        ServiceRequest req = new ServiceRequest(
                resultSet.getString("id"),
                resultSet.getString("location_id"),
                resultSet.getString("description"),
                resultSet.getInt("priority_level"),
                resultSet.getDouble("budget_required"),
                resultSet.getDouble("estimated_duration_hrs"),
                resultSet.getString("status")
        );
        try {
            req.setCreatedAt(resultSet.getString("created_at"));
        } catch (Exception ignored) {}
        return req;
    }
}
