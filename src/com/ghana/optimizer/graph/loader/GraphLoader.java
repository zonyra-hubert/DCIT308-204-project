package com.ghana.optimizer.graph.loader;

import com.ghana.optimizer.graph.Edge;
import com.ghana.optimizer.graph.Graph;
import com.ghana.optimizer.graph.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads graph data from CSV files.
 */
public class GraphLoader {

    /**
     * Loads all campus locations into the graph.
     */
    public static void loadLocations(String filePath, Graph graph)
            throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;

        // Skip header
        reader.readLine();

        while ((line = reader.readLine()) != null) {

            String[] data = line.split(",");

            String id = data[0].trim();
            String name = data[1].trim();
            String region = data[2].trim();

            double latitude = Double.parseDouble(data[3].trim());
            double longitude = Double.parseDouble(data[4].trim());

            Vertex vertex = new Vertex(
                    id,
                    name,
                    region,
                    latitude,
                    longitude
            );

            graph.addVertex(vertex);
        }

        reader.close();
    }

    /**
     * Loads all campus roads into the graph.
     */
    public static void loadRoads(String filePath, Graph graph)
            throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;

        // Skip header
        reader.readLine();

        while ((line = reader.readLine()) != null) {

            String[] data = line.split(",");

            String sourceId = data[1].trim();
            String destinationId = data[2].trim();

            int distance =
                    (int) Double.parseDouble(data[3].trim());

            int travelTime =
                    Integer.parseInt(data[4].trim());

            double condition =
                    Double.parseDouble(data[5].trim());

            double penalty =
                    Double.parseDouble(data[6].trim());

            Vertex source =
                    graph.findVertexById(sourceId);

            Vertex destination =
                    graph.findVertexById(destinationId);

            if (source != null && destination != null) {

                Edge edge = new Edge(
                        source,
                        destination,
                        distance,
                        travelTime,
                        condition,
                        penalty
                );

                graph.addEdge(edge);
            }
        }

        reader.close();
    }
}

