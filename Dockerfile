# Etapa 1: Compilar JAR con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con JavaFX
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Instalar JavaFX manualmente
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_linux-x64_bin-sdk.zip && \
    unzip openjfx-17.0.2_linux-x64_bin-sdk.zip && \
    mv javafx-sdk-17.0.2 /opt/javafx && \
    rm openjfx-17.0.2_linux-x64_bin-sdk.zip

# Copiar el JAR generado con todas las dependencias
COPY --from=builder /app/target/SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Comando para ejecutar la app con JavaFX
CMD ["java", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]
