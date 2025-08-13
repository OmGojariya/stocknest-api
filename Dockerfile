# Use Maven image with OpenJDK 17
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .

# Download dependencies (for better Docker layer caching)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK 17 runtime for final image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/stocknest-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
CMD ["java", "-jar", "app.jar"]
