![Spring Boot](https://user-images.githubusercontent.com/84719774/129191080-723b3b46-4e0b-4aa5-8eb9-654c2c025b18.png)

# Light Control App - Backend

Este es el backend de **Light Control App**, desarrollado con **Spring Boot**. Proporciona servicios RESTful para gestionar dispositivos de iluminación de manera remota.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **JWT (JSON Web Token)**
- **PostgreSQL**
- **JPA/Hibernate**
- **Docker**

## 📌 Requisitos Previos

Asegúrate de tener instalado en tu sistema:
- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- PostgreSQL configurado con una base de datos para la aplicación

## ⚙️ Configuración del Proyecto

1. **Clonar el repositorio:**
   ```sh
   git clone https://github.com/tu-usuario/lightcontrol-backend.git
   cd lightcontrol-backend
   ```

2. **Configurar las credenciales en `application.yml`:**
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/lightcontrol
       username: tu_usuario
       password: tu_contraseña
     jpa:
       hibernate:
         ddl-auto: update
   jwt:
     secret: tu_secreto_seguro
   ```

3. **Construir y ejecutar la aplicación:**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## 🛠️ Endpoints Principales

### 🔑 Autenticación
- `POST /login` → Iniciar sesión (Devuelve un JWT)
- `POST /register` → Registrar un nuevo usuario

### 💡 Control de Luces
- `GET /lights` → Obtener todas las luces
- `POST /lights/{id}/toggle` → Encender o apagar una luz específica
- `PUT /lights/{id}` → Modificar una luz

### 👤 Gestión de Usuarios
- `GET /users` → Obtener todos los usuarios
- `GET /users/{id}` → Obtener un usuario por ID
- `DELETE /users/{id}` → Eliminar un usuario

## 🐳 Uso con Docker

Para ejecutar la aplicación con Docker, usa:
```sh
mvn clean package -DskipTests
```
Luego, construye y ejecuta el contenedor:
```sh
docker build -t lightcontrol-backend .
docker run -p 8080:8080 lightcontrol-backend
```

## 📜 Licencia
Este proyecto está bajo la Licencia MIT. Puedes usarlo y modificarlo libremente.

---
**Autor:** Tu Nombre | [LinkedIn](https://linkedin.com/in/tuperfil) | [GitHub](https://github.com/tu-usuario)

