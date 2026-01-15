FROM python:3.10-slim
LABEL authors="pranjal"

WORKDIR /app

# Install system dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements first to cache dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy application code and models
COPY app.py .
COPY *.pkl ./

EXPOSE 5000

CMD ["python3", "app.py"]
