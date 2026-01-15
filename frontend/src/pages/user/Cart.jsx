import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { cartAPI } from '../../utils/api';
import { getUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Loading from '../../components/common/Loading';
import './Cart.css';

const Cart = () => {
    const navigate = useNavigate();
    const user = getUser();
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [updating, setUpdating] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchCart = async () => {
            try {
                setLoading(true);
                const response = await cartAPI.getByUser(user.id);
                if (response.status === 'success') {
                    setCart(response.data);
                }
            } catch {
                setError('Failed to load cart');
            } finally {
                setLoading(false);
            }
        };

        fetchCart();
    }, [user.id]);

    const updateQuantity = async (productId, newQuantity) => {
        if (newQuantity < 1) return;

        try {
            setUpdating(true);
            const response = await cartAPI.updateItem(cart.id, productId, { quantity: newQuantity });
            if (response.status === 'success') {
                setCart(response.data);
            }
        } catch {
            setError('Failed to update quantity');
        } finally {
            setUpdating(false);
        }
    };

    const increaseQuantity = async (productId) => {
        try {
            setUpdating(true);
            const response = await cartAPI.increaseQuantity(cart.id, productId, 1);
            if (response.status === 'success') {
                setCart(response.data);
            }
        } catch {
            setError('Failed to increase quantity');
        } finally {
            setUpdating(false);
        }
    };

    const decreaseQuantity = async (productId) => {
        try {
            setUpdating(true);
            const response = await cartAPI.decreaseQuantity(cart.id, productId, 1);
            if (response.status === 'success') {
                setCart(response.data);
            }
        } catch {
            setError('Failed to decrease quantity');
        } finally {
            setUpdating(false);
        }
    };

    const removeItem = async (productId) => {
        if (!window.confirm('Remove this item from cart?')) return;

        try {
            setUpdating(true);
            const response = await cartAPI.removeItem(cart.id, productId);
            if (response.status === 'success') {
                setCart(response.data);
            }
        } catch {
            setError('Failed to remove item');
        } finally {
            setUpdating(false);
        }
    };

    const clearCart = async () => {
        if (!window.confirm('Clear all items from cart?')) return;

        try {
            setUpdating(true);
            await cartAPI.clearCart(cart.id);
            setCart({ ...cart, items: [] });
        } catch {
            setError('Failed to clear cart');
        } finally {
            setUpdating(false);
        }
    };

    const calculateSubtotal = () => {
        if (!cart?.items) return 0;
        return cart.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    };

    const calculateTax = () => {
        return calculateSubtotal() * 0.18; // 18% GST
    };

    const calculateTotal = () => {
        return calculateSubtotal() + calculateTax();
    };

    if (loading) return <Loading fullPage text="Loading cart..." />;

    if (!cart || !cart.items || cart.items.length === 0) {
        return (
            <div className="cart-page">
                <div className="container">
                    <Card className="empty-cart">
                        <div className="empty-cart-icon">🛒</div>
                        <h2>Your Cart is Empty</h2>
                        <p>Add some products to get started!</p>
                        <Button variant="primary" onClick={() => navigate('/marketplace')}>
                            Browse Products
                        </Button>
                    </Card>
                </div>
            </div>
        );
    }

    return (
        <div className="cart-page">
            <div className="container">
                <div className="cart-header">
                    <h1>Shopping Cart</h1>
                    <Button variant="outline" onClick={clearCart} disabled={updating}>
                        Clear Cart
                    </Button>
                </div>

                {error && <div className="alert alert-error">{error}</div>}

                <div className="cart-layout">
                    <div className="cart-items">
                        {cart.items.map((item) => (
                            <Card key={item.productId} className="cart-item">
                                <div className="item-image">
                                    {item.productImage ? (
                                        <img src={item.productImage} alt={item.productName} />
                                    ) : (
                                        <div className="item-placeholder">🌾</div>
                                    )}
                                </div>

                                <div className="item-details">
                                    <h3>{item.productName}</h3>
                                    <p className="item-price">₹{item.price} per unit</p>
                                </div>

                                <div className="item-quantity">
                                    <button
                                        className="qty-btn"
                                        onClick={() => decreaseQuantity(item.productId)}
                                        disabled={updating || item.quantity <= 1}
                                    >
                                        −
                                    </button>
                                    <input
                                        type="number"
                                        value={item.quantity}
                                        onChange={(e) => updateQuantity(item.productId, parseInt(e.target.value) || 1)}
                                        min="1"
                                        disabled={updating}
                                    />
                                    <button
                                        className="qty-btn"
                                        onClick={() => increaseQuantity(item.productId)}
                                        disabled={updating}
                                    >
                                        +
                                    </button>
                                </div>

                                <div className="item-total">
                                    <p className="total-label">Total</p>
                                    <p className="total-price">₹{(item.price * item.quantity).toFixed(2)}</p>
                                </div>

                                <button
                                    className="remove-btn"
                                    onClick={() => removeItem(item.productId)}
                                    disabled={updating}
                                >
                                    ×
                                </button>
                            </Card>
                        ))}
                    </div>

                    <div className="cart-summary">
                        <Card className="summary-card">
                            <h3>Order Summary</h3>

                            <div className="summary-row">
                                <span>Subtotal</span>
                                <span>₹{calculateSubtotal().toFixed(2)}</span>
                            </div>

                            <div className="summary-row">
                                <span>Tax (18% GST)</span>
                                <span>₹{calculateTax().toFixed(2)}</span>
                            </div>

                            <div className="summary-divider"></div>

                            <div className="summary-row summary-total">
                                <span>Total</span>
                                <span>₹{calculateTotal().toFixed(2)}</span>
                            </div>

                            <Button
                                variant="primary"
                                size="lg"
                                fullWidth
                                onClick={() => navigate('/user/checkout')}
                                disabled={updating}
                            >
                                Proceed to Checkout
                            </Button>

                            <Button
                                variant="outline"
                                size="md"
                                fullWidth
                                onClick={() => navigate('/marketplace')}
                            >
                                Continue Shopping
                            </Button>
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Cart;
