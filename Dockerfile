# Etapa de construcción
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar archivos de configuración de Maven primero para aprovechar la caché de capas
COPY pom.xml .

# Descargar dependencias (se ejecutará solo si pom.xml cambia)
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Copiar el código fuente
COPY src/ src/

# Compilar y empaquetar la aplicación
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear un usuario no privilegiado para ejecutar la aplicación
RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --ingroup appgroup appuser

# Copiar solo el JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Cambiar al usuario no privilegiado
USER appuser

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]