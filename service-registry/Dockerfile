# Stage 1: Build the application using a Maven container
FROM maven:3.8.5-openjdk-17 AS build
# Set the working directory
WORKDIR /app
# Copy the Maven project file
COPY pom.xml .
# Copy the rest of the source code
COPY src ./src
# Package the application, skipping tests for a faster build
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal runtime image using OpenJDK
FROM openjdk:17-jdk-slim
# Set the working directory
WORKDIR /app
# Copy the executable JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
# Expose the port the application runs on
EXPOSE 9002
# Command to run the application
ENTRYPOINT ["java","-jar","app.jar"]