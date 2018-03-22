pipeline {
    agent { docker { image 'maven:3.3.3' } }
    node ('Windows') {
    stages {
        stage ('Checkout') {
		    checkout scm
        }
        stage('build') {
            steps {
                 bat 'echo hello world'
            }
        }
    }
}
}
