#!/bin/bash

# Color codes for output formatting
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0;37m' # No Color

echo -e "${BLUE}====================================================${NC}"
echo -e "${BLUE}   🌾 AgriProject Microservices Health Check 🌾   ${NC}"
echo -e "${BLUE}====================================================${NC}"

# Variables to track overall status
ALL_PASSED=true

# Helper function to check if a port is listening
check_port() {
    local service_name=$1
    local port=$2
    local pid=$(lsof -t -i :"$port")
    
    if [ -n "$pid" ]; then
        echo -e "[ ${GREEN}PORT OK${NC} ] $service_name is listening on port $port (PID: $pid)"
        return 0
    else
        echo -e "[ ${RED}PORT FAIL${NC} ] $service_name is NOT listening on port $port"
        ALL_PASSED=false
        return 1
    fi
}

# Helper function to perform HTTP checks
check_endpoint() {
    local test_name=$1
    local url=$2
    local expected_keyword=$3
    local method=${4:-GET}
    local post_data=$5
    
    local response
    local http_code
    
    if [ "$method" == "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST -H "Content-Type: application/json" -d "$post_data" "$url")
    else
        response=$(curl -s -w "\n%{http_code}" "$url")
    fi
    
    http_code=$(echo "$response" | tail -n 1)
    body=$(echo "$response" | head -n -1)
    
    # Check if curl failed (http_code 000 or empty)
    if [ -z "$http_code" ] || [ "$http_code" -eq 000 ]; then
        echo -e "[ ${RED}HTTP FAIL${NC} ] $test_name: Connection refused at $url"
        ALL_PASSED=false
        return 1
    fi
    
    # Validate based on expected keyword or expected response codes
    if [[ "$body" == *"$expected_keyword"* ]]; then
        echo -e "[ ${GREEN}HTTP OK${NC} ] $test_name passed (HTTP $http_code)"
        return 0
    else
        echo -e "[ ${RED}HTTP FAIL${NC} ] $test_name: Expected keyword '$expected_keyword' not found in response (HTTP $http_code)"
        echo -e "           Response: $body"
        ALL_PASSED=false
        return 1
    fi
}

echo -e "\n${YELLOW}🔍 Step 1: Checking Active Ports...${NC}"
check_port "Eureka Discovery Server" 8761
check_port "API Gateway" 8080
check_port "FarmFlow Backend" 8081
check_port "Python ML Service" 5000
check_port "React Frontend" 5173

echo -e "\n${YELLOW}🔍 Step 2: Testing Microservice Endpoints...${NC}"

# 1. Test Eureka Server Page
check_endpoint "Eureka Dashboard" "http://localhost:8761/" "Eureka"

# 2. Test ML Service Directly
check_endpoint "ML Service Direct Health" "http://localhost:5000/test" "running"

# 3. Test API Gateway Health
check_endpoint "Gateway Health Check" "http://localhost:8080/actuator/health" "\"status\":\"UP\""

# 4. Test FarmFlow Backend Health (direct, bypasses gateway check for health endpoints)
check_endpoint "FarmFlow Direct Health" "http://localhost:8081/actuator/health" "\"status\":\"UP\""

echo -e "\n${YELLOW}🔍 Step 3: Testing Inter-Service Routing Through API Gateway...${NC}"

# 1. Test Gateway to ML Service routing
check_endpoint "Gateway -> ML Service Route" "http://localhost:8080/ml-service/test" "running"

# 2. Test Gateway to FarmFlow Auth routing
# This verifies gateway validation filter (X-Gateway-Request header verification)
# We expect 'User not found' indicating the request successfully bypassed the security filter and reached the User controller.
check_endpoint "Gateway -> FarmFlow Auth Route" \
               "http://localhost:8080/api/v1/auth/login" \
               "User not found" \
               "POST" \
               '{"email":"dummy_test@example.com","password":"test"}'

echo -e "\n${BLUE}====================================================${NC}"
if [ "$ALL_PASSED" = true ]; then
    echo -e "${GREEN}🎉 SUCCESS: All microservices are healthy and properly routed!${NC}"
else
    echo -e "${RED}⚠️ WARNING: Some services or routes failed verification. Check logs!${NC}"
fi
echo -e "${BLUE}====================================================${NC}"
