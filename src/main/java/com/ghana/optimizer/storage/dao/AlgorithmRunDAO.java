package com.ghana.optimizer.storage.dao;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.model.AlgorithmRun;
import com.ghana.optimizer.storage.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for logging empirical algorithm runs and performance metrics.
 */
public class AlgorithmRunDAO {

    public void insert(AlgorithmRun run) throws SQLException {
        String sql = "INSERT INTO algorithm_runs (id, algorithm_name, dataset_size, execution_time_ns, memory_used_kb, hash_capacity, budget_limit, parameters_json) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, run.getId());
            preparedStatement.setString(2, run.getAlgorithmName());
            preparedStatement.setInt(3, run.getDatasetSize());
            preparedStatement.setLong(4, run.getExecutionTimeNs());
            preparedStatement.setLong(5, run.getMemoryUsedKb());
            preparedStatement.setInt(6, run.getHashCapacity());
            preparedStatement.setDouble(7, run.getBudgetLimit());
            preparedStatement.setString(8, run.getParametersJson());
            preparedStatement.executeUpdate();
        }
    }

    public DynamicArray<AlgorithmRun> findAll() throws SQLException {
        DynamicArray<AlgorithmRun> runsList = new DynamicArray<>();
        String sql = "SELECT id, algorithm_name, dataset_size, execution_time_ns, memory_used_kb, hash_capacity, budget_limit, parameters_json, executed_at FROM algorithm_runs ORDER BY executed_at DESC";
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                runsList.add(mapRow(resultSet));
            }
        }
        return runsList;
    }

    public DynamicArray<AlgorithmRun> findByAlgorithm(String algorithmName) throws SQLException {
        DynamicArray<AlgorithmRun> runsList = new DynamicArray<>();
        String sql = "SELECT id, algorithm_name, dataset_size, execution_time_ns, memory_used_kb, hash_capacity, budget_limit, parameters_json, executed_at FROM algorithm_runs WHERE algorithm_name = ? ORDER BY dataset_size ASC";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, algorithmName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    runsList.add(mapRow(resultSet));
                }
            }
        }
        return runsList;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM algorithm_runs";
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
            statement.executeUpdate("DELETE FROM algorithm_runs");
        }
    }

    private AlgorithmRun mapRow(ResultSet resultSet) throws SQLException {
        return new AlgorithmRun(
                resultSet.getString("id"),
                resultSet.getString("algorithm_name"),
                resultSet.getInt("dataset_size"),
                resultSet.getLong("execution_time_ns"),
                resultSet.getLong("memory_used_kb"),
                resultSet.getInt("hash_capacity"),
                resultSet.getDouble("budget_limit"),
                resultSet.getString("parameters_json"),
                resultSet.getString("executed_at")
        );
    }
}
