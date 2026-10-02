import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  LayoutDashboard, 
  ShoppingCart, 
  Settings,
  Bell,
  Search,
  TrendingUp,
  Box,
  Plus,
  Trash2,
  CheckCircle,
  Coffee,
  X,
  Printer
} from 'lucide-react';

const SurfaceCard = ({ children, delay = 0, className = '' }) => (
  <motion.div
    initial={{ opacity: 0, y: 15 }}
    animate={{ opacity: 1, y: 0 }}
    transition={{ duration: 0.5, delay, ease: [0.16, 1, 0.3, 1] }}
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
  const [activeTab, setActiveTab] = useState('overview');
  
  // Application State
  const [cart, setCart] = useState({});
  const [searchQuery, setSearchQuery] = useState('');
  const [billOrder, setBillOrder] = useState(null); // Used to show the bill modal
  const [toast, setToast] = useState(null);
  const [justAdded, setJustAdded] = useState({});

  const menuItems = [
    { id: 'F101', name: 'Burger - Classic Beef', price: 8.99, category: 'Main' },
    { id: 'F102', name: 'Pizza - Margherita Large', price: 12.50, category: 'Main' },
    { id: 'F103', name: 'Beverage - Iced Tea', price: 2.50, category: 'Drinks' },
    { id: 'F104', name: 'French Fries', price: 3.99, category: 'Sides' },
    { id: 'F105', name: 'Chocolate Shake', price: 4.50, category: 'Drinks' },
    { id: 'F106', name: 'Caesar Salad', price: 6.50, category: 'Sides' },
  ];

  const [orders, setOrders] = useState([
    { 
      id: 'ORD-1001', 
      name: 'Walk-in Customer', 
      date: 'Today, 10:23 AM', 
      status: 'Completed', 
      amount: 12.98,
      items: { 'F101': 1, 'F104': 1 }
    }
  ]);

  const cartTotalItems = Object.values(cart).reduce((a, b) => a + b, 0);

  const navItems = [
    { id: 'overview', label: 'Overview', icon: LayoutDashboard },
    { id: 'menu', label: 'Food Menu', icon: Coffee },
    { id: 'cart', label: `Cart (${cartTotalItems})`, icon: ShoppingCart },
    { id: 'orders', label: 'Orders', icon: Box },
  ];

  // Logic
  const addToCart = (item) => {
    setCart(prev => ({ ...prev, [item.id]: (prev[item.id] || 0) + 1 }));
    setJustAdded(prev => ({ ...prev, [item.id]: true }));
    setToast(`${item.name} added to cart!`);
    setTimeout(() => {
      setJustAdded(prev => ({ ...prev, [item.id]: false }));
    }, 1000);
  };

  useEffect(() => {
    if (toast) {
      const timer = setTimeout(() => setToast(null), 3000);
      return () => clearTimeout(timer);
    }
  }, [toast]);

  const removeFromCart = (itemId) => {
    setCart(prev => {
      const newCart = { ...prev };
      if (newCart[itemId] > 1) {
        newCart[itemId] -= 1;
      } else {
        delete newCart[itemId];
      }
      return newCart;
    });
  };

  const calculateSubtotal = () => {
    return Object.entries(cart).reduce((total, [id, qty]) => {
      const item = menuItems.find(m => m.id === id);
      return total + (item.price * qty);
    }, 0);
  };

  const handleCheckout = () => {
    const total = calculateSubtotal();
    if (total === 0) return;

    const newOrder = {
      id: `ORD-${1000 + orders.length + 1}`,
      name: 'Walk-in Customer',
      date: new Date().toLocaleString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true, month: 'short', day: 'numeric' }),
      status: 'Completed',
      amount: total,
      items: { ...cart }
    };

    setOrders([newOrder, ...orders]);
    setCart({});
    
    // Show the bill generation modal instead of just going to orders
    setBillOrder(newOrder);
    setToast("Checkout successful! Generating Bill...");
  };

  const totalRevenue = orders.reduce((sum, order) => sum + order.amount, 0);

  // Search filtering logic
  const filteredMenu = menuItems.filter(item => item.name.toLowerCase().includes(searchQuery.toLowerCase()) || item.category.toLowerCase().includes(searchQuery.toLowerCase()));
  const filteredOrders = orders.filter(order => order.id.toLowerCase().includes(searchQuery.toLowerCase()) || order.name.toLowerCase().includes(searchQuery.toLowerCase()));

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
                onClick={() => { setActiveTab(item.id); setSearchQuery(''); }}
              >
                {isActive && (
                  <motion.div 
                    layoutId="active-pill"
                    className="active-pill"
                    transition={{ type: "spring", stiffness: 400, damping: 30 }}
                  />
                )}
                <span style={{ position: 'relative', zIndex: 1, display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <item.icon size={16} />
                  {item.label}
                  {item.id === 'cart' && cartTotalItems > 0 && (
                    <motion.div
                      key={cartTotalItems}
                      initial={{ scale: 1.5, color: '#ef4444' }}
                      animate={{ scale: 1, color: isActive ? 'var(--text-primary)' : 'var(--text-secondary)' }}
                      transition={{ type: 'spring', stiffness: 300 }}
                      style={{ position: 'absolute', right: -4, top: -4, background: '#ef4444', width: 6, height: 6, borderRadius: '50%' }}
                    />
                  )}
                </span>
              </button>
            );
          })}
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '16px', paddingRight: '8px' }}>
          {/* Functional Search Bar */}
          <div style={{ position: 'relative' }}>
            <Search size={14} style={{ position: 'absolute', left: '10px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
            <input 
              type="text" 
              placeholder={`Search ${activeTab}...`}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={{
                background: 'var(--hover-bg)',
                border: '1px solid var(--border-color)',
                padding: '6px 12px 6px 32px',
                borderRadius: '99px',
                outline: 'none',
                fontSize: '0.85rem',
                width: '160px',
                transition: 'all 0.2s ease',
                color: 'var(--text-primary)'
              }}
              onFocus={(e) => { e.target.style.width = '200px'; e.target.style.background = 'var(--surface-color)'; e.target.style.borderColor = 'var(--text-secondary)'; }}
              onBlur={(e) => { e.target.style.width = '160px'; e.target.style.background = 'var(--hover-bg)'; e.target.style.borderColor = 'var(--border-color)'; }}
            />
          </div>

          <div style={{ width: '28px', height: '28px', borderRadius: '50%', background: 'var(--border-color)', backgroundImage: 'url("https://i.pravatar.cc/150?img=11")', backgroundSize: 'cover' }} />
        </div>
      </motion.nav>

      {/* Main Content */}
      <main className="main-container">
        <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '32px' }}>
          <div>
            <motion.h1 initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }} style={{ margin: 0 }}>
              {activeTab.charAt(0).toUpperCase() + activeTab.slice(1)}
            </motion.h1>
            <motion.p className="text-muted" initial={{ opacity: 0 }} animate={{ opacity: 1 }} style={{ marginTop: '8px', fontSize: '0.95rem' }}>
              {searchQuery ? `Searching for "${searchQuery}"` : (
                <>
                  {activeTab === 'overview' && "Here's what's happening with your store today."}
                  {activeTab === 'menu' && "Browse and add items to your cart."}
                  {activeTab === 'cart' && "Review your items before checkout."}
                  {activeTab === 'orders' && "View your recent transaction history."}
                </>
              )}
            </motion.p>
          </div>
          
          {activeTab === 'menu' && (
            <motion.button className="btn" initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }} onClick={() => setActiveTab('cart')}>
              <ShoppingCart size={16} /> Go to Cart ({cartTotalItems})
            </motion.button>
          )}
        </header>

        {/* Dynamic Content area */}
        <AnimatePresence mode="wait">
          
          {/* OVERVIEW TAB */}
          {activeTab === 'overview' && (
            <motion.div key="overview" initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }} transition={{ duration: 0.2 }}>
              <div className="dashboard-grid">
                <StatCard title="Total Revenue" value={`$${totalRevenue.toFixed(2)}`} icon={TrendingUp} trend="+12.5%" delay={0.1} />
                <StatCard title="Total Orders" value={orders.length} icon={ShoppingCart} trend="+5.2%" delay={0.15} />
              </div>

              <SurfaceCard delay={0.2}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
                  <h3 style={{ fontSize: '1.2rem', margin: 0 }}>Recent Transactions</h3>
                  <button className="btn outline" style={{ padding: '4px 12px', fontSize: '0.85rem' }} onClick={() => setActiveTab('orders')}>View All</button>
                </div>
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>Customer</th>
                      <th>Date</th>
                      <th>Status</th>
                      <th style={{ textAlign: 'center' }}>Bill</th>
                      <th style={{ textAlign: 'right' }}>Amount</th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders.slice(0, 5).map((order) => (
                      <tr key={order.id}>
                        <td style={{ fontWeight: 500, color: 'var(--text-primary)' }}>{order.id}</td>
                        <td style={{ color: 'var(--text-secondary)' }}>{order.name}</td>
                        <td style={{ color: 'var(--text-secondary)' }}>{order.date}</td>
                        <td><span className="badge success">{order.status}</span></td>
                        <td style={{ textAlign: 'center' }}>
                           <button onClick={() => setBillOrder(order)} style={{ background: 'none', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer', textDecoration: 'underline' }}>View</button>
                        </td>
                        <td style={{ textAlign: 'right', fontWeight: 500 }}>${order.amount.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </SurfaceCard>
            </motion.div>
          )}

          {/* MENU TAB */}
          {activeTab === 'menu' && (
            <motion.div key="menu" initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }} transition={{ duration: 0.2 }}>
              {filteredMenu.length === 0 ? (
                 <div style={{ padding: '40px', textAlign: 'center' }}>
                   <p className="text-muted">No menu items match your search.</p>
                 </div>
              ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }}>
                  {filteredMenu.map((item, index) => (
                    <SurfaceCard key={item.id} delay={index * 0.05}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '16px' }}>
                        <span className="badge neutral">{item.category}</span>
                        <span style={{ fontWeight: 600 }}>${item.price.toFixed(2)}</span>
                      </div>
                      <h3 style={{ fontSize: '1.1rem', marginBottom: '4px' }}>{item.name}</h3>
                      <p className="text-muted" style={{ fontSize: '0.85rem', marginBottom: '24px' }}>Item ID: {item.id}</p>
                      <button 
                        className={`btn ${justAdded[item.id] ? '' : 'outline'}`} 
                        style={{ 
                          width: '100%', 
                          background: justAdded[item.id] ? '#10b981' : '', 
                          color: justAdded[item.id] ? '#fff' : '', 
                          borderColor: justAdded[item.id] ? '#10b981' : '' 
                        }} 
                        onClick={() => addToCart(item)}
                      >
                        {justAdded[item.id] ? <CheckCircle size={16} /> : <Plus size={16} />} 
                        {justAdded[item.id] ? ' Added!' : ' Add to Cart'}
                      </button>
                    </SurfaceCard>
                  ))}
                </div>
              )}
            </motion.div>
          )}

          {/* CART TAB */}
          {activeTab === 'cart' && (
            <motion.div key="cart" initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }} transition={{ duration: 0.2 }}>
              {Object.keys(cart).length === 0 ? (
                <div style={{ padding: '60px', textAlign: 'center', background: 'var(--surface-color)', borderRadius: '16px', border: '1px dashed var(--border-color)' }}>
                  <ShoppingCart size={48} color="var(--border-color)" style={{ margin: '0 auto 16px' }} />
                  <h3>Your cart is empty</h3>
                  <p className="text-muted" style={{ marginTop: '8px', marginBottom: '24px' }}>Looks like you haven't added any food items yet.</p>
                  <button className="btn" onClick={() => setActiveTab('menu')}>Go to Menu</button>
                </div>
              ) : (
                <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '24px' }}>
                  <SurfaceCard>
                    <h3 style={{ marginBottom: '20px' }}>Cart Items</h3>
                    <table className="data-table">
                      <thead>
                        <tr>
                          <th>Item</th>
                          <th>Price</th>
                          <th>Qty</th>
                          <th style={{ textAlign: 'right' }}>Total</th>
                          <th></th>
                        </tr>
                      </thead>
                      <tbody>
                        {Object.entries(cart).map(([id, qty]) => {
                          const item = menuItems.find(m => m.id === id);
                          if (!item) return null;
                          return (
                            <tr key={id}>
                              <td style={{ fontWeight: 500 }}>{item.name}</td>
                              <td style={{ color: 'var(--text-secondary)' }}>${item.price.toFixed(2)}</td>
                              <td>{qty}</td>
                              <td style={{ textAlign: 'right', fontWeight: 500 }}>${(item.price * qty).toFixed(2)}</td>
                              <td style={{ textAlign: 'right' }}>
                                <button onClick={() => removeFromCart(id)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer' }}>
                                  <Trash2 size={16} />
                                </button>
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </SurfaceCard>

                  <SurfaceCard>
                    <h3 style={{ marginBottom: '20px' }}>Order Summary</h3>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                      <span className="text-muted">Subtotal</span>
                      <span style={{ fontWeight: 500 }}>${calculateSubtotal().toFixed(2)}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '24px', paddingBottom: '24px', borderBottom: '1px solid var(--border-color)' }}>
                      <span className="text-muted">Tax (0%)</span>
                      <span style={{ fontWeight: 500 }}>$0.00</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '32px' }}>
                      <span style={{ fontWeight: 600, fontSize: '1.2rem' }}>Total</span>
                      <span style={{ fontWeight: 600, fontSize: '1.2rem' }}>${calculateSubtotal().toFixed(2)}</span>
                    </div>
                    <button className="btn" style={{ width: '100%', padding: '14px' }} onClick={handleCheckout}>
                      <CheckCircle size={18} /> Checkout & Generate Bill
                    </button>
                  </SurfaceCard>
                </div>
              )}
            </motion.div>
          )}

          {/* ORDERS TAB */}
          {activeTab === 'orders' && (
            <motion.div key="orders" initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }} transition={{ duration: 0.2 }}>
               <SurfaceCard>
                 {filteredOrders.length === 0 ? (
                    <div style={{ padding: '40px', textAlign: 'center' }}>
                      <p className="text-muted">No orders match your search.</p>
                    </div>
                 ) : (
                  <table className="data-table">
                    <thead>
                      <tr>
                        <th>Order ID</th>
                        <th>Customer</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th style={{ textAlign: 'center' }}>Bill</th>
                        <th style={{ textAlign: 'right' }}>Amount</th>
                      </tr>
                    </thead>
                    <tbody>
                      {filteredOrders.map((order) => (
                        <tr key={order.id}>
                          <td style={{ fontWeight: 500, color: 'var(--text-primary)' }}>{order.id}</td>
                          <td style={{ color: 'var(--text-secondary)' }}>{order.name}</td>
                          <td style={{ color: 'var(--text-secondary)' }}>{order.date}</td>
                          <td><span className="badge success">{order.status}</span></td>
                          <td style={{ textAlign: 'center' }}>
                            <button onClick={() => setBillOrder(order)} style={{ background: 'none', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer', textDecoration: 'underline' }}>View</button>
                          </td>
                          <td style={{ textAlign: 'right', fontWeight: 500 }}>${order.amount.toFixed(2)}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                 )}
              </SurfaceCard>
            </motion.div>
          )}

        </AnimatePresence>

        {/* Global Toast Notification */}
        <AnimatePresence>
          {toast && (
            <motion.div
              initial={{ opacity: 0, y: 50, scale: 0.9 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              exit={{ opacity: 0, y: 20, scale: 0.9 }}
              style={{
                position: 'fixed',
                bottom: '32px',
                right: '32px',
                background: 'var(--primary-color)',
                color: 'white',
                padding: '12px 24px',
                borderRadius: '8px',
                boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
                display: 'flex',
                alignItems: 'center',
                gap: '12px',
                zIndex: 1000
              }}
            >
              <CheckCircle size={18} color="#10b981" />
              <span style={{ fontWeight: 500, fontSize: '0.95rem' }}>{toast}</span>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Bill Modal */}
        <AnimatePresence>
          {billOrder && (
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              style={{
                position: 'fixed',
                top: 0, left: 0, right: 0, bottom: 0,
                background: 'rgba(0,0,0,0.4)',
                backdropFilter: 'blur(4px)',
                zIndex: 2000,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                padding: '24px'
              }}
              onClick={() => setBillOrder(null)}
            >
              <motion.div
                initial={{ opacity: 0, scale: 0.95, y: 20 }}
                animate={{ opacity: 1, scale: 1, y: 0 }}
                exit={{ opacity: 0, scale: 0.95, y: 20 }}
                onClick={(e) => e.stopPropagation()}
                style={{
                  background: '#fff',
                  padding: '40px',
                  borderRadius: '16px',
                  boxShadow: '0 24px 48px rgba(0,0,0,0.2)',
                  width: '100%',
                  maxWidth: '500px',
                  maxHeight: '90vh',
                  overflowY: 'auto',
                  display: 'flex',
                  flexDirection: 'column',
                  position: 'relative'
                }}
              >
                <button 
                  onClick={() => setBillOrder(null)}
                  style={{ position: 'absolute', top: '24px', right: '24px', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-secondary)' }}
                >
                  <X size={20} />
                </button>
                
                <div style={{ textAlign: 'center', borderBottom: '1px dashed var(--border-color)', paddingBottom: '24px', marginBottom: '24px' }}>
                  <div style={{ width: '48px', height: '48px', background: 'var(--primary-color)', color: 'white', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 16px', fontSize: '1.5rem', fontWeight: 'bold' }}>
                    O
                  </div>
                  <h2 style={{ margin: '0 0 8px 0', fontSize: '1.5rem', color: 'var(--text-primary)' }}>OrderMaster Receipt</h2>
                  <p className="text-muted" style={{ margin: 0, fontSize: '0.9rem' }}>Thank you for your purchase!</p>
                </div>

                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', fontSize: '0.9rem' }}>
                  <span className="text-muted">Order ID:</span>
                  <span style={{ fontWeight: 500, color: 'var(--text-primary)' }}>{billOrder.id}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '24px', fontSize: '0.9rem' }}>
                  <span className="text-muted">Date:</span>
                  <span style={{ fontWeight: 500, color: 'var(--text-primary)' }}>{billOrder.date}</span>
                </div>

                <div style={{ marginBottom: '24px' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', borderBottom: '1px solid var(--border-color)', paddingBottom: '8px', marginBottom: '12px', fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase' }}>
                    <span>Item</span>
                    <span>Total</span>
                  </div>
                  {Object.entries(billOrder.items || {}).map(([id, qty]) => {
                    const item = menuItems.find(m => m.id === id);
                    if (!item) return null;
                    return (
                      <div key={id} style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', fontSize: '0.95rem', color: 'var(--text-primary)' }}>
                        <span>{qty}x {item.name}</span>
                        <span>${(item.price * qty).toFixed(2)}</span>
                      </div>
                    );
                  })}
                </div>

                <div style={{ borderTop: '1px dashed var(--border-color)', paddingTop: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', color: 'var(--text-primary)' }}>
                  <span style={{ fontSize: '1.2rem', fontWeight: 600 }}>Total Paid</span>
                  <span style={{ fontSize: '1.5rem', fontWeight: 700 }}>${billOrder.amount.toFixed(2)}</span>
                </div>

                <div style={{ display: 'flex', gap: '12px', marginTop: '40px' }}>
                  <button className="btn outline" style={{ flex: 1 }} onClick={() => setBillOrder(null)}>Close</button>
                  <button className="btn" style={{ flex: 1 }} onClick={() => { window.print(); setBillOrder(null); }}>
                    <Printer size={16} /> Print Bill
                  </button>
                </div>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>

      </main>
    </div>
  );
}
