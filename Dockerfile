# Build stage
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .
COPY mvnw.cmd .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Create data directory for H2 database persistence
RUN mkdir -p /app/data && chmod 777 /app/data
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Set Spring profile to prod
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"] 