import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { productAPI } from '../../utils/api';
import { getUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Loading from '../../components/common/Loading';
import './Products.css';

const Products = () => {
    const user = getUser();
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            setLoading(true);
            const response = await productAPI.getByFarmer(user.farmerId);
            if (response.status === 'success') {
                setProducts(response.data || []);
            }
        } catch (err) {
            console.error('Failed to fetch products:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this product?')) return;

        try {
            await productAPI.delete(id);
            fetchProducts();
        } catch (err) {
            console.error('Failed to delete product:', err);
        }
    };

    const toggleAvailability = async (product) => {
        try {
            await productAPI.update(product.id, {
                ...product,
                available: !product.available
            });
            fetchProducts();
        } catch (err) {
            console.error('Failed to update product:', err);
        }
    };

    if (loading) return <Loading fullPage text="Loading products..." />;

    return (
        <div className="farmer-products-page">
            <div className="container">
                <div className="page-header">
                    <h1>My Products</h1>
                    <Link to="/farmer/products/add">
                        <Button variant="primary">+ Add New Product</Button>
                    </Link>
                </div>

                {products.length === 0 ? (
                    <Card className="no-products">
                        <div className="no-products-icon">📦</div>
                        <h2>No Products Yet</h2>
                        <p>Start selling by adding your first product!</p>
                        <Link to="/farmer/products/add">
                            <Button variant="primary">Add Product</Button>
                        </Link>
                    </Card>
                ) : (
                    <div className="products-grid">
                        {products.map((product) => (
                            <Card key={product.id} className="product-card">
                                <div className="product-image">
                                    {product.images?.[0] ? (
                                        <img src={product.images[0].url} alt={product.name} />
                                    ) : (
                                        <div className="image-placeholder">🌾</div>
                                    )}
                                </div>

                                <div className="product-info">
                                    <h3>{product.name}</h3>
                                    <p className="product-price">₹{product.price}/{product.unit}</p>
                                    <p className="product-stock">Stock: {product.quantity}</p>

                                    <div className="product-status">
                                        <span className={`status-badge ${product.available ? 'available' : 'unavailable'}`}>
                                            {product.available ? 'Available' : 'Unavailable'}
                                        </span>
                                    </div>

                                    <div className="product-actions">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => toggleAvailability(product)}
                                        >
                                            {product.available ? 'Mark Unavailable' : 'Mark Available'}
                                        </Button>
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => window.location.href = `/farmer/products/edit/${product.id}`}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(product.id)}
                                        >
                                            Delete
                                        </Button>
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

export default Products;
