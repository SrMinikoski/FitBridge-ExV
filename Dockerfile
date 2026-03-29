# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy dependency manifest first to leverage Docker layer cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the fat JAR (skip tests during image build)
COPY src ./src
RUN mvn package -DskipTests -B

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Non-root user for security
RUN addgroup -S fitbridge && adduser -S fitbridge -G fitbridge

COPY --from=build /app/target/fitbridge-0.0.1-SNAPSHOT.jar app.jar

# Copy static public assets (exercise images, etc.)
COPY public ./public

RUN chown -R fitbridge:fitbridge /app
USER fitbridge

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
