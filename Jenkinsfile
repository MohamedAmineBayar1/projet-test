pipeline {
    agent any
    
    // Définition des outils (assurez-vous qu'ils sont configurés dans Jenkins)
    tools {
        maven 'maven'
        jdk 'JAVA_21'
    }

    environment {
        DOCKERHUB_USER = 'aaminebayar'
        APP_NAME = 'student-management'
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupération depuis votre dépôt privé
                checkout scm
            }
        }

        stage('Clean & Compilation') {
            steps {
                // Suppression du dossier target et compilation
                sh 'mvn clean compile'
            }
        }

        stage('Création du livrable') {
            steps {
                // Génération du fichier .jar
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                // Construction de l'image Docker avec un tag basé sur le numéro de build
                sh "docker build -t ${DOCKERHUB_USER}/${APP_NAME}:${env.BUILD_NUMBER} ."
                sh "docker tag ${DOCKERHUB_USER}/${APP_NAME}:${env.BUILD_NUMBER} ${DOCKERHUB_USER}/${APP_NAME}:latest"
            }
        }
        stage('GIT') {
            steps {
               echo " Getting Project from Git " ;
            }
        }
        
        stage('MVN SONARQUBE') {
   steps {
        // Option A : Utilisation du Token (Recommandé)
        // Notez que le token va dans -Dsonar.login et on ne met PAS de password
        sh "mvn sonar:sonar \
            -Dsonar.projectKey=student-management \
            -Dsonar.host.url=http://192.168.33.10:9001 \
            -Dsonar.login=961b7a856d7c39c52fabd11fa213b7fe8189dc6f"
    }
        }

        stage('Docker Push') {
            steps {
                // Authentification et push vers DockerHub
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "echo ${PASS} | docker login -u ${USER} --password-stdin"
                        sh "docker push ${DOCKERHUB_USER}/${APP_NAME}:${env.BUILD_NUMBER}"
                        sh "docker push ${DOCKERHUB_USER}/${APP_NAME}:latest"
                    }
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                // 'Sonar-Server' est le nom configuré dans System Configuration
                withSonarQubeEnv('Sonar-Server') {
                    sh 'mvn sonar:sonar \
                        -Dsonar.projectKey=student-management \
                        -Dsonar.projectName="Student Management" \
                        -Dsonar.java.binaries=target/classes'
                }
            }
        }

        stage('Deploy to K8s') {
            steps {
                // Utilisation de kubectl avec la config qu'on a réparée ensemble
                sh 'kubectl apply -f k8s/deployment.yaml -n devops'
                sh 'kubectl apply -f k8s/service.yaml -n devops'
            }
        }
    }
}
