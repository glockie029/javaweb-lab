pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    environment {
        MODULE_DIR = 'labs/lab-02-springboot-basics'
        SEMGREP_RULES = 'semgrep/rules/mybatis-security.yml'
        SEMGREP_REPORT = 'reports/semgrep/semgrep.json'
    }

    tools {
        jdk 'jdk11'
        maven 'maven-3.9.9'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build And Verify') {
            steps {
                sh '''
                    mvn -B -f "${MODULE_DIR}/pom.xml" clean verify
                '''
            }
            post {
                always {
                    junit testResults: "${MODULE_DIR}/target/surefire-reports/TEST-*.xml", allowEmptyResults: true
                    archiveArtifacts artifacts: "${MODULE_DIR}/target/site/jacoco/**/*,${MODULE_DIR}/target/spotbugs*.xml", allowEmptyArchive: true
                }
            }
        }

        stage('Semgrep') {
            steps {
                sh '''
                    mkdir -p reports/semgrep
                    semgrep scan \
                      --config p/java \
                      --config "${SEMGREP_RULES}" \
                      --json \
                      --output "${SEMGREP_REPORT}" \
                      "${MODULE_DIR}"
                '''
            }
            post {
                always {
                    archiveArtifacts artifacts: "${SEMGREP_REPORT}", allowEmptyArchive: true
                }
            }
        }

        stage('SonarQube Scan') {
            steps {
                script {
                    def jdk17Home = tool name: 'jdk17', type: 'hudson.model.JDK'
                    withSonarQubeEnv('sonarqube') {
                        withEnv(["JAVA_HOME=${jdk17Home}", "PATH+JAVA=${jdk17Home}/bin"]) {
                            sh '''
                                mvn -B -f "${MODULE_DIR}/pom.xml" \
                                  org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
                                  -Dsonar.projectKey=lab-02-springboot-basics \
                                  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                            '''
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
    }

}
