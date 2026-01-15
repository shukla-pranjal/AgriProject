import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { isAuthenticated, getUser, logout, isAdmin, isFarmer } from '../../utils/auth';
import './Navbar.css';

const Navbar = () => {
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const [userMenuOpen, setUserMenuOpen] = useState(false);
    const navigate = useNavigate();
    const authenticated = isAuthenticated();
    const user = getUser();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const getDashboardLink = () => {
        if (isAdmin()) return '/admin/dashboard';
        if (isFarmer()) return '/farmer/dashboard';
        return '/user/dashboard';
    };

    return (
        <nav className="navbar">
            <div className="container">
                <div className="navbar-content">
                    {/* Logo */}
                    <Link to="/" className="navbar-logo">
                        <span className="logo-icon">🌾</span>
                        <span className="logo-text gradient-text">FarmFlow</span>
                    </Link>

                    {/* Desktop Navigation */}
                    <div className="navbar-links">
                        <Link to="/" className="nav-link">Home</Link>
                        <Link to="/marketplace" className="nav-link">Marketplace</Link>
                        <div className="nav-dropdown">
                            <button className="nav-link">ML Tools</button>
                            <div className="dropdown-menu">
                                <Link to="/ml/crop-recommendation" className="dropdown-item">Crop Recommendation</Link>
                                <Link to="/ml/pump-control" className="dropdown-item">Pump Control</Link>
                                <Link to="/ml/fertilizer" className="dropdown-item">Fertilizer Guide</Link>
                            </div>
                        </div>
                    </div>

                    {/* Right Section */}
                    <div className="navbar-actions">
                        {authenticated ? (
                            <>
                                <Link to="/user/cart" className="nav-icon-btn">
                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                                    </svg>
                                </Link>

                                <div className="user-menu">
                                    <button
                                        className="user-menu-btn"
                                        onClick={() => setUserMenuOpen(!userMenuOpen)}
                                    >
                                        <div className="user-avatar">
                                            {user?.name?.charAt(0).toUpperCase() || 'U'}
                                        </div>
                                        <span className="user-name">{user?.name || 'User'}</span>
                                    </button>

                                    {userMenuOpen && (
                                        <div className="user-dropdown">
                                            <Link to={getDashboardLink()} className="dropdown-item">Dashboard</Link>
                                            <Link to="/user/profile" className="dropdown-item">Profile</Link>
                                            <Link to="/user/orders" className="dropdown-item">My Orders</Link>
                                            {isFarmer() ? (
                                                <>
                                                    <div className="dropdown-divider"></div>
                                                    <Link to="/farmer/products" className="dropdown-item">My Products</Link>
                                                    <Link to="/farmer/profile" className="dropdown-item">Farm Profile</Link>
                                                </>
                                            ) : (
                                                !isAdmin() && (
                                                    <Link to="/become-farmer" className="dropdown-item">Become a Farmer</Link>
                                                )
                                            )}
                                            {isAdmin() && (
                                                <>
                                                    <div className="dropdown-divider"></div>
                                                    <Link to="/admin/users" className="dropdown-item">Manage Users</Link>
                                                    <Link to="/admin/products" className="dropdown-item">Manage Products</Link>
                                                </>
                                            )}
                                            <div className="dropdown-divider"></div>
                                            <button onClick={handleLogout} className="dropdown-item dropdown-item-danger">
                                                Logout
                                            </button>
                                        </div>
                                    )}
                                </div>
                            </>
                        ) : (
                            <>
                                <Link to="/login" className="nav-link">Login</Link>
                                <Link to="/register">
                                    <button className="btn btn-primary btn-sm">Get Started</button>
                                </Link>
                            </>
                        )}
                    </div>

                    {/* Mobile Menu Button */}
                    <button
                        className="mobile-menu-btn"
                        onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                    >
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
                        </svg>
                    </button>
                </div>

                {/* Mobile Menu */}
                {mobileMenuOpen && (
                    <div className="mobile-menu">
                        <Link to="/" className="mobile-link">Home</Link>
                        <Link to="/marketplace" className="mobile-link">Marketplace</Link>
                        <Link to="/ml/crop-recommendation" className="mobile-link">Crop Recommendation</Link>
                        <Link to="/ml/pump-control" className="mobile-link">Pump Control</Link>
                        <Link to="/ml/fertilizer" className="mobile-link">Fertilizer Guide</Link>
                        {authenticated ? (
                            <>
                                <div className="mobile-divider"></div>
                                <Link to={getDashboardLink()} className="mobile-link">Dashboard</Link>
                                <Link to="/user/profile" className="mobile-link">Profile</Link>
                                <button onClick={handleLogout} className="mobile-link mobile-link-danger">
                                    Logout
                                </button>
                            </>
                        ) : (
                            <>
                                <div className="mobile-divider"></div>
                                <Link to="/login" className="mobile-link">Login</Link>
                                <Link to="/register" className="mobile-link">Register</Link>
                            </>
                        )}
                    </div>
                )}
            </div>
        </nav>
    );
};

export default Navbar;
