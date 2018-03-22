pipeline {
    agent any
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
