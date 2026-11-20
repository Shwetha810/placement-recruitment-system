# Placement & Recruitment Management System

A console-based Placement & Recruitment Management System developed using Core Java, JDBC, and MySQL. The application helps manage student registrations, job postings, eligibility checks, job applications, and recruitment activities.

## Overview

The system provides separate functionalities for students and recruiters. Students can view jobs matching their academic eligibility and manage their applications, while recruiters can review applicants and update application statuses.

## Technologies Used

- Java 21
- Core Java
- JDBC
- MySQL
- Maven
- BCrypt (password hashing)

## Features

### Student Module
- Student registration and profile creation
- Secure login with password hashing
- Automatic student profile identification
- View eligible job opportunities
- Apply for jobs
- View application history
- Withdraw job applications

### Recruiter Module
- View applicants for job postings
- Update application statuses
- View application statistics

### Company & Job Management
- Add, view, search, update, and delete company details
- Create and manage job postings
- Define eligible branches and academic requirements
- Check student eligibility based on CGPA, backlogs, branch, and graduation year

### Authentication
- User login and registration
- Role-based dashboard navigation
- BCrypt password hashing

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/placement/
    │       ├── config/
    │       ├── model/
    │       ├── dao/
    │       ├── service/
    │       └── ui/
    └── resources/
```

## Database

The application uses MySQL database named `placement_management`.

Database configuration is maintained locally in `src/main/resources/db.properties`.

For security reasons, the actual configuration file containing database credentials is excluded from this repository. Refer to `db.properties.example` for the configuration format.

## Getting Started

### Prerequisites

- Java JDK 21
- Apache Maven
- MySQL Server
- MySQL Workbench (optional)
- Git

### Setup

1. Clone the repository:

```bash
git clone https://github.com/Shwetha810/placement-recruitment-system.git
```

2. Open the project directory:

```bash
cd placement-recruitment-system
```

3. Create a MySQL database named `placement_management`.

4. Create the required database tables using the project's database schema.

5. Create `db.properties` inside `src/main/resources/` and configure your local MySQL connection details. Use `db.properties.example` as a reference and match the property names expected by `DBConnection.java`.

6. Build the project:

```bash
mvn clean compile
```

7. Run the application:

```bash
mvn exec:java "-Dexec.mainClass=com.placement.ui.LoginTest" "-Dexec.classpathScope=runtime"
```

## Current Development Status

The student, company, job management, eligibility, application, authentication, and recruiter functionalities have been implemented.

The admin module and further security improvements are planned for upcoming development.

## Future Enhancements

- Complete Admin Dashboard
- Stronger role-based authorization
- Spring Boot REST API integration
- Web-based user interface
- Improved reporting and analytics

## Author

**Shwetha Nayak**

Information Science and Engineering Graduate

[GitHub](https://github.com/Shwetha810)  
[LinkedIn](https://www.linkedin.com/in/shwetha-nayak18/?isSelfProfile=true)
