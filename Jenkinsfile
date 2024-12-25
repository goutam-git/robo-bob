pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('gghoshdocker')  // Docker Hub creds
        DOCKER_IMAGE = 'gghoshdocker/robo-bob:1.0'
        BRANCH_NAME = 'branch-1.2'
        GITHUB_REPO = 'https://github.com/goutam-git/robo-bob.git'
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    echo 'Cloning GitHub repository...'
                    checkout scmGit(
                        branches: [[name: BRANCH_NAME]],
                        userRemoteConfigs: [[
                            url: GITHUB_REPO,
                            credentialsId: 'goutam-git'  // GitHub PAT
                        ]]
                    )
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    echo 'Logging in to Docker Hub...'
                    sh """
                      docker login -u \$DOCKER_HUB_CREDENTIALS_USR -p \$DOCKER_HUB_CREDENTIALS_PSW
                       """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker image to Docker Hub...'
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo 'Deploying to Kubernetes...'
                    sh """
                        kubectl apply -f k8/robo-bob-deployment.yaml
                        kubectl rollout status deployment/robo-bob
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline executed successfully! Docker image pushed and deployed."
        }
        failure {
            echo "Pipeline failed at some stage."
        }
    }
}