# 🏛️ University of Ghana Campus Service Operations Optimizer (UG-CSOO)

An enterprise-grade Java console application and empirical algorithm laboratory designed to model, route, schedule, and optimize campus service operations, facility maintenance, shuttle dispatches, and IT support allocation across the **University of Ghana (UG), Legon Campus** road and pedestrian network.

---

## 📌 1. System Overview & Problem Statement

### Operational Context: UG Legon Campus
The **University of Ghana, Legon Campus** operates as a bustling academic micro-city spanning over 13 square kilometers. Daily campus operations involve handling hundreds of facilities maintenance tickets across traditional and diaspora student hostels, managing peak shuttle schedules, servicing department IT infrastructure, and transferring library archives.

The **UG-CSOO** system provides automated, algorithmic decision support for campus logistics. It utilizes custom-built data structures (without relying on standard `java.util` collection frameworks) to solve shortest path routing, priority maintenance scheduling, resource allocation under strict budget constraints, and real-time operational auditing.

---

## ⚙️ 2. Explicit System Parameters & Operational Constraints

| Parameter Name | Value | Description & Technical Application |
| :--- | :--- | :--- |
| **Parameter 1: Road Penalty Weight** | `43` | Weight factor applied to deteriorated campus road segments (e.g., speed bumps, pothole delays, pedestrian-heavy zones on Annie Jiagge & Guggisberg Ave). Used in Dijkstra/BFS routing cost calculations (`edge_weight = distance_m + 43 * (5.0 - condition_score)`). |
| **Parameter 2: Custom Hash Table Capacity** | `761` | Initial prime capacity for the custom hash map index (`CustomHashTable`) used for $O(1)$ lookups of active campus service tickets by ticket ID. |
| **Parameter 3: Operational Budget Constraint** | `GHS 1,089.00` | Budget limit applied to the 0/1 Knapsack optimization solver for batch maintenance allocation per shift/crew. |

---

## 📂 3. Real-World Domain & Entity Mappings

### 📍 Locations (50+ Campus Nodes)
- **Traditional Hostels:** Akuafo Hall (`LOC-UG-06`), Volta Hall (`LOC-UG-07`), Legon Hall (`LOC-UG-08`), Commonwealth Hall (`LOC-UG-09`), Mensah Sarbah Hall (`LOC-UG-10`), K.A. Busia Hall (`LOC-UG-45`).
- **Diaspora & Private Hostels:** Jean Nelson Aka Hall (`LOC-UG-11`), Hilla Limann Hall (`LOC-UG-12`), Alexander Kwapong Hall (`LOC-UG-13`), Elizabeth Sey Hall (`LOC-UG-14`), Jubilee Hall (`LOC-UG-15`), Pentagon Hostels (`LOC-UG-39` to `LOC-UG-41`), TF Hostel (`LOC-UG-42`).
- **Academic Departments:** Computer Science (`LOC-UG-02`), Mathematics (`LOC-UG-03`), Physics (`LOC-UG-04`), Chemistry (`LOC-UG-05`), Statistics (`LOC-UG-33`), Economics (`LOC-UG-35`), Business School (`LOC-UG-26`), School of Law (`LOC-UG-27`), School of Engineering (`LOC-UG-28`).
- **Key Landmarks & Hubs:** Balme Library (`LOC-UG-01`), Central Canteen (`LOC-UG-19`), CC Halls (`LOC-UG-20`), Great Hall (`LOC-UG-16`), ISSER (`LOC-UG-17`), Night Market (`LOC-UG-18`), Banking Square (`LOC-UG-48`), UGCS IT Hub (`LOC-UG-44`), Works & Maintenance (`LOC-UG-43`), UG Health Centre (`LOC-UG-32`).
- **Shuttle Transit Stops:** Main Gate Stop (`LOC-UG-21`), Diaspora Terminal (`LOC-UG-22`), Balme Library Stop (`LOC-UG-23`), Sarbah Field Stop (`LOC-UG-46`), Commonwealth Gate Stop (`LOC-UG-47`).

### 🛣️ Roads / Edges (100+ Campus Segments)
- **Named Avenues & Roads:** Annie Jiagge Road, J.S. Annan Road, Guggisberg Avenue, University Avenue, Extension Roads, and Hostel Connecting Paths.
- **Edge Metrics:** Measured distance in meters ($m$), travel time in minutes, road condition rating ($1.0 - 5.0$), and penalty factor ($43$).

### 🎟️ Service Requests (300+ Active Records)
- **Hostel Maintenance Tickets:** Plumbing leaks in Akuafo/Volta Hall blocks, electrical circuit faults at Sarbah/Commonwealth Halls, water tank valve overhauls.
- **ICT Infrastructure Dispatches:** Projector and network switch repairs in N-Block, JQB, and CS Computer Labs.
- **Campus Shuttle Priority Dispatches:** Peak hour shuttle queue overrides at Main Gate and Diaspora Terminals.
- **Library Logistics:** Balme Library archive book transfers to ISSER and Department libraries.

### 🚜 Operational Resources (30+ Units)
- **Maintenance Crews & Teams:** Legon Central Plumbing Teams, Electrical Response Squads, Facilities Repair Crews.
- **IT Support Officers:** UGCS Infrastructure & AV Technicians.
- **Transit & Utility Vehicles:** 30-Seater Campus Shuttles, Library Courier Vans, Physical Development Utility Pickups, 15-Ton Hydraulic Crane.

---

## 🗄️ 4. Database Schema Outline

```
DCIT308-204-project/
├── data/
│   ├── ghana_optimizer.db                   # SQLite Database File
│   ├── seed/                                # Seed CSV Datasets
│   │   ├── locations.csv                    # 52 Campus Locations
│   │   ├── roads.csv                        # 105 Campus Road Segments (Penalty: 43)
│   │   ├── requests.csv                     # 300+ Campus Service Tickets
│   │   └── resources.csv                    # 32 Campus Resources & Vehicles
│   └── sql/
│       ├── schema.sql                       # Database DDL Table Definitions
│       └── seed_data.sql                    # SQL Seed Scripts
```

### Table Definitions (`data/sql/schema.sql`)
1. `locations`: `(id, name, region, latitude, longitude)`
2. `roads`: `(id, source_location_id, target_location_id, distance_m, travel_time_mins, condition_score, penalty_weight DEFAULT 59.0)`
3. `service_requests`: `(id, location_id, description, priority_level, budget_required, estimated_duration_hrs, status)`
4. `resources`: `(id, name, type, capacity, cost_per_hour, current_location_id, is_available)`
5. `algorithm_runs`: `(id, algorithm_name, dataset_size, execution_time_ns, memory_used_kb, hash_capacity DEFAULT 761, budget_limit DEFAULT 1089.0, parameters_json, executed_at)`
6. `audit_events`: `(id, action_type, entity_name, entity_id, details, timestamp)`

---

## 💻 5. Algorithm Scenarios & Pseudocode Snippets

### A. Graph Shortest Path Routing (Dijkstra Algorithm)
*Scenario:* Dispatching a plumbing repair truck from Physical Development (`LOC-UG-43`) to Commonwealth Hall (`LOC-UG-09`) via Annie Jiagge Road with road penalty factor $43$.

```java
// Pseudocode: Dijkstra Shortest Path with Road Penalty Weight 43
public double calculateCampusRouteCost(Road edge) {
    double baseDistanceMeters = edge.getDistanceMeters();
    double conditionScore = edge.getConditionScore(); // Scale 1.0 to 5.0
    double penaltyWeight = 59.0; // System Parameter 1
    
    // Deteriorated paths add penalty weight of 43 * (5.0 - condition)
    double effectiveWeight = baseDistanceMeters + penaltyWeight * (5.0 - conditionScore);
    return effectiveWeight;
}
```

### B. Custom Hash Table Indexing (`CustomHashTable`)
*Scenario:* Storing and querying 300+ campus service tickets in $O(1)$ expected time using an initial prime capacity of $761$.

```java
// Pseudocode: Custom Hash Table Indexing for UG Service Tickets
public class CustomHashTable<K, V> {
    private static final int INITIAL_CAPACITY = 761; // System Parameter 2 (Prime Capacity)
    private HashNode<K, V>[] buckets;

    public CustomHashTable() {
        this.buckets = new HashNode[INITIAL_CAPACITY];
    }

    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        int index = Math.abs(hashCode) % INITIAL_CAPACITY;
        return index;
    }
}
```

### C. 0/1 Knapsack Budget Optimization Solver
*Scenario:* Selecting the set of hostel plumbing and electrical tickets that maximizes total priority value without exceeding the shift budget of **GHS 1,089.00**.

```java
// Pseudocode: Dynamic Programming 0/1 Knapsack Budget Optimizer
public DynamicArray<ServiceRequest> optimizeMaintenanceBudget(ServiceRequest[] requests) {
    int budgetLimitGHS = 1089; // System Parameter 3 (GHS 1,089)
    int n = requests.length;
    int[][] dp = new int[n + 1][budgetLimitGHS + 1];

    for (int i = 1; i <= n; i++) {
        int cost = (int) Math.ceil(requests[i - 1].getBudgetRequired());
        int priorityValue = requests[i - 1].getPriorityLevel();

        for (int w = 0; w <= budgetLimitGHS; w++) {
            if (cost <= w) {
                dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - cost] + priorityValue);
            } else {
                dp[i][w] = dp[i - 1][w];
            }
        }
    }
    return backtrackSelectedRequests(dp, requests, budgetLimitGHS);
}
```

---

## 🖥️ 6. Console Mocks & Terminal Verification

Running the application (`java -cp ... com.ghana.optimizer.Main`):

```text
==========================================================================
  University of Ghana Campus Service Operations Optimizer (UG-CSOO)  
  Operational Domain: UG Legon Campus, Accra, Ghana                       
==========================================================================
System Parameters Initialized:
  - Parameter 1 (Road Penalty Weight): 59.0
  - Parameter 2 (Custom Hash Table Capacity): 761
  - Parameter 3 (Budget Constraint): GHS 1089.0
--------------------------------------------------------------------------
[DB STATUS] Connecting to SQLite Database...
  -> Campus Nodes/Locations Loaded: 52
  -> Campus Road Segments Loaded: 105
  -> Active Service Requests Loaded: 304
  -> Campus Maintenance/IT Resources Loaded: 32
--------------------------------------------------------------------------
UG-CSOO System Engine Initialized Successfully.
```

---

## 🛠️ 7. Build and Execution Instructions

### 1. Initialize SQLite Database from Terminal
```bash
sqlite3 data/ghana_optimizer.db < data/sql/schema.sql
sqlite3 data/ghana_optimizer.db < data/sql/seed_data.sql
```

### 2. Compile Java Source Code
```bash
mkdir -p bin
javac -cp "~/.m2/repository/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar:~/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar" -d bin src/main/java/com/ghana/optimizer/config/DatabaseConfig.java src/main/java/com/ghana/optimizer/storage/db/ConnectionManager.java src/main/java/com/ghana/optimizer/Main.java
```

### 3. Run System Launcher
```bash
java -cp "bin:src/main/resources:~/.m2/repository/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar:~/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar" com.ghana.optimizer.Main
```
