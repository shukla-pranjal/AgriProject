import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { productAPI } from '../utils/api';
import Card from '../components/common/Card';
import Loading from '../components/common/Loading';
import Button from '../components/common/Button';
import './Marketplace.css';

const Marketplace = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await productAPI.getAllAvailable();
            if (response.status === 'success') {
                setProducts(response.data || []);
            }
        } catch (err) {
            setError('Failed to load products');
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Loading fullPage text="Loading products..." />;

    return (
        <div className="marketplace-page">
            <div className="container">
                <div className="marketplace-header">
                    <h1>Marketplace</h1>
                    <p>Fresh products directly from farmers</p>
                </div>

                {error && <div className="alert alert-error">{error}</div>}

                <div className="products-grid">
                    {products.length === 0 ? (
                        <p>No products available</p>
                    ) : (
                        products.map((product) => (
                            <Card key={product.id} className="product-card" hoverable>
                                <div className="product-image">
                                    {product.images?.[0] ? (
                                        <img src={product.images[0].url} alt={product.name} />
                                    ) : (
                                        <div className="product-placeholder">🌾</div>
                                    )}
                                </div>
                                <div className="product-info">
                                    <h3>{product.name}</h3>
                                    <p className="product-price">₹{product.price}/{product.unit}</p>
                                    <p className="product-description">{product.description}</p>
                                    <Link to={`/product/${product.id}`}>
                                        <Button variant="primary" size="sm" fullWidth>
                                            View Details
                                        </Button>
                                    </Link>
                                </div>
                            </Card>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default Marketplace;
