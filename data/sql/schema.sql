-- University of Ghana Campus Service Operations Optimizer (UG-CSOO) - Database Schema

-- 1. Location Entity Table (Nodes across UG Legon Campus)
CREATE TABLE IF NOT EXISTS locations (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    region VARCHAR(50) NOT NULL, -- e.g., Traditional Halls Zone, Academic Quad, Diaspora Hub
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

-- 2. Road Entity Table (Campus Roads, Paths & Transit Edges)
CREATE TABLE IF NOT EXISTS roads (
    id VARCHAR(50) PRIMARY KEY,
    source_location_id VARCHAR(50) NOT NULL,
    target_location_id VARCHAR(50) NOT NULL,
    distance_m DOUBLE NOT NULL, -- Campus distance in meters
    travel_time_mins INT NOT NULL,
    condition_score DOUBLE NOT NULL, -- Road condition (1.0 - 5.0)
    penalty_weight DOUBLE DEFAULT 43.0, -- Parameter 1: Road Condition / Penalty Weight (43)
    FOREIGN KEY (source_location_id) REFERENCES locations(id),
    FOREIGN KEY (target_location_id) REFERENCES locations(id)
);

-- 3. ServiceRequest Entity Table (Campus Hostel Maintenance, ICT Dispatch, Shuttle Priority)
CREATE TABLE IF NOT EXISTS service_requests (
    id VARCHAR(50) PRIMARY KEY,
    location_id VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    priority_level INT NOT NULL, -- 1 (Low) to 5 (Critical)
    budget_required DOUBLE NOT NULL, -- Allocated against System Budget Parameter 1089.00
    estimated_duration_hrs DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL, -- PENDING, DISPATCHED, COMPLETED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

-- 4. Resource Entity Table (Technicians, IT Support, Shuttle Buses, Utility Trucks)
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

-- 5. AlgorithmRun Entity Table (Log of empirical lab execution runs)
CREATE TABLE IF NOT EXISTS algorithm_runs (
    id VARCHAR(50) PRIMARY KEY,
    algorithm_name VARCHAR(100) NOT NULL,
    dataset_size INT NOT NULL,
    execution_time_ns BIGINT NOT NULL,
    memory_used_kb BIGINT NOT NULL,
    hash_capacity INT DEFAULT 547, -- Parameter 2: Custom Hash Table Capacity (547)
    budget_limit DOUBLE DEFAULT 1089.0, -- Parameter 3: Budget Constraint (1089)
    parameters_json TEXT,
    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. AuditEvent Entity Table (Undo/Redo & Campus Operations Audit Log)
CREATE TABLE IF NOT EXISTS audit_events (
    id VARCHAR(50) PRIMARY KEY,
    action_type VARCHAR(50) NOT NULL, -- CREATE, UPDATE, DELETE, DISPATCH, ROUTE_CHANGE
    entity_name VARCHAR(50) NOT NULL,
    entity_id VARCHAR(50) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
