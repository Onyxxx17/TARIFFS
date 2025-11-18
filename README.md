# TARIFFS

A full-stack web application built with a React frontend and a Spring Boot backend. This project features a tariffs calculation engine, an interactive dashboard for data visualization, mobile responsiveness, an AI-powered chatbot, API documentation via Swagger, comprehensive unit testing, and a contact form for inquiries.

## Team Members 
- Aung Ye Thant Hein
- Chue Myat Sandy
- Lam Yi Nam
- Lin Khant Pe Thein
- Jonathan

## Features

- **Tariffs Calculation Engine**: A sophisticated engine designed to handle complex tariffs calculations efficiently.
- **Interactive Dashboard**: Provides visualizations of key data and metrics for better insights.
- **Mobile-Responsive Design**: Fully functional and optimized for all screen sizes and devices.
- **AI Chatbot**: An integrated assistant to support users with queries and assistance.
- **API Documentation**: Live, interactive API docs accessible via Swagger UI for easy endpoint exploration and testing.
- **Unit Testing**: Backend logic is thoroughly covered by a robust suite of unit tests.
- **Contact Form**: Allows users to send emails for inquiries and support.

## Technologies

- **Frontend**: React, TypeScript, Vite
- **Backend**: Java Spring Boot, Maven
- **Other**: Node.js, npm, Swagger UI

## Prerequisites

Before running the application, ensure the following are installed on your system:

- Java JDK: Version 17 or later
- Apache Maven: For building and running the backend
- Node.js: For building and running the frontend (includes npm)

## Installation and Setup

Follow these steps to set up and run the application locally.

### 1. Backend Setup (Spring Boot)

The backend provides the core API and business logic.

```bash
# Navigate to the backend directory
cd backend

# Run the Spring Boot application
mvn spring-boot:run
```

The backend will be available at `http://localhost:8080`.

### 2. Frontend Setup

The frontend is the user-facing interface. Open a new terminal for this step.

```bash
# Navigate to the frontend directory
cd frontend

# Install dependencies
npm install

# Start the development server
npm run dev
```

The frontend will be accessible at `http://localhost:3000` (check your terminal for the exact port).

## API Documentation

Once the backend is running, explore the full API documentation through Swagger UI. This interactive interface allows you to view and test all available endpoints directly in your browser.

- **Swagger UI URL**: `http://localhost:8080/swagger-ui.html`

## Testing

The backend uses Maven for compilation and testing.

### Check Compilation

To verify for compilation errors without running the full application:

```bash
# From the backend directory
mvn clean compile
```

### Run Unit Tests

To execute the complete unit test suite:

```bash
# From the backend directory
mvn test
```

### View Test Reports

After running tests, an HTML report is generated. Open the following file in your browser for detailed results:

- **Report Path**: `backend/target/sites/index.html`

## Contributing

Contributions are welcome! Please fork the repository, create a feature branch, and submit a pull request. Ensure all changes include appropriate tests and follow the project's coding standards.

## License

This project is licensed under the [MIT License](LICENSE) – see the LICENSE file for details.

## Acknowledgments

- Special thanks to the open-source community for tools like React, Spring Boot, and Swagger.
