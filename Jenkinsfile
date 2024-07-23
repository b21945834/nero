pipeline {
    agent any
    tools {
        maven "MavenTool"
    }
    stages {
        stage("Build Maven") {
            steps {
                echo 'Building Maven Project'
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/b21945834/nero']])
                bat 'mvn clean install'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker Image'
                    bat 'docker build -t kadiraydogan/nero .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker Image'
                    bat 'docker logout'
                    bat 'docker login -u kadiraydogan -p Kadir.1442'
                    bat 'docker push kadiraydogan/nero'
                }
            }
        }
        stage('Run Docker Compose') {
            steps {
                script {
                    echo 'Running Docker Compose'
                    bat 'docker-compose down || echo "No containers to stop"'
                    bat 'docker-compose up -d'
                }
            }
        }
    }
}