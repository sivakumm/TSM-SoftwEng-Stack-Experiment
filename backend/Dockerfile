# ==========================================
# Stage 1: Build and extract the application
# ==========================================
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /workspace/app

# 1. Copy Gradle wrapper and configuration files first for layer caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x ./gradlew

# 2. Download dependencies (cached unless build files change)
RUN ./gradlew dependencies --no-daemon

# 3. Copy source code and build the executable JAR
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# 4. Extract the JAR into Spring Boot layers
RUN java -Djarmode=tools -jar build/libs/*.jar extract --layers --launcher --destination extracted


# ==========================================
# Stage 2: Minimal Java 25 JRE Runtime
# ==========================================
FROM eclipse-temurin:25-jre-alpine AS runtime

WORKDIR /app

# Run as a non-root user
RUN addgroup -S spring && adduser -S -G spring spring
USER spring:spring

# Copy extracted layers from the builder container
COPY --from=builder /workspace/app/extracted/dependencies/ ./
COPY --from=builder /workspace/app/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/app/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/app/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
