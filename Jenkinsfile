properties([[$class: 'GitLabConnectionProperty', gitLabConnection: 'YOUR_GITLAB_CONNECTION_NAME']])

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
		checkout scm
		setBuildStatus ("${context}", 'Checking code coverage levels', 'PENDING')
		gitlabCommitStatus {
    			bat 'echo "Testing"'
  		}
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
