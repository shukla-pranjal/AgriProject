import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { cartAPI, orderAPI } from '../../utils/api';
import { getUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Loading from '../../components/common/Loading';
import AddressManager from '../../components/AddressManager';
import './Checkout.css';

const Checkout = () => {
    const navigate = useNavigate();
    const user = getUser();
    const [cart, setCart] = useState(null);
    const [selectedAddress, setSelectedAddress] = useState(null);
    const [loading, setLoading] = useState(true);
    const [placing, setPlacing] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchCart = async () => {
            try {
                setLoading(true);
                const response = await cartAPI.getByUser(user.id);
                if (response.status === 'success') {
                    setCart(response.data);
                    if (!response.data?.items || response.data.items.length === 0) {
                        navigate('/user/cart');
                    }
                }
            } catch {
                setError('Failed to load cart');
            } finally {
                setLoading(false);
            }
        };

        fetchCart();
    }, [user.id, navigate]);

    const calculateSubtotal = () => {
        if (!cart?.items) return 0;
        return cart.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    };

    const calculateTax = () => {
        return calculateSubtotal() * 0.18;
    };

    const calculateTotal = () => {
        return calculateSubtotal() + calculateTax();
    };

    const handlePlaceOrder = async () => {
        if (!selectedAddress) {
            setError('Please select a delivery address');
            return;
        }

        try {
            setPlacing(true);
            setError('');

            const response = await orderAPI.createFromCart(cart.id);

            if (response.status === 'success') {
                // Order placed successfully
                navigate('/user/orders', {
                    state: { message: 'Order placed successfully!' }
                });
            }
        } catch {
            setError('Failed to place order. Please try again.');
        } finally {
            setPlacing(false);
        }
    };

    if (loading) return <Loading fullPage text="Loading checkout..." />;

    return (
        <div className="checkout-page">
            <div className="container">
                <h1>Checkout</h1>

                {error && <div className="alert alert-error">{error}</div>}

                <div className="checkout-layout">
                    <div className="checkout-main">
                        {/* Address Selection */}
                        <AddressManager
                            onSelect={setSelectedAddress}
                            selectedId={selectedAddress?.id}
                        />

                        {/* Order Items */}
                        <Card className="order-items-card">
                            <h3>Order Items ({cart?.items?.length || 0})</h3>
                            <div className="order-items">
                                {cart?.items?.map((item) => (
                                    <div key={item.productId} className="order-item">
                                        <div className="item-image">
                                            {item.productImage ? (
                                                <img src={item.productImage} alt={item.productName} />
                                            ) : (
                                                <div className="item-placeholder">🌾</div>
                                            )}
                                        </div>
                                        <div className="item-info">
                                            <h4>{item.productName}</h4>
                                            <p>Quantity: {item.quantity}</p>
                                        </div>
                                        <div className="item-price">
                                            ₹{(item.price * item.quantity).toFixed(2)}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </Card>
                    </div>

                    {/* Order Summary */}
                    <div className="checkout-sidebar">
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

                            <div className="summary-row">
                                <span>Delivery</span>
                                <span className="free-delivery">FREE</span>
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
                                onClick={handlePlaceOrder}
                                loading={placing}
                                disabled={!selectedAddress}
                            >
                                Place Order
                            </Button>

                            {!selectedAddress && (
                                <p className="address-warning">Please select a delivery address</p>
                            )}
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Checkout;
