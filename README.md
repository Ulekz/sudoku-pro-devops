# Sudoku - DevOps Quality Tools Integration

Este proyecto incluye la integración de herramientas de calidad de código y pruebas automatizadas como parte de la **Fase 2 del pipeline DevOps**.

## ✅ Herramientas Integradas

| Herramienta   | Descripción |
|---------------|-------------|
| **JUnit 5**   | Pruebas unitarias automáticas |
| **Checkstyle** | Análisis de estilo de código Java |
| **PMD**       | Detección de errores comunes, malas prácticas y código duplicado |
| **SpotBugs**  | Análisis estático para detectar errores de tiempo de ejecución |

⚠️ Nota importante sobre Checkstyle

El archivo Main.java no puede ser procesado correctamente por Checkstyle debido a una incompatibilidad de sintaxis que rompe el análisis.
Este archivo no será modificado ni excluido por decisión del equipo, ya que funciona correctamente en producción.
Puedes ignorar este error mientras las demás herramientas funcionen como se espera.

## 🧪 Cómo ejecutar el análisis

Ejecuta desde terminal o Jenkins:

```bash
mvn clean install site

