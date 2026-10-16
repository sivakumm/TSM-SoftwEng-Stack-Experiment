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

# 5. Build a minimal custom JRE containing only the modules the application needs
#    (a flat extraction is used so jdeps can analyse the app jar together with its libraries)
RUN java -Djarmode=tools -jar build/libs/*.jar extract --destination flat \
 && jdeps --ignore-missing-deps -q --recursive --multi-release 25 \
      --print-module-deps --class-path 'flat/lib/*' flat/*.jar > modules.txt \
 && echo "Detected modules: $(cat modules.txt)" \
 && jlink --add-modules "$(cat modules.txt),jdk.crypto.cryptoki,jdk.management,jdk.unsupported,jdk.zipfs" \
      --strip-debug --no-man-pages --no-header-files --compress=zip-6 \
      --output /jre


# ==========================================
# Stage 2: Minimal runtime with custom JRE
# ==========================================
FROM alpine:3.22 AS runtime

ENV JAVA_HOME=/opt/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=builder /jre ${JAVA_HOME}

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

# Uses BusyBox wget (part of the Alpine base image), so no extra packages are required
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -q --spider http://127.0.0.1:8080/api/health || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
