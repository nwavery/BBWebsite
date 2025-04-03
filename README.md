# Blood Bowl League Stat Tracker

This is a web application for managing and tracking statistics for a Blood Bowl league, built with Spring Boot, Spring Security, Spring Data JPA, Thymeleaf, and MySQL.

## Features

*   User registration and login
*   Team CRUD (Create, Read, Update, Delete) management
    *   Race selection via Enum dropdown
    *   Validation: Unique name per user
    *   Validation: Cannot delete team with recorded matches
*   Player CRUD (Create, Read, Update, Delete) management
    *   Position selection via Enum dropdown
    *   Players associated with specific teams
    *   Validation: Unique name per team
*   Match result recording
    *   Selection of participating teams
    *   Input for scores, touchdowns, casualties
*   Match History display
*   League Standings calculation and display (sorted by Points > TD Diff > TDs Scored)
*   UI using Bootstrap 5

## Prerequisites

*   Java Development Kit (JDK) 1.8 or higher
*   Maven 3.x
*   MySQL Server

## Setup

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url>
    cd BBWebsite
    ```
2.  **Database Configuration:**
    *   Create a MySQL database (e.g., `bloodbowl_db`).
    *   Update the database connection details (URL, username, password) in `src/main/resources/application.properties`:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/bloodbowl_db?useSSL=false&serverTimezone=UTC
        spring.datasource.username=your_db_user
        spring.datasource.password=your_db_password
        ```
        *Note: The current configuration uses AWS RDS. Update as needed for your local setup.*

## Running the Application

1.  **Build the project using Maven:**
    *   Ensure Maven reloads dependencies after `pom.xml` changes (Bootstrap 5).
    ```bash
    mvn clean install
    ```
2.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the packaged JAR file:
    ```bash
    java -jar target/registration-login-springboot-security-thymeleaf-0.0.1-SNAPSHOT.jar
    ```
3.  **Access the application:**
    Open your web browser and go to `http://localhost:8069` (or the configured port).

## TODO / Potential Improvements

*   Implement Player-level statistics.
*   Add Season management.
*   Refine Authorization checks (e.g., only allow edits/deletes by owners/admins).
*   Enhance UI/UX further (custom styling, JS interactions).
*   Update dependencies (Spring Boot 2.0.4 is old).
*   Write unit and integration tests.