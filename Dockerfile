# Etapa 1: Construcción del proyecto
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiamos el contenido del proyecto
COPY . .

# Compilamos el proyecto (sin tests opcionalmente con -DskipTests)
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con Java 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos el JAR construido en la etapa anterior
COPY --from=builder /app/target/SudokuV1-1.0-SNAPSHOT.jar app.jar

# Comando para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
