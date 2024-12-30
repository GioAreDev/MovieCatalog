# Usa una imagen base de Java
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR generado por Gradle al contenedor
COPY MovieCatalog-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto por donde escuchará Spring Boot (ajústalo según tu configuración)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
