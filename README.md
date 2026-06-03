# KeyManager

KeyManager is a Java 21 application built with Spring Boot and Maven. It provides a REST API for managing and validating keys. The project follows a layered architecture with controllers, services, repositories, DTOs, validators, and exception handling components.

## Technology Stack

* Java 21
* Spring Boot 4
* Maven
* JUnit and Mockito

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── io.github.maksluczak.keymanager
│   │       ├── controller
│   │       ├── dto
│   │       ├── exception
│   │       ├── model
│   │       ├── repository
│   │       ├── service
│   │       ├── validator
│   │       └── KeymanagerApplication.java
│   └── resources
└── test
    └── java
        └── io.github.maksluczak.keymanager
            ├── controller
            ├── repository
            ├── service
            ├── validator
            └── KeymanagerApplicationTests.java
```

### Layers

* **controller** – REST API endpoints
* **service** – business logic
* **repository** – data access layer
* **model** – domain entities
* **dto** – request and response objects
* **validator** – custom validation logic
* **exception** – exception handling

## Prerequisites

Before running the application, make sure the following software is installed:

* JDK 21
* Maven 3.9+

Verify installation:

```bash
java -version
mvn -version
```

## Building the Project

```bash
./mvnw clean package
```

or

```bash
mvn clean package
```

## Running the Application

Using Maven:

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

Running the generated JAR:

```bash
java -jar target/keymanager-0.0.1-SNAPSHOT.jar
```

## Running Tests

```bash
./mvnw test
```

or

```bash
mvn test
```

## Docker

### Build Docker Image

```bash
docker build -t keymanager .
```

### Run Container

```bash
docker run -p 8080:8080 keymanager
```