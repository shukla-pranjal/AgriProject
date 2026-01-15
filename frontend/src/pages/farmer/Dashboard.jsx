import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { productAPI } from '../../utils/api';
import { getUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Loading from '../../components/common/Loading';
import '../user/Dashboard.css';

const FarmerDashboard = () => {
    const user = getUser();
    const [stats, setStats] = useState({
        totalProducts: 0,
        availableProducts: 0,
        totalOrders: 0
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                setLoading(true);
                const productsResponse = await productAPI.getByFarmer(user.farmerId);

                if (productsResponse.status === 'success') {
                    const products = productsResponse.data || [];
                    setStats({
                        totalProducts: products.length,
                        availableProducts: products.filter(p => p.available).length,
                        totalOrders: 0 // Would need order endpoint for farmer
                    });
                }
            } catch {
                console.error('Failed to fetch dashboard data');
            } finally {
                setLoading(false);
            }
        };

        fetchDashboardData();
    }, [user.farmerId]);

    if (loading) return <Loading fullPage text="Loading dashboard..." />;

    return (
        <div className="dashboard-page">
            <div className="container">
                <div className="dashboard-header">
                    <h1>Farmer Dashboard</h1>
                    <p>Manage your farm and products</p>
                </div>

                <div className="stats-grid">
                    <Card className="stat-card">
                        <div className="stat-icon">📦</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.totalProducts}</div>
                            <div className="stat-label">Total Products</div>
                        </div>
                    </Card>

                    <Card className="stat-card">
                        <div className="stat-icon">✅</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.availableProducts}</div>
                            <div className="stat-label">Available Products</div>
                        </div>
                    </Card>

                    <Card className="stat-card">
                        <div className="stat-icon">📊</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.totalOrders}</div>
                            <div className="stat-label">Total Orders</div>
                        </div>
                    </Card>
                </div>

                <Card className="quick-actions-card">
                    <h3>Quick Actions</h3>
                    <div className="actions-grid">
                        <Link to="/farmer/products/add" className="action-btn">
                            <span className="action-icon">➕</span>
                            <span>Add Product</span>
                        </Link>
                        <Link to="/farmer/products" className="action-btn">
                            <span className="action-icon">📦</span>
                            <span>My Products</span>
                        </Link>
                        <Link to="/farmer/profile" className="action-btn">
                            <span className="action-icon">🏡</span>
                            <span>Farm Profile</span>
                        </Link>
                        <Link to="/marketplace" className="action-btn">
                            <span className="action-icon">🛒</span>
                            <span>Browse Market</span>
                        </Link>
                    </div>
                </Card>
            </div>
        </div>
    );
};

export default FarmerDashboard;
