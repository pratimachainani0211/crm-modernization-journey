# Phase 1: API Gateway Dockerfile
FROM openjdk:17-jdk-slim

# Install curl for health checks and debugging
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy gradle wrapper and build files (for better layer caching)
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies first (better Docker layer caching)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew build -x test --no-daemon

# Copy the executable JAR (avoiding plain JAR)
RUN cp build/libs/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose API Gateway port
EXPOSE 8000

# Health check for API Gateway
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8000/actuator/health || exit 1

# Run the API Gateway application
ENTRYPOINT ["java", "-jar", "app.jar"]