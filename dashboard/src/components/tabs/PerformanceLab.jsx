import React, { useState } from 'react';
import { BarChart3, TrendingUp, Zap, Database, Activity, Clock, ShieldCheck } from 'lucide-react';
import { BENCHMARK_METRICS, SYSTEM_PARAMETERS } from '../../data/mockData';

export default function PerformanceLab() {
  const [activeSubTab, setActiveSubTab] = useState("sorting");

  return (
    <div className="space-y-6">
      
      {/* Sub-tab selection */}
      <div className="flex flex-wrap items-center justify-between gap-4 glass-card p-4 rounded-2xl border border-gray-800">
        <div className="flex items-center space-x-2">
          <BarChart3 className="w-5 h-5 text-amber-400" />
          <h3 className="text-base font-bold text-white">Empirical Performance & Benchmark Lab</h3>
        </div>

        <div className="flex items-center space-x-2 bg-gray-900 p-1 rounded-xl border border-gray-800 text-xs">
          <button
            onClick={() => setActiveSubTab("sorting")}
            className={`px-3.5 py-1.5 rounded-lg font-bold transition ${activeSubTab === "sorting" ? "bg-amber-500 text-gray-950 glow-gold" : "text-gray-400 hover:text-white"}`}
          >
            Sorting Benchmark
          </button>
          <button
            onClick={() => setActiveSubTab("searching")}
            className={`px-3.5 py-1.5 rounded-lg font-bold transition ${activeSubTab === "searching" ? "bg-amber-500 text-gray-950 glow-gold" : "text-gray-400 hover:text-white"}`}
          >
            Search Benchmark
          </button>
          <button
            onClick={() => setActiveSubTab("dijkstra")}
            className={`px-3.5 py-1.5 rounded-lg font-bold transition ${activeSubTab === "dijkstra" ? "bg-amber-500 text-gray-950 glow-gold" : "text-gray-400 hover:text-white"}`}
          >
            Graph Dijkstra Engine
          </button>
          <button
            onClick={() => setActiveSubTab("hashtable")}
            className={`px-3.5 py-1.5 rounded-lg font-bold transition ${activeSubTab === "hashtable" ? "bg-amber-500 text-gray-950 glow-gold" : "text-gray-400 hover:text-white"}`}
          >
            Hash Table Index (547)
          </button>
        </div>
      </div>

      {/* Lab Content Panels */}
      {activeSubTab === "sorting" && (
        <div className="space-y-6">
          <div className="glass-card p-6 rounded-2xl border border-gray-800 space-y-4">
            <h4 className="text-sm font-bold text-white flex items-center gap-2">
              <TrendingUp className="w-4 h-4 text-amber-400" />
              Sorting Complexity: QuickSort vs MergeSort vs SelectionSort Execution Time (μs)
            </h4>

            {/* Custom Interactive Bar Graph */}
            <div className="space-y-4 pt-2">
              {BENCHMARK_METRICS.sorting.map((item) => (
                <div key={item.size} className="space-y-1.5">
                  <div className="flex justify-between text-xs text-gray-300 font-mono">
                    <span>N = {item.size.toLocaleString()} Items</span>
                    <span className="text-amber-400 font-bold">QuickSort: {item.quicksort} μs | MergeSort: {item.mergesort} μs</span>
                  </div>
                  
                  {/* Bars */}
                  <div className="space-y-1 bg-gray-900/80 p-2 rounded-xl border border-gray-800">
                    <div className="flex items-center space-x-2">
                      <span className="w-16 text-[10px] text-amber-400 font-bold">QuickSort</span>
                      <div className="flex-1 bg-gray-800 h-2.5 rounded-full overflow-hidden">
                        <div
                          className="bg-gradient-to-r from-amber-500 to-yellow-400 h-full rounded-full transition-all duration-500"
                          style={{ width: `${Math.min(100, (item.quicksort / item.mergesort) * 75)}%` }}
                        ></div>
                      </div>
                    </div>

                    <div className="flex items-center space-x-2">
                      <span className="w-16 text-[10px] text-blue-400 font-bold">MergeSort</span>
                      <div className="flex-1 bg-gray-800 h-2.5 rounded-full overflow-hidden">
                        <div
                          className="bg-gradient-to-r from-blue-500 to-indigo-400 h-full rounded-full transition-all duration-500"
                          style={{ width: `${Math.min(100, (item.mergesort / item.mergesort) * 85)}%` }}
                        ></div>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {activeSubTab === "searching" && (
        <div className="space-y-6">
          <div className="glass-card p-6 rounded-2xl border border-gray-800 space-y-4">
            <h4 className="text-sm font-bold text-white flex items-center gap-2">
              <Zap className="w-4 h-4 text-amber-400" />
              Searching Efficiency: Binary Search O(log N) vs Linear Search O(N) Execution Time (μs)
            </h4>

            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs border-collapse">
                <thead>
                  <tr className="border-b border-gray-800 bg-gray-900/60 text-gray-400 font-semibold uppercase">
                    <th className="py-3 px-4">Dataset Size (N)</th>
                    <th className="py-3 px-4 text-amber-400">Binary Search O(log N)</th>
                    <th className="py-3 px-4 text-red-400">Linear Search O(N)</th>
                    <th className="py-3 px-4 text-right">Speedup Factor</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-800 text-gray-200 font-mono">
                  {BENCHMARK_METRICS.searching.map((row) => (
                    <tr key={row.size} className="hover:bg-gray-800/40">
                      <td className="py-3 px-4 font-bold text-white">N = {row.size.toLocaleString()}</td>
                      <td className="py-3 px-4 text-amber-400 font-bold">{row.binarySearch} μs</td>
                      <td className="py-3 px-4 text-red-400">{row.linearSearch} μs</td>
                      <td className="py-3 px-4 text-right text-emerald-400 font-bold">
                        {(row.linearSearch / row.binarySearch).toFixed(1)}x faster
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {activeSubTab === "dijkstra" && (
        <div className="space-y-6">
          <div className="glass-card p-6 rounded-2xl border border-gray-800 space-y-4">
            <h4 className="text-sm font-bold text-white flex items-center gap-2">
              <Activity className="w-4 h-4 text-amber-400" />
              Campus Dijkstra Graph Engine Scalability across Node Size (V, E)
            </h4>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {BENCHMARK_METRICS.dijkstra.map((run, idx) => (
                <div key={idx} className="p-4 rounded-xl bg-gray-900/60 border border-gray-800 space-y-2">
                  <div className="text-xs text-gray-400 font-semibold uppercase">Scale Test {idx + 1}</div>
                  <div className="text-lg font-black text-white">{run.nodes} Nodes • {run.edges} Edges</div>
                  <div className="text-xs font-mono text-amber-400">{(run.executionNs / 1000).toFixed(1)} μs runtime</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {activeSubTab === "hashtable" && (
        <div className="space-y-6">
          <div className="glass-card-gold p-6 rounded-2xl border border-amber-500/40 space-y-5">
            <h4 className="text-sm font-bold text-white flex items-center gap-2">
              <Database className="w-4 h-4 text-amber-400" />
              Custom Hash Table Indexing Metrics (Capacity: {SYSTEM_PARAMETERS.hashTableCapacity})
            </h4>

            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-xs">
              <div className="p-4 rounded-xl bg-gray-900/80 border border-gray-800">
                <span className="text-gray-400 block text-[11px]">Prime Bucket Capacity</span>
                <span className="text-xl font-black text-amber-400">{BENCHMARK_METRICS.hashTable.capacity}</span>
              </div>

              <div className="p-4 rounded-xl bg-gray-900/80 border border-gray-800">
                <span className="text-gray-400 block text-[11px]">Indexed Elements</span>
                <span className="text-xl font-black text-white">{BENCHMARK_METRICS.hashTable.currentElements}</span>
              </div>

              <div className="p-4 rounded-xl bg-gray-900/80 border border-gray-800">
                <span className="text-gray-400 block text-[11px]">Load Factor α</span>
                <span className="text-xl font-black text-emerald-400">{BENCHMARK_METRICS.hashTable.loadFactor}</span>
              </div>

              <div className="p-4 rounded-xl bg-gray-900/80 border border-gray-800">
                <span className="text-gray-400 block text-[11px]">Collision Count</span>
                <span className="text-xl font-black text-yellow-400">{BENCHMARK_METRICS.hashTable.collisionCount}</span>
              </div>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
