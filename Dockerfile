# Usa una imagen base con Java 17 (o la versión que uses)
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo jar generado en target/
# Ajusta el nombre del jar según tu proyecto
COPY target/*.jar app.jar

# Expone el puerto de Render (usa la variable de entorno PORT que Render asigna)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]