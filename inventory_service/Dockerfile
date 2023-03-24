FROM openjdk:17-jdk-slim-buster AS builder
COPY target/inventory-service-0.0.1-SNAPSHOT.jar inventory-server.jar
ENTRYPOINT ["java","-jar","/inventory-server.jar"]