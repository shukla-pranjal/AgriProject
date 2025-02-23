from flask import Flask, request, jsonify
import joblib
import numpy as np

# Load Model
m5 = joblib.load(open("ML_py/m5.pkl", "rb"))
app = Flask(__name__)

@app.route("/predict/5", methods=["POST"])
def predict():
    try:
        data = request.get_json()
        if not data:
            return jsonify({"error": "No data provided"}), 400

        required_fields = ["N", "P", "K", "temperature", "humidity", "ph", "rainfall"]
        missing_fields = [field for field in required_fields if field not in data]
        if missing_fields:
            return jsonify({"error": f"Missing fields: {', '.join(missing_fields)}"}), 400

        features = np.array([[data["N"], data["P"], data["K"], data["temperature"],
                              data["humidity"], data["ph"], data["rainfall"]]])

        prediction = m5.predict(features)
        return jsonify({"crop": prediction[0]})

    except Exception as e:
        print("Error:", traceback.format_exc()) # for debugging
        return jsonify({"error": f"Something went wrong: {str(e)}"}), 500



if __name__ == "__main__":
    app.run(port=5000)
