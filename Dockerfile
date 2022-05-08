FROM openjdk:16
MAINTAINER sergey sundukovskiy
COPY target/HealthLakeService-0.0.1-SNAPSHOT.jar HealthLakeService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/HealthLakeService-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080