FROM maven:3.9.9-eclipse-temurin-17-focal AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:17-jdk-focal
ADD target/app-0.0.1-SNAPSHOT.jar app-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app-0.0.1-SNAPSHOT.jar"]