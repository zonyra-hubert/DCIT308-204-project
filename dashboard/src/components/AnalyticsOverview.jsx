import React from 'react';
import { MapPin, FileText, GitFork, Truck, ArrowUpRight, CheckCircle2, Clock } from 'lucide-react';

export default function AnalyticsOverview({ locationsCount, requests, roadsCount, resourcesCount }) {
  const pendingCount = requests.filter(r => r.status === 'PENDING').length;
  const dispatchedCount = requests.filter(r => r.status === 'DISPATCHED').length;

  const stats = [
    {
      title: "Campus Locations",
      value: `${locationsCount} Nodes`,
      subtext: "Traditional & Diaspora Hostels, Depts & Hubs",
      icon: MapPin,
      color: "from-amber-500/20 to-yellow-600/10",
      borderColor: "border-amber-500/30",
      iconColor: "text-amber-400"
    },
    {
      title: "Active Service Requests",
      value: `${requests.length} Total`,
      subtext: `${pendingCount} Pending • ${dispatchedCount} Dispatched`,
      icon: FileText,
      color: "from-amber-500/20 to-yellow-600/10",
      borderColor: "border-amber-500/30",
      iconColor: "text-amber-400"
    },
    {
      title: "Connected Road Segments",
      value: `${roadsCount} Segments`,
      subtext: `Road Penalty Factor: 43 • Meter Distances`,
      icon: GitFork,
      color: "from-amber-500/20 to-yellow-600/10",
      borderColor: "border-amber-500/30",
      iconColor: "text-amber-400"
    },
    {
      title: "Dispatchable Resources",
      value: `${resourcesCount} Units`,
      subtext: "Technicians, IT Officers & Shuttle Fleet",
      icon: Truck,
      color: "from-amber-500/20 to-yellow-600/10",
      borderColor: "border-amber-500/30",
      iconColor: "text-amber-400"
    }
  ];

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      {stats.map((stat, idx) => {
        const IconComponent = stat.icon;
        return (
          <div
            key={idx}
            className="glass-card hover:glass-card-gold p-5 rounded-2xl border transition-all duration-300 transform hover:-translate-y-0.5 group"
          >
            <div className="flex items-center justify-between mb-3">
              <span className="text-xs font-semibold uppercase tracking-wider text-gray-400 group-hover:text-amber-400 transition-colors">
                {stat.title}
              </span>
              <div className={`p-2.5 rounded-xl bg-gradient-to-br ${stat.color} border ${stat.borderColor}`}>
                <IconComponent className={`w-5 h-5 ${stat.iconColor}`} />
              </div>
            </div>
            
            <div className="flex items-baseline justify-between">
              <div className="text-2xl font-black text-white tracking-tight">
                {stat.value}
              </div>
            </div>
            
            <p className="text-xs text-gray-400 mt-2 font-medium flex items-center gap-1">
              <span>{stat.subtext}</span>
            </p>
          </div>
        );
      })}
    </div>
  );
}
