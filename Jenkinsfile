pipeline {
    agent any

    environment {
        JAR_NAME = "SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar"
        DOCKER_USER = "ec2-user"
        DOCKER_HOST = "44.211.132.177"
        REMOTE_PATH = "/home/ec2-user/sudoku-deploy"
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
                        error "❌ Falló la compilación. No se encontró el .jar esperado: ${jarPath}"
                    }
                }
            }
        }

        stage('📦 Copiar archivos a la VM Docker') {
            steps {
                echo '📤 Enviando .jar y html/ al servidor Docker...'
                sh """
                    rsync -avz -e 'ssh -i /var/lib/jenkins/.ssh/docker-key.pem -o StrictHostKeyChecking=no' target/${JAR_NAME} ${DOCKER_USER}@${DOCKER_HOST}:${REMOTE_PATH}/html/
                    rsync -avz -e 'ssh -i /var/lib/jenkins/.ssh/docker-key.pem -o StrictHostKeyChecking=no' html/index.html ${DOCKER_USER}@${DOCKER_HOST}:${REMOTE_PATH}/html/
                    rsync -avz -e 'ssh -i /var/lib/jenkins/.ssh/docker-key.pem -o StrictHostKeyChecking=no' Dockerfile docker-compose.yml ${DOCKER_USER}@${DOCKER_HOST}:${REMOTE_PATH}/
                """
            }
        }

        stage('🐳 Desplegar en Docker') {
            steps {
                echo '🚀 Ejecutando docker-compose en la VM Docker...'
                sh """
                    ssh -i /var/lib/jenkins/.ssh/docker-key.pem -o StrictHostKeyChecking=no ${DOCKER_USER}@${DOCKER_HOST} '
                        cd ${REMOTE_PATH} &&
                        docker-compose down &&
                        docker-compose build --no-cache &&
                        docker-compose up -d
                    '
                """
            }
        }
    }

    post {
        success {
            echo "🎉 Despliegue completo. Verifica: http://${DOCKER_HOST}:8080"
        }
        failure {
            echo "❌ Algo falló en el pipeline. Verifica los errores anteriores."
        }
    }
}
