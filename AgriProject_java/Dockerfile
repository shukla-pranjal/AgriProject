FROM openjdk:21-jdk

WORKDIR /app

COPY target/AgriProject-0.0.1-SNAPSHOT.jar /app/AgriProject-0.0.1-SNAPSHOT.jar

# port 8080 -- REST API
EXPOSE 8080

# Command to run the app (replace with your JAR file name)
ENTRYPOINT ["java", "-jar", "AgriProject-0.0.1-SNAPSHOT.jar"]