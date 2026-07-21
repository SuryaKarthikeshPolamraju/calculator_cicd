pipeline {
    agent any
    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }
    environment {
        TOMCAT_CREDS = credentials('tomcat-creds')
    }
    stages {
        stage('Checkput') {
            steps{
                checkout scm
            }
        }
        stage('Build, Test & Package') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'tomcat-creds', url: "http://${TOMCAT_IP}:8080")],
                    contextPath: '/',
                    war: 'target/app.war'
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