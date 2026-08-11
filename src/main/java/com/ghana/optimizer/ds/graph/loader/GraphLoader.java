package com.ghana.optimizer.ds.graph.loader;

import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Graph;
import com.ghana.optimizer.ds.graph.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads campus graph data (locations and roads) from CSV files into Graph structures.
 */
public class GraphLoader {

    public static void loadLocations(String filePath, Graph graph) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                String id = data[0].trim();
                String name = data[1].trim();
                String region = data[2].trim();
                double latitude = Double.parseDouble(data[3].trim());
                double longitude = Double.parseDouble(data[4].trim());

                Vertex vertex = new Vertex(id, name, region, latitude, longitude);
                graph.addVertex(vertex);
            }
        }
    }

    public static void loadRoads(String filePath, Graph graph) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                String sourceId = data[1].trim();
                String destinationId = data[2].trim();
                int distance = (int) Double.parseDouble(data[3].trim());
                int travelTime = Integer.parseInt(data[4].trim());
                double condition = Double.parseDouble(data[5].trim());
                double penalty = Double.parseDouble(data[6].trim());

                Vertex source = graph.findVertexById(sourceId);
                Vertex destination = graph.findVertexById(destinationId);

                if (source != null && destination != null) {
                    Edge edge = new Edge(source, destination, distance, travelTime, condition, penalty);
                    graph.addEdge(edge);
                }
            }
        }
    }
}
