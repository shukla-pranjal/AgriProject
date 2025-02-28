from flask import Flask, request, jsonify
import pickle
import numpy as np

# Load Model
model = pickle.load(open("model.pkl", "rb"))

app = Flask(__name__)


@app.route("/predict", methods=["POST"])
def predict():
    try:
        data = request.json  # Get JSON data from request
        features = np.array([[data["N"], data["P"], data["K"], data["temperature"], 
                              data["humidity"], data["ph"], data["rainfall"]]])
        
        prediction = model.predict(features)  # Predict crop
        return jsonify({"crop": prediction[0]})  # Return crop name
    
    except Exception as e:
        return jsonify({"error": str(e)})


if __name__ == "__main__":
    app.run(port=5000)
