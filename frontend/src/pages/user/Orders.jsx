import React, { useState, useEffect, useCallback } from 'react';
import { useLocation } from 'react-router-dom';
import { orderAPI } from '../../utils/api';
import { getUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Loading from '../../components/common/Loading';
import './Orders.css';

const STATUS_COLORS = {
    PENDING: 'status-pending',
    PAID: 'status-paid',
    CONFIRMED: 'status-confirmed',
    SHIPPED: 'status-shipped',
    DELIVERED: 'status-delivered',
    CANCELLED: 'status-cancelled'
};

const Orders = () => {
    const location = useLocation();
    const user = getUser();
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const [message, setMessage] = useState(location.state?.message || '');

    const fetchOrders = useCallback(async () => {
        try {
            setLoading(true);
            const response = await orderAPI.getByUser(user.id);
            if (response.status === 'success') {
                setOrders(response.data || []);
            }
        } catch {
            console.error('Failed to fetch orders');
        } finally {
            setLoading(false);
        }
    }, [user.id]);

    useEffect(() => {
        fetchOrders();
        if (message) {
            setTimeout(() => setMessage(''), 5000);
        }
    }, [fetchOrders, message]);

    const handleCancelOrder = async (orderId) => {
        if (!window.confirm('Are you sure you want to cancel this order?')) return;

        try {
            await orderAPI.cancel(orderId);
            fetchOrders();
        } catch {
            console.error('Failed to cancel order:');
        }
    };

    const handleReorder = async (orderId) => {
        try {
            await orderAPI.reorder(orderId);
            fetchOrders();
        } catch {
            console.error('Failed to reorder:');
        }
    };

    const filteredOrders = filter === 'ALL'
        ? orders
        : orders.filter(order => order.status === filter);

    if (loading) return <Loading fullPage text="Loading orders..." />;

    return (
        <div className="orders-page">
            <div className="container">
                <h1>My Orders</h1>

                {message && <div className="alert alert-success">{message}</div>}

                {/* Filter Tabs */}
                <div className="order-filters">
                    {['ALL', 'PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'].map(status => (
                        <button
                            key={status}
                            className={`filter-btn ${filter === status ? 'active' : ''}`}
                            onClick={() => setFilter(status)}
                        >
                            {status}
                        </button>
                    ))}
                </div>

                {/* Orders List */}
                {filteredOrders.length === 0 ? (
                    <Card className="no-orders">
                        <div className="no-orders-icon">📦</div>
                        <h2>No Orders Found</h2>
                        <p>You haven't placed any orders yet.</p>
                        <Button variant="primary" onClick={() => window.location.href = '/marketplace'}>
                            Start Shopping
                        </Button>
                    </Card>
                ) : (
                    <div className="orders-list">
                        {filteredOrders.map((order) => (
                            <Card key={order.id} className="order-card">
                                <div className="order-header">
                                    <div>
                                        <h3>Order #{order.id}</h3>
                                        <p className="order-date">
                                            {new Date(order.orderDate).toLocaleDateString('en-IN', {
                                                year: 'numeric',
                                                month: 'long',
                                                day: 'numeric'
                                            })}
                                        </p>
                                    </div>
                                    <span className={`status-badge ${STATUS_COLORS[order.status]}`}>
                                        {order.status}
                                    </span>
                                </div>

                                <div className="order-items">
                                    {order.items?.map((item, index) => (
                                        <div key={index} className="order-item">
                                            <span>{item.productName || `Product #${item.productId}`}</span>
                                            <span>Qty: {item.quantity}</span>
                                            <span>₹{item.price?.toFixed(2)}</span>
                                        </div>
                                    ))}
                                </div>

                                <div className="order-footer">
                                    <div className="order-total">
                                        <span>Total:</span>
                                        <span className="total-amount">₹{order.totalPrice?.toFixed(2)}</span>
                                    </div>
                                    <div className="order-actions">
                                        {order.status === 'PENDING' && (
                                            <Button
                                                variant="outline"
                                                size="sm"
                                                onClick={() => handleCancelOrder(order.id)}
                                            >
                                                Cancel Order
                                            </Button>
                                        )}
                                        {order.status === 'DELIVERED' && (
                                            <Button
                                                variant="primary"
                                                size="sm"
                                                onClick={() => handleReorder(order.id)}
                                            >
                                                Reorder
                                            </Button>
                                        )}
                                    </div>
                                </div>
                            </Card>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Orders;
