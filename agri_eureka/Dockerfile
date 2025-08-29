# Stage 1: Build the jar using Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

LABEL authors="pranjal"

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/agri_eureka-0.0.1-SNAPSHOT.jar app.jar

# Expose Eureka server port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
