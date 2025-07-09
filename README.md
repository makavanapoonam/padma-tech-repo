# Employee Management System

A Spring Boot application for managing employees and departments with a web frontend.

## Features

- RESTful API for managing employees and departments
- H2 in-memory database
- JasperReports for generating PDF reports
- HTML/CSS/JavaScript frontend
- Bootstrap 5 responsive design
- Product card showcase

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Running the Application

1. Clone this repository
2. Navigate to the project directory
3. Run the application using Maven:

```bash
mvn spring-boot:run
```

4. Access the application at http://localhost:8080

## API Endpoints

### Employees
- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `GET /api/employees/department/{departmentId}` - Get employees by department ID
- `POST /api/employees/department/{departmentId}` - Add a new employee to a department
- `PUT /api/employees/{id}` - Update an employee
- `DELETE /api/employees/{id}` - Delete an employee

### Departments
- `GET /api/departments` - Get all departments
- `GET /api/departments/{id}` - Get department by ID
- `POST /api/departments` - Create a new department
- `PUT /api/departments/{id}` - Update a department
- `DELETE /api/departments/{id}` - Delete a department
- `GET /api/departments/report` - Generate PDF report of employees by department

## Technologies Used

- Spring Boot
- Spring Data JPA
- H2 Database
- JasperReports
- Bootstrap 5
- HTML/CSS/JavaScript 