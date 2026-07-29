import React, { useState } from 'react';
import { Search, Filter, Plus, Send, CheckCircle2, Clock, AlertTriangle, AlertCircle, Wrench, Bus, Laptop, BookOpen } from 'lucide-react';
import { LOCATIONS } from '../../data/mockData';

export default function ServiceRequestQueue({ requests, onDispatch, onCreateRequest }) {
  const [searchQuery, setSearchQuery] = useState("");
  const [priorityFilter, setPriorityFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [showAddModal, setShowAddModal] = useState(false);

  // Form state for new request
  const [newLocId, setNewLocId] = useState("LOC-UG-06");
  const [newDesc, setNewDesc] = useState("");
  const [newPriority, setNewPriority] = useState(5);
  const [newBudget, setNewBudget] = useState(150);
  const [newCategory, setNewCategory] = useState("Plumbing");

  const filteredRequests = requests.filter(req => {
    const matchesSearch = req.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          req.locationName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          req.id.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesPriority = priorityFilter === "ALL" || req.priority === Number(priorityFilter);
    const matchesStatus = statusFilter === "ALL" || req.status === statusFilter;
    return matchesSearch && matchesPriority && matchesStatus;
  });

  const handleSubmitNew = (e) => {
    e.preventDefault();
    if (!newDesc.trim()) return;
    const locObj = LOCATIONS.find(l => l.id === newLocId) || LOCATIONS[0];
    const newReq = {
      id: `REQ-UG-${Math.floor(110 + Math.random() * 900)}`,
      locationId: locObj.id,
      locationName: locObj.name,
      description: newDesc,
      priority: Number(newPriority),
      budget: Number(newBudget),
      durationHrs: 2.5,
      status: "PENDING",
      category: newCategory
    };
    onCreateRequest(newReq);
    setNewDesc("");
    setShowAddModal(false);
  };

  const getPriorityBadge = (prio) => {
    switch (prio) {
      case 5:
        return <span className="px-2.5 py-1 rounded-full text-xs font-bold bg-red-500/10 text-red-400 border border-red-500/30 flex items-center gap-1 shadow-sm"><AlertCircle className="w-3 h-3" /> P5 - CRITICAL</span>;
      case 4:
        return <span className="px-2.5 py-1 rounded-full text-xs font-bold bg-amber-500/10 text-amber-400 border border-amber-500/30 flex items-center gap-1"><AlertTriangle className="w-3 h-3" /> P4 - HIGH</span>;
      case 3:
        return <span className="px-2.5 py-1 rounded-full text-xs font-bold bg-yellow-500/10 text-yellow-300 border border-yellow-500/30">P3 - MEDIUM</span>;
      default:
        return <span className="px-2.5 py-1 rounded-full text-xs font-medium bg-gray-800 text-gray-300 border border-gray-700">P{prio} - STANDARD</span>;
    }
  };

  const getCategoryIcon = (cat) => {
    switch (cat) {
      case "Plumbing": return <Wrench className="w-4 h-4 text-blue-400" />;
      case "ICT": return <Laptop className="w-4 h-4 text-purple-400" />;
      case "Shuttle": return <Bus className="w-4 h-4 text-amber-400" />;
      case "Logistics": return <BookOpen className="w-4 h-4 text-emerald-400" />;
      default: return <Wrench className="w-4 h-4 text-amber-400" />;
    }
  };

  return (
    <div className="space-y-6">
      
      {/* Controls Bar */}
      <div className="glass-card p-4 rounded-2xl border border-gray-800 flex flex-col md:flex-row items-center justify-between gap-4">
        
        {/* Search */}
        <div className="relative w-full md:w-80">
          <Search className="w-4 h-4 absolute left-3.5 top-3 text-gray-400" />
          <input
            type="text"
            placeholder="Search tickets, hostels, departments..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-10 pr-4 py-2 bg-gray-900/80 border border-gray-700 rounded-xl text-xs text-white placeholder-gray-500 focus:outline-none focus:border-amber-500 transition"
          />
        </div>

        {/* Filters */}
        <div className="flex flex-wrap items-center gap-3 w-full md:w-auto">
          <div className="flex items-center space-x-2">
            <Filter className="w-3.5 h-3.5 text-gray-400" />
            <span className="text-xs text-gray-400 font-medium">Priority:</span>
            <select
              value={priorityFilter}
              onChange={(e) => setPriorityFilter(e.target.value)}
              className="bg-gray-900 border border-gray-700 rounded-xl text-xs text-gray-200 px-3 py-2 focus:outline-none focus:border-amber-500"
            >
              <option value="ALL">All Priorities (1-5)</option>
              <option value="5">P5 - Critical Only</option>
              <option value="4">P4 - High</option>
              <option value="3">P3 - Medium</option>
            </select>
          </div>

          <div className="flex items-center space-x-2">
            <span className="text-xs text-gray-400 font-medium">Status:</span>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="bg-gray-900 border border-gray-700 rounded-xl text-xs text-gray-200 px-3 py-2 focus:outline-none focus:border-amber-500"
            >
              <option value="ALL">All Statuses</option>
              <option value="PENDING">Pending</option>
              <option value="DISPATCHED">Dispatched</option>
              <option value="COMPLETED">Completed</option>
            </select>
          </div>

          <button
            onClick={() => setShowAddModal(true)}
            className="flex items-center space-x-1.5 px-4 py-2 rounded-xl bg-gradient-to-r from-amber-500 to-yellow-600 text-gray-950 font-bold text-xs hover:brightness-110 transition shadow-md glow-gold"
          >
            <Plus className="w-4 h-4" />
            <span>New Ticket</span>
          </button>
        </div>
      </div>

      {/* Requests Table / Cards */}
      <div className="glass-card rounded-2xl border border-gray-800 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="border-b border-gray-800 bg-gray-900/60 text-gray-400 font-semibold uppercase tracking-wider">
                <th className="py-3.5 px-4">Ticket ID</th>
                <th className="py-3.5 px-4">Location / Zone</th>
                <th className="py-3.5 px-4">Description</th>
                <th className="py-3.5 px-4">Priority</th>
                <th className="py-3.5 px-4">Budget Required</th>
                <th className="py-3.5 px-4">Status</th>
                <th className="py-3.5 px-4 text-right">Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-800/60 text-gray-200">
              {filteredRequests.map((req) => (
                <tr key={req.id} className="hover:bg-gray-800/40 transition">
                  <td className="py-3.5 px-4 font-mono font-bold text-amber-400">
                    {req.id}
                  </td>
                  <td className="py-3.5 px-4">
                    <div className="font-semibold text-white">{req.locationName}</div>
                    <div className="text-[11px] text-gray-400">{req.locationId}</div>
                  </td>
                  <td className="py-3.5 px-4 max-w-xs">
                    <div className="flex items-center space-x-2">
                      {getCategoryIcon(req.category)}
                      <span className="truncate text-gray-100 font-medium">{req.description}</span>
                    </div>
                  </td>
                  <td className="py-3.5 px-4">
                    {getPriorityBadge(req.priority)}
                  </td>
                  <td className="py-3.5 px-4 font-semibold text-amber-300">
                    GHS {req.budget.toFixed(2)}
                  </td>
                  <td className="py-3.5 px-4">
                    {req.status === "PENDING" && (
                      <span className="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-semibold bg-yellow-500/10 text-yellow-400 border border-yellow-500/20">
                        <Clock className="w-3 h-3 mr-1 animate-pulse" /> PENDING
                      </span>
                    )}
                    {req.status === "DISPATCHED" && (
                      <span className="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-semibold bg-blue-500/10 text-blue-400 border border-blue-500/20">
                        <Send className="w-3 h-3 mr-1" /> DISPATCHED
                      </span>
                    )}
                    {req.status === "COMPLETED" && (
                      <span className="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                        <CheckCircle2 className="w-3 h-3 mr-1" /> COMPLETED
                      </span>
                    )}
                  </td>
                  <td className="py-3.5 px-4 text-right">
                    {req.status === "PENDING" ? (
                      <button
                        onClick={() => onDispatch(req.id)}
                        className="px-3 py-1 rounded-lg bg-amber-500/10 hover:bg-amber-500/20 border border-amber-500/40 text-amber-400 font-bold text-xs transition"
                      >
                        Dispatch Crew
                      </button>
                    ) : req.status === "DISPATCHED" ? (
                      <button
                        onClick={() => onDispatch(req.id)}
                        className="px-3 py-1 rounded-lg bg-emerald-500/10 hover:bg-emerald-500/20 border border-emerald-500/40 text-emerald-400 font-bold text-xs transition"
                      >
                        Mark Complete
                      </button>
                    ) : (
                      <span className="text-gray-500 italic text-[11px]">Resolved</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal for New Ticket */}
      {showAddModal && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="glass-card-gold p-6 rounded-2xl max-w-md w-full border border-amber-500/40 shadow-2xl space-y-4">
            <h3 className="text-lg font-bold text-white flex items-center gap-2">
              <Plus className="w-5 h-5 text-amber-400" />
              Create Campus Service Ticket
            </h3>
            <form onSubmit={handleSubmitNew} className="space-y-3 text-xs">
              <div>
                <label className="block text-gray-400 mb-1">Target Location</label>
                <select
                  value={newLocId}
                  onChange={(e) => setNewLocId(e.target.value)}
                  className="w-full bg-gray-900 border border-gray-700 rounded-xl p-2.5 text-white"
                >
                  {LOCATIONS.map(loc => (
                    <option key={loc.id} value={loc.id}>{loc.name} ({loc.region})</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-gray-400 mb-1">Description / Issue</label>
                <input
                  type="text"
                  placeholder="e.g. Electrical breaker trip at Volta Hall"
                  value={newDesc}
                  onChange={(e) => setNewDesc(e.target.value)}
                  required
                  className="w-full bg-gray-900 border border-gray-700 rounded-xl p-2.5 text-white"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-gray-400 mb-1">Priority (1-5)</label>
                  <select
                    value={newPriority}
                    onChange={(e) => setNewPriority(e.target.value)}
                    className="w-full bg-gray-900 border border-gray-700 rounded-xl p-2.5 text-white"
                  >
                    <option value="5">5 - Critical Emergency</option>
                    <option value="4">4 - High Priority</option>
                    <option value="3">3 - Medium</option>
                    <option value="2">2 - Low</option>
                  </select>
                </div>

                <div>
                  <label className="block text-gray-400 mb-1">Budget Required (GHS)</label>
                  <input
                    type="number"
                    value={newBudget}
                    onChange={(e) => setNewBudget(e.target.value)}
                    className="w-full bg-gray-900 border border-gray-700 rounded-xl p-2.5 text-white"
                  />
                </div>
              </div>

              <div>
                <label className="block text-gray-400 mb-1">Category</label>
                <select
                  value={newCategory}
                  onChange={(e) => setNewCategory(e.target.value)}
                  className="w-full bg-gray-900 border border-gray-700 rounded-xl p-2.5 text-white"
                >
                  <option value="Plumbing">Plumbing</option>
                  <option value="Electrical">Electrical</option>
                  <option value="ICT">ICT & AV</option>
                  <option value="Shuttle">Shuttle Logistics</option>
                  <option value="Mechanical">Mechanical</option>
                </select>
              </div>

              <div className="flex justify-end space-x-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowAddModal(false)}
                  className="px-4 py-2 rounded-xl bg-gray-800 text-gray-300 font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 rounded-xl bg-gradient-to-r from-amber-500 to-yellow-600 text-gray-950 font-bold glow-gold"
                >
                  Submit Ticket
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
}
