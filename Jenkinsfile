pipeline {
    agent any
    tools {
        maven "MavenTool"
    }
    stages {
        stage("Build Maven") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/b21945834/nero']])
                powershell 'mvn clean install'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    powershell 'docker build -t kadiraydogan/nero .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    powershell 'docker logout'
                    powershell 'docker login -u kadiraydogan -p Kadir.1442'
                    powershell 'docker push kadiraydogan/nero'
                }
            }
        }
        stage('Stop and Remove Existing Containers') {
            steps {
                script {
                    echo "Stopping and removing existing Docker containers"
                    powershell '''
                        # Stop all running containers
                        $containers = docker ps -q
                        if ($containers) {
                            docker stop $containers
                        } else {
                            Write-Output "No containers to stop"
                        }

                        # Remove all containers
                        $containers = docker ps -a -q
                        if ($containers) {
                            docker rm $containers
                        } else {
                            Write-Output "No containers to remove"
                        }
                    '''
                }
            }
        }
        stage('Run Spring Boot Container') {
            steps {
                script {
                    bat 'docker run -d --name redis --network my-network -p 6379:6379 redis'
                    bat 'docker run -d --name nero-app --network my-network -e SPRING_REDIS_HOST=redis -e SPRING_REDIS_PORT=6379 -p 9090:8080 kadiraydogan/nero:latest'
                }
            }
        }
    }
}

