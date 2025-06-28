# TripManager

TripManager is a full-stack application designed to help users manage their trips, budgets, and related activities. The backend is built with Spring Boot, providing a robust REST API, while the frontend is developed using Angular, offering a dynamic and responsive user interface. The application leverages MongoDB for data storage and RabbitMQ for message queuing.

## Features

*   **User Authentication & Authorization:** Secure user registration, login, and access control.
*   **Trip Management:** Create, view, update, and delete trips.
*   **Budgeting:** Manage budgets for trips, including income and expenses.
*   **Google Maps Integration:** Visualize trip locations and plan routes.
*   **Email Notifications:** Automated email notifications for various events (e.g., password reset, budget invitations).
*   **Message Queuing:** Asynchronous processing of tasks using RabbitMQ.
*   **RESTful API:** A well-documented API for seamless frontend-backend communication.

## Technologies Used

### Backend (Spring Boot)

*   **Java 17**
*   **Spring Boot 3.1.3**
*   **Spring Data MongoDB:** For database interactions.
*   **Spring Security:** For authentication and authorization.
*   **Spring Web:** Building RESTful APIs.
*   **Spring Mail:** Sending email notifications.
*   **Thymeleaf:** For server-side rendering of email templates.
*   **Spring AMQP:** Integration with RabbitMQ.
*   **Log4j2:** For logging.
*   **java-jwt:** JSON Web Token implementation.
*   **Springdoc OpenAPI UI:** API documentation (Swagger UI).

### Frontend (Angular)

*   **Angular 18.2.3**
*   **Angular Material:** UI component library.
*   **Angular Flex Layout:** For responsive UI design.
*   **Angular Google Maps:** Integration with Google Maps API.
*   **ngx-toastr:** For toast notifications.
*   **ngx-translate:** For internationalization (i18n).

### Database & Messaging

*   **MongoDB:** NoSQL database for data storage.
*   **RabbitMQ:** Message broker for asynchronous communication.

### Development & Deployment

*   **Maven:** Build automation tool for the backend.
*   **npm/Yarn:** Package manager for the frontend.
*   **Docker Compose:** For orchestrating multi-container Docker applications (MongoDB, RabbitMQ).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

*   Java 17 Development Kit (JDK)
*   Maven
*   Node.js and npm (or Yarn)
*   Docker and Docker Compose

### Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd TripManager
    ```

2.  **Start Docker services (MongoDB and RabbitMQ):**
    ```bash
    cd docker
    docker-compose up -d
    cd ..
    ```

3.  **Build and run the Backend:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The backend will typically run on `http://localhost:8080`.

4.  **Install Frontend dependencies and run the Frontend:**
    ```bash
    cd webapp
    npm install # or yarn install
    ng serve
    ```
    The frontend will typically run on `http://localhost:4200`. By default, `ng serve` uses the development environment (`environment.development.ts`).

## Configuration

### Backend (`src/main/resources/application.properties`)

This file contains configuration for the Spring Boot backend. You might need to adjust these settings based on your environment.

```properties
# Server Port
server.port=8080

# MongoDB Configuration
# These values should match the environment variables set in docker/docker-compose.yml for the 'mongodb' service.
# For local development, they typically are:
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=tripmanager
spring.data.mongodb.username=rootuser
spring.data.mongodb.password=rootpass

# RabbitMQ Configuration
# These values should match the environment variables set in docker/docker-compose.yml for the 'rabbitmq' service.
# For local development, they typically are:
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# JWT Configuration
jwt.secret=YourSuperSecretKeyThatIsAtLeast256BitsLongAndShouldBeStoredSecurely
jwt.expiration=3600000 # 1 hour in milliseconds

# Email Configuration (Example values - configure for your SMTP server)
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.example.com
```
**Important Security Note:**
For production environments, it is **crucial** to change the following values to strong, unique, and securely managed credentials:
*   `spring.data.mongodb.username` and `spring.data.mongodb.password`: Use strong, unique credentials for your production MongoDB instance.
*   `spring.rabbitmq.username` and `spring.rabbitmq.password`: Use strong, unique credentials for your production RabbitMQ instance.
*   `jwt.secret`: Generate a very long, random, and unique secret key. **Never hardcode this in production.** Use environment variables or a dedicated secrets management system.
*   `spring.mail.username` and `spring.mail.password`: Use secure credentials for your production email service. **Never hardcode these in production.**

### Frontend (`webapp/src/environments/environment.ts` and `environment.development.ts`)

The Angular frontend uses environment files for configuration. `environment.ts` is for production, and `environment.development.ts` is for development.

**`webapp/src/environments/environment.ts` (Production)**
```typescript
export const environment = {
  apiUrl: 'https://api.yourdomain.com', // Your production backend API URL
  googleMapsApiKey: 'YOUR_PRODUCTION_GOOGLE_MAPS_API_KEY' // Your production Google Maps API Key
};
```

**`webapp/src/environments/environment.development.ts` (Development)**
```typescript
export const environment = {
  apiUrl: 'http://localhost:8080', // Your local backend API URL
  googleMapsApiKey: 'YOUR_DEVELOPMENT_GOOGLE_MAPS_API_KEY' // Your development Google Maps API Key
};
```
**Important Security Note:**
*   Replace `YOUR_PRODUCTION_GOOGLE_MAPS_API_KEY` and `YOUR_DEVELOPMENT_GOOGLE_MAPS_API_KEY` with your actual Google Maps API keys.
*   **Crucially, restrict your Google Maps API keys** to only allow requests from your specific domains (for production) or `localhost` (for development) and to only enable the necessary APIs (e.g., Maps JavaScript API, Geocoding API). This prevents unauthorized use and potential billing issues.

## Project Structure

```
.
├── docker/                 # Docker Compose configuration for services
│   └── docker-compose.yml
├── src/                    # Backend (Spring Boot) source code
│   ├── main/
│   │   ├── java/           # Java source files
│   │   └── resources/      # Application resources (properties, templates)
│   └── test/               # Backend test code
├── webapp/                 # Frontend (Angular) source code
│   ├── src/                # Angular application files
│   └── package.json        # Frontend dependencies and scripts
├── pom.xml                 # Maven build configuration for the backend
└── README.md               # This file
```
