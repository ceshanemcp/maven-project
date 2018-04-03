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
		step([$class: 'GitHubCommitStatusSetter', statusResultSource: [$class: 'ConditionalStatusResultSource', results: []]])

      		}
    	}
	    
        stage('build') {
            steps {
                 bat 'echo hello world'
            }
        }
    }
}
void setBuildStatus(context, message, state) {
  // partially hard coded URL because of https://issues.jenkins-ci.org/browse/JENKINS-36961, adjust to your own GitHub instance
  step([
      $class: "GitHubCommitStatusSetter",
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/${getRepoSlug()}"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}
