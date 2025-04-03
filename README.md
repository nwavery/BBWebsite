# Blood Bowl League Stat Tracker

This is a web application for managing and tracking statistics for a Blood Bowl league, built with Spring Boot (v2.7.18), Spring Security, Spring Data JPA, Thymeleaf, and MySQL (for production).

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
*   **Match Editing and Deletion**
*   League Standings calculation and display (sorted by Points > TD Diff > TDs Scored)
*   UI using Bootstrap 5

## Prerequisites

*   Java Development Kit (JDK) 1.8 or higher
*   Maven 3.x
*   **For Production/Persistent Storage:** MySQL Server (or compatible, e.g., PostgreSQL, GCP Cloud SQL)

## Setup

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url>
    cd BBWebsite
    ```
2.  **Database Configuration (Production/Persistent):**
    *   The application is configured in `src/main/resources/application.properties` to connect to a MySQL database.
    *   It uses placeholders that default to a local setup (`jdbc:mysql://localhost:3306/bbstats`, user `dbuser`, pass `dbpassword`).
    *   **Crucially, for deployment or connecting to a real database (like Cloud SQL), override these placeholders using environment variables:**
        *   `SPRING_DATASOURCE_URL`: Full JDBC URL (e.g., `jdbc:mysql://<your_db_ip>:3306/your_db_name?useSSL=false...`)
        *   `SPRING_DATASOURCE_USERNAME`: Your database username.
        *   `SPRING_DATASOURCE_PASSWORD`: Your database password.
    *   **Schema Management:** `spring.jpa.hibernate.ddl-auto` is set to `validate`. This means the application expects the database tables to exist already. You need to create the schema manually (e.g., using SQL scripts) or implement database migrations (see TODO).
    *   *(Note: H2 dependency is included with `test` scope, suitable for running unit/integration tests without an external DB if tests are configured appropriately).* 

## Running the Application

1.  **Build the project using Maven:**
    ```bash
    mvn clean install
    ```
2.  **Run the application:**
    *   If using a production database, ensure the environment variables (`SPRING_DATASOURCE_URL`, etc.) are set before running.
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
*   **Dependencies updated to Spring Boot 2.7.18.** (Consider eventual migration to 3.x)
*   **Implement database migrations (e.g., using Flyway or Liquibase)** for reliable schema management.
*   **Configure for cloud deployment (e.g., GCP).**
*   Write unit and integration tests.