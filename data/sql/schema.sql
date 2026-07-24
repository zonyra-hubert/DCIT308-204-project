-- Ghana Smart Service Operations Optimizer - Database Schema

-- 1. Location Entity Table (Nodes in Road Network)
CREATE TABLE IF NOT EXISTS locations (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    region VARCHAR(50) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

-- 2. Road Entity Table (Edges in Road Network)
CREATE TABLE IF NOT EXISTS roads (
    id VARCHAR(50) PRIMARY KEY,
    source_location_id VARCHAR(50) NOT NULL,
    target_location_id VARCHAR(50) NOT NULL,
    distance_km DOUBLE NOT NULL,
    travel_time_mins INT NOT NULL,
    condition_score DOUBLE NOT NULL, -- Road quality metric (1.0 - 5.0)
    FOREIGN KEY (source_location_id) REFERENCES locations(id),
    FOREIGN KEY (target_location_id) REFERENCES locations(id)
);

-- 3. ServiceRequest Entity Table
CREATE TABLE IF NOT EXISTS service_requests (
    id VARCHAR(50) PRIMARY KEY,
    location_id VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    priority_level INT NOT NULL, -- 1 (Low) to 5 (Critical)
    budget_required DOUBLE NOT NULL,
    estimated_duration_hrs DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL, -- PENDING, DISPATCHED, COMPLETED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

-- 4. Resource Entity Table (Vehicles, Maintenance Teams, Equipment)
CREATE TABLE IF NOT EXISTS resources (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL, -- VEHICLE, PERSONNEL, EQUIPMENT
    capacity DOUBLE NOT NULL,
    cost_per_hour DOUBLE NOT NULL,
    current_location_id VARCHAR(50) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (current_location_id) REFERENCES locations(id)
);

-- 5. AlgorithmRun Entity Table (Log of algorithm executions)
CREATE TABLE IF NOT EXISTS algorithm_runs (
    id VARCHAR(50) PRIMARY KEY,
    algorithm_name VARCHAR(100) NOT NULL,
    dataset_size INT NOT NULL,
    execution_time_ns BIGINT NOT NULL,
    memory_used_kb BIGINT NOT NULL,
    parameters_json TEXT,
    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. AuditEvent Entity Table (Undo/Redo & System Operation Logs)
CREATE TABLE IF NOT EXISTS audit_events (
    id VARCHAR(50) PRIMARY KEY,
    action_type VARCHAR(50) NOT NULL, -- CREATE, UPDATE, DELETE, DISPATCH, ROUTE_CHANGE
    entity_name VARCHAR(50) NOT NULL,
    entity_id VARCHAR(50) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
