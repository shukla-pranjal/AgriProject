#!/usr/bin/env bash

# Color codes for output styling
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0;m' # No Color

echo -e "${GREEN}🌾 Starting AgriProject Development Services...${NC}"

# Check for Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed or not in PATH.${NC}"
    exit 1
fi

# Helper function to run spring-boot:run using local wrapper or global mvn
run_maven() {
    local log_file=$1
    if [ -f "./mvnw" ] && [ -d ".mvn/wrapper" ]; then
        chmod +x ./mvnw
        ./mvnw spring-boot:run > "$log_file" 2>&1 &
        echo $!
    elif command -v mvn &> /dev/null; then
        mvn spring-boot:run > "$log_file" 2>&1 &
        echo $!
    else
        echo -e "${RED}Error: Neither a working ./mvnw nor global mvn was found in $(pwd)${NC}" >&2
        return 1
    fi
}

# 1. Start Eureka Server
echo -e "${YELLOW}🚀 Starting Eureka Server (Port 8761)...${NC}"
cd backend/eureka || exit 1
EUREKA_PID=$(run_maven eureka.log)
cd ../..

# Wait for Eureka to be up
echo -e "${BLUE}Waiting 8 seconds for Eureka Server to initialize...${NC}"
sleep 8

# 2. Start API Gateway
echo -e "${YELLOW}🚀 Starting API Gateway (Port 8080)...${NC}"
cd backend/api-gateway || exit 1
GATEWAY_PID=$(run_maven gateway.log)
cd ../..

# 3. Start Main Backend (FarmFlow)
echo -e "${YELLOW}🚀 Starting FarmFlow Backend (Port 8081)...${NC}"
cd backend/farmflow || exit 1
FARMFLLOW_PID=$(run_maven farmflow.log)
cd ../..

# 4. Start ML Service
if command -v python3 &> /dev/null; then
    echo -e "${YELLOW}🚀 Preparing Python ML Service (Port 5000)...${NC}"
    cd ml || exit 1
    # Resolve virtual environment path (relative to ml directory)
    VENV_TO_ACTIVATE=""
    if [ -d "../../python_env/agri_env" ]; then
        VENV_TO_ACTIVATE="$(realpath ../../python_env/agri_env)"
    elif [ -d "../python_env/agri_env" ]; then
        VENV_TO_ACTIVATE="$(realpath ../python_env/agri_env)"
    elif [ -d "../../python_env/venv_epics_01" ]; then
        VENV_TO_ACTIVATE="$(realpath ../../python_env/venv_epics_01)"
    elif [ -d "../python_env/venv_epics_01" ]; then
        VENV_TO_ACTIVATE="$(realpath ../python_env/venv_epics_01)"
    elif [ -d "venv" ]; then
        VENV_TO_ACTIVATE="$(realpath venv)"
    fi

    if [ -n "$VENV_TO_ACTIVATE" ]; then
        echo -e "${BLUE}Activating virtual environment: $VENV_TO_ACTIVATE${NC}"
        source "$VENV_TO_ACTIVATE/bin/activate"
    else
        echo -e "${BLUE}Creating virtual environment and installing Python dependencies...${NC}"
        python3 -m venv venv
        source venv/bin/activate
        pip install --upgrade pip
        pip install -r requirements.txt
    fi
    python3 app.py > ml.log 2>&1 &
    ML_PID=$!
    cd ..
else
    echo -e "${RED}Warning: Python 3 not found. ML service not started.${NC}"
fi

# 5. Start Frontend
if command -v npm &> /dev/null; then
    echo -e "${YELLOW}🚀 Starting Frontend Development Server...${NC}"
    cd frontend || exit 1
    npm run dev > frontend.log 2>&1 &
    FRONTEND_PID=$!
    cd ..
else
    echo -e "${RED}Warning: npm not found. Frontend not started.${NC}"
fi

echo -e "${GREEN}====================================================${NC}"
echo -e "${GREEN}All services are running in the background!${NC}"
echo -e "${GREEN}- Eureka Server:      http://localhost:8761${NC}"
echo -e "${GREEN}- API Gateway:        http://localhost:8080${NC}"
echo -e "${GREEN}- FarmFlow Backend:   http://localhost:8081${NC}"
echo -e "${GREEN}- ML Service:         http://localhost:5000${NC}"
echo -e "${GREEN}- React Frontend:     http://localhost:5173${NC}"
echo -e "${GREEN}====================================================${NC}"
echo -e "${BLUE}Logs are saved to:${NC}"
echo -e "  - backend/eureka/eureka.log"
echo -e "  - backend/api-gateway/gateway.log"
echo -e "  - backend/farmflow/farmflow.log"
echo -e "  - ml/ml.log"
echo -e "  - frontend/frontend.log"
echo -e ""
echo -e "${YELLOW}Press Ctrl+C to stop all services...${NC}"

# Helper function to kill process on a port
kill_port() {
    local port=$1
    local pids=$(lsof -t -i :"$port")
    if [ -n "$pids" ]; then
        kill -9 $pids 2>/dev/null
    fi
}

# Handle cleanup on Ctrl+C or script termination
cleanup() {
    echo -e "\n${RED}🛑 Stopping all background services...${NC}"
    kill_port 8761
    kill_port 8080
    kill_port 8081
    kill_port 5000
    kill_port 5173
    exit 0
}

trap cleanup SIGINT SIGTERM

# Keep the script running to keep process monitors active
while true; do
    sleep 1
done
