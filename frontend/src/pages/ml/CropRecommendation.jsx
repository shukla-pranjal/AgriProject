import React, { useState } from 'react';
import { mlAPI } from '../../utils/api';
import Card from '../../components/common/Card';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import './ML.css';

const CropRecommendation = () => {
    const [formData, setFormData] = useState({
        n: '',
        p: '',
        k: '',
        temperature: '',
        humidity: '',
        ph: '',
        rainfall: '',
    });
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setResult(null);

        try {
            const response = await mlAPI.cropRecommendation(formData);
            setResult(response.response);
        } catch (err) {
            setError('Failed to get recommendation. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="ml-page">
            <div className="container">
                <div className="ml-header">
                    <h1>🌱 Crop Recommendation</h1>
                    <p>Get AI-powered crop suggestions based on soil and climate conditions</p>
                </div>

                <div className="ml-content">
                    <Card className="ml-form-card">
                        <h3>Enter Soil & Climate Parameters</h3>
                        <form onSubmit={handleSubmit} className="ml-form">
                            <div className="form-grid">
                                <Input
                                    label="Nitrogen (N)"
                                    type="number"
                                    name="n"
                                    value={formData.n}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Phosphorus (P)"
                                    type="number"
                                    name="p"
                                    value={formData.p}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Potassium (K)"
                                    type="number"
                                    name="k"
                                    value={formData.k}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Temperature (°C)"
                                    type="number"
                                    name="temperature"
                                    value={formData.temperature}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Humidity (%)"
                                    type="number"
                                    name="humidity"
                                    value={formData.humidity}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="pH Level"
                                    type="number"
                                    step="0.1"
                                    name="ph"
                                    value={formData.ph}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Rainfall (mm)"
                                    type="number"
                                    name="rainfall"
                                    value={formData.rainfall}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                            </div>

                            <Button type="submit" variant="primary" size="lg" fullWidth loading={loading}>
                                Get Recommendation
                            </Button>
                        </form>
                    </Card>

                    {error && (
                        <div className="alert alert-error">{error}</div>
                    )}

                    {result && (
                        <Card className="ml-result-card">
                            <div className="ml-result">
                                <div className="result-icon">✅</div>
                                <h2>Recommended Crop</h2>
                                <div className="result-value">{result}</div>
                                <p>Based on your soil and climate conditions, we recommend growing {result}.</p>
                            </div>
                        </Card>
                    )}
                </div>
            </div>
        </div>
    );
};

export default CropRecommendation;
