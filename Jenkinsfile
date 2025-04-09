pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-17'
            args '-v $HOME/.m2:/root/.m2' // Usa el cache local de Maven
        }
    }


    environment {
        SSH_KEY = "~/.ssh/docker-key.pem"
        REMOTE_USER = "ec2-user"
        REMOTE_HOST = "44.202.86.80"  // 👈 actualiza cada vez que cambie la IP
        REMOTE_PATH = "/home/ec2-user/app"
    }

    stages {
        stage('Preparar entorno') {
            steps {
                echo 'Código ya clonado por Jenkins.'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Copiar archivos al servidor remoto') {
            steps {
                echo "Copiando JAR y Dockerfile al servidor remoto..."
                sh """
                    ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} 'mkdir -p ${REMOTE_PATH}'
                    scp -i ${SSH_KEY} target/SudokuV1-1.0-SNAPSHOT.jar ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/app.jar
                    scp -i ${SSH_KEY} Dockerfile ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/Dockerfile
                """
            }
        }

        stage('Construir y levantar contenedor en remoto') {
            steps {
                echo 'Conectando al servidor remoto para levantar contenedor Docker...'
                sh """
                    ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} '
                    cd ${REMOTE_PATH} &&
                    docker build -t sudoku-pro . &&
                    docker stop sudoku-app || true &&
                    docker rm sudoku-app || true &&
                    docker run -d --name sudoku-app -p 9090:8080 sudoku-pro
                    '
                """
            }
        }
    }

    post {
        success {
            echo 'Despliegue completado con éxito.'
        }
        failure {
            echo 'Ocurrió un error en el pipeline.'
        }
    }
}
