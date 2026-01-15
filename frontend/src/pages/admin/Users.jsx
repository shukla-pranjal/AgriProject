import React, { useState, useEffect } from 'react';
import { userAPI } from '../../utils/api';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Loading from '../../components/common/Loading';
import './Admin.css';

const AdminUsers = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            const response = await userAPI.getAll();
            if (response.status === 'success') {
                setUsers(response.data || []);
            }
        } catch {
            console.error('Failed to fetch users:');
        } finally {
            setLoading(false);
        }
    };

    const handlePromote = async (userId) => {
        try {
            await userAPI.promoteToAdmin(userId);
            fetchUsers();
        } catch {
            console.error('Failed to promote user:');
        }
    };

    const handleDemote = async (userId) => {
        try {
            await userAPI.demoteFromAdmin(userId);
            fetchUsers();
        } catch {
            console.error('Failed to demote user:');
        }
    };

    if (loading) return <Loading fullPage text="Loading users..." />;

    return (
        <div className="admin-page">
            <div className="container">
                <h1>Manage Users</h1>

                <div className="users-list">
                    {users.map((user) => (
                        <Card key={user.id} className="user-card">
                            <div className="user-info">
                                <h3>{user.name}</h3>
                                <p>{user.email}</p>
                                <span className={`role-badge role-${user.role?.toLowerCase()}`}>
                                    {user.role}
                                </span>
                            </div>
                            <div className="user-actions">
                                {user.role !== 'ADMIN' ? (
                                    <Button variant="primary" size="sm" onClick={() => handlePromote(user.id)}>
                                        Promote to Admin
                                    </Button>
                                ) : (
                                    <Button variant="outline" size="sm" onClick={() => handleDemote(user.id)}>
                                        Demote from Admin
                                    </Button>
                                )}
                            </div>
                        </Card>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default AdminUsers;
