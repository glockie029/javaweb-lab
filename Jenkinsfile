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
                    whoami
                    semgrep --version
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
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        mvn -B -f "${MODULE_DIR}/pom.xml" \
                          org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
                          -Dsonar.projectKey=lab-02-springboot-basics \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
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
