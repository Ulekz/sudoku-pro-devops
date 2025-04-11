FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]
