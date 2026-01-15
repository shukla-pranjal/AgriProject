import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { farmerAPI } from '../../utils/api';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import Card from '../../components/common/Card';
import Loading from '../../components/common/Loading';
import Modal from '../../components/common/Modal';

const FarmerProfile = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [editing, setEditing] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [farmerData, setFarmerData] = useState(null);
    const [formData, setFormData] = useState({
        farmName: '',
        farmType: '',
        locationDiscription: '',
        governmentId: '',
    });

    const farmTypes = [
        'Organic',
        'Traditional',
        'Hydroponic',
        'Greenhouse',
        'Mixed Farming',
        'Dairy Farm',
        'Poultry Farm',
        'Other'
    ];

    useEffect(() => {
        fetchFarmerProfile();
    }, []);

    const fetchFarmerProfile = async () => {
        try {
            const user = JSON.parse(localStorage.getItem('user'));
            if (!user || !user.id) {
                toast.error('Please login to continue');
                navigate('/login');
                return;
            }

            // Check if user is a farmer
            const isFarmerResponse = await farmerAPI.isFarmer(user.id);

            if (isFarmerResponse.data.status === 'success' && isFarmerResponse.data.data) {
                const farmerResponse = await farmerAPI.getById(isFarmerResponse.data.data.id);

                if (farmerResponse.data.status === 'success') {
                    setFarmerData(farmerResponse.data.data);
                    setFormData({
                        farmName: farmerResponse.data.data.farmName || '',
                        farmType: farmerResponse.data.data.farmType || '',
                        locationDiscription: farmerResponse.data.data.locationDiscription || '',
                        governmentId: farmerResponse.data.data.governmentId || '',
                    });
                }
            } else {
                toast.info('You are not registered as a farmer yet');
                navigate('/become-farmer');
            }
        } catch (error) {
            console.error('Error fetching farmer profile:', error);
            toast.error('Failed to load farmer profile');
            navigate('/become-farmer');
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleUpdate = async (e) => {
        e.preventDefault();

        if (!formData.farmName || !formData.farmType || !formData.locationDiscription) {
            toast.error('Please fill in all required fields');
            return;
        }

        setSaving(true);
        try {
            const response = await farmerAPI.update(farmerData.id, formData);

            if (response.data.status === 'success') {
                toast.success('Profile updated successfully!');
                setFarmerData(response.data.data);
                setEditing(false);
            } else {
                toast.error(response.data.message || 'Failed to update profile');
            }
        } catch (error) {
            console.error('Error updating profile:', error);
            toast.error(error.response?.data?.message || 'Failed to update profile');
        } finally {
            setSaving(false);
        }
    };

    const handleDelete = async () => {
        try {
            const response = await farmerAPI.delete(farmerData.id);

            if (response.data.status === 'success') {
                toast.success('Farmer profile deleted successfully');
                navigate('/user/dashboard');
            } else {
                toast.error(response.data.message || 'Failed to delete profile');
            }
        } catch (error) {
            console.error('Error deleting profile:', error);
            toast.error(error.response?.data?.message || 'Failed to delete profile');
        } finally {
            setShowDeleteModal(false);
        }
    };

    if (loading) {
        return <Loading />;
    }

    return (
        <div className="min-h-screen bg-gray-50 py-12 px-4">
            <div className="max-w-4xl mx-auto">
                <div className="flex justify-between items-center mb-8">
                    <div>
                        <h1 className="text-4xl font-bold text-gray-900">Farmer Profile</h1>
                        <p className="text-gray-600 mt-2">Manage your farm information and settings</p>
                    </div>
                    <Button onClick={() => navigate('/farmer/dashboard')} variant="secondary">
                        Go to Dashboard
                    </Button>
                </div>

                <Card>
                    <div className="flex justify-between items-center mb-6">
                        <h2 className="text-2xl font-bold text-gray-800">Farm Information</h2>
                        {!editing && (
                            <Button onClick={() => setEditing(true)} variant="secondary">
                                Edit Profile
                            </Button>
                        )}
                    </div>

                    {editing ? (
                        <form onSubmit={handleUpdate} className="space-y-6">
                            <Input
                                label="Farm Name *"
                                name="farmName"
                                value={formData.farmName}
                                onChange={handleChange}
                                placeholder="e.g., Green Acres Farm"
                                required
                            />

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Farm Type *
                                </label>
                                <select
                                    name="farmType"
                                    value={formData.farmType}
                                    onChange={handleChange}
                                    required
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                                >
                                    <option value="">Select farm type</option>
                                    {farmTypes.map((type) => (
                                        <option key={type} value={type}>
                                            {type}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Location Description *
                                </label>
                                <textarea
                                    name="locationDiscription"
                                    value={formData.locationDiscription}
                                    onChange={handleChange}
                                    required
                                    rows="4"
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                                    placeholder="Provide detailed location information"
                                />
                            </div>

                            <Input
                                label="Government ID"
                                name="governmentId"
                                value={formData.governmentId}
                                onChange={handleChange}
                                placeholder="e.g., Aadhaar, PAN, or Farmer ID"
                            />

                            <div className="flex justify-between">
                                <Button
                                    type="button"
                                    onClick={() => {
                                        setEditing(false);
                                        setFormData({
                                            farmName: farmerData.farmName || '',
                                            farmType: farmerData.farmType || '',
                                            locationDiscription: farmerData.locationDiscription || '',
                                            governmentId: farmerData.governmentId || '',
                                        });
                                    }}
                                    variant="secondary"
                                >
                                    Cancel
                                </Button>
                                <Button type="submit" loading={saving}>
                                    {saving ? 'Saving...' : 'Save Changes'}
                                </Button>
                            </div>
                        </form>
                    ) : (
                        <div className="space-y-6">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-medium text-gray-500 mb-1">
                                        Farm Name
                                    </label>
                                    <p className="text-lg font-semibold text-gray-900">
                                        {farmerData?.farmName || 'N/A'}
                                    </p>
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-500 mb-1">
                                        Farm Type
                                    </label>
                                    <p className="text-lg font-semibold text-gray-900">
                                        {farmerData?.farmType || 'N/A'}
                                    </p>
                                </div>

                                <div className="md:col-span-2">
                                    <label className="block text-sm font-medium text-gray-500 mb-1">
                                        Location
                                    </label>
                                    <p className="text-lg text-gray-900">
                                        {farmerData?.locationDiscription || 'N/A'}
                                    </p>
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-500 mb-1">
                                        Government ID
                                    </label>
                                    <p className="text-lg text-gray-900">
                                        {farmerData?.governmentId || 'Not provided'}
                                    </p>
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-500 mb-1">
                                        Farmer ID
                                    </label>
                                    <p className="text-lg font-semibold text-gray-900">
                                        #{farmerData?.id}
                                    </p>
                                </div>
                            </div>
                        </div>
                    )}
                </Card>

                {!editing && (
                    <Card className="mt-6 bg-red-50 border-red-200">
                        <h3 className="text-lg font-bold text-red-900 mb-2">Danger Zone</h3>
                        <p className="text-red-700 mb-4">
                            Deleting your farmer profile will remove all your farm information and products. This action cannot be undone.
                        </p>
                        <Button
                            onClick={() => setShowDeleteModal(true)}
                            variant="danger"
                        >
                            Delete Farmer Profile
                        </Button>
                    </Card>
                )}

                <Modal
                    isOpen={showDeleteModal}
                    onClose={() => setShowDeleteModal(false)}
                    title="Delete Farmer Profile"
                >
                    <div className="space-y-4">
                        <p className="text-gray-700">
                            Are you sure you want to delete your farmer profile? This will remove all your farm information and products.
                        </p>
                        <p className="text-red-600 font-semibold">
                            This action cannot be undone!
                        </p>
                        <div className="flex justify-end space-x-3">
                            <Button onClick={() => setShowDeleteModal(false)} variant="secondary">
                                Cancel
                            </Button>
                            <Button onClick={handleDelete} variant="danger">
                                Yes, Delete Profile
                            </Button>
                        </div>
                    </div>
                </Modal>
            </div>
        </div>
    );
};

export default FarmerProfile;
