import React from 'react';
import { Shield, Activity, Zap, Database, DollarSign, Sparkles, RefreshCw } from 'lucide-react';
import { SYSTEM_PARAMETERS } from '../data/mockData';

export default function Header({ onResetData }) {
  return (
    <header className="sticky top-0 z-50 border-b border-gray-800 bg-[#0B0C10]/90 backdrop-blur-md px-4 lg:px-8 py-3 transition-all">
      <div className="max-w-7xl mx-auto flex flex-col md:flex-row md:items-center justify-between gap-4">
        
        {/* Title & Badge */}
        <div className="flex items-center space-x-3">
          <div className="h-10 w-10 rounded-xl bg-gradient-to-br from-amber-400 via-amber-500 to-yellow-600 p-[1px] shadow-lg glow-gold">
            <div className="h-full w-full bg-[#121212] rounded-[11px] flex items-center justify-center">
              <Shield className="h-5 w-5 text-amber-400" />
            </div>
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <h1 className="text-xl font-extrabold tracking-tight text-white flex items-center gap-2">
                UG Campus Service Operations Optimizer
              </h1>
              <span className="hidden sm:inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-amber-500/10 text-amber-400 border border-amber-500/30">
                <Sparkles className="w-3 h-3 mr-1" />
                UG-CSOO
              </span>
            </div>
            <p className="text-xs text-gray-400 flex items-center gap-1.5 mt-0.5">
              <span>{SYSTEM_PARAMETERS.campusName}</span>
              <span className="text-gray-600">•</span>
              <span className="text-amber-400/90 font-medium">{SYSTEM_PARAMETERS.courseCode}</span>
            </p>
          </div>
        </div>

        {/* Quick Metrics Pills & System Status */}
        <div className="flex flex-wrap items-center gap-2 sm:gap-3">
          
          {/* Active System Pill */}
          <div className="flex items-center space-x-2 px-3 py-1.5 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-semibold">
            <span className="relative flex h-2 w-2">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
            </span>
            <span>SYSTEM ACTIVE</span>
          </div>

          {/* Parameter 1 Pill */}
          <div className="flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-gray-900 border border-gray-800 text-xs text-gray-300">
            <Zap className="w-3.5 h-3.5 text-amber-400" />
            <span className="text-gray-400">Road Penalty:</span>
            <span className="font-bold text-amber-400">{SYSTEM_PARAMETERS.roadPenaltyWeight}</span>
          </div>

          {/* Parameter 2 Pill */}
          <div className="flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-gray-900 border border-gray-800 text-xs text-gray-300">
            <Database className="w-3.5 h-3.5 text-amber-400" />
            <span className="text-gray-400">Hash Cap:</span>
            <span className="font-bold text-amber-400">{SYSTEM_PARAMETERS.hashTableCapacity}</span>
          </div>

          {/* Parameter 3 Pill */}
          <div className="flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-gray-900 border border-amber-500/30 text-xs text-gray-300">
            <DollarSign className="w-3.5 h-3.5 text-amber-400" />
            <span className="text-gray-400">Budget:</span>
            <span className="font-bold text-amber-400">GHS {SYSTEM_PARAMETERS.budgetConstraintGHS.toLocaleString()}</span>
          </div>

          {/* Reset Action */}
          <button
            onClick={onResetData}
            title="Reset Datasets & System State"
            className="p-1.5 rounded-lg bg-gray-900 hover:bg-gray-800 border border-gray-800 text-gray-400 hover:text-white transition"
          >
            <RefreshCw className="w-4 h-4" />
          </button>

        </div>
      </div>
    </header>
  );
}
