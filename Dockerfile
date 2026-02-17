# Multi-stage build for Forum service
# Stage 1: Build stage
FROM maven:3.9-eclipse-temurin-25-noble AS build

WORKDIR /workspace

# 1. Copia o código dos dois módulos inteiros
COPY common ./common
COPY forum ./forum

# 2. Instala o módulo Common primeiro (para que o Forum consiga achá-lo)
WORKDIR /workspace/common
RUN mvn clean install -DskipTests

# 3. Compila o Forum usando o common que acabou de ser instalado
WORKDIR /workspace/forum
RUN mvn clean package -DskipTests


# Stage 2: Runtime stage
FROM eclipse-temurin:25-jre-noble

WORKDIR /app

# Create a non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the built jar from build stage
COPY --from=build /workspace/forum/target/*.jar app.jar

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Set JVM options for container environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]