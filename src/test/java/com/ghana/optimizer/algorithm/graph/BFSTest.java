package com.ghana.optimizer.algorithm.graph;

import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BFS.bfs() — breadth-first traversal, hop-distance tracking,
 * and shortest-path reconstruction over an AdjacencyListGraph.
 */
class BFSTest {

    // ---------------- fixture builders ----------------

    private Vertex vertex(String id) {
        return new Vertex(id, "Name-" + id, "Region", 0.0, 0.0);
    }

    /**
     * Builds: A-B, A-C, B-D, C-D, D-E   (one connected cluster, contains a
     * cycle A-B-D-C-A so BFS can't rely on tree-shaped input),
     * plus a separate disconnected pair X-Y.
     */
    private AdjacencyListGraph buildMixedGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(10);
        Vertex a = vertex("A"), b = vertex("B"), c = vertex("C"), d = vertex("D"), e = vertex("E");
        Vertex x = vertex("X"), y = vertex("Y");
        for (Vertex v : new Vertex[]{a, b, c, d, e, x, y}) graph.addVertex(v);

        graph.addEdge(new Edge(a, b, 100, 2, 4.0, 43));
        graph.addEdge(new Edge(a, c, 150, 3, 4.0, 43));
        graph.addEdge(new Edge(b, d, 120, 2, 4.0, 43));
        graph.addEdge(new Edge(c, d, 90, 2, 4.0, 43));
        graph.addEdge(new Edge(d, e, 200, 4, 4.0, 43));
        graph.addEdge(new Edge(x, y, 50, 1, 4.0, 43));

        return graph;
    }

    private BFS.BFSStep findStep(List<BFS.BFSStep> steps, String vertexId) {
        return steps.stream().filter(s -> s.vertexId.equals(vertexId)).findFirst().orElse(null);
    }

    // ---------------- correctness ----------------

    @Test
    void bfs_visitsAllVerticesInConnectedComponent_normalCase() {
        AdjacencyListGraph graph = buildMixedGraph();

        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        assertEquals(5, result.size(), "Should reach A, B, C, D, E but not the disconnected X/Y pair");
    }

    @Test
    void bfs_hopDistancesAreCorrect_shortestPathFirst() {
        AdjacencyListGraph graph = buildMixedGraph();

        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        assertEquals(0, findStep(result, "A").distance);
        assertEquals(1, findStep(result, "B").distance);
        assertEquals(1, findStep(result, "C").distance);
        assertEquals(2, findStep(result, "D").distance, "D is reachable in 2 hops via either B or C");
        assertEquals(3, findStep(result, "E").distance);
    }

    @Test
    void bfs_startVertexHasNoPredecessor() {
        AdjacencyListGraph graph = buildMixedGraph();

        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        assertNull(findStep(result, "A").predecessorId);
    }

    // ---------------- disconnected graph handling ----------------

    @Test
    void bfs_disconnectedGraph_doesNotVisitUnreachableComponent() {
        AdjacencyListGraph graph = buildMixedGraph();

        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        assertNull(findStep(result, "X"), "X is in a disconnected component and must not appear");
        assertNull(findStep(result, "Y"), "Y is in a disconnected component and must not appear");
    }

    @Test
    void bfs_fromOtherComponent_onlyReachesThatComponent() {
        AdjacencyListGraph graph = buildMixedGraph();

        List<BFS.BFSStep> result = BFS.bfs(graph, "X");

        assertEquals(2, result.size());
        assertNotNull(findStep(result, "Y"));
        assertNull(findStep(result, "A"));
    }

    // ---------------- boundary cases ----------------

    @Test
    void bfs_singleIsolatedVertex_boundaryCase() {
        AdjacencyListGraph graph = new AdjacencyListGraph(5);
        Vertex solo = vertex("Z");
        graph.addVertex(solo);

        List<BFS.BFSStep> result = BFS.bfs(graph, "Z");

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).distance);
    }

    @Test
    void bfs_nonexistentStartVertex_throwsIllegalArgumentException() {
        AdjacencyListGraph graph = buildMixedGraph();

        assertThrows(IllegalArgumentException.class, () -> BFS.bfs(graph, "DOES-NOT-EXIST"));
    }

    // ---------------- path reconstruction ----------------

    @Test
    void reconstructPath_returnsShortestPathToTarget() {
        AdjacencyListGraph graph = buildMixedGraph();
        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        List<String> path = BFS.reconstructPath(result, "E");

        assertEquals("A", path.get(0), "Path must start at the BFS start vertex");
        assertEquals("E", path.get(path.size() - 1), "Path must end at the target vertex");
        assertEquals(4, path.size(), "A -> (B or C) -> D -> E is 4 vertices");
    }

    @Test
    void reconstructPath_unreachableTarget_returnsEmptyList() {
        AdjacencyListGraph graph = buildMixedGraph();
        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        List<String> path = BFS.reconstructPath(result, "X");

        assertTrue(path.isEmpty(), "X is unreachable from A, so the path must be empty");
    }

    @Test
    void reconstructPath_targetIsStartVertex_returnsSingleElementPath() {
        AdjacencyListGraph graph = buildMixedGraph();
        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        List<String> path = BFS.reconstructPath(result, "A");

        assertEquals(1, path.size());
        assertEquals("A", path.get(0));
    }

    // ---------------- trace table shape ----------------

    @Test
    void printTrace_hasOneRowPerVisitedVertex() {
        AdjacencyListGraph graph = buildMixedGraph();
        List<BFS.BFSStep> result = BFS.bfs(graph, "A");

        String trace = BFS.printTrace(result, "A");

        long dataRowCount = trace.lines()
                .filter(line -> line.matches("^\\d+\\s+\\S+\\s+\\S+.*"))
                .count();
        assertEquals(result.size(), dataRowCount, "Trace table must have exactly one row per visited vertex");
    }
}