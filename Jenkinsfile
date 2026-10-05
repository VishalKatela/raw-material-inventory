pipeline {
    agent any

    environment {
        APP_NAME = 'raw-material-inventory'
        APP_PORT = '8080'
        TARGET_ENV = 'dev'
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

                stage('Deploy and Health Check') {
            steps {
                sh '''
                                    docker rm -f ${APP_NAME} 2>/dev/null || true
                                    docker run --detach \\
                                        --name ${APP_NAME} \\
                                        --restart unless-stopped \\
                                        --publish ${APP_PORT}:8080 \\
                                        raw-material-inventory:${BUILD_NUMBER}

                                    for attempt in $(seq 1 30); do
                                        if curl --fail --silent http://localhost:${APP_PORT}/ >/dev/null; then
                                            echo "${APP_NAME} is healthy in ${TARGET_ENV} on port ${APP_PORT}"
                                            exit 0
                                        fi
                                        sleep 2
                                    done

                                    docker logs ${APP_NAME}
                                    docker rm -f ${APP_NAME}
                                    exit 1
                '''
            }
        }
    }
}
