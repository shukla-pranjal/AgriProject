import React, { useState } from 'react';
import { mlAPI } from '../../utils/api';
import Card from '../../components/common/Card';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import './ML.css';

const PumpControl = () => {
    const [formData, setFormData] = useState({
        moisture: '',
        temp: '',
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
            const response = await mlAPI.pumpControl(formData);
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
                    <h1>💧 Smart Pump Control</h1>
                    <p>AI-powered irrigation system to optimize water usage</p>
                </div>

                <div className="ml-content">
                    <Card className="ml-form-card">
                        <h3>Enter Sensor Data</h3>
                        <form onSubmit={handleSubmit} className="ml-form">
                            <Input
                                label="Soil Moisture Level"
                                type="number"
                                name="moisture"
                                value={formData.moisture}
                                onChange={handleChange}
                                required
                                fullWidth
                            />
                            <Input
                                label="Temperature (°C)"
                                type="number"
                                name="temp"
                                value={formData.temp}
                                onChange={handleChange}
                                required
                                fullWidth
                            />

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
                                <div className={`result-icon ${result === 'ON' ? 'pump-on' : 'pump-off'}`}>
                                    {result === 'ON' ? '💦' : '🚫'}
                                </div>
                                <h2>Pump Status</h2>
                                <div className={`result-value ${result === 'ON' ? 'status-on' : 'status-off'}`}>
                                    {result}
                                </div>
                                <p>
                                    {result === 'ON'
                                        ? 'Turn the irrigation pump ON to water your crops.'
                                        : 'Keep the irrigation pump OFF. Soil moisture is adequate.'}
                                </p>
                            </div>
                        </Card>
                    )}
                </div>
            </div>
        </div>
    );
};

export default PumpControl;
