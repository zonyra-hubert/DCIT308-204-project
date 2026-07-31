package com.ghana.optimizer;

import com.ghana.optimizer.graph.loader.GraphLoader;
import com.ghana.optimizer.graph.matrix.AdjacencyMatrixGraph;
import com.ghana.optimizer.graph.list.AdjacencyListGraph;

public class Main {

    public static void main(String[] args) {

        try {

            final int MAX_VERTICES = 52;

            /*
             * -------------------------------
             * Adjacency Matrix
             * -------------------------------
             */
            AdjacencyMatrixGraph matrixGraph =
                    new AdjacencyMatrixGraph(MAX_VERTICES);

            GraphLoader.loadLocations(
                    "data/seed/locations.csv",
                    matrixGraph
            );

            GraphLoader.loadRoads(
                    "data/seed/roads.csv",
                    matrixGraph
            );

            System.out.println("\n===============================================");
            System.out.println(" UNIVERSITY OF GHANA CAMPUS GRAPH ");
            System.out.println(" ADJACENCY MATRIX REPRESENTATION");
            System.out.println("===============================================");

            matrixGraph.printGraph();

            /*
             * -------------------------------
             * Adjacency List
             * -------------------------------
             */

            AdjacencyListGraph listGraph =
                    new AdjacencyListGraph(MAX_VERTICES);

            GraphLoader.loadLocations(
                    "data/seed/locations.csv",
                    listGraph
            );

            GraphLoader.loadRoads(
                    "data/seed/roads.csv",
                    listGraph
            );

            System.out.println("\n===============================================");
            System.out.println(" UNIVERSITY OF GHANA CAMPUS GRAPH ");
            System.out.println(" ADJACENCY LIST REPRESENTATION");
            System.out.println("===============================================");

            listGraph.printGraph();

        }

        catch (Exception e) {

            System.out.println("Error loading graph.");

            e.printStackTrace();

        }

    }
}

