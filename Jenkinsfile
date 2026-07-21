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
        stage('Clone') {
            steps {
                git branch: 'main', url: 'https://github.com/SuryaKarthikeshPolamraju/calculator_cicd.git'
            }
        }
        stage('Build, Test & Package') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                sh '''
                curl -u $TOMCAT_CREDS_USR:$TOMCAT_CREDS_PSW --upload-file target/app.war \
                "http://${TOMCAT_IP}:8080/manager/text/deploy?path=/&update=true"
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