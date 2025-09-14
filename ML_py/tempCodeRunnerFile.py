
from flask import Flask, request, jsonify
import joblib
import numpy as np
import pandas as pd
import traceback
import py_eureka_client.eureka_client as eureka_client

ML_REST_PORT = 5000

eureka_client.init(
    eureka_server="http://localhost:8761/eureka",
    app_name="ML-SERVICE",
    instance_port=ML_REST_PORT
)

# Initialize Flask App
app = Flask(__name__)

models = {
    "5": joblib.load("ML_py/m5.pkl"),
    "6": joblib.load("ML_py/m6.pkl"),
    "7": joblib.load("ML_py/model7.pkl"),
}
label_encoder7 = joblib.load("ML_py/label_encoder7.pkl")  # For Model 7 (Fertilizer)

REQUIRED_FIELDS = {
    "5": ["N", "P", "K", "temperature", "humidity", "ph", "rainfall"],
    "6": ["moisture", "temp"],
    "7": ['temparature', 'humidity', 'moisture', 'nitrogen', 'potassium', 'phosphorous',  'soil_type', 'crop_type']
}

def process_input(data, required_fields):
    """ Validates and converts input data to NumPy array or DataFrame. """
    missing_fields = [field for field in required_fields if field not in data]
    if missing_fields:
        return None, jsonify({"error": f"Missing fields: {', '.join(missing_fields)}"}), 400

    try:
        if "soil_type" in required_fields:  # Model 7 uses categorical & numerical data
            for field in ['temparature', 'humidity', 'moisture', 'nitrogen',  'potassium', 'phosphorous']:
                data[field] = float(data[field])
            features = pd.DataFrame([data], columns=required_fields)  # Convert to DataFrame
        else:
            features = np.array([[float(data[field]) for field in required_fields]])  # Convert to NumPy array
        return features, None, None
    except ValueError:
        return None, jsonify({"error": "Invalid data format. Please provide numeric values."}), 400

# Dynamic Prediction Endpoint
@app.route("/predict/<model_id>", methods=["POST"])
def predict(model_id):
    try:
        # Check if Model Exists
        if model_id not in models:
            return jsonify({"error": f"Model {model_id} not found"}), 400

        model = models[model_id]
        data = request.get_json()
        if not data:
            return jsonify({"error": "No data provided"}), 400

        # Validate & Process Input
        features, error_response, status_code = process_input(data, REQUIRED_FIELDS[model_id])
        if error_response:
            return error_response, status_code

        # Make Prediction
        prediction = model.predict(features)

        # Handle Model-Specific Output Formatting
        if model_id == "6":
            response = {"response": "ON" if prediction[0] == 1 else "OFF"}
        elif model_id == "7":
            response = {"response": label_encoder7.inverse_transform([prediction[0]])[0]}
        else:
            response = {"response": str(prediction[0])}

        return jsonify(response)

    except Exception as e:
        print("Error:", traceback.format_exc())  # Debugging
        return jsonify({"error": f"Something went wrong: {str(e)}"}), 500

# Run Flask App
if __name__ == "__main__":
    app.run(port=ML_REST_PORT, debug=True)
