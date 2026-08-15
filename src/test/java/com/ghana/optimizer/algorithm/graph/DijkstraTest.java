package com.ghana.optimizer.algorithm.graph;

import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Dijkstra.dijkstra() — shortest EFFECTIVE-WEIGHT path
 * (distance + road-condition penalty), not raw distance, over an
 * AdjacencyListGraph.
 */
public class DijkstraTest {

    private static final double DELTA = 0.001; // floating-point comparison tolerance

    private Vertex vertex(String id) {
        return new Vertex(id, "Name-" + id, "Region", 0.0, 0.0);
    }

    private Dijkstra.DijkstraStep findStep(List<Dijkstra.DijkstraStep> steps, String vertexId) {
        return steps.stream().filter(s -> s.vertexId.equals(vertexId)).findFirst().orElse(null);
    }

    // ---------------- the core design decision: effective weight beats raw distance ----------------

    /**
     * A-D direct: 1000m, PERFECT condition (5.0) -> effective = 1000 + 43*(5-5) = 1000
     * A-B-C-D via 3 legs of 200m each, TERRIBLE condition (1.0) each
     *   -> effective per leg = 200 + 43*(5-1) = 372, total = 1116
     * Raw distance would prefer the 3-leg route (600m < 1000m).
     * Effective weight must prefer the direct route (1000 < 1116).
     */
    private AdjacencyListGraph buildPenaltyDivergenceGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(10);
        Vertex a = vertex("A"), b = vertex("B"), c = vertex("C"), d = vertex("D");
        for (Vertex v : new Vertex[]{a, b, c, d}) graph.addVertex(v);

        graph.addEdge(new Edge(a, d, 1000, 10, 5.0, 43)); // perfect condition, direct, long
        graph.addEdge(new Edge(a, b, 200, 3, 1.0, 43));   // terrible condition
        graph.addEdge(new Edge(b, c, 200, 3, 1.0, 43));   // terrible condition
        graph.addEdge(new Edge(c, d, 200, 3, 1.0, 43));   // terrible condition

        return graph;
    }

    @Test
    void dijkstra_choosesDirectRoute_whenAlternativeHasWorseRoadCondition() {
        AdjacencyListGraph graph = buildPenaltyDivergenceGraph();

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "A");
        List<String> path = Dijkstra.reconstructPath(result, "D");

        assertEquals(List.of("A", "D"), path,
                "Direct route (effective=1000) must beat the shorter-in-meters route (effective=1116)");
    }

    @Test
    void dijkstra_effectiveDistanceToTarget_matchesExpectedPenaltyCalculation() {
        AdjacencyListGraph graph = buildPenaltyDivergenceGraph();

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "A");

        assertEquals(1000.0, findStep(result, "D").distance, DELTA,
                "Effective weight = distanceMeters + penaltyWeight * (5.0 - conditionScore)");
    }

    // ---------------- basic correctness (uniform weights, no penalty differences) ----------------

    private AdjacencyListGraph buildUniformWeightGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(10);
        Vertex p = vertex("P"), q = vertex("Q"), r = vertex("R"), s = vertex("S");
        for (Vertex v : new Vertex[]{p, q, r, s}) graph.addVertex(v);

        graph.addEdge(new Edge(p, q, 100, 1, 4.0, 0));
        graph.addEdge(new Edge(q, r, 100, 1, 4.0, 0));
        graph.addEdge(new Edge(p, r, 500, 1, 4.0, 0)); // direct but much longer
        graph.addEdge(new Edge(r, s, 50, 1, 4.0, 0));

        return graph;
    }

    @Test
    void dijkstra_choosesShorterMultiHopRoute_overLongerDirectRoute() {
        AdjacencyListGraph graph = buildUniformWeightGraph();

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "P");
        List<String> path = Dijkstra.reconstructPath(result, "S");

        assertEquals(List.of("P", "Q", "R", "S"), path);
    }

    @Test
    void dijkstra_startVertexHasZeroDistanceAndNoPredecessor() {
        AdjacencyListGraph graph = buildUniformWeightGraph();

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "P");

        Dijkstra.DijkstraStep start = findStep(result, "P");
        assertEquals(0.0, start.distance, DELTA);
        assertNull(start.predecessorId);
    }

    @Test
    void dijkstra_finalizationOrderIsNonDecreasingByDistance() {
        AdjacencyListGraph graph = buildUniformWeightGraph();

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "P");

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).distance <= result.get(i + 1).distance,
                    "Dijkstra must finalize vertices in non-decreasing distance order");
        }
    }

    // ---------------- disconnected graph handling ----------------

    @Test
    void dijkstra_disconnectedGraph_doesNotReachUnreachableComponent() {
        AdjacencyListGraph graph = new AdjacencyListGraph(10);
        Vertex a = vertex("A"), b = vertex("B"), x = vertex("X"), y = vertex("Y");
        for (Vertex v : new Vertex[]{a, b, x, y}) graph.addVertex(v);
        graph.addEdge(new Edge(a, b, 100, 1, 4.0, 43));
        graph.addEdge(new Edge(x, y, 100, 1, 4.0, 43));

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "A");

        assertEquals(2, result.size());
        assertNull(findStep(result, "X"));
    }

    // ---------------- boundary cases ----------------

    @Test
    void dijkstra_singleIsolatedVertex_boundaryCase() {
        AdjacencyListGraph graph = new AdjacencyListGraph(5);
        graph.addVertex(vertex("Z"));

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "Z");

        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).distance, DELTA);
    }

    @Test
    void dijkstra_nonexistentStartVertex_throwsIllegalArgumentException() {
        AdjacencyListGraph graph = buildUniformWeightGraph();

        assertThrows(IllegalArgumentException.class, () -> Dijkstra.dijkstra(graph, "DOES-NOT-EXIST"));
    }

    // ---------------- path reconstruction ----------------

    @Test
    void reconstructPath_unreachableTarget_returnsEmptyList() {
        AdjacencyListGraph graph = new AdjacencyListGraph(10);
        Vertex a = vertex("A"), x = vertex("X");
        graph.addVertex(a);
        graph.addVertex(x); // no edge -- X is unreachable from A

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "A");
        List<String> path = Dijkstra.reconstructPath(result, "X");

        assertTrue(path.isEmpty());
    }

    @Test
    void reconstructPath_targetIsStartVertex_returnsSingleElementPath() {
        AdjacencyListGraph graph = buildUniformWeightGraph();

        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "P");
        List<String> path = Dijkstra.reconstructPath(result, "P");

        assertEquals(List.of("P"), path);
    }

    // ---------------- trace table shape ----------------

    @Test
    void printTrace_hasOneRowPerVisitedVertex() {
        AdjacencyListGraph graph = buildUniformWeightGraph();
        List<Dijkstra.DijkstraStep> result = Dijkstra.dijkstra(graph, "P");

        String trace = Dijkstra.printTrace(result, "P");

        long dataRowCount = trace.lines()
                .filter(line -> line.matches("^\\d+\\s+\\S+\\s+\\S+.*"))
                .count();
        assertEquals(result.size(), dataRowCount, "Trace table must have exactly one row per visited vertex");
    }
}