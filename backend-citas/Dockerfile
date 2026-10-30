# ============================================
# Etapa 1: BUILD (compila el JAR con Maven)
# ============================================
FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ============================================
# Etapa 2: RUNTIME (solo JRE + JAR)
# ============================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Usuario no-root por seguridad (UID 1001 funciona en Debian/Ubuntu/Alpine)
USER 1001

COPY --from=build --chown=1001:1001 /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]