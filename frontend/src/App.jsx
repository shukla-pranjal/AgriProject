import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/layout/Navbar';
import Footer from './components/layout/Footer';
import ProtectedRoute from './components/ProtectedRoute';

// Public Pages
import Home from './pages/Home';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Marketplace from './pages/Marketplace';
import ProductDetail from './pages/ProductDetail';

// ML Pages
import CropRecommendation from './pages/ml/CropRecommendation';
import PumpControl from './pages/ml/PumpControl';
import FertilizerRecommendation from './pages/ml/FertilizerRecommendation';

// User Pages
import UserDashboard from './pages/user/Dashboard';
import Cart from './pages/user/Cart';
import Checkout from './pages/user/Checkout';
import Orders from './pages/user/Orders';
import UserProfile from './pages/user/Profile';

// Farmer Pages
import FarmerDashboard from './pages/farmer/Dashboard';
import FarmerProfile from './pages/farmer/FarmerProfile';
import FarmerProducts from './pages/farmer/Products';
import AddProduct from './pages/farmer/AddProduct';
import BecomeFarmer from './pages/farmer/BecomeFarmer';

// Admin Pages
import AdminDashboard from './pages/admin/Dashboard';
import AdminUsers from './pages/admin/Users';
import AdminProducts from './pages/admin/Products';
import AdminOrders from './pages/admin/Orders';

import './App.css';

function App() {
  return (
    <Router>
      <div id="root">
        <Navbar />
        <main className="main-content">
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/marketplace" element={<Marketplace />} />
            <Route path="/product/:id" element={<ProductDetail />} />

            {/* ML Routes */}
            <Route path="/ml/crop-recommendation" element={<CropRecommendation />} />
            <Route path="/ml/pump-control" element={<PumpControl />} />
            <Route path="/ml/fertilizer" element={<FertilizerRecommendation />} />

            {/* User Protected Routes */}
            <Route
              path="/user/dashboard"
              element={
                <ProtectedRoute>
                  <UserDashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/user/cart"
              element={
                <ProtectedRoute>
                  <Cart />
                </ProtectedRoute>
              }
            />
            <Route
              path="/user/checkout"
              element={
                <ProtectedRoute>
                  <Checkout />
                </ProtectedRoute>
              }
            />
            <Route
              path="/user/orders"
              element={
                <ProtectedRoute>
                  <Orders />
                </ProtectedRoute>
              }
            />
            <Route
              path="/user/profile"
              element={
                <ProtectedRoute>
                  <UserProfile />
                </ProtectedRoute>
              }
            />

            {/* Farmer Registration */}
            <Route
              path="/become-farmer"
              element={
                <ProtectedRoute>
                  <BecomeFarmer />
                </ProtectedRoute>
              }
            />

            {/* Farmer Protected Routes */}
            <Route
              path="/farmer/dashboard"
              element={
                <ProtectedRoute requiredRole="FARMER">
                  <FarmerDashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/farmer/profile"
              element={
                <ProtectedRoute>
                  <FarmerProfile />
                </ProtectedRoute>
              }
            />
            <Route
              path="/farmer/products"
              element={
                <ProtectedRoute requiredRole="FARMER">
                  <FarmerProducts />
                </ProtectedRoute>
              }
            />
            <Route
              path="/farmer/products/add"
              element={
                <ProtectedRoute requiredRole="FARMER">
                  <AddProduct />
                </ProtectedRoute>
              }
            />

            {/* Admin Protected Routes */}
            <Route
              path="/admin/dashboard"
              element={
                <ProtectedRoute requiredRole="ADMIN">
                  <AdminDashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/users"
              element={
                <ProtectedRoute requiredRole="ADMIN">
                  <AdminUsers />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/products"
              element={
                <ProtectedRoute requiredRole="ADMIN">
                  <AdminProducts />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/orders"
              element={
                <ProtectedRoute requiredRole="ADMIN">
                  <AdminOrders />
                </ProtectedRoute>
              }
            />

            {/* 404 */}
            <Route path="*" element={<div className="container" style={{ padding: '4rem', textAlign: 'center' }}><h1>404 - Page Not Found</h1></div>} />
          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
