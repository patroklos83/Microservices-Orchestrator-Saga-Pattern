
# Sample Microservices Orchestrator including Saga Pattern Rollback

An example of orchestrating several microservices using event-driven architecture and reactive programming.


## Requirements

For building and running the application you need:

-   [JDK 17 or newer](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
-  [Eclipse IDE or other Java compatible IDE](https://www.eclipse.org/ide/)
- [Docker 4.17.0 or newer ](https://www.docker.com/products/docker-desktop/)

## Some useful information

 - Microservices are built using Spring Boot 3 
 - Event-Driven design using Kafka broker and Zookeeper Docker container

## Case Study

An Order Management system uses Microservices in the backed to execute and process Order requests. When an Order request is created it stays in Pending status until the payment is executed successfully and product item is deduced from inventory stock with no failures.

![enter image description here](/images/uml.png)

## Running the Microservices

To run the microservices there are 2 options. 

 1. Running the whole system in Docker containers. 
 2. Run only Kafka services on Docker container and launch the Spring Boot
    Microservices from the IDE.

## Running the Microservices on Docker containers

Step 1. Build target .jar files

    /common/mvnw clean install
    /inventory_service/mvnw clean package
    /orchestrator_service/mvnw clean package
    /order_service/mvnw clean package
    /payment_service/mvnw clean package

Step 2. Run Docker Compose file
*from the root directory run command* 

    docker compose up


## Running the Microservices from IDE 

Step 1. Import project in the IDE 

Step 2. From root directory run command

     docker compose -f docker-compose-dev up
 
Step 3. Clean and build the nested projects/springboot applications 
 
Step 4. Replace application.yaml file contents with application-local-dev.yaml [*where applicable*]

Step 5. Go to each SpringBoot project and run com.patroclos.SpringbootApplication as java application.

**Wait until the log message appears on screen indicating the service is started, for example (ORCHESTRATOR SERVICE STARTED)*

## Microservices REST API URLs

 - http://localhost:8083/orchestrator-service/swagger-ui/index.html#/
 - http://localhost:8080/order-service/swagger-ui/index.html#/   
 - http://localhost:8082/payment-service/swagger-ui/index.html#/
 - http://localhost:8086/inventory-service/swagger-ui/index.html#/

## Creating a successful Event

When all services are up and running, go to below URL

http://localhost:8080/order-service/swagger-ui/index.html#/

Try creating an Order with below sample JSON request

/order/create

    {
      "customerId": 1,
      "itemId": "a9112c62-0206-4711-beaf-220c2d1bbfb0"
    }

From the ***Orchestrator service console output*** a success 'Order Complete' message should appear. In   ***Order service console output***, Order should be updated as well with status **CREATED**.

Process can be retrieved from Orchestrator Service REST API
 http://localhost:8083/orchestrator-service/swagger-ui/index.html#/

![enter image description here](/images/orchestrator-swagger-complete.PNG)

## Creating a Failed Event with Rollback

When all services are up and running, go to below URL

http://localhost:8080/order-service/swagger-ui/index.html#/

Try creating an Order with below sample JSON request
Customer with Id = 2 has inciefficient funds to pay for the Order Item.
This will result in a failed payment debit attempt. In continuation the Order will be updated from the event, as **CANCELLED**;

/order/create

    {
      "customerId": 2,
      "itemId": "a9112c62-0206-4711-beaf-220c2d1bbfb0"
    }


![enter image description here](/images/notnoughfunds.PNG)

/order/all

![enter image description here](/images/ordercancelled.PNG)

Fell free to grab a copy of this sample code, and play it yourself.