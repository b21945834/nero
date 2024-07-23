pipeline {
    agent any
    tools {
        maven "MavenTool"
    }
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code'
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/b21945834/nero']])
            }
        }
        stage('Start PostgreSQL') {
            steps {
                script {
                    echo 'Starting PostgreSQL container'
                    bat 'docker-compose up -d postgres'
                    bat '''
                    echo "Waiting for PostgreSQL to be ready..."
                    while ! docker exec -it postgres pg_isready -U nero_user; do
                      echo "PostgreSQL is not ready yet..."
                      sleep 5
                    done
                    '''
                }
            }
        }
        stage('Build Maven') {
            steps {
                echo 'Building Maven Project'
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
        stage('Run Full Docker Compose') {
            steps {
                script {
                    echo 'Running Full Docker Compose'
                    bat 'docker-compose down || echo "No containers to stop"'
                    bat 'docker-compose up -d'
                }
            }
        }
    }
    post {
        always {
            script {
                echo 'Stopping Docker Compose'
                bat 'docker-compose down'
            }
        }
    }
}
