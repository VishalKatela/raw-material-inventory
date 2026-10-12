pipeline {
    agent any

    parameters {
        choice(name: 'TARGET_ENV', choices: ['dev', 'staging', 'production'], description: 'Deployment Target Environment')
        string(name: 'APP_PORT', defaultValue: '8080', description: 'Application Port')
        booleanParam(name: 'PUSH_TO_REGISTRY', defaultValue: false, description: 'Push image to Docker registry')
        string(name: 'DOCKER_REGISTRY', defaultValue: 'docker.io/vishalkatela', description: 'Docker Registry/Repository')
    }

    environment {
        APP_NAME = 'raw-material-inventory'
        IMAGE_NAME = 'raw-material-inventory'
        IMAGE_TAG = "${env.BUILD_NUMBER ?: 'v1.0.0'}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile and Test') {
            steps {
                echo "Running unit and Selenium automated quality gate suite..."
                sh './mvnw clean test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building versioned container image: ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Push to Registry') {
            when {
                expression { return params.PUSH_TO_REGISTRY == true }
            }
            steps {
                sh """
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${params.DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${params.DOCKER_REGISTRY}/${IMAGE_NAME}:latest
                    echo "Publishing ${params.DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} to registry..."
                """
            }
        }

        stage('Deploy and Health Check') {
            steps {
                sh """
                    docker rm -f ${APP_NAME} 2>/dev/null || true
                    docker run --detach \\
                        --name ${APP_NAME} \\
                        --restart unless-stopped \\
                        --publish ${params.APP_PORT}:8080 \\
                        ${IMAGE_NAME}:${IMAGE_TAG}

                    for attempt in \$(seq 1 30); do
                        if curl --fail --silent http://localhost:${params.APP_PORT}/ >/dev/null; then
                            echo "${APP_NAME} is healthy in ${params.TARGET_ENV} on port ${params.APP_PORT}"
                            exit 0
                        fi
                        sleep 2
                    done

                    docker logs ${APP_NAME}
                    docker rm -f ${APP_NAME}
                    exit 1
                """
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/selenium-reports/**'
        }
        failure {
            echo "Pipeline Quality Gate Failed! Halting deployment and capturing failure logs."
        }
        success {
            echo "Pipeline succeeded! Application deployed and health verified."
        }
    }
}
