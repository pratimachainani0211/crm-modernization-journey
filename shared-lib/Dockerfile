# shared-lib/Dockerfile
FROM openjdk:17-slim AS builder

WORKDIR /app

# Copy gradle wrapper and root configuration
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
COPY settings.gradle build.gradle ./

# Copy shared-lib specific files
COPY shared-lib/build.gradle shared-lib/build.gradle
COPY shared-lib/settings.gradle shared-lib/settings.gradle
COPY shared-lib/src/ shared-lib/src/

RUN chmod +x gradlew

# Build and publish shared library to local Maven repository
RUN ./gradlew :shared-lib:publishToMavenLocal --no-daemon

# Export the Maven repository as an artifact
FROM scratch AS maven-repo
COPY --from=builder /root/.m2/repository /maven-repo