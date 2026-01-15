import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authAPI } from '../../utils/api';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import Card from '../../components/common/Card';
import './Auth.css';

const Register = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        phone: '',
    });
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [success, setSuccess] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
        if (errors[e.target.name]) {
            setErrors({ ...errors, [e.target.name]: '' });
        }
    };

    const validate = () => {
        const newErrors = {};
        if (!formData.name || formData.name.length < 3) {
            newErrors.name = 'Name must be at least 3 characters';
        }
        if (!formData.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
            newErrors.email = 'Valid email is required';
        }
        if (!formData.password || formData.password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters';
        }
        if (!formData.phone || !/^\d{10}$/.test(formData.phone)) {
            newErrors.phone = 'Phone must be 10 digits';
        }
        return newErrors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newErrors = validate();

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        setLoading(true);
        setMessage('');

        try {
            const response = await authAPI.register(formData);

            if (response.status === 'success') {
                setSuccess(true);
                setMessage('Registration successful! Please check your email to verify your account.');
                setTimeout(() => navigate('/login'), 3000);
            } else {
                setMessage(response.message || 'Registration failed');
            }
        } catch (error) {
            setMessage(error.message || 'Registration failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-container">
                <Card className="auth-card">
                    <div className="auth-header">
                        <h1>Create Account</h1>
                        <p>Join FarmFlow today</p>
                    </div>

                    <form onSubmit={handleSubmit} className="auth-form">
                        {message && (
                            <div className={`alert ${success ? 'alert-success' : 'alert-error'}`}>
                                {message}
                            </div>
                        )}

                        <Input
                            label="Full Name"
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            error={errors.name}
                            placeholder="Enter your full name"
                            fullWidth
                        />

                        <Input
                            label="Email"
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            error={errors.email}
                            placeholder="Enter your email"
                            fullWidth
                        />

                        <Input
                            label="Phone Number"
                            type="tel"
                            name="phone"
                            value={formData.phone}
                            onChange={handleChange}
                            error={errors.phone}
                            placeholder="10-digit phone number"
                            fullWidth
                        />

                        <Input
                            label="Password"
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            error={errors.password}
                            placeholder="At least 6 characters"
                            fullWidth
                        />

                        <Button
                            type="submit"
                            variant="primary"
                            size="lg"
                            fullWidth
                            loading={loading}
                            disabled={success}
                        >
                            Register
                        </Button>

                        <div className="auth-footer">
                            <p>
                                Already have an account?{' '}
                                <Link to="/login" className="auth-link-primary">
                                    Login here
                                </Link>
                            </p>
                        </div>
                    </form>
                </Card>
            </div>
        </div>
    );
};

export default Register;
