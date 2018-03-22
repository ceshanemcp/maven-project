pipeline {
    agent { docker { image 'maven:3.3.3' } }
    stages {
        stage('Checkout') {
            steps {
		checkout scm
	    }
        }
        stage('build') {
            steps {
                 bat 'echo hello world'
            }
        }
    }
}
