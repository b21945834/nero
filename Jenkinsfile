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
        stage('Run Redis and Spring Boot Containers') {
            steps {
                script {
                    bat 'docker run -d -p 6380:6379 redis'
                    bat 'docker run -d -e SPRING_REDIS_HOST=redis -e SPRING_REDIS_PORT=6380 -p 9090:8080 kadiraydogan/nero:latest'
                }
            }
        }
    }
}