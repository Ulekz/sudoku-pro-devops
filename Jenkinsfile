pipeline {
    agent any

    environment {
        // Usuario e IP de la VM donde está Docker
        DOCKER_VM_USER = 'ec2-user'
        DOCKER_VM_IP = '44.204.81.122' // ⚠️ Actualiza si cambia

        // Ruta de la clave privada en el servidor Jenkins
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
                echo '📂 Preparando archivo .jar en carpeta html/'
                sh '''
                    mkdir -p html
                    sh 'cp target/SudokuV1-1.0-SNAPSHOT.jar html/sudoku.jar'
                '''
            }
        }

        stage('🚀 Copiar archivos a VM Docker') {
            steps {
                echo '📤 Transfiriendo archivos a la máquina Docker...'
                sh '''
                    scp -i $REMOTE_KEY -o StrictHostKeyChecking=no -r * \
                    $DOCKER_VM_USER@$DOCKER_VM_IP:/home/ec2-user/sudoku-deploy
                '''
            }
        }

        stage('🐳 Desplegar aplicación con Docker Compose') {
            steps {
                echo '⚙️ Ejecutando docker-compose remotamente...'
                sh '''
                    ssh -i $REMOTE_KEY -o StrictHostKeyChecking=no $DOCKER_VM_USER@$DOCKER_VM_IP '
                        cd /home/ec2-user/sudoku-deploy &&
                        docker-compose down || true &&
                        docker-compose build &&
                        docker-compose up -d
                    '
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Despliegue exitoso. El .jar está disponible para descarga.'
            echo "🌐 Accede a: http://$DOCKER_VM_IP:8080/sudoku.jar"
        }
        failure {
            echo '❌ Algo falló en el pipeline. Revisa los logs para más detalles.'
        }
    }
}
