from flask import Flask, request, jsonify
import joblib
import numpy as np

# Load Model
model = joblib.load(open("ML_py/m5.pkl", "rb"))

data ={'P': 25, 'rainfall': 130.0608093, 'temperature': 21.81167649, 'ph': 5.7941585039999985, 'humidity': 23.20591245, 'K': 80, 'N': 22} # Get JSON data from request

features = np.array([[data["N"], data["P"], data["K"], data["temperature"], 
                    data["humidity"], data["ph"], data["rainfall"]]])
print(features)

prediction = model.predict(features)  # Predict crop

print(prediction)