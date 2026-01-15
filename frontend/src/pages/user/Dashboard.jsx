import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { orderAPI, productAPI, farmerAPI } from '../../utils/api';
import { getUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Loading from '../../components/common/Loading';
import './Dashboard.css';

const Dashboard = () => {
    const user = getUser();
    const [stats, setStats] = useState({
        totalOrders: 0,
        pendingOrders: 0,
        deliveredOrders: 0
    });
    const [recentOrders, setRecentOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isFarmer, setIsFarmer] = useState(false);
    const [checkingFarmer, setCheckingFarmer] = useState(true);

    useEffect(() => {
        fetchDashboardData();
        checkFarmerStatus();
    }, []);

    const checkFarmerStatus = async () => {
        try {
            const response = await farmerAPI.isFarmer(user.id);
            if (response.data.status === 'success' && response.data.data) {
                setIsFarmer(true);
            }
        } catch (err) {
            console.error('Failed to check farmer status:', err);
        } finally {
            setCheckingFarmer(false);
        }
    };

    const fetchDashboardData = async () => {
        try {
            setLoading(true);
            const ordersResponse = await orderAPI.getByUser(user.id);

            if (ordersResponse.status === 'success') {
                const orders = ordersResponse.data || [];
                setStats({
                    totalOrders: orders.length,
                    pendingOrders: orders.filter(o => o.status === 'PENDING').length,
                    deliveredOrders: orders.filter(o => o.status === 'DELIVERED').length
                });
                setRecentOrders(orders.slice(0, 5));
            }
        } catch (err) {
            console.error('Failed to fetch dashboard data:', err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Loading fullPage text="Loading dashboard..." />;

    return (
        <div className="dashboard-page">
            <div className="container">
                <div className="dashboard-header">
                    <h1>Welcome back, {user?.name}!</h1>
                    <p>Here's what's happening with your account</p>
                </div>

                {/* Stats Cards */}
                <div className="stats-grid">
                    <Card className="stat-card">
                        <div className="stat-icon">📦</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.totalOrders}</div>
                            <div className="stat-label">Total Orders</div>
                        </div>
                    </Card>

                    <Card className="stat-card">
                        <div className="stat-icon">⏳</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.pendingOrders}</div>
                            <div className="stat-label">Pending Orders</div>
                        </div>
                    </Card>

                    <Card className="stat-card">
                        <div className="stat-icon">✅</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.deliveredOrders}</div>
                            <div className="stat-label">Delivered</div>
                        </div>
                    </Card>
                </div>

                {/* Become a Farmer CTA */}
                {!checkingFarmer && !isFarmer && (
                    <Card className="farmer-cta-card" style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white', marginBottom: '2rem' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <div>
                                <h3 style={{ color: 'white', marginBottom: '0.5rem' }}>🌾 Become a Farmer</h3>
                                <p style={{ color: 'rgba(255,255,255,0.9)', marginBottom: '1rem' }}>
                                    Start selling your farm products and reach thousands of buyers
                                </p>
                                <Link
                                    to="/become-farmer"
                                    className="action-btn"
                                    style={{ background: 'white', color: '#667eea', padding: '0.75rem 1.5rem', borderRadius: '0.5rem', textDecoration: 'none', display: 'inline-block', fontWeight: '600' }}
                                >
                                    Get Started →
                                </Link>
                            </div>
                            <div style={{ fontSize: '4rem' }}>🚜</div>
                        </div>
                    </Card>
                )}

                {!checkingFarmer && isFarmer && (
                    <Card className="farmer-dashboard-card" style={{ background: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)', color: 'white', marginBottom: '2rem' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <div>
                                <h3 style={{ color: 'white', marginBottom: '0.5rem' }}>👨‍🌾 Farmer Dashboard</h3>
                                <p style={{ color: 'rgba(255,255,255,0.9)', marginBottom: '1rem' }}>
                                    Manage your farm, products, and orders
                                </p>
                                <Link
                                    to="/farmer/dashboard"
                                    className="action-btn"
                                    style={{ background: 'white', color: '#11998e', padding: '0.75rem 1.5rem', borderRadius: '0.5rem', textDecoration: 'none', display: 'inline-block', fontWeight: '600', marginRight: '1rem' }}
                                >
                                    Go to Dashboard →
                                </Link>
                                <Link
                                    to="/farmer/profile"
                                    className="action-btn"
                                    style={{ background: 'rgba(255,255,255,0.2)', color: 'white', padding: '0.75rem 1.5rem', borderRadius: '0.5rem', textDecoration: 'none', display: 'inline-block', fontWeight: '600' }}
                                >
                                    View Profile
                                </Link>
                            </div>
                            <div style={{ fontSize: '4rem' }}>🌾</div>
                        </div>
                    </Card>
                )}

                {/* Quick Actions */}
                <Card className="quick-actions-card">
                    <h3>Quick Actions</h3>
                    <div className="actions-grid">
                        <Link to="/marketplace" className="action-btn">
                            <span className="action-icon">🛒</span>
                            <span>Browse Products</span>
                        </Link>
                        <Link to="/user/cart" className="action-btn">
                            <span className="action-icon">🛍️</span>
                            <span>View Cart</span>
                        </Link>
                        <Link to="/user/orders" className="action-btn">
                            <span className="action-icon">📋</span>
                            <span>My Orders</span>
                        </Link>
                        <Link to="/user/profile" className="action-btn">
                            <span className="action-icon">👤</span>
                            <span>Profile</span>
                        </Link>
                    </div>
                </Card>

                {/* Recent Orders */}
                <Card className="recent-orders-card">
                    <div className="card-header">
                        <h3>Recent Orders</h3>
                        <Link to="/user/orders" className="view-all-link">View All</Link>
                    </div>

                    {recentOrders.length === 0 ? (
                        <p className="no-data">No orders yet</p>
                    ) : (
                        <div className="orders-list">
                            {recentOrders.map((order) => (
                                <div key={order.id} className="order-item">
                                    <div className="order-info">
                                        <div className="order-id">Order #{order.id}</div>
                                        <div className="order-date">
                                            {new Date(order.orderDate).toLocaleDateString()}
                                        </div>
                                    </div>
                                    <div className="order-status">
                                        <span className={`status-badge status-${order.status.toLowerCase()}`}>
                                            {order.status}
                                        </span>
                                    </div>
                                    <div className="order-total">₹{order.totalPrice?.toFixed(2)}</div>
                                </div>
                            ))}
                        </div>
                    )}
                </Card>
            </div>
        </div>
    );
};

export default Dashboard;
