pipeline {
    agent any

    environment {
        DOCKER_HOST = 'ec2-user@44.202.86.80'  // Actualiza con la IP pública de la VM Docker
        REMOTE_PATH = "/home/ec2-user/app"
        PEM_KEY = "/var/lib/jenkins/docker-key.pem"
    }

    stages {
        stage('Clonar proyecto') {
            steps {
                git url: 'https://github.com/Ulekz/sudoku-pro-devops.git', branch: 'main'
            }
        }

        stage('Compilar con Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Copiar archivos por SSH') {
            steps {
                sh """
                scp -i ${PEM_KEY} -r * ${DOCKER_HOST}:${REMOTE_PATH}
                """
            }
        }

        stage('Desplegar app Sudoku en Docker') {
            steps {
                sh """
                ssh -i ${PEM_KEY} ${DOCKER_HOST} 'cd ${REMOTE_PATH} && docker-compose down && docker-compose up -d --build'
                """
            }
        }
    }
}
