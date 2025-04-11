pipeline {
    agent any

    environment {
        REMOTE_KEY = '/var/lib/jenkins/.ssh/docker-key.pem'
        DOCKER_VM_USER = 'ec2-user'
        DOCKER_VM_IP = '54.210.172.86' // Actualiza si cambia
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
    }

    post {
        success {
            echo '✅ JAR generado y listo. Continuemos con el paso de transferencia.'
        }
        failure {
            echo '❌ Falló la compilación o la generación del JAR.'
        }
    }
}
