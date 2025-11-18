# TARIFFS

This project is a full-stack application featuring React frontend and a Java Springboot backend. It includes a tariff calculation engine, an interactive dashboard, and an AI chatbot.

Features
Tariff Calculation: Complex calculation engine for handling tariffs.

Interactive Dashboard: Visualizing key data and metrics.

Mobile-Responsive: Fully functional and usable on all screen sizes.

AI Chatbot: Integrated assistant for user support.

API Documentation: Live API documentation via Swagger UI.

Unit Tested: Backend logic is covered by a robust suite of unit tests.

Contact page: Able to send emails for queries

Prerequisites
Before you begin, ensure you have the following installed on your system:

Java JDK: Version 17 or newer.

Apache Maven: To build and run the backend.

Node.js: To build and run the frontend (includes npm).

Getting Started
To get a local copy up and running, follow these steps.

1. Backend Setup (Spring Boot)
The backend server provides the core API and business logic.

Bash

# Navigate to the backend directory
cd backend

# Run the Spring Boot application
mvn spring-boot:run
The backend will now be running at http://localhost:8080.

2. Frontend Setup
The frontend is the user-facing application. Open a new terminal for this step.

Bash

# Navigate to the frontend directory
cd frontend

# Install all necessary dependencies
npm install

# Run the frontend development server
npm run dev
The frontend application will be accessible at http://localhost:3000 (or check your terminal for the correct port).

API Documentation (Swagger)
Once the backend is running, you can explore the full API documentation. This interactive UI allows you to see all available endpoints and test them directly from your browser.

Swagger UI URL: http://localhost:8080/swagger-ui.html

Testing the Backend
The backend is configured with Maven for compiling and testing.

Check Compilation
To check for any compilation errors without running the full application:

Bash

# From the /backend directory
mvn clean compile
Run Unit Tests
To execute the complete unit test suite:

Bash

# From the /backend directory
mvn test
View Test Reports
After running mvn test, a detailed HTML report is generated. You can open the following file in your browser to view the results:

Report Path: backend/target/sites/index.html
