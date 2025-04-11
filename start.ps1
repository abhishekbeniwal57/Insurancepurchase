$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$env:MAVEN_OPTS = "-Dmaven.compiler.source=17 -Dmaven.compiler.target=17"

Write-Host "Using Java version:"
& "$env:JAVA_HOME\bin\java" -version
Write-Host "`nStarting the application..."
& mvn spring-boot:run 