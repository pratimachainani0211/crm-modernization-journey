# shared-base.Dockerfile - Build this first
FROM openjdk:17-jdk-slim AS shared-builder

WORKDIR /app

# Copy dependency files for caching
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
COPY settings.gradle build.gradle ./
COPY shared-lib/build.gradle shared-lib/build.gradle

RUN chmod +x gradlew

# Download all dependencies first (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy and build shared library
COPY shared-lib/src/ shared-lib/src/
RUN ./gradlew :shared-lib:publishToMavenLocal --no-daemon

# Export the maven repository
FROM scratch AS shared-artifacts
COPY --from=shared-builder /root/.m2 /root/.m2