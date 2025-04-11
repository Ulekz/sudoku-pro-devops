pipeline {
    agent any

    environment {
        // Variables opcionales si necesitas usarlas luego
        JAR_NAME = "SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar"
    }

    stages {
        stage('🧱 Compilar Proyecto con Maven') {
            steps {
                echo '🚧 Compilando el proyecto...'
                sh 'mvn clean package'
            }
        }

        stage('✅ Confirmar build') {
            steps {
                script {
                    def jarPath = "target/${env.JAR_NAME}"
                    if (fileExists(jarPath)) {
                        echo "✅ Build exitoso. Archivo generado: ${jarPath}"
                    } else {
                        error "❌ Falló la compilación. No se encontró el .jar esperado: ${jarPath}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "🎉 Paso 2 completado correctamente. ¡La app se compiló!"
        }
        failure {
            echo "❌ El paso de compilación falló. Revisa los errores de Maven arriba."
        }
    }
}
