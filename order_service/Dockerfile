FROM openjdk:17-jdk-slim-buster AS builder
COPY target/order-service-0.0.1-SNAPSHOT.jar order-server.jar
ENTRYPOINT ["java","-jar","/order-server.jar"]