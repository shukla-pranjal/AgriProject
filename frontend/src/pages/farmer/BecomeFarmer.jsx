import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { farmerAPI } from '../../utils/api';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import Card from '../../components/common/Card';

const BecomeFarmer = () => {
    const navigate = useNavigate();
    const [currentStep, setCurrentStep] = useState(1);
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        farmName: '',
        farmType: '',
        locationDiscription: '',
        governmentId: '',
    });

    const [errors, setErrors] = useState({});

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

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
        }
    };

    const validateStep = (step) => {
        const newErrors = {};

        if (step === 1) {
            if (!formData.farmName || formData.farmName.trim().length < 2) {
                newErrors.farmName = 'Farm name must be at least 2 characters';
            }
            if (!formData.farmType) {
                newErrors.farmType = 'Please select a farm type';
            }
        }

        if (step === 2) {
            if (!formData.locationDiscription || formData.locationDiscription.trim().length < 10) {
                newErrors.locationDiscription = 'Please provide a detailed location (at least 10 characters)';
            }
        }

        if (step === 3) {
            if (!formData.governmentId || formData.governmentId.trim().length < 5) {
                newErrors.governmentId = 'Please provide a valid government ID';
            }
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleNext = () => {
        if (validateStep(currentStep)) {
            setCurrentStep(prev => prev + 1);
        }
    };

    const handlePrevious = () => {
        setCurrentStep(prev => prev - 1);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateStep(3)) {
            return;
        }

        setLoading(true);
        try {
            // Get user ID from localStorage
            const user = JSON.parse(localStorage.getItem('user'));
            if (!user || !user.id) {
                toast.error('Please login to continue');
                navigate('/login');
                return;
            }

            const farmerData = {
                ...formData,
                userId: user.id
            };

            const response = await farmerAPI.create(farmerData);

            if (response.data.status === 'success') {
                toast.success('Farmer profile created successfully! 🎉');

                // Update user in localStorage to include new role/status
                const updatedUser = { ...user, farmerId: response.data.data.id };
                localStorage.setItem('user', JSON.stringify(updatedUser));

                // Force reload to update navigation state
                window.location.href = '/farmer/dashboard';
            } else {
                toast.error(response.data.message || 'Failed to create farmer profile');
            }
        } catch (error) {
            console.error('Error creating farmer profile:', error);
            toast.error(error.response?.data?.message || 'Failed to create farmer profile');
        } finally {
            setLoading(false);
        }
    };

    const renderProgressBar = () => (
        <div className="mb-8">
            <div className="flex justify-between items-center mb-2">
                {[1, 2, 3].map((step) => (
                    <div key={step} className="flex items-center flex-1">
                        <div
                            className={`w-10 h-10 rounded-full flex items-center justify-center font-semibold ${currentStep >= step
                                ? 'bg-green-500 text-white'
                                : 'bg-gray-200 text-gray-600'
                                }`}
                        >
                            {step}
                        </div>
                        {step < 3 && (
                            <div
                                className={`flex-1 h-1 mx-2 ${currentStep > step ? 'bg-green-500' : 'bg-gray-200'
                                    }`}
                            />
                        )}
                    </div>
                ))}
            </div>
            <div className="flex justify-between text-sm text-gray-600 mt-2">
                <span>Farm Details</span>
                <span>Location</span>
                <span>Verification</span>
            </div>
        </div>
    );

    const renderStep1 = () => (
        <div className="space-y-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-4">Farm Details</h2>

            <Input
                label="Farm Name *"
                name="farmName"
                value={formData.farmName}
                onChange={handleChange}
                placeholder="e.g., Green Acres Farm"
                error={errors.farmName}
            />

            <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                    Farm Type *
                </label>
                <select
                    name="farmType"
                    value={formData.farmType}
                    onChange={handleChange}
                    className={`w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent ${errors.farmType ? 'border-red-500' : 'border-gray-300'
                        }`}
                >
                    <option value="">Select farm type</option>
                    {farmTypes.map((type) => (
                        <option key={type} value={type}>
                            {type}
                        </option>
                    ))}
                </select>
                {errors.farmType && (
                    <p className="mt-1 text-sm text-red-600">{errors.farmType}</p>
                )}
            </div>

            <div className="flex justify-end">
                <Button onClick={handleNext}>
                    Next →
                </Button>
            </div>
        </div>
    );

    const renderStep2 = () => (
        <div className="space-y-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-4">Location Details</h2>

            <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                    Location Description *
                </label>
                <textarea
                    name="locationDiscription"
                    value={formData.locationDiscription}
                    onChange={handleChange}
                    placeholder="Provide detailed location information (e.g., Near River Road, Village X, District Y)"
                    rows="4"
                    className={`w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent ${errors.locationDiscription ? 'border-red-500' : 'border-gray-300'
                        }`}
                />
                {errors.locationDiscription && (
                    <p className="mt-1 text-sm text-red-600">{errors.locationDiscription}</p>
                )}
                <p className="mt-1 text-sm text-gray-500">
                    Include landmarks, village/city name, and district for better visibility
                </p>
            </div>

            <div className="flex justify-between">
                <Button onClick={handlePrevious} variant="secondary">
                    ← Previous
                </Button>
                <Button onClick={handleNext}>
                    Next →
                </Button>
            </div>
        </div>
    );

    const renderStep3 = () => (
        <div className="space-y-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-4">Verification</h2>

            <Input
                label="Government ID *"
                name="governmentId"
                value={formData.governmentId}
                onChange={handleChange}
                placeholder="e.g., Aadhaar, PAN, or Farmer ID"
                error={errors.governmentId}
                helperText="This helps verify your identity and build trust with buyers"
            />

            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <h3 className="font-semibold text-blue-900 mb-2">📋 Review Your Information</h3>
                <div className="space-y-2 text-sm text-blue-800">
                    <p><strong>Farm Name:</strong> {formData.farmName}</p>
                    <p><strong>Farm Type:</strong> {formData.farmType}</p>
                    <p><strong>Location:</strong> {formData.locationDiscription}</p>
                    <p><strong>Government ID:</strong> {formData.governmentId}</p>
                </div>
            </div>

            <div className="flex justify-between">
                <Button onClick={handlePrevious} variant="secondary">
                    ← Previous
                </Button>
                <Button onClick={handleSubmit} loading={loading}>
                    {loading ? 'Creating Profile...' : 'Complete Registration'}
                </Button>
            </div>
        </div>
    );

    return (
        <div className="min-h-screen bg-gray-50 py-12 px-4">
            <div className="max-w-3xl mx-auto">
                <div className="text-center mb-8">
                    <h1 className="text-4xl font-bold text-gray-900 mb-2">
                        Become a Farmer
                    </h1>
                    <p className="text-gray-600">
                        Start selling your farm products to buyers across the platform
                    </p>
                </div>

                <Card>
                    {renderProgressBar()}

                    <form onSubmit={handleSubmit}>
                        {currentStep === 1 && renderStep1()}
                        {currentStep === 2 && renderStep2()}
                        {currentStep === 3 && renderStep3()}
                    </form>
                </Card>

                <div className="mt-6 text-center text-sm text-gray-600">
                    <p>Already have a farmer profile? <a href="/farmer/profile" className="text-green-600 hover:underline">View Profile</a></p>
                </div>
            </div>
        </div>
    );
};

export default BecomeFarmer;
