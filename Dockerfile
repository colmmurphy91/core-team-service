FROM openjdk:8-jdk-alpine
ARG key1

RUN echo $key1
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/coreteamservice
ADD target/core-team-service-$key1.jar coreteamservice.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/coreteamservice.jar"]

