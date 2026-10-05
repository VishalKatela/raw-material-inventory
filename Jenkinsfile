pipeline {
    agent any

    environment {
        APP_NAME = 'raw-material-inventory'
        APP_PORT = '8080'
        TARGET_ENV = 'dev'
        MAVEN_OPTS = '-DskipTests=false'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean test package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t raw-material-inventory:${BUILD_NUMBER} .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                  echo "Deploying ${APP_NAME} to ${TARGET_ENV} on port ${APP_PORT}"
                '''
            }
        }
    }
}
