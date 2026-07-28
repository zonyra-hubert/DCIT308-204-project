-- University of Ghana Campus Service Operations Optimizer (UG-CSOO) - Seed SQL Data

INSERT INTO locations (id, name, region, latitude, longitude) VALUES
('LOC-UG-01', 'Balme Library', 'Legon Main Campus', 5.6505, -0.1872),
('LOC-UG-02', 'Department of Computer Science', 'Mathematical Sciences Sub-Cell', 5.6530, -0.1865),
('LOC-UG-03', 'Department of Mathematics', 'Mathematical Sciences Sub-Cell', 5.6528, -0.1860),
('LOC-UG-04', 'Department of Physics', 'Physical Sciences Complex', 5.6522, -0.1855),
('LOC-UG-05', 'Department of Chemistry', 'Physical Sciences Complex', 5.6520, -0.1850),
('LOC-UG-06', 'Akuafo Hall', 'Traditional Halls Zone', 5.6490, -0.1880),
('LOC-UG-07', 'Volta Hall', 'Traditional Halls Zone', 5.6485, -0.1868),
('LOC-UG-08', 'Legon Hall', 'Traditional Halls Zone', 5.6498, -0.1860),
('LOC-UG-09', 'Commonwealth Hall', 'Vandal City Zone', 5.6540, -0.1888),
('LOC-UG-10', 'Mensah Sarbah Hall', 'Traditional Halls Zone', 5.6475, -0.1895),
('LOC-UG-11', 'Jean Nelson Aka Hall', 'Diaspora Halls Zone', 5.6420, -0.1840),
('LOC-UG-12', 'Hilla Limann Hall', 'Diaspora Halls Zone', 5.6415, -0.1830),
('LOC-UG-15', 'Jubilee Hall', 'Postgraduate Zone', 5.6460, -0.1835),
('LOC-UG-16', 'Great Hall', 'University Heights', 5.6550, -0.1880),
('LOC-UG-17', 'ISSER', 'Social Sciences Quad', 5.6515, -0.1890),
('LOC-UG-18', 'Night Market', 'South Campus Hub', 5.6440, -0.1875),
('LOC-UG-21', 'Main Gate Shuttle Stop', 'Campus Entrance', 5.6400, -0.1890),
('LOC-UG-24', 'N-Block Lecture Theatre', 'Academic Quad', 5.6525, -0.1870),
('LOC-UG-25', 'JQB (Jones Quartey Building)', 'Academic Quad', 5.6512, -0.1852),
('LOC-UG-44', 'UG Computing Services (UGCS)', 'IT Hub', 5.6515, -0.1865);

INSERT INTO roads (id, source_location_id, target_location_id, distance_m, travel_time_mins, condition_score, penalty_weight) VALUES
('RD-UG-001', 'LOC-UG-21', 'LOC-UG-18', 550.0, 4, 4.5, 43.0),
('RD-UG-002', 'LOC-UG-18', 'LOC-UG-10', 380.0, 3, 4.0, 43.0),
('RD-UG-003', 'LOC-UG-10', 'LOC-UG-06', 220.0, 2, 4.8, 43.0),
('RD-UG-004', 'LOC-UG-06', 'LOC-UG-01', 350.0, 3, 4.2, 43.0),
('RD-UG-005', 'LOC-UG-01', 'LOC-UG-16', 500.0, 5, 3.8, 43.0),
('RD-UG-006', 'LOC-UG-16', 'LOC-UG-09', 200.0, 2, 4.6, 43.0),
('RD-UG-007', 'LOC-UG-01', 'LOC-UG-02', 320.0, 3, 4.9, 43.0),
('RD-UG-008', 'LOC-UG-02', 'LOC-UG-03', 120.0, 1, 5.0, 43.0),
('RD-UG-009', 'LOC-UG-03', 'LOC-UG-04', 180.0, 2, 4.7, 43.0),
('RD-UG-010', 'LOC-UG-04', 'LOC-UG-05', 150.0, 1, 4.5, 43.0),
('RD-UG-011', 'LOC-UG-01', 'LOC-UG-25', 250.0, 2, 4.8, 43.0),
('RD-UG-012', 'LOC-UG-25', 'LOC-UG-24', 190.0, 2, 4.4, 43.0);

INSERT INTO service_requests (id, location_id, description, priority_level, budget_required, estimated_duration_hrs, status) VALUES
('REQ-UG-101', 'LOC-UG-06', 'Akuafo Hall Plumbing Leaks Block B', 5, 250.00, 3.5, 'PENDING'),
('REQ-UG-102', 'LOC-UG-02', 'Dept of CS Lab 3 Projector & Switch Repair', 5, 320.00, 2.0, 'PENDING'),
('REQ-UG-103', 'LOC-UG-09', 'Commonwealth Hall Electrical Main Panel Fault', 5, 280.00, 4.0, 'PENDING'),
('REQ-UG-104', 'LOC-UG-01', 'Balme Library Archive Journal Resource Transfer', 3, 120.00, 2.5, 'PENDING'),
('REQ-UG-105', 'LOC-UG-11', 'Jean Nelson Aka Hall Hostel Water Tank Repair', 4, 119.00, 3.0, 'PENDING');

INSERT INTO resources (id, name, type, capacity, cost_per_hour, current_location_id, is_available) VALUES
('RES-UG-01', 'Legon Central Plumbing Rapid Response Team 1', 'PERSONNEL', 5.0, 85.00, 'LOC-UG-43', 1),
('RES-UG-07', 'UGCS IT Infrastructure Support Team 1', 'PERSONNEL', 4.0, 120.00, 'LOC-UG-44', 1),
('RES-UG-11', 'UG Campus Shuttle Bus 01 (30-Seater)', 'VEHICLE', 30.0, 110.00, 'LOC-UG-21', 1),
('RES-UG-17', 'Balme Library Document Courier Van', 'VEHICLE', 15.0, 70.00, 'LOC-UG-01', 1),
('RES-UG-22', 'UG Mobile Hydraulic Lift Crane (15-Ton)', 'EQUIPMENT', 15.0, 350.00, 'LOC-UG-43', 1);
