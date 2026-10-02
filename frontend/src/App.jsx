import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  LayoutDashboard, 
  Package, 
  Users, 
  ShoppingCart, 
  Settings,
  Bell,
  Search,
  TrendingUp,
  Box,
  Plus
} from 'lucide-react';

const SurfaceCard = ({ children, delay = 0, className = '' }) => (
  <motion.div
    initial={{ opacity: 0, y: 15 }}
    animate={{ opacity: 1, y: 0 }}
    transition={{ duration: 0.5, delay, ease: [0.16, 1, 0.3, 1] }} // smooth spring-like ease
    className={`surface-card ${className}`}
  >
    {children}
  </motion.div>
);

const StatCard = ({ title, value, icon: Icon, trend, delay }) => (
  <SurfaceCard delay={delay}>
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
      <div>
        <p className="text-muted" style={{ marginBottom: '8px', fontSize: '0.9rem' }}>{title}</p>
        <h3 style={{ fontSize: '2rem', margin: '0' }}>{value}</h3>
      </div>
      <div style={{ color: 'var(--text-secondary)' }}>
        <Icon size={24} />
      </div>
    </div>
    <div style={{ marginTop: '24px', display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.85rem' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', color: '#166534', background: '#f0fdf4', padding: '4px 8px', borderRadius: '6px' }}>
        <TrendingUp size={14} />
        <span style={{ fontWeight: 600 }}>{trend}</span>
      </div>
      <span className="text-muted">vs last month</span>
    </div>
  </SurfaceCard>
);

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  const navItems = [
    { id: 'dashboard', label: 'Overview', icon: LayoutDashboard },
    { id: 'orders', label: 'Orders', icon: ShoppingCart },
    { id: 'inventory', label: 'Inventory', icon: Box },
    { id: 'customers', label: 'Customers', icon: Users },
  ];

  return (
    <div className="app-wrapper">
      {/* Floating Island Navigation */}
      <motion.nav 
        className="floating-nav"
        initial={{ y: -40, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px', paddingLeft: '8px' }}>
          <div style={{ 
            width: '28px', 
            height: '28px', 
            background: 'var(--primary-color)',
            borderRadius: '8px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            fontWeight: '600',
            fontSize: '0.85rem'
          }}>
            O
          </div>
          <h2 style={{ fontSize: '1rem', margin: 0 }}>OrderMaster</h2>
        </div>

        <div className="nav-links">
          {navItems.map((item) => {
            const isActive = activeTab === item.id;
            return (
              <button
                key={item.id}
                className={`nav-item ${isActive ? 'active' : ''}`}
                onClick={() => setActiveTab(item.id)}
              >
                {isActive && (
                  <motion.div 
                    layoutId="active-pill"
                    className="active-pill"
                    transition={{ type: "spring", stiffness: 400, damping: 30 }}
                  />
                )}
                <span style={{ position: 'relative', zIndex: 1, display: 'flex', alignItems: 'center', gap: '6px' }}>
                  {item.label}
                </span>
              </button>
            );
          })}
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '16px', paddingRight: '8px' }}>
          <button style={{ background: 'transparent', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer', display: 'flex' }}>
            <Search size={18} />
          </button>
          <button style={{ background: 'transparent', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer', display: 'flex', position: 'relative' }}>
            <Bell size={18} />
            <span style={{ position: 'absolute', top: -2, right: -2, width: 6, height: 6, background: '#ef4444', borderRadius: '50%' }}></span>
          </button>
          <div style={{ width: '28px', height: '28px', borderRadius: '50%', background: 'var(--border-color)', backgroundImage: 'url("https://i.pravatar.cc/150?img=11")', backgroundSize: 'cover' }} />
        </div>
      </motion.nav>

      {/* Main Content */}
      <main className="main-container">
        <header style={{ 
          display: 'flex', 
          justifyContent: 'space-between', 
          alignItems: 'flex-end',
          marginBottom: '32px'
        }}>
          <div>
            <motion.h1 
              initial={{ opacity: 0, x: -10 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.1 }}
              style={{ margin: 0 }}
            >
              {activeTab === 'dashboard' ? 'Overview' : activeTab.charAt(0).toUpperCase() + activeTab.slice(1)}
            </motion.h1>
            <motion.p 
              className="text-muted" 
              initial={{ opacity: 0 }} 
              animate={{ opacity: 1 }} 
              transition={{ delay: 0.2 }}
              style={{ marginTop: '8px', fontSize: '0.95rem' }}
            >
              Here's what's happening with your store today.
            </motion.p>
          </div>
          
          <motion.div initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }} transition={{ delay: 0.2 }}>
             <button className="btn">
               <Plus size={16} /> New Order
             </button>
          </motion.div>
        </header>

        {/* Dynamic Content area */}
        <AnimatePresence mode="wait">
          {activeTab === 'dashboard' && (
            <motion.div 
              key="dashboard"
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -10 }}
              transition={{ duration: 0.2 }}
            >
              <div className="dashboard-grid">
                <StatCard title="Total Revenue" value="$45,231.89" icon={TrendingUp} trend="+20.1%" delay={0.1} />
                <StatCard title="Active Orders" value="1,340" icon={ShoppingCart} trend="+15.5%" delay={0.15} />
                <StatCard title="Total Customers" value="8,549" icon={Users} trend="+5.2%" delay={0.2} />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr', gap: '24px' }}>
                <SurfaceCard delay={0.25}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                    <h3 style={{ margin: 0, fontSize: '1.2rem' }}>Recent Transactions</h3>
                    <button className="btn outline" style={{ padding: '6px 12px', fontSize: '0.85rem' }}>View All</button>
                  </div>
                  <div style={{ overflowX: 'auto' }}>
                    <table className="data-table">
                      <thead>
                        <tr>
                          <th>Order</th>
                          <th>Customer</th>
                          <th>Date</th>
                          <th>Status</th>
                          <th style={{ textAlign: 'right' }}>Amount</th>
                        </tr>
                      </thead>
                      <tbody>
                        {[
                          { id: 'ORD-001', name: 'Alex Johnson', date: 'Today, 2:45 PM', status: 'Completed', amount: '$124.50' },
                          { id: 'ORD-002', name: 'Maria Garcia', date: 'Today, 1:12 PM', status: 'Processing', amount: '$85.00' },
                          { id: 'ORD-003', name: 'James Smith', date: 'Yesterday', status: 'Pending', amount: '$210.25' },
                          { id: 'ORD-004', name: 'Linda Brown', date: 'Yesterday', status: 'Completed', amount: '$45.00' },
                        ].map((order) => (
                          <tr key={order.id}>
                            <td style={{ fontWeight: 500, color: 'var(--text-primary)' }}>{order.id}</td>
                            <td style={{ color: 'var(--text-secondary)' }}>{order.name}</td>
                            <td style={{ color: 'var(--text-secondary)' }}>{order.date}</td>
                            <td>
                              <span className={`badge ${order.status === 'Completed' ? 'success' : order.status === 'Processing' ? 'neutral' : 'warning'}`}>
                                {order.status}
                              </span>
                            </td>
                            <td style={{ textAlign: 'right', fontWeight: 500 }}>{order.amount}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </SurfaceCard>
              </div>
            </motion.div>
          )}

          {activeTab !== 'dashboard' && (
            <motion.div
              key="other"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              style={{
                padding: '60px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                flexDirection: 'column',
                gap: '16px',
                border: '1px dashed var(--border-color)',
                borderRadius: '16px',
                background: 'var(--surface-color)'
              }}
            >
              <Package size={32} color="var(--text-secondary)" />
              <div>
                <h3 style={{ textAlign: 'center', marginBottom: '8px' }}>{activeTab.charAt(0).toUpperCase() + activeTab.slice(1)}</h3>
                <p className="text-muted" style={{ fontSize: '0.95rem' }}>This module will connect to your Java backend.</p>
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </main>
    </div>
  );
}
