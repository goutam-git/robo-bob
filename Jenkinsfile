pipeline {
    agent any

    environment {
        GITHUB_REPO = 'https://github.com/goutam-git/robo-bob.git'
        DOCKER_IMAGE = 'yourdockerhubusername/robo-bob'
        DOCKER_TAG = '1.0'
        K8S_DEPLOYMENT = 'robo-bob-deployment.yaml'
        BRANCH_NAME = 'main'
    }

    stages {

        stage('Clone Repository') {
            steps {
                script {
                    echo 'Cloning repository...'
                    checkout scmGit(
                        branches: [[name: BRANCH_NAME]],
                        userRemoteConfigs: [[
                            url: GITHUB_REPO,
                            credentialsId: 'github-token'
                        ]]
                    )
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    echo 'Building with Maven...'
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo 'Running tests...'
                    sh 'mvn test'
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh """
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    echo ${env.DOCKER_HUB_TOKEN} | docker login -u ${env.DOCKER_HUB_USER} --p
                    docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo 'Deploying to Kubernetes...'
                    sh """
                    kubectl apply -f k8/${K8S_DEPLOYMENT}
                    kubectl rollout status deployment/robo-bob
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}