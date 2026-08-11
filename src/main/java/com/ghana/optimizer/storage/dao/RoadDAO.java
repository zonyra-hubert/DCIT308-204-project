package com.ghana.optimizer.storage.dao;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.Road;
import com.ghana.optimizer.storage.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for campus Road network segments.
 */
public class RoadDAO {

    public void insert(Road road) throws SQLException {
        String sql = "INSERT OR REPLACE INTO roads (id, source_location_id, target_location_id, distance_m, travel_time_mins, condition_score, penalty_weight) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, road.getId());
            preparedStatement.setString(2, road.getSourceLocationId());
            preparedStatement.setString(3, road.getTargetLocationId());
            preparedStatement.setDouble(4, road.getDistanceM());
            preparedStatement.setInt(5, road.getTravelTimeMins());
            preparedStatement.setDouble(6, road.getConditionScore());
            preparedStatement.setDouble(7, road.getPenaltyWeight());
            preparedStatement.executeUpdate();
        }
    }

    public Road findById(String roadId) throws SQLException {
        String sql = "SELECT id, source_location_id, target_location_id, distance_m, travel_time_mins, condition_score, penalty_weight FROM roads WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roadId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public DynamicArray<Road> findAll() throws SQLException {
        DynamicArray<Road> roadsList = new DynamicArray<>();
        String sql = "SELECT id, source_location_id, target_location_id, distance_m, travel_time_mins, condition_score, penalty_weight FROM roads ORDER BY id";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                roadsList.add(mapRow(resultSet));
            }
        }
        return roadsList;
    }

    public DynamicArray<Road> findByConnectedLocation(String locationId) throws SQLException {
        DynamicArray<Road> roadsList = new DynamicArray<>();
        String sql = "SELECT id, source_location_id, target_location_id, distance_m, travel_time_mins, condition_score, penalty_weight FROM roads WHERE source_location_id = ? OR target_location_id = ? ORDER BY id";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, locationId);
            preparedStatement.setString(2, locationId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    roadsList.add(mapRow(resultSet));
                }
            }
        }
        return roadsList;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM roads";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public boolean delete(String roadId) throws SQLException {
        String sql = "DELETE FROM roads WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roadId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public void clearAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM roads");
        }
    }

    private Road mapRow(ResultSet resultSet) throws SQLException {
        return new Road(
                resultSet.getString("id"),
                resultSet.getString("source_location_id"),
                resultSet.getString("target_location_id"),
                resultSet.getDouble("distance_m"),
                resultSet.getInt("travel_time_mins"),
                resultSet.getDouble("condition_score"),
                resultSet.getDouble("penalty_weight")
        );
    }
}
