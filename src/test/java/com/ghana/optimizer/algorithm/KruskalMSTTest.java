package com.ghana.optimizer.algorithm;

import com.ghana.optimizer.algorithm.graph.KruskalMST;
import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KruskalMSTTest {

    private AdjacencyListGraph graph;
    private Vertex v1, v2, v3, v4;

    @BeforeEach
    public void setUp() {
        graph = new AdjacencyListGraph(5);
        v1 = new Vertex("LOC-1", "Point 1", "Zone A", 5.65, -0.18);
        v2 = new Vertex("LOC-2", "Point 2", "Zone A", 5.66, -0.18);
        v3 = new Vertex("LOC-3", "Point 3", "Zone B", 5.65, -0.19);
        v4 = new Vertex("LOC-4", "Point 4", "Zone B", 5.66, -0.19);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        // 5 edges with weights:
        // (1,2) cost 100
        // (2,4) cost 150
        // (3,4) cost 200
        // (1,3) cost 250
        // (2,3) cost 300 (cycle edge)
        graph.addEdge(new Edge(v1, v2, 100, 1, 5.0, 43.0));
        graph.addEdge(new Edge(v2, v4, 150, 2, 5.0, 43.0));
        graph.addEdge(new Edge(v3, v4, 200, 3, 5.0, 43.0));
        graph.addEdge(new Edge(v1, v3, 250, 4, 5.0, 43.0));
        graph.addEdge(new Edge(v2, v3, 300, 5, 5.0, 43.0));
    }

    @Test
    public void testKruskalMSTComputesCorrectBackbone() {
        KruskalMST.MSTResult result = KruskalMST.computeMST(graph);

        assertTrue(result.isSpanning(), "MST should span all 4 vertices");
        assertEquals(3, result.getMstEdges().size(), "MST on 4 vertices must have exactly 3 edges (V-1)");
        assertEquals(450.0, result.getTotalEffectiveCost(), 0.01, "Total MST cost should be 100 + 150 + 200 = 450");
        assertEquals(1, result.getConnectedComponents(), "Should have 1 connected component");
    }

    @Test
    public void testEmptyAndSingleVertexGraph() {
        AdjacencyListGraph emptyGraph = new AdjacencyListGraph(5);
        KruskalMST.MSTResult emptyRes = KruskalMST.computeMST(emptyGraph);
        assertEquals(0, emptyRes.getMstEdges().size());

        AdjacencyListGraph singleNodeGraph = new AdjacencyListGraph(5);
        singleNodeGraph.addVertex(v1);
        KruskalMST.MSTResult singleRes = KruskalMST.computeMST(singleNodeGraph);
        assertEquals(0, singleRes.getMstEdges().size());
        assertTrue(singleRes.isSpanning());
    }
}
