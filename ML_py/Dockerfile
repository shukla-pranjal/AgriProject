FROM python:3.10-slim

WORKDIR /app

# Install system dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    build-essential \
    && rm -rf /var/lib/apt/lists/*


COPY requirements.txt requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

COPY m5.py m5.py
COPY m5.pkl m5.pkl

EXPOSE 5000

CMD ["python3", "m5.py"]
