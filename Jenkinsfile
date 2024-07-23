pipeline {
    agent any
    tools {
        maven "MavenTool"
    }
    stages {
        stage("Build Maven") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/b21945834/nero']])
                bat 'mvn clean install'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    bat 'docker build -t kadiraydogan/nero .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    bat 'docker logout'
                    bat 'docker login -u kadiraydogan -p Kadir.1442'
                    bat 'docker push kadiraydogan/nero'
                }
            }
        }
        stage('Stop and Remove Existing Containers') {
            steps {
                script {
                    echo "Stopping and removing existing Docker containers"
                    bat 'docker stop $(docker ps -q) || echo "No containers to stop"'
                    bat 'docker rm $(docker ps -a -q) || echo "No containers to remove"'
                }
            }
        }
        stage('Run Redis Container') {
            steps {
                script {
                    echo "Running Redis container"
                    bat 'docker run -d --name redis -p 6379:6379 redis || echo "Redis container already running"'
                }
            }
        }
        stage('Run Spring Boot Container') {
            steps {
                script {
                    echo "Running Spring Boot container"
                    bat 'docker run -d --name nero-app -e SPRING_REDIS_HOST=redis -e SPRING_REDIS_PORT=6379 -p 9090:8080 kadiraydogan/nero:latest || echo "Spring Boot container already running"'
                }
            }
        }
    }
}
