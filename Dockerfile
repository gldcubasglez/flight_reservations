# Utiliza una imagen base de Java 21
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor
COPY target/flight_reservations-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que tu aplicación escucha
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
