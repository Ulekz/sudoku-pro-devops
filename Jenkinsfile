pipeline {
    agent any

    environment {
        SSH_KEY = "~/.ssh/docker-key.pem"     // 🔐 Ruta a la clave PEM en Jenkins
        REMOTE_USER = "ec2-user"              // 👤 Usuario de la VM con Docker
        REMOTE_HOST = "44.202.86.80"          // 🌐 IP pública de la VM con Docker (actualízala si cambia)
        REMOTE_PATH = "/home/ec2-user/app"    // 📁 Carpeta donde se copiará el proyecto
    }

    stages {
        stage('Preparar entorno') {
            steps {
                echo 'Clonación de código completada. Iniciando despliegue remoto.'
            }
        }

        stage('Copiar archivos al servidor remoto') {
            steps {
                echo 'Copiando proyecto al servidor Docker...'
                sh '''
                    ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "mkdir -p ${REMOTE_PATH}"
                    scp -i ${SSH_KEY} -o StrictHostKeyChecking=no -r * ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/
                '''
            }
        }

        stage('Build & Docker run en servidor remoto') {
            steps {
                echo 'Compilando y desplegando en la VM con Docker...'
                sh '''
                    ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "
                        cd ${REMOTE_PATH} &&
                        mvn clean package -DskipTests &&
                        docker stop sudoku-app || true &&
                        docker rm sudoku-app || true &&
                        docker build -t sudoku-pro . &&
                        docker run -d --name sudoku-app -p 9090:8080 sudoku-pro
                    "
                '''
            }
        }
    }

    post {
        success {
            echo '🚀 Despliegue remoto completado con éxito.'
        }
        failure {
            echo '❌ Falló el proceso de despliegue.'
        }
    }
}
