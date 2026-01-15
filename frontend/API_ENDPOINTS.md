# Frontend API Endpoints - Complete Reference

## ✅ Updated & Fixed Endpoints

### 🔐 Authentication (`/api/v1/auth`)

| Method | Endpoint | Frontend Function | Description |
|--------|----------|-------------------|-------------|
| POST | `/auth` | `authAPI.register(data)` | **FIXED**: Was `/auth/register`, now `/auth` |
| POST | `/auth/login` | `authAPI.login(data)` | User login |
| POST | `/auth/verify-email` | `authAPI.verifyEmail(data)` | Verify email with code |
| POST | `/auth/resend-verification` | `authAPI.resendVerification(data)` | Resend verification email |
| POST | `/auth/change-password` | `authAPI.changePassword(data)` | Change password |
| POST | `/auth/forgot-password` | `authAPI.forgotPassword(data)` | Request password reset |
| POST | `/auth/reset-password` | `authAPI.resetPassword(data)` | Reset password with token |
| POST | `/auth/change-email` | `authAPI.changeEmail(data)` | Change email address |

---

### 👤 Users (`/api/v1/users`)

| Method | Endpoint | Frontend Function | Description |
|--------|----------|-------------------|-------------|
| GET | `/users` | `userAPI.getAll()` | Get all users |
| GET | `/users/profile` | `userAPI.getProfile()` | **NEW**: Get current user profile |
| GET | `/users/{id}` | `userAPI.getById(id)` | Get user by ID |
| PUT | `/users/{id}` | `userAPI.update(id, data)` | Update user |
| DELETE | `/users/{id}` | `userAPI.delete(id)` | Delete user |
| GET | `/users/search` | `userAPI.search(params)` | Search users |
| POST | `/users/paged` | `userAPI.getPaged(data)` | Get paginated users |

---

### 🌾 Farmers (`/api/v1/farmers`)

| Method | Endpoint | Frontend Function | Description |
|--------|----------|-------------------|-------------|
| GET | `/farmers` | `farmerAPI.getAll()` | Get all farmers |
| GET | `/farmers/{id}` | `farmerAPI.getById(id)` | Get farmer by ID |
| POST | `/farmers` | `farmerAPI.create(data)` | Create farmer profile |
| PUT | `/farmers/{id}` | `farmerAPI.update(id, data)` | Update farmer |
| DELETE | `/farmers/{id}` | `farmerAPI.delete(id)` | Delete farmer |
| GET | `/farmers/isFarmer/{userId}` | `farmerAPI.isFarmer(userId)` | **NEW**: Check if user is farmer |
| GET | `/farmers/search` | `farmerAPI.search(params)` | Search farmers |
| POST | `/farmers/paged` | `farmerAPI.getPaged(data)` | Get paginated farmers |

---

### 🛒 Cart (`/api/v1/carts`)

| Method | Endpoint | Frontend Function | Description |
|--------|----------|-------------------|-------------|
| POST | `/carts` | `cartAPI.create(data)` | Create cart |
| GET | `/carts/{id}` | `cartAPI.getById(id)` | Get cart by ID |
| PUT | `/carts/{id}` | `cartAPI.update(id, data)` | Update cart |
| DELETE | `/carts/{id}` | `cartAPI.delete(id)` | Delete cart |
| POST | `/carts/{id}/items` | `cartAPI.addItem(cartId, data)` | Add item to cart |
| PUT | `/carts/{id}/items/{productId}` | `cartAPI.updateItem(cartId, productId, data)` | **FIXED**: Was `itemId`, now `productId` |
| DELETE | `/carts/{id}/items/{productId}` | `cartAPI.removeItem(cartId, productId)` | **FIXED**: Was `itemId`, now `productId` |
| PUT | `/carts/{id}/items/{productId}/increase` | `cartAPI.increaseQuantity(cartId, productId)` | **NEW**: Increase item quantity |
| PUT | `/carts/{id}/items/{productId}/decrease` | `cartAPI.decreaseQuantity(cartId, productId)` | **NEW**: Decrease item quantity |
| GET | `/carts/{id}/items/search` | `cartAPI.searchItems(cartId, params)` | **NEW**: Search cart items |
| GET | `/carts/user/{userId}` | `cartAPI.getByUser(userId)` | Get cart by user |
| POST | `/carts/paged` | `cartAPI.getPaged(data)` | **NEW**: Get paginated carts |
| POST | `/carts/{id}/items/paged` | `cartAPI.getItemsPaged(cartId, data)` | **NEW**: Get paginated cart items |

---

### 📦 Products (`/api/v1/products`)

| Method | Endpoint | Frontend Function | Description |
|--------|----------|-------------------|-------------|
| GET | `/products` | `productAPI.getAll()` | Get all products |
| GET | `/products/available` | `productAPI.getAllAvailable()` | Get available products |
| GET | `/products/{id}` | `productAPI.getById(id)` | Get product by ID |
| POST | `/products` | `productAPI.create(data)` | Create product |
| PUT | `/products/{id}` | `productAPI.update(id, data)` | Update product |
| DELETE | `/products/{id}` | `productAPI.delete(id)` | Delete product |
| GET | `/products/category/{categoryId}` | `productAPI.getByCategory(categoryId)` | Get products by category |
| GET | `/products/farmer/{farmerId}` | `productAPI.getByFarmer(farmerId)` | Get products by farmer |
| GET | `/products/search` | `productAPI.search(params)` | Search products |
| POST | `/products/paged` | `productAPI.getPaged(data)` | Get paginated products |
| POST | `/products/available/paged` | `productAPI.getAvailablePaged(data)` | Get paginated available products |
| POST | `/products/search/paged` | `productAPI.searchPaged(data, params)` | Search with pagination |

---

### 📍 Addresses (`/api/v1/addresses`)

| Method | Endpoint | Frontend Function | Description |
|--------|----------|-------------------|-------------|
| POST | `/addresses` | `addressAPI.create(data)` | Create address |
| GET | `/addresses/{id}` | `addressAPI.getById(id)` | Get address by ID |
| PUT | `/addresses/{id}` | `addressAPI.update(id, data)` | Update address |
| DELETE | `/addresses/{id}` | `addressAPI.delete(id)` | Delete address |
| GET | `/addresses/user/{userId}` | `addressAPI.getByUser(userId)` | Get addresses by user |
| GET | `/addresses/user/me` | `addressAPI.getMyAddresses()` | Get current user's addresses |
| POST | `/addresses/paged` | `addressAPI.getPaged(data)` | Get paginated addresses |

---

## 🎯 Key Changes Summary

### ✅ Fixed Endpoints
1. **Registration**: Changed from `/auth/register` to `/auth` (POST)
2. **Cart Items**: Changed parameter from `itemId` to `productId`

### ✨ New Endpoints Added
1. **User Profile**: `userAPI.getProfile()` - Get current user
2. **Farmer Check**: `farmerAPI.isFarmer(userId)` - Check if user is farmer
3. **Cart Quantity**: `cartAPI.increaseQuantity()` & `cartAPI.decreaseQuantity()`
4. **Cart Search**: `cartAPI.searchItems()` - Search items in cart
5. **Cart Pagination**: `cartAPI.getPaged()` & `cartAPI.getItemsPaged()`

---

## 🔗 API Base URL

```javascript
const API_BASE_URL = 'http://localhost:8080/agri-java/api/v1';
```

**Important**: All requests go through the API Gateway on port 8080, which routes to the FarmFlow service via Eureka discovery.

---

## 📝 Usage Examples

### Registration (Fixed)
```javascript
// ✅ CORRECT
await authAPI.register({
  name: "John Doe",
  email: "john@example.com",
  password: "password123",
  phone: "1234567890"
});

// ❌ WRONG (old way)
// await authAPI.register(...) // was calling /auth/register
```

### Cart Item Management (New)
```javascript
// Increase quantity
await cartAPI.increaseQuantity(cartId, productId);

// Decrease quantity
await cartAPI.decreaseQuantity(cartId, productId);

// Search items in cart
const results = await cartAPI.searchItems(cartId, { query: "apple" });
```

### Check if User is Farmer (New)
```javascript
const response = await farmerAPI.isFarmer(userId);
// Returns boolean or farmer status
```

---

## ✅ All Endpoints Verified

All endpoints have been cross-referenced with the backend `*Endpoint.java` files and are now correctly mapped in the frontend `api.js` file.
