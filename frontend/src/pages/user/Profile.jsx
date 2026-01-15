import React, { useState, useEffect } from 'react';
import { userAPI } from '../../utils/api';
import { getUser, setUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import Loading from '../../components/common/Loading';
import './Profile.css';

const Profile = () => {
    const currentUser = getUser();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        phone: ''
    });
    const [passwordData, setPasswordData] = useState({
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
    });
    const [loading, setLoading] = useState(true);
    const [updating, setUpdating] = useState(false);
    const [changingPassword, setChangingPassword] = useState(false);
    const [message, setMessage] = useState('');
    const [errors, setErrors] = useState({});

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                setLoading(true);
                const response = await userAPI.getById(currentUser.id);
                if (response.status === 'success') {
                    const userData = response.data;
                    setFormData({
                        name: userData.name || '',
                        email: userData.email || '',
                        phone: userData.phone || ''
                    });
                }
            } catch {
                console.error('Failed to fetch user data');
            } finally {
                setLoading(false);
            }
        };

        fetchUserData();
    }, [currentUser.id]);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
        if (errors[e.target.name]) {
            setErrors({ ...errors, [e.target.name]: '' });
        }
    };

    const handlePasswordChange = (e) => {
        setPasswordData({
            ...passwordData,
            [e.target.name]: e.target.value
        });
        if (errors[e.target.name]) {
            setErrors({ ...errors, [e.target.name]: '' });
        }
    };

    const handleUpdateProfile = async (e) => {
        e.preventDefault();

        try {
            setUpdating(true);
            setMessage('');
            setErrors({});

            const response = await userAPI.update(currentUser.id, formData);

            if (response.status === 'success') {
                setUser(response.data);
                setMessage('Profile updated successfully!');
            }
        } catch {
            setErrors({ submit: 'Failed to update profile' });
        } finally {
            setUpdating(false);
        }
    };

    const handleChangePassword = async (e) => {
        e.preventDefault();

        const newErrors = {};
        if (!passwordData.oldPassword) newErrors.oldPassword = 'Current password is required';
        if (!passwordData.newPassword || passwordData.newPassword.length < 6) {
            newErrors.newPassword = 'New password must be at least 6 characters';
        }
        if (passwordData.newPassword !== passwordData.confirmPassword) {
            newErrors.confirmPassword = 'Passwords do not match';
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        try {
            setChangingPassword(true);
            setMessage('');
            setErrors({});

            await userAPI.changePassword({
                oldPassword: passwordData.oldPassword,
                newPassword: passwordData.newPassword
            });

            setMessage('Password changed successfully!');
            setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' });
        } catch {
            setErrors({ passwordSubmit: 'Failed to change password. Check your current password.' });
        } finally {
            setChangingPassword(false);
        }
    };

    if (loading) return <Loading fullPage text="Loading profile..." />;

    return (
        <div className="profile-page">
            <div className="container">
                <h1>My Profile</h1>

                {message && <div className="alert alert-success">{message}</div>}

                <div className="profile-layout">
                    {/* Profile Info */}
                    <Card className="profile-card">
                        <h3>Profile Information</h3>
                        <form onSubmit={handleUpdateProfile}>
                            {errors.submit && <div className="alert alert-error">{errors.submit}</div>}

                            <Input
                                label="Full Name"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                error={errors.name}
                                fullWidth
                            />

                            <Input
                                label="Email"
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                error={errors.email}
                                fullWidth
                            />

                            <Input
                                label="Phone"
                                name="phone"
                                value={formData.phone}
                                onChange={handleChange}
                                error={errors.phone}
                                fullWidth
                            />

                            <Button type="submit" variant="primary" loading={updating}>
                                Update Profile
                            </Button>
                        </form>
                    </Card>

                    {/* Change Password */}
                    <Card className="profile-card">
                        <h3>Change Password</h3>
                        <form onSubmit={handleChangePassword}>
                            {errors.passwordSubmit && <div className="alert alert-error">{errors.passwordSubmit}</div>}

                            <Input
                                label="Current Password"
                                type="password"
                                name="oldPassword"
                                value={passwordData.oldPassword}
                                onChange={handlePasswordChange}
                                error={errors.oldPassword}
                                fullWidth
                            />

                            <Input
                                label="New Password"
                                type="password"
                                name="newPassword"
                                value={passwordData.newPassword}
                                onChange={handlePasswordChange}
                                error={errors.newPassword}
                                fullWidth
                            />

                            <Input
                                label="Confirm New Password"
                                type="password"
                                name="confirmPassword"
                                value={passwordData.confirmPassword}
                                onChange={handlePasswordChange}
                                error={errors.confirmPassword}
                                fullWidth
                            />

                            <Button type="submit" variant="primary" loading={changingPassword}>
                                Change Password
                            </Button>
                        </form>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default Profile;
