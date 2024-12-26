# robo-bob
<H2>Part 1</H2>
<H5>Development</H5>
<br> The application is developed as a Spring Boot application.</br>
<br>The code for RoboBob application
is under src/main/java</br> 
<br> Resource files are under src/main/resources</br>
<H5>Testing</H5>
<br> Test files are under src/main/test</br>
<br> Code coverage is done using jacoco. Files are under target\site\jacoco</br>
<br><img src="src/main/resources/images/robo-bob-jacoco.png" style="float:left"></br>

<H5>Deployment</H5>
<br> Project is deployed as docker container being managed by kubernetes</br>
<br>Kubernetes files are under k8,DockerFile is created for building and pushing the image</br>
<br><img src="src/main/resources/images/robo-bob-deployment-kubernetes.png" style="float:left"></br>

<H5>CICD</H5>
<br> A Jenkins pipeline is used to automate the entire workflow</br>
<br>JenkinsFile is created for managing the pipeline</br>
<br><img src="src/main/resources/images/robo-bob-pipeline [Jenkins].png" style="float:left"></br>
