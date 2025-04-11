pipeline {
    agent any

    environment {
        JAR_NAME = "SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar"
    }

    stages {
        stage('🧱 Compilar Proyecto con Maven') {
            steps {
                echo '🚧 Compilando el proyecto...'
                sh 'mvn clean package'
            }
        }

        stage('✅ Verificar archivo .jar') {
            steps {
                script {
                    def jarPath = "target/${env.JAR_NAME}"
                    if (fileExists(jarPath)) {
                        echo "✅ Build exitoso. Archivo generado: ${jarPath}"
                    } else {
                        error "❌ Falló la compilación. No se encontró el archivo .jar: ${jarPath}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "🎉 Compilación y tests pasaron exitosamente."
        }
        failure {
            echo "❌ La compilación o los tests fallaron. Revisa los logs arriba."
        }
    }
}
