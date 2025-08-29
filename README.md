
# 🌾 AgriProject – FarmFlow with ML & Eureka

[![Docker Hub](https://img.shields.io/badge/Docker-Hub-blue?logo=docker)](https://hub.docker.com/u/pranjalkumar09)
[![GitHub Workflow](https://github.com/PranjalKumar09/agri-project/actions/workflows/docker.yml/badge.svg)](https://github.com/PranjalKumar09/agri-project/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

---

## 📖 Overview

**AgriProject (FarmFlow)** is a **microservice-based agriculture platform** built with:

* **Spring Boot** – Business logic & APIs
* **Python ML Models** – Prediction services (95%+ accuracy)
* **Eureka (Spring Cloud Netflix)** – Service discovery & registry
* **MySQL** – Relational database persistence
* **Docker & Docker Compose** – Containerized orchestration

The platform allows **farmers and users** to interact:

* 👨‍🌾 Farmers can **list products for sale**
* 🛒 Users can **search & buy products**
* 🔐 Supports **different roles** → Farmer, User, Admin

---

## 🚀 Features

* ⚡ **120+ REST APIs** implemented with **Spring Boot**
* 📦 **Caching** with [Caffeine](https://github.com/ben-manes/caffeine) for high performance
* 📝 **Logging** integrated for monitoring and debugging
* 📑 **Pagination & Searching** across product listings and APIs
* 🔐 **User modes** – Farmer, User, Admin with role-based access
* 📧 **Active Email Authentication** (via Spring Mail + Gmail SMTP)
* 🤖 **Machine Learning Service** – 3 models with **95%+ accuracy**
* 🌍 **Service Discovery** using Eureka
* 🗂️ **Swagger UI & API Docs** integrated (`/farmer_app_shopping-doc`)

---

## 🏗️ Architecture

The project is split into **3 microservices**:

1. **Eureka Service** → Service registry (`http://localhost:8761`)
2. **ML Service (Python)** → Model predictions (`http://localhost:5000`)
3. **FarmFlow App (Spring Boot)** → Core business logic (`http://localhost:8080`)

All services are orchestrated via **Docker Compose**.

---

## 📂 Project Structure

```
.
├── ML_py/                # ML Service (Python + Models)
│   ├── Dockerfile
│   ├── requirements.txt
│   ├── model.py
│   └── *.pkl
│
├── agri_eureka/          # Eureka Server (Spring Boot)
│   ├── Dockerfile
│   └── src/...
│
├── FarmFlow/             # Main Business Logic (Spring Boot)
│   ├── Dockerfile
│   └── src/...
│
├── docker-compose.yml
├── LICENSE
└── README.md
```

---

## 🐳 Running with Docker Compose

Make sure you have **Docker** and **Docker Compose** installed.

```bash
# Clone repository
git clone https://github.com/PranjalKumar09/agri-project.git
cd agri-project

# Start services
docker-compose up --build
```

This will start:

* **ML Service** → [http://localhost:5000](http://localhost:5000)
* **Eureka Server** → [http://localhost:8761](http://localhost:8761)
* **FarmFlow App** → [http://localhost:8080](http://localhost:8080)
* **MySQL** → localhost:3306

---

## 🔍 Health Check Endpoints

To quickly test services:

* **ML Service** → `http://localhost:5000/test`
* **Eureka** → `http://localhost:8761/test`
* **FarmFlow App** → `http://localhost:8080/test`

---

## ⚙️ Environment Variables

Already defined inside `docker-compose.yml`:

```yaml
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/agri
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=09072005
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8761/eureka/
```

---

## 🔮 Future Enhancements

* ☸️ **Kubernetes (K8s)** deployment for scalability
* 📊 **Grafana** & **Prometheus** for monitoring and observability
* 🎨 **React Frontend** for rich user experience
* 🛡️ **Enhanced Security** with JWT/OAuth2
* 📦 **CI/CD pipeline improvements** for production readiness

---

## 📝 License

This project is licensed under the **MIT License** – see [LICENSE](./LICENSE).

---

👉 GitHub Repo: [PranjalKumar09](https://github.com/PranjalKumar09)

