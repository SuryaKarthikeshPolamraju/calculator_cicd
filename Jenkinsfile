pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        JFROG_REPO    = 'calculator-artifacts'
        ARTIFACT_NAME = 'app.war'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean, Test & Package') {
            steps {
                sh 'mvn clean test package'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-host-url', variable: 'SONAR_HOST_URL')]) {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar -Dsonar.host.url=$SONAR_HOST_URL'
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Upload to JFrog') {
            steps {
                withCredentials([
                    string(credentialsId: 'jfrog-url', variable: 'JFROG_URL'),
                    string(credentialsId: 'jfrog-token', variable: 'JFROG_TOKEN')
                ]) {
                    sh '''
                        curl -fL https://install-cli.jfrog.io | sh
                        ./jf c add jfrog-server --url=$JFROG_URL --access-token=$JFROG_TOKEN --interactive=false
                        ./jf rt upload target/${ARTIFACT_NAME} ${JFROG_REPO}/com/yourorg/app/${BUILD_NUMBER}/ --server-id=jfrog-server
                    '''
                }
            }
        }

        stage('Download Latest from JFrog') {
            steps {
                withCredentials([
                    string(credentialsId: 'jfrog-url', variable: 'JFROG_URL'),
                    string(credentialsId: 'jfrog-token', variable: 'JFROG_TOKEN')
                ]) {
                    sh '''
                        ./jf rt download "${JFROG_REPO}/com/yourorg/app/*/${ARTIFACT_NAME}" downloaded/ --sort-by=created --sort-order=desc --limit=1 --flat=true --server-id=jfrog-server
                    '''
                }
            }
        }

        stage('Deploy to Tomcat via SCP') {
            steps {
                withCredentials([
                    string(credentialsId: 'tomcat-ip', variable: 'TOMCAT_IP'),
                    string(credentialsId: 'tomcat-user', variable: 'TOMCAT_USER'),
                    string(credentialsId: 'tomcat-webapps-path', variable: 'TOMCAT_WEBAPPS')
                ]) {
                    sshagent(credentials: ['tomcat-ssh']) {
                        sh '''
                            ssh -o StrictHostKeyChecking=no ${TOMCAT_USER}@${TOMCAT_IP} '
                                sudo rm -rf ${TOMCAT_WEBAPPS}/ROOT
                                sudo rm -f ${TOMCAT_WEBAPPS}/ROOT.war
                            '
                            scp -o StrictHostKeyChecking=no downloaded/${ARTIFACT_NAME} ${TOMCAT_USER}@${TOMCAT_IP}:${TOMCAT_WEBAPPS}/ROOT.war
                            ssh -o StrictHostKeyChecking=no ${TOMCAT_USER}@${TOMCAT_IP} '
                                sudo systemctl restart tomcat9
                            '
                        '''
                    }
                }
            }
        }
    }

    post {
        success { echo 'Build, Sonar scan, JFrog upload/download, and SCP Deploy completed successfully' }
        failure { echo 'Pipeline failed' }
    }
}