# ═══════════════════════════════════════════════════════════════
#  Dockerfile — Despliegue en producción (Render)
# ═══════════════════════════════════════════════════════════════

# --- Etapa de compilación (Build) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Descargar dependencias para cachear capas
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Compilar el JAR ejecutable saltando pruebas unitarias
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Etapa de ejecución (Run) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el artefacto compilado
COPY --from=build /app/target/*.jar app.jar

# Configurar el perfil de producción por defecto
ENV JAVA_OPTS="-Dspring.profiles.active=prod"

# El puerto se inyecta por Render mediante la variable PORT
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
