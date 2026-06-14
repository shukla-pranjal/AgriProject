from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import numpy as np
import pandas as pd
import traceback
import py_eureka_client.eureka_client as eureka_client

ML_REST_PORT = 5000

import os

eureka_server = os.environ.get("EUREKA_SERVER", "http://localhost:8761/eureka")

eureka_client.init(
    eureka_server=eureka_server,
    app_name="ML-SERVICE",
    instance_port=ML_REST_PORT
)

# Initialize Flask App
app = Flask(__name__)
CORS(app)

import os

# Load Models
def load_file(filename):
    for path in [filename, os.path.join("ML_py", filename), os.path.join("ml", filename)]:
        if os.path.exists(path):
            return joblib.load(path)
    return joblib.load(filename)

models = {
    "5": load_file("model5.pkl"),
    "6": load_file("model6.pkl"),
    "7": load_file("model7.pkl"),
}
label_encoder7 = load_file("label_encoder7.pkl")  # For Model 7 (Fertilizer)

# Define Required Fields
REQUIRED_FIELDS = {
    "5": ["n", "p", "k", "temperature", "humidity", "ph", "rainfall"],
    "6": ["moisture", "temp"],
    "7": ['temparature', 'humidity', 'moisture', 'nitrogen', 'potassium', 'phosphorous', 'soil_type', 'crop_type']
}

def process_input(data, required_fields):
    """ Converts input data into the correct format for the model. """
    try:
        if "soil_type" in required_fields:  # Model 7 (categorical + numerical)
            features = pd.DataFrame([data], columns=required_fields)  # Convert to DataFrame
        else:
            features = np.array([[float(data[field]) for field in required_fields]])  # Convert to NumPy array
        return features
    except ValueError:
        return None

# Dynamic Prediction Endpoint
@app.route("/predict/<model_id>", methods=["POST"])
def predict(model_id):
    try:
        if model_id not in models:
            return jsonify({"error": f"Model {model_id} not found"}), 400

        data = request.get_json()
        if not data:
            return jsonify({"error": "No data provided"}), 400

        # Process Input Data
        features = process_input(data, REQUIRED_FIELDS[model_id])
        if features is None:
            return jsonify({"error": "Invalid data format. Please provide valid inputs."}), 400

        # Make Prediction
        model = models[model_id]
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



@app.route('/test', methods=['GET'])
def test():
    return jsonify({
        "service": "ML Service",
        "status": "running"
    }), 200
    
    
    
# Run Flask App
if __name__ == "__main__":
    app.run(port=ML_REST_PORT, debug=True, host='0.0.0.0')
