# Etapa 1: Compilar el JAR
FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con JavaFX
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Instalar JavaFX
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_linux-x64_bin-sdk.zip && \
    unzip openjfx-17.0.2_linux-x64_bin-sdk.zip && \
    mv javafx-sdk-17.0.2 /opt/javafx && \
    rm openjfx-17.0.2_linux-x64_bin-sdk.zip

# Copiar el jar generado
COPY --from=builder /app/target/SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Ejecutar el JAR con JavaFX
CMD ["java", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]
