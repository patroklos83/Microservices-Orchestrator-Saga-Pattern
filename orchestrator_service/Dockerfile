FROM openjdk:17-jdk-slim-buster AS builder
COPY target/orchestrator_service-0.0.1-SNAPSHOT.jar orchestrator-server.jar
ENTRYPOINT ["java","-jar", "/orchestrator-server.jar"]