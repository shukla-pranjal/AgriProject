from flask import Flask, request, jsonify
import joblib
import numpy as np

# Load Model
model = joblib.load(open("ML_py/m5.pkl", "rb"))

app = Flask(__name__)


@app.route("/predict", methods=["POST"])
def predict():
    try:
        data = request.json  # Get JSON data from request
        print(data)
        features = np.array([[data["N"], data["P"], data["K"], data["temperature"], 
                              data["humidity"], data["ph"], data["rainfall"]]])
        print(features)
        
        prediction = model.predict(features)  # Predict crop
        return jsonify({"crop": prediction[0]})  # Return crop name
    
    except Exception as e:
        return jsonify({"error": str(e)})


if __name__ == "__main__":
    app.run(port=5000)
