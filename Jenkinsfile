pipeline {
    agent any
    
    // Définition des outils (assurez-vous qu'ils sont configurés dans Jenkins)
    tools {
        maven 'Maven 3'
        jdk 'JDK 17'
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
    }
}
