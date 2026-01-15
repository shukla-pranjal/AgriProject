import React from 'react';
import { Link } from 'react-router-dom';
import Button from '../components/common/Button';
import './Home.css';

const Home = () => {
    return (
        <div className="home-page">
            {/* Hero Section */}
            <section className="hero">
                <div className="hero-background">
                    <div className="hero-circle hero-circle-1"></div>
                    <div className="hero-circle hero-circle-2"></div>
                    <div className="hero-circle hero-circle-3"></div>
                </div>

                <div className="container">
                    <div className="hero-content">
                        <h1 className="hero-title">
                            Empowering Farmers with
                            <span className="gradient-text"> AI & Technology</span>
                        </h1>
                        <p className="hero-subtitle">
                            Smart agriculture solutions combining ML-powered predictions with a modern marketplace for agricultural products
                        </p>
                        <div className="hero-actions">
                            <Link to="/marketplace">
                                <Button variant="primary" size="lg">
                                    Explore Marketplace
                                </Button>
                            </Link>
                            <Link to="/ml/crop-recommendation">
                                <Button variant="outline" size="lg">
                                    Try ML Tools
                                </Button>
                            </Link>
                        </div>
                    </div>
                </div>
            </section>

            {/* Features Section */}
            <section className="features">
                <div className="container">
                    <div className="section-header">
                        <h2>Why Choose FarmFlow?</h2>
                        <p>Comprehensive solutions for modern agriculture</p>
                    </div>

                    <div className="features-grid">
                        <div className="feature-card card hoverable">
                            <div className="feature-icon">🌱</div>
                            <h3>Crop Recommendation</h3>
                            <p>AI-powered crop suggestions based on soil and climate conditions with 95%+ accuracy</p>
                        </div>

                        <div className="feature-card card hoverable">
                            <div className="feature-icon">💧</div>
                            <h3>Smart Irrigation</h3>
                            <p>Intelligent pump control system to optimize water usage and reduce costs</p>
                        </div>

                        <div className="feature-card card hoverable">
                            <div className="feature-icon">🧪</div>
                            <h3>Fertilizer Guide</h3>
                            <p>Precise fertilizer recommendations tailored to your crop and soil type</p>
                        </div>

                        <div className="feature-card card hoverable">
                            <div className="feature-icon">🛒</div>
                            <h3>Marketplace</h3>
                            <p>Buy and sell agricultural products directly from farmers to consumers</p>
                        </div>

                        <div className="feature-card card hoverable">
                            <div className="feature-icon">📊</div>
                            <h3>Analytics Dashboard</h3>
                            <p>Track your farm performance, orders, and revenue in real-time</p>
                        </div>

                        <div className="feature-card card hoverable">
                            <div className="feature-icon">🔒</div>
                            <h3>Secure Transactions</h3>
                            <p>Safe and secure payment processing for all marketplace transactions</p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Stats Section */}
            <section className="stats">
                <div className="container">
                    <div className="stats-grid">
                        <div className="stat-item">
                            <div className="stat-number gradient-text">95%+</div>
                            <div className="stat-label">ML Accuracy</div>
                        </div>
                        <div className="stat-item">
                            <div className="stat-number gradient-text">120+</div>
                            <div className="stat-label">API Endpoints</div>
                        </div>
                        <div className="stat-item">
                            <div className="stat-number gradient-text">3</div>
                            <div className="stat-label">ML Models</div>
                        </div>
                        <div className="stat-item">
                            <div className="stat-number gradient-text">24/7</div>
                            <div className="stat-label">Support</div>
                        </div>
                    </div>
                </div>
            </section>

            {/* CTA Section */}
            <section className="cta">
                <div className="container">
                    <div className="cta-content">
                        <h2>Ready to Transform Your Farm?</h2>
                        <p>Join thousands of farmers already using FarmFlow</p>
                        <Link to="/register">
                            <Button variant="primary" size="lg">
                                Get Started Free
                            </Button>
                        </Link>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default Home;
