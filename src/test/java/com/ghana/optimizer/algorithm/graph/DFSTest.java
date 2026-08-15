package com.ghana.optimizer.algorithm.graph;

import com.ghana.optimizer.ds.graph.Edge;
import com.ghana.optimizer.ds.graph.Vertex;
import com.ghana.optimizer.ds.graph.list.AdjacencyListGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DFS.dfs() / DFS.hasCycle() / DFS.findConnectedComponents()
 * over an AdjacencyListGraph.
 */
class DFSTest {

    private Vertex vertex(String id) {
        return new Vertex(id, "Name-" + id, "Region", 0.0, 0.0);
    }

    /** A-B-C-D-E as a straight chain (tree-shaped, no cycle), plus a disconnected X-Y pair. */
    private AdjacencyListGraph buildTreeShapedGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(10);
        Vertex a = vertex("A"), b = vertex("B"), c = vertex("C"), d = vertex("D"), e = vertex("E");
        Vertex x = vertex("X"), y = vertex("Y");
        for (Vertex v : new Vertex[]{a, b, c, d, e, x, y}) graph.addVertex(v);

        graph.addEdge(new Edge(a, b, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(b, c, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(c, d, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(d, e, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(x, y, 10, 1, 4.0, 43));

        return graph;
    }

    /** Same chain A-B-C-D-E, but closed into a loop with an extra E-A edge. */
    private AdjacencyListGraph buildGraphWithCycle() {
        AdjacencyListGraph graph = new AdjacencyListGraph(10);
        Vertex a = vertex("A"), b = vertex("B"), c = vertex("C"), d = vertex("D"), e = vertex("E");
        for (Vertex v : new Vertex[]{a, b, c, d, e}) graph.addVertex(v);

        graph.addEdge(new Edge(a, b, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(b, c, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(c, d, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(d, e, 10, 1, 4.0, 43));
        graph.addEdge(new Edge(e, a, 10, 1, 4.0, 43)); // closes the loop

        return graph;
    }

    // ---------------- correctness ----------------

    @Test
    void dfs_visitsAllVerticesInConnectedComponent_normalCase() {
        AdjacencyListGraph graph = buildTreeShapedGraph();

        List<DFS.DFSStep> result = DFS.dfs(graph, "A");

        assertEquals(5, result.size(), "Should reach A, B, C, D, E but not the disconnected X/Y pair");
    }

    @Test
    void dfs_startVertexHasNoPredecessor() {
        AdjacencyListGraph graph = buildTreeShapedGraph();

        List<DFS.DFSStep> result = DFS.dfs(graph, "A");

        assertNull(result.get(0).predecessorId);
        assertEquals("A", result.get(0).vertexId);
    }

    @Test
    void dfs_discoveryOrderIsSequentialStartingAtOne() {
        AdjacencyListGraph graph = buildTreeShapedGraph();

        List<DFS.DFSStep> result = DFS.dfs(graph, "A");

        for (int i = 0; i < result.size(); i++) {
            assertEquals(i + 1, result.get(i).discoveryOrder);
        }
    }

    // ---------------- disconnected graph handling ----------------

    @Test
    void dfs_disconnectedGraph_doesNotVisitUnreachableComponent() {
        AdjacencyListGraph graph = buildTreeShapedGraph();

        List<DFS.DFSStep> result = DFS.dfs(graph, "A");

        boolean reachedX = result.stream().anyMatch(s -> s.vertexId.equals("X"));
        assertFalse(reachedX, "X is in a disconnected component and must not appear");
    }

    // ---------------- boundary cases ----------------

    @Test
    void dfs_singleIsolatedVertex_boundaryCase() {
        AdjacencyListGraph graph = new AdjacencyListGraph(5);
        graph.addVertex(vertex("Z"));

        List<DFS.DFSStep> result = DFS.dfs(graph, "Z");

        assertEquals(1, result.size());
    }

    @Test
    void dfs_nonexistentStartVertex_throwsIllegalArgumentException() {
        AdjacencyListGraph graph = buildTreeShapedGraph();

        assertThrows(IllegalArgumentException.class, () -> DFS.dfs(graph, "DOES-NOT-EXIST"));
    }

    // ---------------- cycle detection ----------------

    @Test
    void hasCycle_returnsFalse_forTreeShapedGraph() {
        AdjacencyListGraph graph = buildTreeShapedGraph();

        assertFalse(DFS.hasCycle(graph), "A straight chain plus a separate pair contains no cycle");
    }

    @Test
    void hasCycle_returnsTrue_whenLoopClosingEdgeExists() {
        AdjacencyListGraph graph = buildGraphWithCycle();

        assertTrue(DFS.hasCycle(graph), "A-B-C-D-E-A is a closed loop and must be detected as a cycle");
    }

    @Test
    void hasCycle_doesNotFalselyFlagBidirectionalEdgeAsCycle() {
        // A single edge A-B, stored as BOTH A->B and B->A internally
        // (AdjacencyListGraph.addEdge always inserts the reverse edge).
        // This must NOT be reported as a cycle -- it's one road, not a loop.
        AdjacencyListGraph graph = new AdjacencyListGraph(5);
        Vertex a = vertex("A"), b = vertex("B");
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addEdge(new Edge(a, b, 10, 1, 4.0, 43));

        assertFalse(DFS.hasCycle(graph),
                "A single bidirectional edge must not be mistaken for a cycle");
    }

    // ---------------- connected components ----------------

    @Test
    void findConnectedComponents_findsBothClustersInDisconnectedGraph() {
        AdjacencyListGraph graph = buildTreeShapedGraph();

        List<List<String>> components = DFS.findConnectedComponents(graph);

        assertEquals(2, components.size(), "Expect the A-B-C-D-E cluster and the X-Y pair as two separate components");
    }

    @Test
    void findConnectedComponents_singleComponent_whenGraphIsFullyConnected() {
        AdjacencyListGraph graph = buildGraphWithCycle(); // all 5 vertices in one connected loop

        List<List<String>> components = DFS.findConnectedComponents(graph);

        assertEquals(1, components.size());
        assertEquals(5, components.get(0).size());
    }

    // ---------------- trace table shape ----------------

    @Test
    void printTrace_hasOneRowPerVisitedVertex() {
        AdjacencyListGraph graph = buildTreeShapedGraph();
        List<DFS.DFSStep> result = DFS.dfs(graph, "A");

        String trace = DFS.printTrace(result, "A");

        long dataRowCount = trace.lines()
                .filter(line -> line.matches("^\\d+\\s+\\S+\\s+\\S+.*"))
                .count();
        assertEquals(result.size(), dataRowCount, "Trace table must have exactly one row per visited vertex");
    }
}
