import React, { useState } from 'react';
import { mlAPI } from '../../utils/api';
import Card from '../../components/common/Card';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import './ML.css';

const FertilizerRecommendation = () => {
    const [formData, setFormData] = useState({
        temparature: '',
        humidity: '',
        moisture: '',
        nitrogen: '',
        potassium: '',
        phosphorous: '',
        soil_type: '',
        crop_type: '',
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
            const response = await mlAPI.fertilizerRecommendation(formData);
            setResult(response.response);
        } catch {
            setError('Failed to get recommendation. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="ml-page">
            <div className="container">
                <div className="ml-header">
                    <h1>🧪 Fertilizer Recommendation</h1>
                    <p>Get precise fertilizer suggestions for optimal crop growth</p>
                </div>

                <div className="ml-content">
                    <Card className="ml-form-card">
                        <h3>Enter Soil & Crop Information</h3>
                        <form onSubmit={handleSubmit} className="ml-form">
                            <div className="form-grid">
                                <Input
                                    label="Temperature (°C)"
                                    type="number"
                                    name="temparature"
                                    value={formData.temparature}
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
                                    label="Moisture"
                                    type="number"
                                    name="moisture"
                                    value={formData.moisture}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Nitrogen (N)"
                                    type="number"
                                    name="nitrogen"
                                    value={formData.nitrogen}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Potassium (K)"
                                    type="number"
                                    name="potassium"
                                    value={formData.potassium}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Phosphorous (P)"
                                    type="number"
                                    name="phosphorous"
                                    value={formData.phosphorous}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Soil Type"
                                    type="text"
                                    name="soil_type"
                                    value={formData.soil_type}
                                    onChange={handleChange}
                                    placeholder="e.g., Sandy, Loamy, Clay"
                                    required
                                    fullWidth
                                />
                                <Input
                                    label="Crop Type"
                                    type="text"
                                    name="crop_type"
                                    value={formData.crop_type}
                                    onChange={handleChange}
                                    placeholder="e.g., Rice, Wheat, Cotton"
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
                                <h2>Recommended Fertilizer</h2>
                                <div className="result-value">{result}</div>
                                <p>Based on your soil and crop conditions, we recommend using {result} fertilizer.</p>
                            </div>
                        </Card>
                    )}
                </div>
            </div>
        </div>
    );
};

export default FertilizerRecommendation;
