
# Overview

This project simulates the need to capture changes from a legacy database (in this case, SQLServer) using a custom-built REST API. The core of this simulation leverages Change Data Capture (CDC), particularly through the use of SpringBoot and Debezium Embedded.
Our architecture is structured in a way that all changes to the dbo.outbox table are captured and handled. Additionally, the outbox pattern has been applied to ensure atomicity of operations and data consistency.


## Kafka and Docker Compose
To simulate the distributed environment, we have incorporated Kafka and a Kafka UI interface, both running through Docker Compose. This allows us to easily simulate message sending and interactions between microservices.
Second Microservice and Kafka Producer

## First Microservice [crud-sqlserver-ms]
The first microservice plays a crucial role in our project: it simulates a legacy database, in our case, SQLServer. This is made possible by implementing a data model and functionalities that reflect those of a typical SQLServer database.
In addition to simulating the DB, the first microservice also applies the Outbox pattern. This design pattern ensures the atomicity of operations and data consistency within the context of a distributed system.

## Second Microservice [polling-ms]
Using the Polling Publisher pattern, we can send events to our message broker, playing a fundamental role in our services' interactions.
We keep track of event states in the Outbox, where we monitor ACKs at the time of publication. This verification system ensures that each event is received. Furthermore, there is a possibility for the consumer to mark an event as "correctly processed" using, for example, a correlation-id.
Unlike the previous example with CDC-Debezium, this time, we have implemented the Polling Publisher pattern.
Note how the scheduler has been implemented.
In particular, an assumption has been made that at time t0, a findAll should be done, while at time t1, a query will be made based on a certain time instant, so as to listen and manually capture all possible modifications.

## Third Microservice [polling-consumer-ms]
This is the third microservice responsible for consuming all the potential topics produced by polling-ms; once consumed, they are stored in MongoDB.


## How to Use
To try the complete flow and test everything locally, you will need Docker. After that, you will need to bring up all the images using docker-compose up. Once this is done, you will need to set up a connection to a SQL Server database. In this case, I used Azure, and you can follow a simple guide here: https://www.youtube.com/watch?v=kMCNTLnna04&ab_channel=SQLServer101. As for MongoDB, you just need to have an account on MongoCloud and create a database.
Once you have both databases set up and the images are up using Docker Compose, you will also have access to Kafka UI, which was chosen for monitoring various topics, messages, and other details accessible in this case via localhost:8080. I used SpringToolSuite as my IDE and Java 11 with Lombok and Maven.


## Links 
For more information, graphics, etc., you can find everything at: https://medium.com/@andreacavallo

## Autore
Andrea Cavallo
