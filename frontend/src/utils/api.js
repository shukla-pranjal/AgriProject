import axios from 'axios';

// API Gateway Configuration (Local Development)
// Frontend → API Gateway (8080) → Backend Services (via Eureka discovery locator)
const API_GATEWAY = 'http://localhost:8080';

// API Base URLs through API Gateway (using discovery locator)
const API_BASE_URL = `${API_GATEWAY}/agri-java/api/v1`;
const ML_BASE_URL = 'http://localhost:5000'; // ML service direct

// Create axios instance for main API
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Create axios instance for ML API
const mlApi = axios.create({
  baseURL: ML_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error.response?.data || error.message);
  }
);

mlApi.interceptors.response.use(
  (response) => response.data,
  (error) => Promise.reject(error.response?.data || error.message)
);

// Auth API
export const authAPI = {
  register: (data) => api.post('/auth', data), // POST /api/v1/auth (not /register)
  login: (data) => api.post('/auth/login', data),
  verifyEmail: (data) => api.post('/auth/verify-email', data),
  resendVerification: (data) => api.post('/auth/resend-verification', data),
  changePassword: (data) => api.post('/auth/change-password', data),
  forgotPassword: (data) => api.post('/auth/forgot-password', data),
  resetPassword: (data) => api.post('/auth/reset-password', data),
  changeEmail: (data) => api.post('/auth/change-email', data),
};

// User API
export const userAPI = {
  getAll: () => api.get('/users'),
  getProfile: () => api.get('/users/profile'), // Get current user profile
  getById: (id) => api.get(`/users/${id}`),
  update: (id, data) => api.put(`/users/${id}`, data),
  delete: (id) => api.delete(`/users/${id}`),
  search: (params) => api.get('/users/search', { params }),
  getPaged: (data) => api.post('/users/paged', data),
};

// Product API
export const productAPI = {
  getAll: () => api.get('/products'),
  getAllAvailable: () => api.get('/products/available'),
  getById: (id) => api.get(`/products/${id}`),
  create: (data) => api.post('/products', data),
  update: (id, data) => api.put(`/products/${id}`, data),
  delete: (id) => api.delete(`/products/${id}`),
  getByCategory: (categoryId) => api.get(`/products/category/${categoryId}`),
  getByFarmer: (farmerId) => api.get(`/products/farmer/${farmerId}`),
  search: (params) => api.get('/products/search', { params }),
  getPaged: (data) => api.post('/products/paged', data),
  getAvailablePaged: (data) => api.post('/products/available/paged', data),
  searchPaged: (data, params) => api.post('/products/search/paged', data, { params }),
};

// Farmer API
export const farmerAPI = {
  getAll: () => api.get('/farmers'),
  getById: (id) => api.get(`/farmers/${id}`),
  create: (data) => api.post('/farmers', data),
  update: (id, data) => api.put(`/farmers/${id}`, data),
  delete: (id) => api.delete(`/farmers/${id}`),
  isFarmer: (userId) => api.get(`/farmers/isFarmer/${userId}`),
  search: (params) => api.get('/farmers/search', { params }),
  getPaged: (data) => api.post('/farmers/paged', data),
};

// Cart API
export const cartAPI = {
  create: (data) => api.post('/carts', data),
  getById: (id) => api.get(`/carts/${id}`),
  update: (id, data) => api.put(`/carts/${id}`, data),
  delete: (id) => api.delete(`/carts/${id}`),
  addItem: (cartId, data) => api.post(`/carts/${cartId}/items`, data),
  updateItem: (cartId, productId, data) => api.put(`/carts/${cartId}/items/${productId}`, data),
  removeItem: (cartId, productId) => api.delete(`/carts/${cartId}/items/${productId}`),
  increaseQuantity: (cartId, productId) => api.put(`/carts/${cartId}/items/${productId}/increase`),
  decreaseQuantity: (cartId, productId) => api.put(`/carts/${cartId}/items/${productId}/decrease`),
  searchItems: (cartId, params) => api.get(`/carts/${cartId}/items/search`, { params }),
  getByUser: (userId) => api.get(`/carts/user/${userId}`),
  getPaged: (data) => api.post('/carts/paged', data),
  getItemsPaged: (cartId, data) => api.post(`/carts/${cartId}/items/paged`, data),
};

// Order API
export const orderAPI = {
  createFromCart: (cartId) => api.post(`/orders/cart/${cartId}`),
  create: (data) => api.post('/orders', data),
  getById: (id) => api.get(`/orders/${id}`),
  updateStatus: (id, status) => api.put(`/orders/${id}/status`, null, { params: { status } }),
  cancel: (id) => api.put(`/orders/${id}/cancel`),
  reorder: (id) => api.post(`/orders/${id}/reorder`),
  getByUser: (userId) => api.get(`/orders/user/${userId}`),
  getPaged: (data) => api.post('/orders/paged', data),
  getUserOrdersPaged: (userId, data) => api.post(`/orders/user/${userId}/paged`, data),
};

// Review API
export const reviewAPI = {
  create: (data) => api.post('/reviews', data),
  getById: (id) => api.get(`/reviews/${id}`),
  update: (id, data) => api.put(`/reviews/${id}`, data),
  delete: (id) => api.delete(`/reviews/${id}`),
  getByProduct: (productId) => api.get(`/reviews/product/${productId}`),
  getByUser: (userId) => api.get(`/reviews/user/${userId}`),
  getPaged: (data) => api.post('/reviews/paged', data),
  getProductReviewsPaged: (productId, data) => api.post(`/reviews/product/${productId}/paged`, data),
};

// Category API
export const categoryAPI = {
  getAll: () => api.get('/categories'),
  getById: (id) => api.get(`/categories/${id}`),
  create: (data) => api.post('/categories', data),
  update: (id, data) => api.put(`/categories/${id}`, data),
  delete: (id) => api.delete(`/categories/${id}`),
  getPaged: (data) => api.post('/categories/paged', data),
};

// Image API
export const imageAPI = {
  upload: (formData) => api.post('/images', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }),
  getById: (id) => api.get(`/images/${id}`),
  update: (id, formData, entityType, entityId) =>
    api.put(`/images/${id}`, formData, {
      params: { entityType, entityId },
      headers: { 'Content-Type': 'multipart/form-data' },
    }),
  delete: (id) => api.delete(`/images/${id}`),
};

// Payment API
export const paymentAPI = {
  create: (data) => api.post('/payments', data),
  getById: (id) => api.get(`/payments/${id}`),
  update: (id, data) => api.put(`/payments/${id}`, data),
  delete: (id) => api.delete(`/payments/${id}`),
  getPaged: (data) => api.post('/payments/paged', data),
};

// Address API
export const addressAPI = {
  create: (data) => api.post('/addresses', data),
  getById: (id) => api.get(`/addresses/${id}`),
  update: (id, data) => api.put(`/addresses/${id}`, data),
  delete: (id) => api.delete(`/addresses/${id}`),
  getByUser: (userId) => api.get(`/addresses/user/${userId}`),
  getMyAddresses: () => api.get('/addresses/user/me'),
  getPaged: (data) => api.post('/addresses/paged', data),
};

// Admin API
export const adminAPI = {
  promoteUser: (userId) => api.post(`/admin/promote/${userId}`),
  demoteUser: (userId) => api.post(`/admin/demote/${userId}`),
  toggleEmailService: () => api.post('/admin/email-service/toggle'),
};

// ML API
export const mlAPI = {
  cropRecommendation: (data) => mlApi.post('/predict/5', data),
  pumpControl: (data) => mlApi.post('/predict/6', data),
  fertilizerRecommendation: (data) => mlApi.post('/predict/7', data),
};

export default api;
