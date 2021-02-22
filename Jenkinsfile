pipeline {
    agent { label 'docker' }
    stages {
        stage('Build') {
            steps {
                script {
                    props=readProperties file: 'gradle.properties'
                }
                sh "docker build --tag ${GIT_COMMIT} --build-arg apiVersion=${props.apiVersion} ."
            }
        }
        stage('Publish') {
            when {
                branch 'master'
            }
            steps {
                sh "docker tag ${GIT_COMMIT} fintlabsacr.azurecr.io/fint-personnel-restservice-adapter:${GIT_COMMIT}"
                withDockerRegistry([credentialsId: 'fintlabsacr.azurecr.io', url: 'https://fintlabsacr.azurecr.io']) {
                    sh "docker push 'fintlabsacr.azurecr.io/fint-personnel-restservice-adapter:${GIT_COMMIT}'"
                }
                kubernetesDeploy configs: 'k8s-alpha.yaml', kubeconfigId: 'aks-alpha-fint'
            }
        }
        stage('Publish PR') {
            when { changeRequest() }
            steps {
                sh "docker tag ${GIT_COMMIT} fintlabsacr.azurecr.io/fint-personnel-restservice-adapter:${BRANCH_NAME}"
                withDockerRegistry([credentialsId: 'fintlabsacr.azurecr.io', url: 'https://fintlabsacr.azurecr.io']) {
                    sh "docker push 'fintlabsacr.azurecr.io/fint-personnel-restservice-adapter:${BRANCH_NAME}'"
                }
            }
        }
    }
}
