pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = "sudoku"
    }

    stages {
        stage('Preparar') {
            steps {
                echo 'Código clonado por Jenkins.'
            }
        }

        stage('Construir JAR') {
            steps {
                echo 'Compilando el proyecto con Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Construir imagen Docker') {
            steps {
                echo 'Construyendo imagen Docker...'
                sh 'docker build -t sudoku-pro .'
            }
        }

        stage('Levantar contenedores') {
            steps {
                echo 'Deteniendo contenedores anteriores (si hay)...'
                sh 'docker-compose down'
                echo 'Levantando aplicación con Docker Compose...'
                sh 'docker-compose up -d --build'
            }
        }
    }

    post {
        success {
            echo '¡Despliegue exitoso!'
        }
        failure {
            echo 'Algo falló durante el despliegue.'
        }
    }
}
