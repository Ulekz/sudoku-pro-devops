pipeline {
    agent any

    environment {
        DOCKER_VM_USER = 'ec2-user'
        DOCKER_VM_IP = '18.207.242.61'
        REMOTE_KEY = '/var/lib/jenkins/.ssh/docker-key.pem'
    }

    stages {
        stage('🧱 Compilar Proyecto con Maven') {
            steps {
                echo '🚧 Compilando el proyecto...'
                sh 'mvn clean package'
            }
        }

        stage('📦 Preparar .jar para Docker') {
            steps {
                echo '📂 Validando y copiando archivo .jar generado...'
                script {
                    def jarPath = "target/SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar"
                    def outputPath = "html/sudoku.jar"

                    // Crear carpeta html
                    sh "mkdir -p html"

                    // Validar existencia del archivo
                    def jarExists = fileExists(jarPath)
                    if (!jarExists) {
                        error "❌ Archivo JAR no encontrado en ${jarPath}"
                    }

                    // Copiar .jar a html/
                    sh "cp ${jarPath} ${outputPath}"
                }
            }
        }

        stage('🚀 Copiar archivos a VM Docker') {
            steps {
                echo '📤 Transfiriendo archivos a la máquina Docker...'
                sh """
                    scp -i $REMOTE_KEY -o StrictHostKeyChecking=no -r * \
                    $DOCKER_VM_USER@$DOCKER_VM_IP:/home/ec2-user/sudoku-deploy
                """
            }
        }

        stage('🐳 Desplegar aplicación con Docker Compose') {
            steps {
                echo '⚙️ Ejecutando docker-compose remotamente...'
                sh """
                    ssh -i $REMOTE_KEY -o StrictHostKeyChecking=no $DOCKER_VM_USER@$DOCKER_VM_IP '
                        cd /home/ec2-user/sudoku-deploy &&
                        docker-compose down || true &&
                        docker-compose build &&
                        docker-compose up -d
                    '
                """
            }
        }
    }

    post {
        success {
            echo '✅ Despliegue exitoso. El .jar está disponible para descarga.'
            echo "🌐 Accede a: http://$DOCKER_VM_IP:8080/sudoku.jar"
        }
        failure {
            echo '❌ Algo falló en el pipeline. Revisa los logs para más detalles.'
        }
    }
}
