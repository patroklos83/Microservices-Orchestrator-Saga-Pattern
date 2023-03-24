FROM openjdk:17-jdk-slim-buster AS builder
COPY target/payment_service-0.0.1-SNAPSHOT.jar payment-server.jar
ENTRYPOINT ["java","-jar","/payment-server.jar"]