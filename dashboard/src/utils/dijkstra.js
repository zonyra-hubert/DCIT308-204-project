import { LOCATIONS, ROADS, SYSTEM_PARAMETERS } from "../data/mockData";

/**
 * Calculates Dijkstra Shortest Path between startNodeId and targetNodeId
 * using edge weight formula: distance_m + penaltyWeight * (5.0 - condition_score)
 */
export function calculateDijkstraPath(startNodeId, targetNodeId) {
  if (startNodeId === targetNodeId) {
    const loc = LOCATIONS.find(l => l.id === startNodeId);
    return {
      pathNodes: [loc],
      segments: [],
      totalDistanceM: 0,
      totalTimeMins: 0,
      totalPenaltyCost: 0,
      success: true
    };
  }

  // Build Adjacency List
  const graph = {};
  LOCATIONS.forEach(loc => {
    graph[loc.id] = [];
  });

  ROADS.forEach(road => {
    const penaltyWeight = road.penaltyWeight || SYSTEM_PARAMETERS.roadPenaltyWeight;
    const conditionPenalty = penaltyWeight * (5.0 - road.condition);
    const weight = road.distanceM + conditionPenalty;

    // Undirected edge support for campus walking/vehicle paths
    if (graph[road.source]) {
      graph[road.source].push({ target: road.target, road, weight });
    }
    if (graph[road.target]) {
      graph[road.target].push({ target: road.source, road, weight });
    }
  });

  const distances = {};
  const previous = {};
  const previousEdge = {};
  const unvisited = new Set();

  LOCATIONS.forEach(loc => {
    distances[loc.id] = Infinity;
    previous[loc.id] = null;
    previousEdge[loc.id] = null;
    unvisited.add(loc.id);
  });

  distances[startNodeId] = 0;

  while (unvisited.size > 0) {
    // Find node with min distance
    let current = null;
    let minDist = Infinity;
    for (const nodeId of unvisited) {
      if (distances[nodeId] < minDist) {
        minDist = distances[nodeId];
        current = nodeId;
      }
    }

    if (current === null || minDist === Infinity || current === targetNodeId) {
      break;
    }

    unvisited.delete(current);

    const neighbors = graph[current] || [];
    for (const neighbor of neighbors) {
      if (unvisited.has(neighbor.target)) {
        const alt = distances[current] + neighbor.weight;
        if (alt < distances[neighbor.target]) {
          distances[neighbor.target] = alt;
          previous[neighbor.target] = current;
          previousEdge[neighbor.target] = neighbor.road;
        }
      }
    }
  }

  if (distances[targetNodeId] === Infinity) {
    return { success: false, message: "No path exists between selected nodes." };
  }

  // Reconstruct path
  const pathIds = [];
  const pathEdges = [];
  let curr = targetNodeId;
  while (curr !== null) {
    pathIds.unshift(curr);
    if (previousEdge[curr]) {
      pathEdges.unshift(previousEdge[curr]);
    }
    curr = previous[curr];
  }

  const pathNodes = pathIds.map(id => LOCATIONS.find(l => l.id === id));
  
  let totalDistanceM = 0;
  let totalTimeMins = 0;
  let totalPenaltyCost = distances[targetNodeId];

  pathEdges.forEach(edge => {
    totalDistanceM += edge.distanceM;
    totalTimeMins += edge.timeMins;
  });

  return {
    pathNodes,
    segments: pathEdges,
    totalDistanceM,
    totalTimeMins,
    totalPenaltyCost: Math.round(totalPenaltyCost),
    success: true
  };
}
