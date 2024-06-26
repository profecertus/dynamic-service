# Etapa 1: Construir con Maven
FROM maven:latest AS build
WORKDIR /app
COPY . /app
RUN mvn clean package

# Etapa 2: Crear el contenedor para despliegue
FROM openjdk:17-ea-oraclelinux8
WORKDIR /app
COPY --from=build /app/target/dynamic-service-0.0.1-SNAPSHOT.jar /app
EXPOSE 8182
#CMD ["java", "-cp", "ojdbc8.jar:GPService-0.0.1-SNAPSHOT.jar", "pe.com.isesystem.gpservice.GpServiceApplication"]
CMD ["java", "-jar", "dynamic-service-0.0.1-SNAPSHOT.jar"]
