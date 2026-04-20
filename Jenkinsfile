pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIAL')
    VERSION = "${env.BUILD_ID}"

  }

  tools {
    maven "Maven"
  }

  stages {

    stage('Maven Build'){
        steps{
        sh 'mvn clean package  -DskipTests'
        }
    }

    

    stage('SonarQube Analysis') {
  steps {
    sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar -Dsonar.host.url=http://3.8.33.92:9000/ -Dsonar.login=squ_75a2d35e0b5b78e970b4fef107cfd81b4b267533'
  }
}


   stage('Check code coverage') {
            steps {
                script {
                    def token = "squ_75a2d35e0b5b78e970b4fef107cfd81b4b267533"
                    def sonarQubeUrl = "3.8.33.92:9000/api"
                    def componentKey = "com.ousama:restaurantlisting"
                    def coverageThreshold = 80.0

                    def response = sh (
                        script: "curl -H 'Authorization: Bearer ${token}' '${sonarQubeUrl}/measures/component?component=${componentKey}&metricKeys=coverage'",
                        returnStdout: true
                    ).trim()
					
					echo "SonarQube API response: ${response}"
                    def coverage = sh (
                        script: "echo '${response}' | jq -r '.component.measures[0].value'",
                        returnStdout: true
                    ).trim().toDouble()

                    echo "Coverage: ${coverage}"

                    if (coverage < coverageThreshold) {
                        error "Coverage is below the threshold of ${coverageThreshold}%. Aborting the pipeline."
                    }
                }
            }
        } 


      stage('Docker Build and Push') {
      steps {
          sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
          sh 'docker build -t ousamalasri/restaurant-listing-service:${VERSION} .'
          sh 'docker push ousamalasri/restaurant-listing-service:${VERSION}'
      }
    } 


     stage('Cleanup Workspace') {
      steps {
        deleteDir()
       
      }
    }



    stage('Update Image Tag in GitOps') {
      steps {
         checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[ credentialsId: 'git-ssh', url: 'git@github.com:Oussama-lasri/deployment-folder.git']])
        script {
       sh '''
          sed -i "s/image:.*/image: ousamalasri\\/restaurant-listing-service:${VERSION}/" AWS/restaurant-manifest.yml
        '''
          sh 'git checkout master'
          sh 'git add .'
          sh 'git commit -m "Update image tag"'
        sshagent(['git-ssh'])
            {
                  sh('git push origin master')
            }
        }
      }
    }

  }

}

