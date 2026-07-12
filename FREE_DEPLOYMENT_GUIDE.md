# Free Deployment Guide for Agri-Project

Follow these step-by-step instructions to deploy your entire stack for **free**. We have optimized the code to bypass `api-gateway` and `eureka` to ensure the backend fits within free tier memory limits.

## 1. Database (MySQL) -> Aiven (Free)
Aiven provides a generous free MySQL database.
1. Go to [Aiven.io](https://aiven.io/) and create a free account.
2. Click **Create Service** -> **MySQL** -> Select the **Free Plan**.
3. Choose a region closest to you.
4. Once created, copy the **Service URI** (Connection String). It will look like this:
   `mysql://avnadmin:password@hostname:port/defaultdb?ssl-mode=REQUIRED`
5. Note down the username, password, hostname, and port.

## 2. Machine Learning Service -> Render (Free)
1. Go to [Render.com](https://render.com/) and connect your GitHub account.
2. Click **New** -> **Web Service**.
3. Select your `agri-project` repository.
4. Set the **Root Directory** to `ml`.
5. Ensure the **Build Command** is `pip install -r requirements.txt` (or if it uses Docker, Render will auto-detect the `Dockerfile`).
6. Set the **Start Command** to `python app.py` (or `flask run --host=0.0.0.0 --port=10000`).
   > *Note: Make sure your `app.py` is configured to listen on `0.0.0.0` and the `$PORT` provided by Render.*
7. Click **Create Web Service**. Once deployed, copy the Render URL (e.g., `https://agri-ml.onrender.com`).

## 3. Backend (Farmflow) -> Render (Free)
1. In Render, click **New** -> **Web Service**.
2. Select your `agri-project` repository.
3. Set the **Root Directory** to `backend/farmflow`.
4. Choose **Docker** as the Runtime (since you have a `Dockerfile` in `backend/farmflow`, or just use the Java runtime if you prefer Maven/Gradle).
   - *If using Docker*: Render will automatically build the image.
   - *If using Native Java*: Set Build Command to `./mvnw clean package -DskipTests` and Start Command to `java -jar target/*.jar`.
5. **Environment Variables**:
   - `DB_PASSWORD`: (Your Aiven MySQL password)
   - `SPRING_DATASOURCE_URL`: `jdbc:mysql://<aiven-hostname>:<aiven-port>/defaultdb?useSSL=true`
   - `SPRING_DATASOURCE_USERNAME`: (Your Aiven username, usually `avnadmin`)
   - `GATEWAY_VALIDATION_ENABLED`: `false`
   - `EUREKA_ENABLED`: `false`
6. Click **Create Web Service**. Once deployed, copy the Render URL (e.g., `https://agri-backend.onrender.com`).

## 4. Frontend (Vite) -> Vercel (Free)
1. Go to [Vercel.com](https://vercel.com/) and connect your GitHub account.
2. Click **Add New** -> **Project**.
3. Import your `agri-project` repository.
4. **Important**: Set the **Root Directory** to `frontend`.
5. Under **Environment Variables**, add the following:
   - `VITE_BACKEND_URL`: (The URL of your Farmflow backend from Render, e.g., `https://agri-backend.onrender.com`)
   - `VITE_ML_BASE_URL`: (The URL of your ML service from Render, e.g., `https://agri-ml.onrender.com`)
6. Click **Deploy**.

> **Note**: Render free web services spin down after 15 minutes of inactivity. When you make the first request after a period of inactivity, it might take 30-50 seconds to wake up. This is normal for free tiers!
