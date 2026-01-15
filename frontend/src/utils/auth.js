// Authentication utilities

export const setAuthToken = (token) => {
    localStorage.setItem('token', token);
};

export const getAuthToken = () => {
    return localStorage.getItem('token');
};

export const removeAuthToken = () => {
    localStorage.removeItem('token');
};

export const setUser = (user) => {
    localStorage.setItem('user', JSON.stringify(user));
};

export const getUser = () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
};

export const removeUser = () => {
    localStorage.removeItem('user');
};

export const isAuthenticated = () => {
    return !!getAuthToken();
};

export const logout = () => {
    removeAuthToken();
    removeUser();
    window.location.href = '/login';
};

export const getUserRole = () => {
    const user = getUser();
    return user?.role || null;
};

export const isAdmin = () => {
    return getUserRole() === 'ADMIN';
};

export const isFarmer = () => {
    const user = getUser();
    return user?.farmerId != null;
};

export const isUser = () => {
    return isAuthenticated() && !isAdmin();
};

// Check if user has required role
export const hasRole = (requiredRole) => {
    const role = getUserRole();
    if (requiredRole === 'ADMIN') return role === 'ADMIN';
    if (requiredRole === 'FARMER') return isFarmer();
    if (requiredRole === 'USER') return isUser();
    return false;
};
