pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
		checkout scm
		setBuildStatus ("${context}", 'Checking code coverage levels', 'PENDING')
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
