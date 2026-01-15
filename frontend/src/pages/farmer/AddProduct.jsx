import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { productAPI } from '../../utils/api';
import { getUser } from '../../utils/auth';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import './AddProduct.css';

const AddProduct = () => {
    const navigate = useNavigate();
    const user = getUser();
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        price: '',
        quantity: '',
        unit: 'KG',
        categoryId: '',
        expiryDate: ''
    });
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);

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
        if (!formData.name) newErrors.name = 'Product name is required';
        if (!formData.description) newErrors.description = 'Description is required';
        if (!formData.price || formData.price <= 0) newErrors.price = 'Valid price is required';
        if (!formData.quantity || formData.quantity <= 0) newErrors.quantity = 'Valid quantity is required';
        if (!formData.unit) newErrors.unit = 'Unit is required';
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
            setLoading(true);
            const productData = {
                ...formData,
                price: parseFloat(formData.price),
                quantity: parseInt(formData.quantity),
                categoryId: formData.categoryId ? parseInt(formData.categoryId) : null,
                farmerId: user.farmerId,
                available: true
            };

            await productAPI.create(productData);
            navigate('/farmer/products');
        } catch {
            setErrors({ submit: 'Failed to add product' });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="add-product-page">
            <div className="container">
                <h1>Add New Product</h1>

                <Card className="product-form-card">
                    <form onSubmit={handleSubmit}>
                        {errors.submit && <div className="alert alert-error">{errors.submit}</div>}

                        <Input
                            label="Product Name"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            error={errors.name}
                            placeholder="e.g., Organic Tomatoes"
                            fullWidth
                        />

                        <div className="form-row">
                            <Input
                                label="Price (₹)"
                                type="number"
                                name="price"
                                value={formData.price}
                                onChange={handleChange}
                                error={errors.price}
                                placeholder="0.00"
                                step="0.01"
                                fullWidth
                            />

                            <div className="input-wrapper input-full">
                                <label className="input-label">Unit</label>
                                <select
                                    name="unit"
                                    value={formData.unit}
                                    onChange={handleChange}
                                    className="input"
                                >
                                    <option value="KG">Kilogram (KG)</option>
                                    <option value="GRAM">Gram (G)</option>
                                    <option value="LITER">Liter (L)</option>
                                    <option value="PIECE">Piece</option>
                                    <option value="DOZEN">Dozen</option>
                                </select>
                            </div>
                        </div>

                        <Input
                            label="Quantity"
                            type="number"
                            name="quantity"
                            value={formData.quantity}
                            onChange={handleChange}
                            error={errors.quantity}
                            placeholder="Available quantity"
                            fullWidth
                        />

                        <div className="input-wrapper input-full">
                            <label className="input-label">Description</label>
                            <textarea
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                className={`input ${errors.description ? 'input-error' : ''}`}
                                rows="4"
                                placeholder="Describe your product..."
                            />
                            {errors.description && <span className="input-error-text">{errors.description}</span>}
                        </div>

                        <Input
                            label="Expiry Date (Optional)"
                            type="date"
                            name="expiryDate"
                            value={formData.expiryDate}
                            onChange={handleChange}
                            fullWidth
                        />

                        <div className="form-actions">
                            <Button type="button" variant="outline" onClick={() => navigate('/farmer/products')}>
                                Cancel
                            </Button>
                            <Button type="submit" variant="primary" loading={loading}>
                                Add Product
                            </Button>
                        </div>
                    </form>
                </Card>
            </div>
        </div>
    );
};

export default AddProduct;
