import React, { useState, useEffect } from 'react';
import { addressAPI } from '../utils/api';
import { getUser } from '../utils/auth';
import Card from './common/Card';
import Button from './common/Button';
import Modal from './common/Modal';
import Input from './common/Input';
import Loading from './common/Loading';
import './AddressManager.css';

const STATES = [
    { id: 1, name: 'Andhra Pradesh' }, { id: 2, name: 'Arunachal Pradesh' }, { id: 3, name: 'Assam' },
    { id: 4, name: 'Bihar' }, { id: 5, name: 'Chhattisgarh' }, { id: 6, name: 'Goa' },
    { id: 7, name: 'Gujarat' }, { id: 8, name: 'Haryana' }, { id: 9, name: 'Himachal Pradesh' },
    { id: 10, name: 'Jharkhand' }, { id: 11, name: 'Karnataka' }, { id: 12, name: 'Kerala' },
    { id: 13, name: 'Madhya Pradesh' }, { id: 14, name: 'Maharashtra' }, { id: 15, name: 'Manipur' },
    { id: 16, name: 'Meghalaya' }, { id: 17, name: 'Mizoram' }, { id: 18, name: 'Nagaland' },
    { id: 19, name: 'Odisha' }, { id: 20, name: 'Punjab' }, { id: 21, name: 'Rajasthan' },
    { id: 22, name: 'Sikkim' }, { id: 23, name: 'Tamil Nadu' }, { id: 24, name: 'Telangana' },
    { id: 25, name: 'Tripura' }, { id: 26, name: 'Uttar Pradesh' }, { id: 27, name: 'Uttarakhand' },
    { id: 28, name: 'West Bengal' }, { id: 29, name: 'Andaman and Nicobar Islands' },
    { id: 30, name: 'Chandigarh' }, { id: 31, name: 'Dadra and Nagar Haveli' },
    { id: 32, name: 'Daman and Diu' }, { id: 33, name: 'Delhi' }, { id: 34, name: 'Jammu and Kashmir' },
    { id: 35, name: 'Ladakh' }, { id: 36, name: 'Lakshadweep' }, { id: 37, name: 'Puducherry' }
];

const ADDRESS_TYPES = [
    { id: 1, name: 'Home' }, { id: 2, name: 'Work' }, { id: 3, name: 'Other' }
];

const AddressManager = ({ onSelect, selectedId }) => {
    const user = getUser();
    const [addresses, setAddresses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [editingAddress, setEditingAddress] = useState(null);
    const [formData, setFormData] = useState({
        street: '',
        district: '',
        pinCode: '',
        state: '',
        addressType: ''
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        fetchAddresses();
    }, []);

    const fetchAddresses = async () => {
        try {
            setLoading(true);
            const response = await addressAPI.getMyAddresses();
            if (response.status === 'success') {
                setAddresses(response.data || []);
            }
        } catch (err) {
            console.error('Failed to fetch addresses:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
        if (errors[e.target.name]) {
            setErrors({ ...errors, [e.target.name]: '' });
        }
    };

    const validate = () => {
        const newErrors = {};
        if (!formData.street) newErrors.street = 'Street is required';
        if (!formData.district) newErrors.district = 'District is required';
        if (!formData.pinCode || !/^\d{6}$/.test(formData.pinCode)) {
            newErrors.pinCode = 'Valid 6-digit pincode required';
        }
        if (!formData.state) newErrors.state = 'State is required';
        if (!formData.addressType) newErrors.addressType = 'Address type is required';
        return newErrors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newErrors = validate();

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        try {
            const addressData = {
                ...formData,
                pinCode: parseInt(formData.pinCode),
                state: parseInt(formData.state),
                addressType: parseInt(formData.addressType),
                userId: user.id
            };

            if (editingAddress) {
                await addressAPI.update(editingAddress.id, addressData);
            } else {
                await addressAPI.create(addressData);
            }

            setShowModal(false);
            setEditingAddress(null);
            setFormData({ street: '', district: '', pinCode: '', state: '', addressType: '' });
            fetchAddresses();
        } catch (err) {
            setErrors({ submit: 'Failed to save address' });
        }
    };

    const handleEdit = (address) => {
        setEditingAddress(address);
        setFormData({
            street: address.street,
            district: address.district,
            pinCode: address.pinCode.toString(),
            state: address.state.toString(),
            addressType: address.addressType.toString()
        });
        setShowModal(true);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this address?')) return;

        try {
            await addressAPI.delete(id);
            fetchAddresses();
        } catch (err) {
            console.error('Failed to delete address:', err);
        }
    };

    const openAddModal = () => {
        setEditingAddress(null);
        setFormData({ street: '', district: '', pinCode: '', state: '', addressType: '' });
        setShowModal(true);
    };

    if (loading) return <Loading />;

    return (
        <div className="address-manager">
            <div className="address-header">
                <h3>Delivery Address</h3>
                <Button variant="primary" size="sm" onClick={openAddModal}>
                    + Add New Address
                </Button>
            </div>

            {addresses.length === 0 ? (
                <Card className="no-addresses">
                    <p>No addresses found. Please add a delivery address.</p>
                </Card>
            ) : (
                <div className="address-list">
                    {addresses.map((address) => (
                        <Card
                            key={address.id}
                            className={`address-card ${selectedId === address.id ? 'selected' : ''}`}
                            onClick={() => onSelect && onSelect(address)}
                        >
                            <div className="address-content">
                                <div className="address-type-badge">
                                    {ADDRESS_TYPES.find(t => t.id === address.addressType)?.name || 'Other'}
                                </div>
                                <p className="address-text">
                                    {address.street}, {address.district}
                                </p>
                                <p className="address-text">
                                    {STATES.find(s => s.id === address.state)?.name || ''} - {address.pinCode}
                                </p>
                            </div>
                            <div className="address-actions">
                                <button onClick={(e) => { e.stopPropagation(); handleEdit(address); }}>
                                    Edit
                                </button>
                                <button onClick={(e) => { e.stopPropagation(); handleDelete(address.id); }}>
                                    Delete
                                </button>
                            </div>
                            {selectedId === address.id && (
                                <div className="selected-indicator">✓ Selected</div>
                            )}
                        </Card>
                    ))}
                </div>
            )}

            <Modal
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                title={editingAddress ? 'Edit Address' : 'Add New Address'}
            >
                <form onSubmit={handleSubmit} className="address-form">
                    {errors.submit && <div className="alert alert-error">{errors.submit}</div>}

                    <Input
                        label="Street Address"
                        name="street"
                        value={formData.street}
                        onChange={handleChange}
                        error={errors.street}
                        placeholder="House no., Building name, Street"
                        fullWidth
                    />

                    <Input
                        label="District/City"
                        name="district"
                        value={formData.district}
                        onChange={handleChange}
                        error={errors.district}
                        placeholder="Enter district or city"
                        fullWidth
                    />

                    <Input
                        label="Pincode"
                        name="pinCode"
                        type="number"
                        value={formData.pinCode}
                        onChange={handleChange}
                        error={errors.pinCode}
                        placeholder="6-digit pincode"
                        fullWidth
                    />

                    <div className="form-row">
                        <div className="input-wrapper input-full">
                            <label className="input-label">State</label>
                            <select
                                name="state"
                                value={formData.state}
                                onChange={handleChange}
                                className={`input ${errors.state ? 'input-error' : ''}`}
                            >
                                <option value="">Select State</option>
                                {STATES.map(state => (
                                    <option key={state.id} value={state.id}>{state.name}</option>
                                ))}
                            </select>
                            {errors.state && <span className="input-error-text">{errors.state}</span>}
                        </div>

                        <div className="input-wrapper input-full">
                            <label className="input-label">Address Type</label>
                            <select
                                name="addressType"
                                value={formData.addressType}
                                onChange={handleChange}
                                className={`input ${errors.addressType ? 'input-error' : ''}`}
                            >
                                <option value="">Select Type</option>
                                {ADDRESS_TYPES.map(type => (
                                    <option key={type.id} value={type.id}>{type.name}</option>
                                ))}
                            </select>
                            {errors.addressType && <span className="input-error-text">{errors.addressType}</span>}
                        </div>
                    </div>

                    <div className="form-actions">
                        <Button type="button" variant="outline" onClick={() => setShowModal(false)}>
                            Cancel
                        </Button>
                        <Button type="submit" variant="primary">
                            {editingAddress ? 'Update' : 'Add'} Address
                        </Button>
                    </div>
                </form>
            </Modal>
        </div>
    );
};

export default AddressManager;
