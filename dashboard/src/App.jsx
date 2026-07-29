import React, { useState } from 'react';
import Header from './components/Header';
import AnalyticsOverview from './components/AnalyticsOverview';
import ServiceRequestQueue from './components/tabs/ServiceRequestQueue';
import CampusMapRouteEngine from './components/tabs/CampusMapRouteEngine';
import ResourceAllocator from './components/tabs/ResourceAllocator';
import PerformanceLab from './components/tabs/PerformanceLab';
import { LOCATIONS, ROADS, INITIAL_SERVICE_REQUESTS, INITIAL_RESOURCES } from './data/mockData';
import { FileText, Navigation, Cpu, BarChart3, CheckCircle2 } from 'lucide-react';

export default function App() {
  const [activeTab, setActiveTab] = useState("queue");
  const [requests, setRequests] = useState(INITIAL_SERVICE_REQUESTS);
  const [resources, setResources] = useState(INITIAL_RESOURCES);
  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => {
      setToastMessage(null);
    }, 4000);
  };

  const handleDispatch = (reqId) => {
    setRequests(prev => prev.map(req => {
      if (req.id === reqId) {
        const nextStatus = req.status === "PENDING" ? "DISPATCHED" : "COMPLETED";
        showToast(`Ticket ${reqId} updated to status: ${nextStatus}`);
        return { ...req, status: nextStatus };
      }
      return req;
    }));
  };

  const handleCreateRequest = (newReq) => {
    setRequests(prev => [newReq, ...prev]);
    showToast(`New service ticket ${newReq.id} created successfully!`);
  };

  const handleResetData = () => {
    setRequests(INITIAL_SERVICE_REQUESTS);
    setResources(INITIAL_RESOURCES);
    showToast("System datasets reset to default state.");
  };

  return (
    <div className="min-h-screen bg-[#0B0C10] text-gray-100 flex flex-col font-sans selection:bg-amber-500 selection:text-gray-950">
      
      {/* Toast Notification */}
      {toastMessage && (
        <div className="fixed bottom-6 right-6 z-50 animate-bounce">
          <div className="glass-card-gold px-4 py-3 rounded-xl border border-amber-500/50 shadow-2xl flex items-center space-x-2 text-xs font-bold text-amber-300">
            <CheckCircle2 className="w-4 h-4 text-amber-400" />
            <span>{toastMessage}</span>
          </div>
        </div>
      )}

      {/* Header */}
      <Header onResetData={handleResetData} />

      {/* Main Container */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 lg:px-8 py-6 space-y-6">
        
        {/* Top Analytics Stat Cards */}
        <AnalyticsOverview
          locationsCount={LOCATIONS.length}
          requests={requests}
          roadsCount={ROADS.length}
          resourcesCount={resources.length}
        />

        {/* Tab Navigation */}
        <div className="flex flex-wrap items-center gap-2 border-b border-gray-800 pb-2">
          <button
            onClick={() => setActiveTab("queue")}
            className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl font-bold text-xs transition ${
              activeTab === "queue"
                ? "bg-amber-500 text-gray-950 shadow-md glow-gold"
                : "bg-gray-900/60 hover:bg-gray-800 text-gray-400 hover:text-white border border-gray-800"
            }`}
          >
            <FileText className="w-4 h-4" />
            <span>1. Service Request Queue</span>
            <span className="ml-1.5 px-2 py-0.5 rounded-full text-[10px] bg-black/20 font-mono">
              {requests.length}
            </span>
          </button>

          <button
            onClick={() => setActiveTab("map")}
            className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl font-bold text-xs transition ${
              activeTab === "map"
                ? "bg-amber-500 text-gray-950 shadow-md glow-gold"
                : "bg-gray-900/60 hover:bg-gray-800 text-gray-400 hover:text-white border border-gray-800"
            }`}
          >
            <Navigation className="w-4 h-4" />
            <span>2. Campus Map & Route Engine</span>
          </button>

          <button
            onClick={() => setActiveTab("allocator")}
            className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl font-bold text-xs transition ${
              activeTab === "allocator"
                ? "bg-amber-500 text-gray-950 shadow-md glow-gold"
                : "bg-gray-900/60 hover:bg-gray-800 text-gray-400 hover:text-white border border-gray-800"
            }`}
          >
            <Cpu className="w-4 h-4" />
            <span>3. Resource Allocator (DP / Greedy)</span>
          </button>

          <button
            onClick={() => setActiveTab("performance")}
            className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl font-bold text-xs transition ${
              activeTab === "performance"
                ? "bg-amber-500 text-gray-950 shadow-md glow-gold"
                : "bg-gray-900/60 hover:bg-gray-800 text-gray-400 hover:text-white border border-gray-800"
            }`}
          >
            <BarChart3 className="w-4 h-4" />
            <span>4. Performance & Efficiency Lab</span>
          </button>
        </div>

        {/* Tab Views */}
        <div className="pt-2">
          {activeTab === "queue" && (
            <ServiceRequestQueue
              requests={requests}
              onDispatch={handleDispatch}
              onCreateRequest={handleCreateRequest}
            />
          )}

          {activeTab === "map" && (
            <CampusMapRouteEngine />
          )}

          {activeTab === "allocator" && (
            <ResourceAllocator requests={requests} />
          )}

          {activeTab === "performance" && (
            <PerformanceLab />
          )}
        </div>

      </main>

      {/* Footer */}
      <footer className="border-t border-gray-900 bg-[#0B0C10] py-4 text-center text-xs text-gray-500">
        <p>University of Ghana Campus Service Operations Optimizer (UG-CSOO) • DCIT 204/308 Joint DSA Project</p>
      </footer>
    </div>
  );
}
