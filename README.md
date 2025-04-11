# 🧩 Sudoku Pro DevOps

Este proyecto es una aplicación JavaFX que implementa un juego de Sudoku profesional, incluyendo:

- 🧠 Generación automática de tableros con dificultad (Fácil, Media, Difícil)
- 💾 Guardado/carga de partidas
- ⏱️ Historial de récords y top 10 por dificultad
- 🖥️ Interfaz JavaFX
- ✅ Tests con JUnit
- 📈 Reportes de cobertura con JaCoCo
- 📋 Análisis de calidad con Checkstyle, PMD y SpotBugs

## 🚀 Pipeline CI/CD con Jenkins + Docker

Este proyecto está completamente automatizado utilizando Jenkins y Docker sobre dos máquinas distintas:

### 🔧 Infraestructura

| Componente     | Descripción |
|----------------|-------------|
| Jenkins VM     | Ejecuta Jenkins y controla el pipeline completo |
| Docker VM      | Recibe los archivos desde Jenkins y ejecuta `docker-compose` |
| GitHub         | Repositorio donde se guarda el código fuente del proyecto |
| Docker         | Contiene y expone un servidor web con el archivo `.jar` descargable |

### 🛠️ Herramientas integradas

- **Java 17**
- **JavaFX 22**
- **JUnit 5.10.0**
- **Checkstyle 3.1.2**
- **PMD 3.15.0**
- **SpotBugs 4.7.3.0**
- **JaCoCo 0.8.8**
- **Maven Assembly Plugin 3.3.0**

---

## ✅ Pasos del pipeline Jenkins

1. **Clona desde GitHub** el repositorio `sudoku-pro-devops`
2. **Compila el proyecto** con Maven
3. **Ejecuta los tests** y genera reporte de cobertura
4. **Verifica que se generó el `.jar`** correctamente
5. **Transfiere el `.jar`, `html/index.html`, `Dockerfile`, `docker-compose.yml`** a la VM Docker
6. **Despliega en Docker** ejecutando `docker-compose build` y `up`
7. **Publica un servidor Nginx** que expone el archivo `.jar` para descarga

---

## 🌐 Acceso al ejecutable `.jar`

El archivo `.jar` compilado se publica automáticamente y es accesible vía navegador:

📥 URL para descargar el juego Sudoku Pro:

### Ejemplo real:
[http://0.0.0.0:8080/SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar](http://44.211.132.177:8080/SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar)

Solo haz clic en el enlace y descarga el `.jar`.

---

## 🧪 Ejecutar el juego manualmente

Una vez descargado el `.jar`, puedes ejecutar el juego en tu máquina local con:

```bash
java -jar SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar
