# order-service/Dockerfile
FROM openjdk:17-slim AS builder

WORKDIR /app

# Copy everything needed for the build
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
COPY settings.gradle build.gradle ./

# Copy ALL modules (this is key!)
COPY shared-lib/ shared-lib/
COPY order-service/ order-service/

RUN chmod +x gradlew

# Build the entire project - this automatically handles shared-lib dependency
RUN ./gradlew :order-service:build -x test --no-daemon

# Runtime stage
FROM openjdk:17-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /app/order-service/build/libs/order-service-*.jar app.jar

EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]