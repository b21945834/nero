pipeline {
    agent any
    tools{
        maven "MavenTool"
    }
    stages {
        stage("Build Maven"){
            steps{
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
        stage('Stop Existing Containers') {
            steps {
                script {
                        bat 'docker stop $(docker ps -q)'
                    }
                }
            }
        }
        stage('Run Redis Container') {
            steps {
                script {
                    echo "Running Redis container"
                    bat 'docker run -d --name redis -p 6379:6379 redis'
                }
            }
        }
        stage('Run Spring Boot Containers') {
            steps {
                script {
                    bat 'docker run -d --name nero-app -e SPRING_REDIS_HOST=redis -e SPRING_REDIS_PORT=6379 -p 9090:8080 kadiraydogan/nero:latest'
                }
            }
        }
    }
}