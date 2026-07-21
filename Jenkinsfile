pipeline {
    agent any
    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }
    stages {
        stage('Clone') {
            steps {
                git branch: 'main', url: 'https://github.com/SuryaKarthikeshPolamraju/calculator_cicd.git'
            }
        }
        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                sh '''
                curl -u admin:Jenkins@123 --upload-file target/app.war \
                "http://44.223.24.228:8080/manager/text/deploy?path=/app&update=true"
                '''
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
        success { echo 'Deployed successfully' }
        failure { echo 'Build/deploy failed' }
    }
}