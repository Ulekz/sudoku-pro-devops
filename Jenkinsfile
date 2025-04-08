pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = "sudoku"
    }

    stages {
        stage('Preparar entorno') {
            steps {
                echo '✅ Código ya clonado por Jenkins'
            }
        }

        stage('Construir JAR') {
            steps {
                echo '⚙️ Compilando JAR...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Construir imagen Docker') {
            steps {
                echo '🐳 Construyendo imagen Docker...'
                sh 'docker build -t sudoku-pro .'
            }
        }

        stage('Desplegar con Docker Compose') {
            steps {
                echo '🚀 Levantando contenedores...'
                sh 'docker-compose down'
                sh 'docker-compose up -d --build'
            }
        }
    }

    post {
        success {
            echo '🎉 ¡Despliegue exitoso!'
        }
        failure {
            echo '❌ Ocurrió un error en el pipeline.'
        }
    }
}
