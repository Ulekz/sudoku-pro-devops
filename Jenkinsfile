pipeline {
    agent any

    environment {
        DOCKER_VM_USER = 'ec2-user'
        DOCKER_VM_IP = 'XXX.XXX.XXX.XXX' // ← Reemplaza con la IP actual de la VM Docker
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
                    sh "mkdir -p html"
                    def jarExists = fileExists(jarPath)
                    if (!jarExists) {
                        error "❌ Archivo JAR no encontrado en ${jarPath}"
                    }
                    sh "cp ${jarPath} ${outputPath}"
                }
            }
        }

        stage('🚀 Transferencia a VM Docker') {
            steps {
                echo '📤 Enviando solo los archivos necesarios...'
                sh '''
                    rsync -avz -e "ssh -i $REMOTE_KEY -o StrictHostKeyChecking=no" \
                    html/ docker-compose.yml Dockerfile \
                    $DOCKER_VM_USER@$DOCKER_VM_IP:/home/ec2-user/sudoku-deploy/
                '''
            }
        }

        stage('🐳 Docker Compose remoto') {
            steps {
                echo '⚙️ Ejecutando docker-compose en la VM Docker...'
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
            echo '❌ Algo falló. Revisa los logs y permisos de la VM Docker.'
        }
    }
}
