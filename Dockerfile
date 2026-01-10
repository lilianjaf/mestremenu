# Multi-stage Dockerfile for Spring Boot (Gradle) with Java 21

# ===== Stage 1: Build (JDK) =====
FROM gradle:9.2.1-jdk21-alpine AS builder

WORKDIR /home/gradle/src

# Cache dependencies
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle --version >/dev/null 2>&1

# Copy sources
COPY src src

# Build application (skip tests for faster image builds)
RUN gradle clean bootJar -x test --no-daemon


# ===== Stage 2: Runtime (JRE) =====
FROM eclipse-temurin:21-jre-alpine

ENV APP_HOME=/app \
    JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod

WORKDIR ${APP_HOME}

# Create non-root user and group for security
RUN apk add --no-cache wget && \
    addgroup -S app && adduser -S app -G app

# Copy built jar from builder stage
COPY --from=builder /home/gradle/src/build/libs/*.jar app.jar

# Ensure correct permissions
RUN chown -R app:app ${APP_HOME}

USER app

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
