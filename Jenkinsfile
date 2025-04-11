pipeline {
    agent any

    environment {
        SSH_KEY = "~/.ssh/docker-key.pem"
        REMOTE_USER = "ec2-user"
        REMOTE_HOST = "3.86.58.76"
        REMOTE_PATH = "/home/ec2-user/app"
    }

    stages {
        stage('Preparar entorno') {
            steps {
                echo 'Iniciando pipeline: conexión y despliegue remoto.'
            }
        }

        stage('Copiar archivos al servidor remoto') {
            steps {
                echo 'Copiando archivos del proyecto al servidor remoto con Docker...'
                sh '''
                    ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "mkdir -p ${REMOTE_PATH}"
                    scp -i ${SSH_KEY} -o StrictHostKeyChecking=no -r * ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/
                '''
            }
        }

        stage('Build & Docker Run en remoto') {
            steps {
                echo 'Conectando al servidor remoto, construyendo JAR y desplegando imagen Docker...'
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
            echo 'Despliegue remoto completado exitosamente.'
        }
        failure {
            echo 'Error durante el proceso de despliegue remoto.'
        }
    }
}
