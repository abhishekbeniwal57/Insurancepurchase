@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-17.0.12"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "MAVEN_HOME=C:\Program Files\Apache\maven\apache-maven-3.9.9"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

echo Using Java version:
"%JAVA_HOME%\bin\java" -version
echo.
echo Starting the application...
"%MAVEN_HOME%\bin\mvn" spring-boot:run 