ARG key1
FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/coreteamservice
ADD target/core-team-service-$key1.jar coreteamservice.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/coreteamservice.jar"]

