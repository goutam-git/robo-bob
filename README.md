# robo-bob
<H2>Part 1</H2>
<H3>Assumptions</H3>
<p>In the current scope add more questions
and answers. will be done by adding questions to the file</p>
<p>Flow of adding the questions to the external storage is not in he current scope</p>
<H5>Development</H5>
<p> The application is developed as a Spring Boot application.</p>
<p>The code for RoboBob application
is under src/main/java</p> 
<p> Resource files are under src/main/resources</p>
<H5>Testing</H5>
<p> Test files are under src/main/test</p>
<p> Code coverage is done using jacoco. Files are under target\site\jacoco</p>
<p><img src="src/main/resources/images/robo-bob-jacoco.png" style="float:left"></p>

<H5>Deployment</H5>
<p> Project is deployed as docker container being managed by kubernetes</p>
<p>Kubernetes files are under k8,DockerFile is created for building and pushing the image</p>
<p><img src="src/main/resources/images/robo-bob-deployment-kubernetes.png" style="float:left"></p>

<H5>CI/CD</H5>
<p> A Jenkins pipeline is used to automate the entire workflow</p>
<p>JenkinsFile is created for managing the pipeline</p>
<p><img src="src/main/resources/images/robo-bob-pipeline-Jenkins.png" style="float:left"></p>

<H2>Part 2</H2>
<H3>Assumptions</H3>
<p>The 100 qps are for reading the basic questions.The addition of questions is assumed to 100 new questions per day</p>
<p>A single record comprising of questions and answers would be around 200 KB</p>
<p>Questions are added by Admins users or users 
 with special privileges and no of such users are far less</p>
<p>The original condition of prompting the user to ask a different question in case of an 
  unknown question is still valid</p>
<p>The admin or privileged users determine which questions to add</p>
<p>F or same question multiple answer may exists</p>
<p>Cloud infrastructure is available through AWS.</p>