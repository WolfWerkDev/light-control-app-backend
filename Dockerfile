# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias primero
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código
COPY src ./src

# Construir el proyecto y crear el jar
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar el jar generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
