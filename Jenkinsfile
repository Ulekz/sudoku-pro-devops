pipeline {
    agent any

    environment {
        DOCKER_VM_USER = 'ec2-user'
        DOCKER_VM_IP = '54.210.172.86' // ⚠️ Reemplaza si cambia
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
                    if (!fileExists(jarPath)) {
                        error "❌ Archivo JAR no encontrado en ${jarPath}"
                    }
                    sh "cp ${jarPath} ${outputPath}"
                }
            }
        }

        stage('🚀 Copiar archivos a VM Docker') {
            steps {
                echo '📤 Transfiriendo archivos a la máquina Docker...'
                dir("${env.WORKSPACE}") {
                    sh """
                        rsync -avz -e "ssh -i $REMOTE_KEY -o StrictHostKeyChecking=no" \
                        html/ docker-compose.yml Dockerfile \
                        $DOCKER_VM_USER@$DOCKER_VM_IP:/home/ec2-user/sudoku-deploy/
                    """
                }
            }
        }

        stage('🐳 Desplegar aplicación en Docker') {
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
            echo '✅ Despliegue exitoso. Puedes descargar el .jar aquí:'
            echo "🌐 http://$DOCKER_VM_IP:8080/sudoku.jar"
        }
        failure {
            echo '❌ Algo falló. Revisa los logs del pipeline.'
        }
    }
}
