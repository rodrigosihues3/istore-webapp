# Usamos una imagen base ligera con Java 21 (JRE es suficiente para correr)
FROM eclipse-temurin:21-jre-alpine

# Añadimos un usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos el .jar generado (asegúrate de hacer mvn clean package antes)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Ejecutamos la app limitando la RAM a 400MB (Vital para tu e2-micro)
ENTRYPOINT ["java", "-Xms256m", "-Xmx400m", "-jar", "/app.jar"]