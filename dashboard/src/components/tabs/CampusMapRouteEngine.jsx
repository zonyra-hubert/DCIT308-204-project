import React, { useState, useMemo } from 'react';
import { MapPin, Navigation, ArrowRight, Zap, Clock, Shield, AlertTriangle, CheckCircle2 } from 'lucide-react';
import { LOCATIONS } from '../../data/mockData';
import { calculateDijkstraPath } from '../../utils/dijkstra';

export default function CampusMapRouteEngine() {
  const [sourceId, setSourceId] = useState("LOC-UG-43"); // Physical Development Works
  const [targetId, setTargetId] = useState("LOC-UG-09"); // Commonwealth Hall

  const routeResult = useMemo(() => {
    return calculateDijkstraPath(sourceId, targetId);
  }, [sourceId, targetId]);

  const sourceLoc = LOCATIONS.find(l => l.id === sourceId);
  const targetLoc = LOCATIONS.find(l => l.id === targetId);

  return (
    <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
      
      {/* Route Control Panel */}
      <div className="lg:col-span-5 space-y-6">
        <div className="glass-card p-5 rounded-2xl border border-gray-800 space-y-4">
          
          <div className="flex items-center space-x-2 border-b border-gray-800 pb-3">
            <Navigation className="w-5 h-5 text-amber-400" />
            <h3 className="text-base font-bold text-white">Campus Dijkstra Route Engine</h3>
          </div>

          {/* Form */}
          <div className="space-y-4 text-xs">
            <div>
              <label className="block text-gray-400 font-medium mb-1 flex items-center gap-1.5">
                <MapPin className="w-3.5 h-3.5 text-emerald-400" />
                Source Location (Start Node)
              </label>
              <select
                value={sourceId}
                onChange={(e) => setSourceId(e.target.value)}
                className="w-full bg-gray-900 border border-gray-700 rounded-xl p-3 text-white font-medium focus:outline-none focus:border-amber-500"
              >
                {LOCATIONS.map(loc => (
                  <option key={loc.id} value={loc.id}>
                    {loc.name} ({loc.region})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-gray-400 font-medium mb-1 flex items-center gap-1.5">
                <MapPin className="w-3.5 h-3.5 text-amber-400" />
                Target Destination (End Node)
              </label>
              <select
                value={targetId}
                onChange={(e) => setTargetId(e.target.value)}
                className="w-full bg-gray-900 border border-gray-700 rounded-xl p-3 text-white font-medium focus:outline-none focus:border-amber-500"
              >
                {LOCATIONS.map(loc => (
                  <option key={loc.id} value={loc.id}>
                    {loc.name} ({loc.region})
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Formula Callout */}
          <div className="p-3.5 rounded-xl bg-amber-500/10 border border-amber-500/20 text-xs text-amber-300 space-y-1">
            <div className="font-bold flex items-center gap-1">
              <Zap className="w-3.5 h-3.5" /> Edge Weight Formula (Parameter 1 = 43)
            </div>
            <p className="text-[11px] text-amber-200/80 font-mono">
              Weight = Distance(m) + 43 * (5.0 - ConditionScore)
            </p>
          </div>

        </div>

        {/* Route Metrics Card */}
        {routeResult.success && (
          <div className="glass-card-gold p-5 rounded-2xl border border-amber-500/30 space-y-4">
            <h4 className="text-xs uppercase tracking-wider font-bold text-amber-400 flex items-center gap-2">
              <Shield className="w-4 h-4" /> Optimal Route Summary
            </h4>

            <div className="grid grid-cols-2 gap-3 text-xs">
              <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
                <span className="text-gray-400 block text-[11px]">Total Distance</span>
                <span className="text-lg font-black text-white">{routeResult.totalDistanceM} m</span>
              </div>

              <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
                <span className="text-gray-400 block text-[11px]">Est. Travel Time</span>
                <span className="text-lg font-black text-white">{routeResult.totalTimeMins} mins</span>
              </div>

              <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800 col-span-2">
                <span className="text-gray-400 block text-[11px]">Effective Penalty Weight Score</span>
                <span className="text-lg font-black text-amber-400">{routeResult.totalPenaltyCost} units</span>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Visual Path Display & Node Trace */}
      <div className="lg:col-span-7 space-y-6">
        <div className="glass-card p-6 rounded-2xl border border-gray-800 space-y-5">
          
          <div className="flex items-center justify-between border-b border-gray-800 pb-3">
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <Navigation className="w-5 h-5 text-amber-400" />
              Graph Node Traversal Trace
            </h3>
            <span className="text-xs text-gray-400 font-mono">
              {routeResult.pathNodes ? `${routeResult.pathNodes.length} Nodes` : "0 Nodes"}
            </span>
          </div>

          {routeResult.success ? (
            <div className="space-y-4">
              
              {/* Path Flow Cards */}
              <div className="flex flex-wrap items-center gap-2 p-4 rounded-xl bg-gray-900/60 border border-gray-800">
                {routeResult.pathNodes.map((node, idx) => (
                  <React.Fragment key={node.id}>
                    <div className="flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-gray-800 border border-gray-700 text-xs font-semibold text-white shadow-sm">
                      <MapPin className="w-3.5 h-3.5 text-amber-400" />
                      <span>{node.name}</span>
                    </div>
                    {idx < routeResult.pathNodes.length - 1 && (
                      <ArrowRight className="w-4 h-4 text-amber-400 animate-pulse" />
                    )}
                  </React.Fragment>
                ))}
              </div>

              {/* Segment Details */}
              <div className="space-y-2">
                <h4 className="text-xs font-bold text-gray-400 uppercase tracking-wider">
                  Campus Segment Breakdown
                </h4>

                <div className="space-y-2 max-h-80 overflow-y-auto pr-1">
                  {routeResult.segments.map((seg, idx) => (
                    <div key={seg.id} className="p-3 rounded-xl bg-gray-900/40 border border-gray-800/80 flex items-center justify-between text-xs">
                      <div>
                        <div className="font-semibold text-white">{seg.name}</div>
                        <div className="text-[11px] text-gray-400">{seg.source} ➔ {seg.target}</div>
                      </div>
                      <div className="text-right space-y-0.5">
                        <div className="text-amber-400 font-bold">{seg.distanceM} m • {seg.timeMins} min</div>
                        <div className="text-[10px] text-gray-400">Condition Score: {seg.condition}/5.0</div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

            </div>
          ) : (
            <div className="p-8 text-center text-gray-400 space-y-2">
              <AlertTriangle className="w-8 h-8 text-amber-400 mx-auto" />
              <p>No valid campus road connection found between selected nodes.</p>
            </div>
          )}

        </div>
      </div>

    </div>
  );
}
