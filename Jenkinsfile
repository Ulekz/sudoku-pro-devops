pipeline {
    agent any

    environment {
        JAR_NAME = "SudokuV1-1.0-SNAPSHOT-jar-with-dependencies.jar"
        REMOTE_KEY = "/var/lib/jenkins/.ssh/docker-key.pem"
        DOCKER_VM_IP = "44.211.132.177" // cámbiala si tu IP cambia
        DOCKER_VM_USER = "ec2-user"
        REMOTE_DIR = "/home/ec2-user/sudoku-deploy"
    }

    stages {
        stage('🧱 Compilar Proyecto con Maven') {
            steps {
                echo '🚧 Compilando el proyecto...'
                sh 'mvn clean package'
            }
        }

        stage('✅ Verificar archivo .jar') {
            steps {
                script {
                    def jarPath = "target/${env.JAR_NAME}"
                    if (fileExists(jarPath)) {
                        echo "✅ Build exitoso. Archivo generado: ${jarPath}"
                    } else {
                        error "❌ Falló la compilación. No se encontró el .jar esperado: ${jarPath}"
                    }
                }
            }
        }

        stage('📦 Copiar archivo .jar a la VM Docker') {
            steps {
                echo '📤 Copiando el .jar al servidor Docker...'
                sh '''
                    rsync -avz -e "ssh -i $REMOTE_KEY -o StrictHostKeyChecking=no" \
                    target/$JAR_NAME \
                    $DOCKER_VM_USER@$DOCKER_VM_IP:$REMOTE_DIR/
                '''
            }
        }
    }

    post {
        success {
            echo "🎉 Paso 3 completado correctamente. ¡El .jar fue transferido a Docker VM!"
        }
        failure {
            echo "❌ Algo falló en el paso 3. Revisa los errores."
        }
    }
}
