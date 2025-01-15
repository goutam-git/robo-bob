pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('gghoshdocker')  // Docker Hub creds
        DOCKER_IMAGE = 'gghoshdocker/robo-bob:1.2'
        BRANCH_NAME = 'branch-1.3'
        GITHUB_REPO = 'https://github.com/goutam-git/robo-bob.git'
        KUBECONFIG = '/var/lib/jenkins/.kube/config'
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
        stage('Build with Maven') {
                    steps {
                        script {
                                echo 'Running Make: build...'
                                sh 'make build'
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
                    echo 'Running Make: docker-build...'
                    sh 'make docker-build'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Running Make: docker-push...'
                    sh 'make docker-push'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withEnv(["KUBECONFIG=${KUBECONFIG}"]) {
                        echo 'Running Make: k8-deploy...'
                        sh 'make k8-deploy'
                     }
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