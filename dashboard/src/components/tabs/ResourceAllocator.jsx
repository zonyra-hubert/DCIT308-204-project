import React, { useState, useMemo } from 'react';
import { DollarSign, Cpu, CheckCircle2, AlertCircle, Award, Sliders, ArrowRight } from 'lucide-react';
import { SYSTEM_PARAMETERS } from '../../data/mockData';
import { solve01Knapsack, solveGreedyAllocation } from '../../utils/knapsack';

export default function ResourceAllocator({ requests }) {
  const [budget, setBudget] = useState(SYSTEM_PARAMETERS.budgetConstraintGHS);

  const pendingRequests = useMemo(() => {
    return requests.filter(r => r.status === "PENDING" || r.status === "DISPATCHED");
  }, [requests]);

  const knapsackResult = useMemo(() => {
    return solve01Knapsack(pendingRequests, budget);
  }, [pendingRequests, budget]);

  const greedyResult = useMemo(() => {
    return solveGreedyAllocation(pendingRequests, budget);
  }, [pendingRequests, budget]);

  const dpGain = knapsackResult.totalValue - greedyResult.totalValue;

  return (
    <div className="space-y-6">
      
      {/* Interactive Budget Control Header */}
      <div className="glass-card p-6 rounded-2xl border border-gray-800 space-y-4">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-gray-800 pb-4">
          <div>
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <Cpu className="w-5 h-5 text-amber-400" />
              0/1 Knapsack DP vs Greedy Optimization Solver
            </h3>
            <p className="text-xs text-gray-400 mt-1">
              Allocates high-priority campus service tickets under budget constraint (Parameter 3 = GHS {SYSTEM_PARAMETERS.budgetConstraintGHS}).
            </p>
          </div>

          <div className="flex items-center space-x-3 bg-gray-900 px-4 py-2 rounded-xl border border-gray-800">
            <DollarSign className="w-4 h-4 text-amber-400" />
            <span className="text-xs text-gray-400">Target Budget:</span>
            <span className="text-lg font-black text-amber-400">GHS {budget.toLocaleString()}</span>
          </div>
        </div>

        {/* Budget Slider */}
        <div className="space-y-2">
          <div className="flex items-center justify-between text-xs text-gray-400 font-semibold">
            <span className="flex items-center gap-1.5"><Sliders className="w-3.5 h-3.5 text-amber-400" /> Adjust Shift Budget Limit:</span>
            <span>GHS {budget.toLocaleString()}</span>
          </div>
          <input
            type="range"
            min="200"
            max="3000"
            step="50"
            value={budget}
            onChange={(e) => setBudget(Number(e.target.value))}
            className="w-full h-2 bg-gray-800 rounded-lg appearance-none cursor-pointer accent-amber-500"
          />
          <div className="flex justify-between text-[10px] text-gray-500 font-mono">
            <span>GHS 200</span>
            <span>Default: GHS 1,089.00</span>
            <span>GHS 3,000</span>
          </div>
        </div>
      </div>

      {/* Solver Side-by-Side Comparison Cards */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        
        {/* 0/1 Knapsack DP Card */}
        <div className="glass-card-gold p-6 rounded-2xl border border-amber-500/40 space-y-5">
          <div className="flex items-center justify-between border-b border-amber-500/20 pb-3">
            <div className="flex items-center space-x-2">
              <Award className="w-5 h-5 text-amber-400" />
              <h4 className="text-sm font-black text-white">0/1 Knapsack Dynamic Programming</h4>
            </div>
            <span className="px-2.5 py-0.5 rounded-full text-[11px] font-bold bg-amber-500/20 text-amber-300 border border-amber-500/40">
              OPTIMAL SOLVER
            </span>
          </div>

          <div className="grid grid-cols-3 gap-3 text-xs">
            <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
              <span className="text-gray-400 block text-[11px]">Priority Value</span>
              <span className="text-xl font-black text-amber-400">{knapsackResult.totalValue} pts</span>
            </div>

            <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
              <span className="text-gray-400 block text-[11px]">Budget Used</span>
              <span className="text-xl font-black text-white">GHS {knapsackResult.totalCost.toFixed(2)}</span>
            </div>

            <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
              <span className="text-gray-400 block text-[11px]">Tickets Selected</span>
              <span className="text-xl font-black text-white">{knapsackResult.selectedRequests.length}</span>
            </div>
          </div>

          <div className="space-y-2">
            <h5 className="text-xs font-bold text-gray-400 uppercase tracking-wider">Allocated Tickets (DP Optimal)</h5>
            <div className="space-y-2 max-h-60 overflow-y-auto pr-1">
              {knapsackResult.selectedRequests.map(req => (
                <div key={req.id} className="p-3 rounded-xl bg-gray-900/60 border border-gray-800 flex items-center justify-between text-xs">
                  <div>
                    <div className="font-semibold text-white">{req.description}</div>
                    <div className="text-[11px] text-amber-400 font-medium">{req.locationName}</div>
                  </div>
                  <div className="text-right">
                    <div className="font-bold text-amber-300">GHS {req.budget.toFixed(2)}</div>
                    <div className="text-[10px] text-gray-400">P{req.priority} Priority</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Greedy Allocator Card */}
        <div className="glass-card p-6 rounded-2xl border border-gray-800 space-y-5">
          <div className="flex items-center justify-between border-b border-gray-800 pb-3">
            <div className="flex items-center space-x-2">
              <Cpu className="w-5 h-5 text-gray-400" />
              <h4 className="text-sm font-black text-white">Greedy Ratio Allocator</h4>
            </div>
            <span className="px-2.5 py-0.5 rounded-full text-[11px] font-bold bg-gray-800 text-gray-400 border border-gray-700">
              HEURISTIC
            </span>
          </div>

          <div className="grid grid-cols-3 gap-3 text-xs">
            <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
              <span className="text-gray-400 block text-[11px]">Priority Value</span>
              <span className="text-xl font-black text-gray-300">{greedyResult.totalValue} pts</span>
            </div>

            <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
              <span className="text-gray-400 block text-[11px]">Budget Used</span>
              <span className="text-xl font-black text-white">GHS {greedyResult.totalCost.toFixed(2)}</span>
            </div>

            <div className="p-3 rounded-xl bg-gray-900/80 border border-gray-800">
              <span className="text-gray-400 block text-[11px]">Tickets Selected</span>
              <span className="text-xl font-black text-white">{greedyResult.selectedRequests.length}</span>
            </div>
          </div>

          <div className="space-y-2">
            <h5 className="text-xs font-bold text-gray-400 uppercase tracking-wider">Allocated Tickets (Greedy Heuristic)</h5>
            <div className="space-y-2 max-h-60 overflow-y-auto pr-1">
              {greedyResult.selectedRequests.map(req => (
                <div key={req.id} className="p-3 rounded-xl bg-gray-900/40 border border-gray-800 flex items-center justify-between text-xs">
                  <div>
                    <div className="font-semibold text-white">{req.description}</div>
                    <div className="text-[11px] text-gray-400">{req.locationName}</div>
                  </div>
                  <div className="text-right">
                    <div className="font-bold text-gray-300">GHS {req.budget.toFixed(2)}</div>
                    <div className="text-[10px] text-gray-400">P{req.priority} Priority</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

      </div>

      {/* Trade-off Insights Box */}
      <div className="glass-card p-5 rounded-2xl border border-gray-800 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div className="flex items-center space-x-3">
          <AlertCircle className="w-5 h-5 text-amber-400 shrink-0" />
          <div className="text-xs">
            <span className="font-bold text-white">Algorithmic Trade-off Analysis: </span>
            <span className="text-gray-300">
              0/1 Knapsack DP achieves <strong className="text-amber-400">+{dpGain} priority points</strong> over the Greedy heuristic under GHS {budget.toLocaleString()} budget limit. Greedy fails when high priority items exceed fractional thresholds.
            </span>
          </div>
        </div>
      </div>

    </div>
  );
}
