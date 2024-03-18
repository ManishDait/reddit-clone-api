# Reddit-Clone-API

This project is a RESTfull API clone for Reddit-Clone developed using Spring Boot and MySQL. It includes JWT (JSON Web Token) for authentication and Spring Security for authorization.

## Features

- **User Authentication**: Utilizes JWT for secure user authentication.
- **Spring Security**: Implements Spring Security to manage user roles and access control.
- **MySQL Database**: Stores user information and posts data in a MySQL database.
- **RESTful API**: Provides a set of RESTful endpoints for creating, reading, updating, and deleting posts.
- **Upvoting/Downvoting**: Allows users to upvote or downvote posts.
- **Commenting**: Users can comment on posts.

## Prerequisites

- Java Development Kit (JDK)
- Apache Maven
- Docker
- Docker Compose

## Setup

1. **Clone the repository**:

    ```bash
    git clone https://github.com/ManishDait/reddit-clone-api.git
    ```

2. **Create MySQL Database**:

    You can use Docker Compose to set up a MySQL database. Ensure Docker and Docker Compose are installed, then run the following command in the project directory:

    ```bash
    docker-compose -f mysql-docker-compose.yaml up -d
    ```

    This will create a MySQL container with the necessary configurations.
    ```
    rootuser: root
    password: root
    ```
    Our else you can use the MySQL on your machine.<br>
    
    Login to mysql and execute command below:
   ```sql
   CREATE DATABASE spring_reddit_clone;
   ```
   This command initializes a database named spring_reddit_clone, ready to be utilized by the Reddit-Clone-API application.

4. **Configure Application Properties**:

    Update the `application.yaml` file in the `src/main/resources` directory with the following MySQL database connection details:

    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/spring_reddit_clone
    ```

    Create a `secret.yaml` file in `src/main/resources` directory and copy the component of `example-secret.yaml` and set the following properties:
   ```yaml
   spring:
     datasource:
       password: <database-password>
   
   mail:
     username: <mailtrap-username>
     password: <mailtrap-password>
     port: <mailtrap-port>
   ```
   For mail properties create a [mailtrap](https://mailtrap.io/) account and get the require properties.
   

6. **Build and Run the Application**:

    Navigate to the project directory and execute the following Maven command:

    ```bash
    mvn spring-boot:run
    ```

7. **Test the API**:

    Once the application is running, you can access the API endpoints using a tool like Postman or curl.

