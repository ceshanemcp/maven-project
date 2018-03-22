pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
		checkout scm
	    }
        }
	stage('Unit Test') { 
      		steps {
        	bat 'mvn clean test'
      		}
    	}
        stage('build') {
            steps {
                 bat 'echo hello world'
            }
        }
    }
}
