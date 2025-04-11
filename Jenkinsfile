pipeline {
    agent any

    environment {
        REMOTE_HOST = 'ec2-user@44.202.86.80' // <- Reemplaza si cambia la IP
        REMOTE_PATH = '/home/ec2-user/app'    // <- Donde vive docker-compose.yml
        SSH_KEY = '/home/jenkins/.ssh/docker-key.pem' // <- Clave que Jenkins usa
    }

    stages {
        stage('Clonar código') {
            steps {
                git url: 'https://github.com/TU_USUARIO/Sudoku-Pro.git', branch: 'main'
            }
        }

        stage('Verificar compilación Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Copiar proyecto a la VM de Docker') {
            steps {
                sh "scp -i ${SSH_KEY} -r . ${REMOTE_HOST}:${REMOTE_PATH}"
            }
        }

        stage('Desplegar remotamente') {
            steps {
                sh """
                    ssh -i ${SSH_KEY} ${REMOTE_HOST} '
                        cd ${REMOTE_PATH} &&
                        docker rm -f sudoku-app || true &&
                        docker-compose down || true &&
                        docker-compose up -d --build
                    '
                """
            }
        }
    }
}
