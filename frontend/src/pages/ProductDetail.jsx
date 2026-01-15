import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { productAPI, cartAPI, reviewAPI } from '../utils/api';
import { getUser, isAuthenticated } from '../utils/auth';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import './ProductDetail.css';

const ProductDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const user = getUser();
    const [product, setProduct] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [quantity, setQuantity] = useState(1);
    const [adding, setAdding] = useState(false);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);

    useEffect(() => {
        fetchProduct();
        fetchReviews();
    }, [id]);

    const fetchProduct = async () => {
        try {
            setLoading(true);
            const response = await productAPI.getById(id);
            if (response.status === 'success') {
                setProduct(response.data);
            }
        } catch (err) {
            console.error('Failed to fetch product:', err);
        } finally {
            setLoading(false);
        }
    };

    const fetchReviews = async () => {
        try {
            const response = await reviewAPI.getByProduct(id);
            if (response.status === 'success') {
                setReviews(response.data || []);
            }
        } catch (err) {
            console.error('Failed to fetch reviews:', err);
        }
    };

    const handleAddToCart = async () => {
        if (!isAuthenticated()) {
            navigate('/login');
            return;
        }

        try {
            setAdding(true);
            // First get or create cart
            let cartResponse = await cartAPI.getByUser(user.id);
            let cartId = cartResponse.data?.id;

            if (!cartId) {
                const createResponse = await cartAPI.create({ userId: user.id });
                cartId = createResponse.data.id;
            }

            // Add item to cart
            await cartAPI.addItem(cartId, {
                productId: parseInt(id),
                quantity: quantity
            });

            navigate('/user/cart');
        } catch (err) {
            console.error('Failed to add to cart:', err);
            alert('Failed to add to cart');
        } finally {
            setAdding(false);
        }
    };

    const calculateAverageRating = () => {
        if (reviews.length === 0) return 0;
        const sum = reviews.reduce((acc, review) => acc + review.rating, 0);
        return (sum / reviews.length).toFixed(1);
    };

    if (loading) return <Loading fullPage text="Loading product..." />;
    if (!product) return <div className="container"><p>Product not found</p></div>;

    const images = product.images || [];
    const currentImage = images[currentImageIndex];

    return (
        <div className="product-detail-page">
            <div className="container">
                <div className="product-layout">
                    {/* Image Gallery */}
                    <div className="product-gallery">
                        <div className="main-image">
                            {currentImage ? (
                                <img src={currentImage.url} alt={product.name} />
                            ) : (
                                <div className="image-placeholder">🌾</div>
                            )}
                        </div>
                        {images.length > 1 && (
                            <div className="thumbnail-list">
                                {images.map((img, index) => (
                                    <div
                                        key={img.id}
                                        className={`thumbnail ${index === currentImageIndex ? 'active' : ''}`}
                                        onClick={() => setCurrentImageIndex(index)}
                                    >
                                        <img src={img.url} alt={`${product.name} ${index + 1}`} />
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    {/* Product Info */}
                    <div className="product-info">
                        <h1>{product.name}</h1>

                        {/* Rating */}
                        <div className="product-rating">
                            <div className="stars">
                                {[1, 2, 3, 4, 5].map((star) => (
                                    <span key={star} className={star <= calculateAverageRating() ? 'star filled' : 'star'}>
                                        ★
                                    </span>
                                ))}
                            </div>
                            <span className="rating-text">
                                {calculateAverageRating()} ({reviews.length} reviews)
                            </span>
                        </div>

                        <div className="product-price">
                            <span className="price">₹{product.price}</span>
                            <span className="unit">per {product.unit}</span>
                        </div>

                        {product.category && (
                            <div className="product-category">
                                <span className="category-badge">{product.category.name}</span>
                            </div>
                        )}

                        <div className="product-description">
                            <h3>Description</h3>
                            <p>{product.description}</p>
                        </div>

                        {product.farmer && (
                            <div className="farmer-info">
                                <h3>Sold by</h3>
                                <p>{product.farmer.farmName || `Farmer #${product.farmerId}`}</p>
                            </div>
                        )}

                        <div className="product-meta">
                            {product.quantity !== undefined && (
                                <div className="meta-item">
                                    <span className="meta-label">Stock:</span>
                                    <span className={product.quantity > 0 ? 'in-stock' : 'out-of-stock'}>
                                        {product.quantity > 0 ? `${product.quantity} available` : 'Out of stock'}
                                    </span>
                                </div>
                            )}
                            {product.expiryDate && (
                                <div className="meta-item">
                                    <span className="meta-label">Expiry:</span>
                                    <span>{new Date(product.expiryDate).toLocaleDateString()}</span>
                                </div>
                            )}
                        </div>

                        {/* Add to Cart */}
                        <div className="add-to-cart-section">
                            <div className="quantity-selector">
                                <button onClick={() => setQuantity(Math.max(1, quantity - 1))}>−</button>
                                <input
                                    type="number"
                                    value={quantity}
                                    onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                                    min="1"
                                />
                                <button onClick={() => setQuantity(quantity + 1)}>+</button>
                            </div>
                            <Button
                                variant="primary"
                                size="lg"
                                onClick={handleAddToCart}
                                loading={adding}
                                disabled={!product.available || product.quantity === 0}
                            >
                                {product.available && product.quantity > 0 ? 'Add to Cart' : 'Out of Stock'}
                            </Button>
                        </div>
                    </div>
                </div>

                {/* Reviews Section */}
                <div className="reviews-section">
                    <h2>Customer Reviews</h2>
                    {reviews.length === 0 ? (
                        <Card className="no-reviews">
                            <p>No reviews yet. Be the first to review this product!</p>
                        </Card>
                    ) : (
                        <div className="reviews-list">
                            {reviews.map((review) => (
                                <Card key={review.id} className="review-card">
                                    <div className="review-header">
                                        <div>
                                            <div className="review-author">{review.userName || 'Anonymous'}</div>
                                            <div className="review-date">
                                                {new Date(review.createdAt).toLocaleDateString()}
                                            </div>
                                        </div>
                                        <div className="review-rating">
                                            {[1, 2, 3, 4, 5].map((star) => (
                                                <span key={star} className={star <= review.rating ? 'star filled' : 'star'}>
                                                    ★
                                                </span>
                                            ))}
                                        </div>
                                    </div>
                                    <p className="review-comment">{review.comment}</p>
                                </Card>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProductDetail;
