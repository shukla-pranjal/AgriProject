import React, { useState, useEffect } from 'react';
import { userAPI, productAPI, orderAPI } from '../../utils/api';
import Card from '../../components/common/Card';
import Loading from '../../components/common/Loading';
import '../user/Dashboard.css';

const AdminDashboard = () => {
    const [stats, setStats] = useState({
        totalUsers: 0,
        totalProducts: 0,
        totalOrders: 0,
        pendingOrders: 0
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            setLoading(true);

            const [usersRes, productsRes, ordersRes] = await Promise.all([
                userAPI.getAll(),
                productAPI.getAll(),
                orderAPI.getAll()
            ]);

            const users = usersRes.data || [];
            const products = productsRes.data || [];
            const orders = ordersRes.data || [];

            setStats({
                totalUsers: users.length,
                totalProducts: products.length,
                totalOrders: orders.length,
                pendingOrders: orders.filter(o => o.status === 'PENDING').length
            });
        } catch {
            console.error('Failed to fetch dashboard data:');
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Loading fullPage text="Loading dashboard..." />;

    return (
        <div className="dashboard-page">
            <div className="container">
                <div className="dashboard-header">
                    <h1>Admin Dashboard</h1>
                    <p>System overview and management</p>
                </div>

                <div className="stats-grid">
                    <Card className="stat-card">
                        <div className="stat-icon">👥</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.totalUsers}</div>
                            <div className="stat-label">Total Users</div>
                        </div>
                    </Card>

                    <Card className="stat-card">
                        <div className="stat-icon">📦</div>
                        <div className="stat-info">
                            <div className="stat-value">{stats.totalProducts}</div>
                            <div className="stat-label">Total Products</div>
                        </div>
                    </Card>

                    <Card className="stat-card">
                        <div className="stat-icon">📋</div>
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
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
