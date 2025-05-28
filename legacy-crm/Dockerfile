# legacy-crm/Dockerfile - FIXED VERSION

FROM openjdk:17-jdk-slim

# Install curl for health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy gradle wrapper and build files first (for better layer caching)
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies first (better caching)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew build -x test --no-daemon

# FIXED: Copy the executable JAR (not the plain JAR)
RUN cp build/libs/legacy-crm-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]