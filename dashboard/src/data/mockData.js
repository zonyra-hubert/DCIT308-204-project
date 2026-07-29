// University of Ghana Campus Service Operations Optimizer (UG-CSOO) - Datasets & Graph Data

export const SYSTEM_PARAMETERS = {
  roadPenaltyWeight: 43.0,
  hashTableCapacity: 547,
  budgetConstraintGHS: 1089.00,
  projectName: "UG-CSOO",
  courseCode: "DCIT 204/308 Joint DSA Project",
  campusName: "University of Ghana, Legon Campus"
};

export const LOCATIONS = [
  { id: "LOC-UG-01", name: "Balme Library", region: "Legon Main Campus", lat: 5.6505, lng: -0.1872, category: "Landmark", icon: "Library" },
  { id: "LOC-UG-02", name: "Dept of Computer Science", region: "Mathematical Sciences", lat: 5.6530, lng: -0.1865, category: "Academic", icon: "Cpu" },
  { id: "LOC-UG-03", name: "Dept of Mathematics", region: "Mathematical Sciences", lat: 5.6528, lng: -0.1860, category: "Academic", icon: "BookOpen" },
  { id: "LOC-UG-04", name: "Dept of Physics", region: "Physical Sciences", lat: 5.6522, lng: -0.1855, category: "Academic", icon: "Zap" },
  { id: "LOC-UG-05", name: "Dept of Chemistry", region: "Physical Sciences", lat: 5.6520, lng: -0.1850, category: "Academic", icon: "FlaskConical" },
  { id: "LOC-UG-06", name: "Akuafo Hall", region: "Traditional Halls Zone", lat: 5.6490, lng: -0.1880, category: "Hostel", icon: "Home" },
  { id: "LOC-UG-07", name: "Volta Hall", region: "Traditional Halls Zone", lat: 5.6485, lng: -0.1868, category: "Hostel", icon: "Home" },
  { id: "LOC-UG-08", name: "Legon Hall", region: "Traditional Halls Zone", lat: 5.6498, lng: -0.1860, category: "Hostel", icon: "Home" },
  { id: "LOC-UG-09", name: "Commonwealth Hall", region: "Vandal City Zone", lat: 5.6540, lng: -0.1888, category: "Hostel", icon: "Shield" },
  { id: "LOC-UG-10", name: "Mensah Sarbah Hall", region: "Traditional Halls Zone", lat: 5.6475, lng: -0.1895, category: "Hostel", icon: "Home" },
  { id: "LOC-UG-11", name: "Jean Nelson Aka Hall", region: "Diaspora Halls Zone", lat: 5.6420, lng: -0.1840, category: "Hostel", icon: "Building" },
  { id: "LOC-UG-12", name: "Hilla Limann Hall", region: "Diaspora Halls Zone", lat: 5.6415, lng: -0.1830, category: "Hostel", icon: "Building" },
  { id: "LOC-UG-13", name: "Alexander Kwapong Hall", region: "Diaspora Halls Zone", lat: 5.6410, lng: -0.1845, category: "Hostel", icon: "Building" },
  { id: "LOC-UG-14", name: "Elizabeth Sey Hall", region: "Diaspora Halls Zone", lat: 5.6425, lng: -0.1850, category: "Hostel", icon: "Building" },
  { id: "LOC-UG-15", name: "Jubilee Hall", region: "Postgraduate Zone", lat: 5.6460, lng: -0.1835, category: "Hostel", icon: "Building" },
  { id: "LOC-UG-16", name: "Great Hall", region: "University Heights", lat: 5.6550, lng: -0.1880, category: "Landmark", icon: "Award" },
  { id: "LOC-UG-17", name: "ISSER", region: "Social Sciences Quad", lat: 5.6515, lng: -0.1890, category: "Research", icon: "BarChart3" },
  { id: "LOC-UG-18", name: "Night Market", region: "South Campus Hub", lat: 5.6440, lng: -0.1875, category: "Commercial", icon: "ShoppingBag" },
  { id: "LOC-UG-19", name: "Central Canteen", region: "Main Campus Core", lat: 5.6500, lng: -0.1860, category: "Landmark", icon: "Utensils" },
  { id: "LOC-UG-20", name: "CC Halls", region: "Main Campus Core", lat: 5.6502, lng: -0.1862, category: "Landmark", icon: "Building2" },
  { id: "LOC-UG-21", name: "Main Gate Shuttle Stop", region: "Campus Entrance", lat: 5.6400, lng: -0.1890, category: "Shuttle", icon: "Bus" },
  { id: "LOC-UG-22", name: "Diaspora Shuttle Terminal", region: "Diaspora Hub", lat: 5.6418, lng: -0.1838, category: "Shuttle", icon: "Bus" },
  { id: "LOC-UG-23", name: "Balme Library Shuttle Stop", region: "Central Transit", lat: 5.6507, lng: -0.1870, category: "Shuttle", icon: "Bus" },
  { id: "LOC-UG-24", name: "N-Block Lecture Theatre", region: "Academic Quad", lat: 5.6525, lng: -0.1870, category: "Academic", icon: "GraduationCap" },
  { id: "LOC-UG-25", name: "JQB (Jones Quartey Building)", region: "Academic Quad", lat: 5.6512, lng: -0.1852, category: "Academic", icon: "School" },
  { id: "LOC-UG-26", name: "UG Business School (UGBS)", region: "Business Quad", lat: 5.6535, lng: -0.1840, category: "Academic", icon: "Briefcase" },
  { id: "LOC-UG-27", name: "School of Law", region: "Law Complex", lat: 5.6538, lng: -0.1830, category: "Academic", icon: "Scale" },
  { id: "LOC-UG-28", name: "School of Engineering", region: "Applied Sciences Complex", lat: 5.6545, lng: -0.1820, category: "Academic", icon: "Wrench" },
  { id: "LOC-UG-29", name: "UG Sports Stadium", region: "Sports Complex", lat: 5.6465, lng: -0.1810, category: "Landmark", icon: "Trophy" },
  { id: "LOC-UG-30", name: "Legon Botanical Gardens", region: "North Campus", lat: 5.6600, lng: -0.1850, category: "Landmark", icon: "Trees" },
  { id: "LOC-UG-31", name: "UG Fire Station", region: "Safety & Utilities", lat: 5.6435, lng: -0.1895, category: "Utility", icon: "Flame" },
  { id: "LOC-UG-32", name: "UG Health Centre / Hospital", region: "Medical Zone", lat: 5.6450, lng: -0.1910, category: "Medical", icon: "Stethoscope" },
  { id: "LOC-UG-33", name: "Dept of Statistics", region: "Mathematical Sciences", lat: 5.6532, lng: -0.1858, category: "Academic", icon: "PieChart" },
  { id: "LOC-UG-34", name: "Dept of Psychology", region: "Social Sciences Quad", lat: 5.6518, lng: -0.1895, category: "Academic", icon: "Brain" },
  { id: "LOC-UG-35", name: "Dept of Economics", region: "Social Sciences Quad", lat: 5.6510, lng: -0.1885, category: "Academic", icon: "TrendingUp" },
  { id: "LOC-UG-36", name: "School of Performing Arts", region: "Arts Quad", lat: 5.6495, lng: -0.1845, category: "Academic", icon: "Music" },
  { id: "LOC-UG-37", name: "International House", region: "Global Student Hub", lat: 5.6480, lng: -0.1840, category: "Hostel", icon: "Globe" },
  { id: "LOC-UG-38", name: "VALCO Trust Hostel", region: "Graduate Zone", lat: 5.6470, lng: -0.1825, category: "Hostel", icon: "Building" },
  { id: "LOC-UG-39", name: "Pentagon Hostel (Block A)", region: "Private Student Zone", lat: 5.6405, lng: -0.1815, category: "Hostel", icon: "Building2" },
  { id: "LOC-UG-40", name: "Pentagon Hostel (Block B)", region: "Private Student Zone", lat: 5.6403, lng: -0.1810, category: "Hostel", icon: "Building2" },
  { id: "LOC-UG-41", name: "Pentagon Hostel (Block C)", region: "Private Student Zone", lat: 5.6400, lng: -0.1805, category: "Hostel", icon: "Building2" },
  { id: "LOC-UG-42", name: "TF Hostel (Evandy)", region: "Private Student Zone", lat: 5.6395, lng: -0.1820, category: "Hostel", icon: "Building2" },
  { id: "LOC-UG-43", name: "UG Physical Development Directorate", region: "Works & Maintenance", lat: 5.6445, lng: -0.1900, category: "Utility", icon: "Hammer" },
  { id: "LOC-UG-44", name: "UG Computing Services (UGCS)", region: "IT Hub", lat: 5.6515, lng: -0.1865, category: "Utility", icon: "Server" },
  { id: "LOC-UG-45", name: "K.A. Busia Hall", region: "Traditional Halls Zone", lat: 5.6488, lng: -0.1875, category: "Hostel", icon: "Home" },
  { id: "LOC-UG-46", name: "Sarbah Field Shuttle Stop", region: "Transit Hub", lat: 5.6472, lng: -0.1890, category: "Shuttle", icon: "Bus" },
  { id: "LOC-UG-47", name: "Commonwealth Hall Gate Stop", region: "North Transit", lat: 5.6542, lng: -0.1885, category: "Shuttle", icon: "Bus" },
  { id: "LOC-UG-48", name: "Banking Square", region: "Commercial Hub", lat: 5.6455, lng: -0.1880, category: "Commercial", icon: "CreditCard" },
  { id: "LOC-UG-49", name: "Post Office & Telecom Hub", region: "Services Hub", lat: 5.6460, lng: -0.1885, category: "Utility", icon: "Mail" },
  { id: "LOC-UG-50", name: "Legon Hall Annex", region: "Traditional Halls Zone", lat: 5.6495, lng: -0.1855, category: "Hostel", icon: "Home" },
  { id: "LOC-UG-51", name: "Noguchi Memorial Institute", region: "Medical Research", lat: 5.6430, lng: -0.1930, category: "Research", icon: "Microscope" },
  { id: "LOC-UG-52", name: "School of Nursing", region: "Health Sciences Zone", lat: 5.6442, lng: -0.1920, category: "Academic", icon: "HeartPulse" }
];

export const ROADS = [
  { id: "RD-UG-001", source: "LOC-UG-21", target: "LOC-UG-18", name: "Main Gate - Night Market Rd", distanceM: 550, timeMins: 4, condition: 4.5, penaltyWeight: 43 },
  { id: "RD-UG-002", source: "LOC-UG-18", target: "LOC-UG-10", name: "Night Market - Sarbah Hall Path", distanceM: 380, timeMins: 3, condition: 4.0, penaltyWeight: 43 },
  { id: "RD-UG-003", source: "LOC-UG-10", target: "LOC-UG-06", name: "Sarbah - Akuafo Connecting Rd", distanceM: 220, timeMins: 2, condition: 4.8, penaltyWeight: 43 },
  { id: "RD-UG-004", source: "LOC-UG-06", target: "LOC-UG-01", name: "Annie Jiagge Rd (South)", distanceM: 350, timeMins: 3, condition: 4.2, penaltyWeight: 43 },
  { id: "RD-UG-005", source: "LOC-UG-01", target: "LOC-UG-16", name: "Annie Jiagge Rd (North)", distanceM: 500, timeMins: 5, condition: 3.8, penaltyWeight: 43 },
  { id: "RD-UG-006", source: "LOC-UG-16", target: "LOC-UG-09", name: "Great Hall - Commonwealth Hill Path", distanceM: 200, timeMins: 2, condition: 4.6, penaltyWeight: 43 },
  { id: "RD-UG-007", source: "LOC-UG-01", target: "LOC-UG-02", name: "Balme - Computer Science Walkway", distanceM: 320, timeMins: 3, condition: 4.9, penaltyWeight: 43 },
  { id: "RD-UG-008", source: "LOC-UG-02", target: "LOC-UG-03", name: "Math Sciences Corridor", distanceM: 120, timeMins: 1, condition: 5.0, penaltyWeight: 43 },
  { id: "RD-UG-009", source: "LOC-UG-03", target: "LOC-UG-04", name: "Physics Quad Link", distanceM: 180, timeMins: 2, condition: 4.7, penaltyWeight: 43 },
  { id: "RD-UG-010", source: "LOC-UG-04", target: "LOC-UG-05", name: "Physical Sciences Connector", distanceM: 150, timeMins: 1, condition: 4.5, penaltyWeight: 43 },
  { id: "RD-UG-011", source: "LOC-UG-01", target: "LOC-UG-25", name: "J.S. Annan Rd (Library - JQB)", distanceM: 250, timeMins: 2, condition: 4.8, penaltyWeight: 43 },
  { id: "RD-UG-012", source: "LOC-UG-25", target: "LOC-UG-24", name: "JQB - N-Block Academic Path", distanceM: 190, timeMins: 2, condition: 4.4, penaltyWeight: 43 },
  { id: "RD-UG-013", source: "LOC-UG-25", target: "LOC-UG-26", name: "JQB - UGBS Business Link", distanceM: 300, timeMins: 3, condition: 4.1, penaltyWeight: 43 },
  { id: "RD-UG-014", source: "LOC-UG-26", target: "LOC-UG-27", name: "UGBS - Law School Walkway", distanceM: 210, timeMins: 2, condition: 4.6, penaltyWeight: 43 },
  { id: "RD-UG-015", source: "LOC-UG-27", target: "LOC-UG-28", name: "Law - Engineering Quad Rd", distanceM: 340, timeMins: 3, condition: 4.0, penaltyWeight: 43 },
  { id: "RD-UG-016", source: "LOC-UG-06", target: "LOC-UG-07", name: "Akuafo - Volta Quad Path", distanceM: 240, timeMins: 2, condition: 4.3, penaltyWeight: 43 },
  { id: "RD-UG-017", source: "LOC-UG-07", target: "LOC-UG-08", name: "Volta - Legon Hall Avenue", distanceM: 210, timeMins: 2, condition: 4.5, penaltyWeight: 43 },
  { id: "RD-UG-018", source: "LOC-UG-08", target: "LOC-UG-20", name: "Legon Hall - CC Halls Link", distanceM: 180, timeMins: 2, condition: 4.7, penaltyWeight: 43 },
  { id: "RD-UG-019", source: "LOC-UG-20", target: "LOC-UG-19", name: "CC Halls - Central Canteen Walkway", distanceM: 90, timeMins: 1, condition: 4.9, penaltyWeight: 43 },
  { id: "RD-UG-020", source: "LOC-UG-19", target: "LOC-UG-17", name: "Central Canteen - ISSER Link", distanceM: 280, timeMins: 3, condition: 4.2, penaltyWeight: 43 },
  { id: "RD-UG-021", source: "LOC-UG-21", target: "LOC-UG-22", name: "Extension Rd (Main Gate to Diaspora)", distanceM: 650, timeMins: 5, condition: 3.9, penaltyWeight: 43 },
  { id: "RD-UG-022", source: "LOC-UG-22", target: "LOC-UG-11", name: "Diaspora Terminal - Jean Nelson", distanceM: 140, timeMins: 1, condition: 4.8, penaltyWeight: 43 },
  { id: "RD-UG-023", source: "LOC-UG-11", target: "LOC-UG-12", name: "Jean Nelson - Limann Hall Path", distanceM: 160, timeMins: 1, condition: 4.7, penaltyWeight: 43 },
  { id: "RD-UG-024", source: "LOC-UG-43", target: "LOC-UG-01", name: "Works Directorate - Balme Library Rd", distanceM: 410, timeMins: 4, condition: 4.0, penaltyWeight: 43 },
  { id: "RD-UG-025", source: "LOC-UG-44", target: "LOC-UG-02", name: "UGCS IT Hub - CS Dept Link", distanceM: 180, timeMins: 2, condition: 5.0, penaltyWeight: 43 }
];

export const INITIAL_SERVICE_REQUESTS = [
  { id: "REQ-UG-101", locationId: "LOC-UG-06", locationName: "Akuafo Hall", description: "Plumbing Leaks - Block B Main Riser", priority: 5, budget: 250.00, durationHrs: 3.5, status: "PENDING", category: "Plumbing" },
  { id: "REQ-UG-102", locationId: "LOC-UG-02", locationName: "Dept of Computer Science", description: "Lab 3 Projector & Switch Overhaul", priority: 5, budget: 320.00, durationHrs: 2.0, status: "PENDING", category: "ICT" },
  { id: "REQ-UG-103", locationId: "LOC-UG-09", locationName: "Commonwealth Hall", description: "Electrical Main Panel Circuit Breaker Trips", priority: 5, budget: 280.00, durationHrs: 4.0, status: "DISPATCHED", category: "Electrical" },
  { id: "REQ-UG-104", locationId: "LOC-UG-01", locationName: "Balme Library", description: "Archive Journal & Reference Book Dispatch", priority: 3, budget: 120.00, durationHrs: 2.5, status: "PENDING", category: "Logistics" },
  { id: "REQ-UG-105", locationId: "LOC-UG-11", locationName: "Jean Nelson Aka Hall", description: "Hostel Water Tank Valve Maintenance", priority: 4, budget: 119.00, durationHrs: 3.0, status: "PENDING", category: "Plumbing" },
  { id: "REQ-UG-106", locationId: "LOC-UG-24", locationName: "N-Block Lecture Theatre", description: "Public Address System Micro-Feedback Fix", priority: 4, budget: 140.00, durationHrs: 1.5, status: "PENDING", category: "ICT" },
  { id: "REQ-UG-107", locationId: "LOC-UG-21", locationName: "Main Gate Shuttle Stop", description: "Peak Hour Shuttle Express Fleet Override", priority: 5, budget: 300.00, durationHrs: 4.0, status: "PENDING", category: "Shuttle" },
  { id: "REQ-UG-108", locationId: "LOC-UG-18", locationName: "Night Market", description: "Street Light Pole 14 Transformer Repair", priority: 3, budget: 190.00, durationHrs: 2.5, status: "PENDING", category: "Electrical" },
  { id: "REQ-UG-109", locationId: "LOC-UG-39", locationName: "Pentagon Hostel (Block A)", description: "Elevator Door Sensor Calibration", priority: 4, budget: 210.00, durationHrs: 3.0, status: "PENDING", category: "Mechanical" },
  { id: "REQ-UG-110", locationId: "LOC-UG-32", locationName: "UG Health Centre", description: "Emergency Vaccine Cooler Generator Check", priority: 5, budget: 450.00, durationHrs: 5.0, status: "DISPATCHED", category: "Electrical" }
];

export const INITIAL_RESOURCES = [
  { id: "RES-UG-01", name: "Legon Central Plumbing Rapid Response 1", type: "PERSONNEL", capacity: 5, costPerHour: 85.00, locationId: "LOC-UG-43", locationName: "Works Directorate", available: true },
  { id: "RES-UG-02", name: "UGCS IT Infrastructure Support Team 1", type: "PERSONNEL", capacity: 4, costPerHour: 120.00, locationId: "LOC-UG-44", locationName: "UGCS IT Hub", available: true },
  { id: "RES-UG-03", name: "UG Campus Shuttle Bus 01 (30-Seater)", type: "VEHICLE", capacity: 30, costPerHour: 110.00, locationId: "LOC-UG-21", locationName: "Main Gate", available: true },
  { id: "RES-UG-04", name: "Balme Library Document Courier Van", type: "VEHICLE", capacity: 15, costPerHour: 70.00, locationId: "LOC-UG-01", locationName: "Balme Library", available: true },
  { id: "RES-UG-05", name: "UG Mobile Hydraulic Crane (15-Ton)", type: "EQUIPMENT", capacity: 15, costPerHour: 350.00, locationId: "LOC-UG-43", locationName: "Works Directorate", available: false },
  { id: "RES-UG-06", name: "Akuafo & Volta Hostel Maintenance Unit", type: "PERSONNEL", capacity: 6, costPerHour: 75.00, locationId: "LOC-UG-06", locationName: "Akuafo Hall", available: true },
  { id: "RES-UG-07", name: "Commonwealth & Legon Hall Works Crew", type: "PERSONNEL", capacity: 8, costPerHour: 90.00, locationId: "LOC-UG-09", locationName: "Commonwealth Hall", available: true },
  { id: "RES-UG-08", name: "Diaspora Halls Facilities Repair Team", type: "PERSONNEL", capacity: 6, costPerHour: 80.00, locationId: "LOC-UG-11", locationName: "Jean Nelson Hall", available: true }
];

export const BENCHMARK_METRICS = {
  sorting: [
    { size: 100, quicksort: 14.2, mergesort: 18.5, selectionsort: 145.0 },
    { size: 500, quicksort: 48.6, mergesort: 62.1, selectionsort: 3200.0 },
    { size: 1000, quicksort: 95.1, mergesort: 124.0, selectionsort: 12800.0 },
    { size: 5000, quicksort: 520.0, mergesort: 680.0, selectionsort: 310000.0 },
    { size: 10000, quicksort: 1120.0, mergesort: 1450.0, selectionsort: 1250000.0 },
    { size: 50000, quicksort: 6100.0, mergesort: 7900.0, selectionsort: 31000000.0 }
  ],
  searching: [
    { size: 100, binarySearch: 0.12, linearSearch: 1.8 },
    { size: 500, binarySearch: 0.18, linearSearch: 8.5 },
    { size: 1000, binarySearch: 0.22, linearSearch: 17.2 },
    { size: 5000, binarySearch: 0.28, linearSearch: 84.0 },
    { size: 10000, binarySearch: 0.32, linearSearch: 168.0 },
    { size: 50000, binarySearch: 0.39, linearSearch: 850.0 }
  ],
  dijkstra: [
    { nodes: 10, edges: 25, executionNs: 4200 },
    { nodes: 52, edges: 105, executionNs: 18500 },
    { nodes: 200, edges: 600, executionNs: 74000 },
    { nodes: 1000, edges: 3500, executionNs: 410000 },
    { nodes: 5000, edges: 20000, executionNs: 2450000 }
  ],
  hashTable: {
    capacity: 547,
    currentElements: 304,
    loadFactor: 0.556,
    collisionCount: 14,
    maxChainDepth: 3
  }
};
