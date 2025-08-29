# Stage 1: Build the jar using Maven
FROM maven:3.9.2-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/FarmFlow-0.0.1-SNAPSHOT.jar app.jar

# Expose server port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]



