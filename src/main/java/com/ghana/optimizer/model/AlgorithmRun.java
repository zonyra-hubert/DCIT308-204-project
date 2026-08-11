package com.ghana.optimizer.model;

/**
 * Domain entity recording empirical performance benchmark runs in the SQLite database.
 */
public class AlgorithmRun {

    private String id;
    private String algorithmName;
    private int datasetSize;
    private long executionTimeNs;
    private long memoryUsedKb;
    private int hashCapacity;
    private double budgetLimit;
    private String parametersJson;
    private String executedAt;

    public AlgorithmRun() {
        this.hashCapacity = 547;
        this.budgetLimit = 1089.0;
    }

    public AlgorithmRun(String id, String algorithmName, int datasetSize, long executionTimeNs,
                        long memoryUsedKb, int hashCapacity, double budgetLimit,
                        String parametersJson, String executedAt) {
        this.id = id;
        this.algorithmName = algorithmName;
        this.datasetSize = datasetSize;
        this.executionTimeNs = executionTimeNs;
        this.memoryUsedKb = memoryUsedKb;
        this.hashCapacity = hashCapacity;
        this.budgetLimit = budgetLimit;
        this.parametersJson = parametersJson;
        this.executedAt = executedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAlgorithmName() { return algorithmName; }
    public void setAlgorithmName(String algorithmName) { this.algorithmName = algorithmName; }

    public int getDatasetSize() { return datasetSize; }
    public void setDatasetSize(int datasetSize) { this.datasetSize = datasetSize; }

    public long getExecutionTimeNs() { return executionTimeNs; }
    public void setExecutionTimeNs(long executionTimeNs) { this.executionTimeNs = executionTimeNs; }

    public long getMemoryUsedKb() { return memoryUsedKb; }
    public void setMemoryUsedKb(long memoryUsedKb) { this.memoryUsedKb = memoryUsedKb; }

    public int getHashCapacity() { return hashCapacity; }
    public void setHashCapacity(int hashCapacity) { this.hashCapacity = hashCapacity; }

    public double getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(double budgetLimit) { this.budgetLimit = budgetLimit; }

    public String getParametersJson() { return parametersJson; }
    public void setParametersJson(String parametersJson) { this.parametersJson = parametersJson; }

    public String getExecutedAt() { return executedAt; }
    public void setExecutedAt(String executedAt) { this.executedAt = executedAt; }

    @Override
    public String toString() {
        return "AlgorithmRun{" + algorithmName + ", N=" + datasetSize + ", time="
                + executionTimeNs + "ns (" + String.format("%.3f", executionTimeNs / 1_000_000.0) + "ms), mem="
                + memoryUsedKb + "KB}";
    }
}
