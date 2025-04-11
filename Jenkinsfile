pipeline {
    agent any

    stages {
        stage('🧱 Compilar Proyecto con Maven') {
            steps {
                echo '🚧 Compilando el proyecto...'
                sh 'mvn clean package'
            }
        }
    }

    post {
        success {
            echo '✅ Compilación exitosa.'
        }
        failure {
            echo '❌ Falló la compilación.'
        }
    }
}
