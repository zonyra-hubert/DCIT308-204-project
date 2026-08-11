package com.ghana.optimizer.storage.dao;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.AuditEvent;
import com.ghana.optimizer.storage.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for operations audit logging and undo/redo trace history.
 */
public class AuditEventDAO {

    public void insert(AuditEvent event) throws SQLException {
        String sql = "INSERT INTO audit_events (id, action_type, entity_name, entity_id, details) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, event.getId());
            preparedStatement.setString(2, event.getActionType());
            preparedStatement.setString(3, event.getEntityName());
            preparedStatement.setString(4, event.getEntityId());
            preparedStatement.setString(5, event.getDetails());
            preparedStatement.executeUpdate();
        }
    }

    public DynamicArray<AuditEvent> findAll() throws SQLException {
        DynamicArray<AuditEvent> eventsList = new DynamicArray<>();
        String sql = "SELECT id, action_type, entity_name, entity_id, details, timestamp FROM audit_events ORDER BY timestamp DESC";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                eventsList.add(mapRow(resultSet));
            }
        }
        return eventsList;
    }

    public DynamicArray<AuditEvent> findByEntity(String entityName, String entityId) throws SQLException {
        DynamicArray<AuditEvent> eventsList = new DynamicArray<>();
        String sql = "SELECT id, action_type, entity_name, entity_id, details, timestamp FROM audit_events WHERE entity_name = ? AND entity_id = ? ORDER BY timestamp DESC";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entityName);
            preparedStatement.setString(2, entityId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    eventsList.add(mapRow(resultSet));
                }
            }
        }
        return eventsList;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM audit_events";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public void clearAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM audit_events");
        }
    }

    private AuditEvent mapRow(ResultSet resultSet) throws SQLException {
        return new AuditEvent(
                resultSet.getString("id"),
                resultSet.getString("action_type"),
                resultSet.getString("entity_name"),
                resultSet.getString("entity_id"),
                resultSet.getString("details"),
                resultSet.getString("timestamp")
        );
    }
}
