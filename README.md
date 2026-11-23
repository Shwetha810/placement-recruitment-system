# Placement & Recruitment Management System

A console-based Placement & Recruitment Management System developed using Core Java, JDBC, and MySQL. The application simplifies campus recruitment activities by managing student registrations, company details, job postings, eligibility checks, job applications, and recruitment processes through role-based dashboards.

## Overview

The Placement & Recruitment Management System provides separate functionalities for Students, Recruiters, and Administrators.

- **Students** can create profiles, check their eligibility for job opportunities, apply for jobs, track application status, and withdraw applications.
- **Recruiters** can manage their job postings, view applicants, update application statuses, and monitor recruitment statistics.
- **Administrators** can manage students, recruiters, companies, job postings, and applications.

The application follows a layered architecture using Model, DAO, Service, and UI packages to maintain separation of responsibilities and improve code organization.

## Technologies Used

- Java 21
- Core Java
- JDBC (Java Database Connectivity)
- MySQL
- Apache Maven
- BCrypt (Password Hashing)
- Git and GitHub
- Visual Studio Code

## Features

### 1. Student Module

- Student registration and secure login
- Student profile creation and management
- Automatic identification of student profiles
- View available job opportunities
- Check eligibility based on CGPA, backlogs, branch, and graduation year
- Apply for eligible job postings
- View application history
- Track application statuses
- Withdraw submitted applications

### 2. Recruiter Module

- Secure recruiter registration and login
- View recruiter-owned job postings
- Create new job postings
- Update job details
- Close and reopen job postings
- Delete job postings
- Search job postings
- View applicants for job postings
- Update application statuses
- View application statistics

### 3. Admin Module

- Admin login and dashboard
- View all registered students
- View all registered recruiters
- Manage company details
- Manage job postings
- Manage applications

### 4. Company Management

- Add new companies
- View all registered companies
- Search companies by name
- View company details using company ID
- Update company information
- Delete company records

### 5. Job Management

- Create job postings associated with companies
- View all job postings
- Search jobs by company and location
- View job details
- Update job requirements and details
- Define eligible branches
- Configure academic eligibility criteria
- Close and reopen job postings
- Delete job postings

### 6. Application Management

- Submit job applications
- Prevent duplicate applications for the same job
- View applications submitted by students
- Withdraw applications
- View applicants for specific job postings
- Update application statuses

Supported application statuses:

- APPLIED
- UNDER_REVIEW
- SHORTLISTED
- REJECTED
- WITHDRAWN

### 7. Authentication and Security

- User registration and login
- Role-based dashboard navigation
- BCrypt password hashing
- Password verification during login
- Input validation for registration and profile details
- Database operations using JDBC

## Project Architecture

The application follows a layered architecture:

| Layer | Responsibility |
|---|---|
| Model | Represents application entities and data |
| DAO | Handles database operations |
| Service | Contains business logic and validation |
| UI | Provides console-based menus and user interaction |
| Config | Manages database connectivity |

## Project Structure

```text
PlacementRecruitmentSystem/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── placement/
│       │           ├── config/
│       │           ├── model/
│       │           ├── dao/
│       │           ├── service/
│       │           └── ui/
│       │
│       └── resources/
│           ├── db.properties
│           └── db.properties.example
│
├── screenshots/
│
├── pom.xml
├── .gitignore
└── README.md
```

## Database

The application uses a MySQL database named `placement_management`.

The database manages the following main entities:

- `users` – Stores user account details and roles.
- `student_profiles` – Stores student academic and contact information.
- `companies` – Stores registered company details.
- `job_postings` – Stores job descriptions and eligibility requirements.
- `job_eligible_branches` – Stores eligible branches for each job.
- `applications` – Stores student job applications and their statuses.

Database configuration is maintained locally in:

`src/main/resources/db.properties`

For security reasons, the actual configuration file containing database credentials should not be committed to the repository.

Use `db.properties.example` as a reference when configuring your local database connection.

## Getting Started

### Prerequisites

Make sure the following software is installed:

- Java JDK 21
- Apache Maven
- MySQL Server
- MySQL Workbench (optional)
- Git

### Setup Instructions

**1. Clone the repository**

```bash
git clone https://github.com/Shwetha810/placement-recruitment-system.git
```

**2. Navigate to the project directory**

```bash
cd placement-recruitment-system
```

**3. Create the MySQL database**

```sql
CREATE DATABASE placement_management;
```

**4. Create the required database tables**

Execute the SQL statements provided in the project's database schema.

**5. Configure database connectivity**

Create a file named `db.properties` inside:

`src/main/resources/`

Add your local MySQL connection details using the property names expected by `DBConnection.java`.

Example:

```properties
db.url=jdbc:mysql://localhost:3306/placement_management
db.username=YOUR_MYSQL_USERNAME
db.password=YOUR_MYSQL_PASSWORD
```

**6. Build the project**

```bash
mvn clean compile
```

**7. Run the application**

```bash
mvn exec:java "-Dexec.mainClass=com.placement.ui.LoginTest" "-Dexec.classpathScope=runtime"
```

The application will start with the main menu, where users can register, log in, or exit.

## Application Screenshots

Screenshots of the application will be added here to demonstrate the Student, Recruiter, and Admin dashboards and their functionalities.

<!-- Add screenshots here after creating the screenshots folder. -->

## Current Development Status

The core Placement & Recruitment Management System has been implemented using Core Java, JDBC, and MySQL.

The Student, Recruiter, and Admin modules are implemented, including authentication, profile management, company and job management, eligibility checking, application processing, and recruitment statistics.

The application currently operates through a console-based interface.

## Future Enhancements

- Develop a web-based user interface.
- Integrate Spring Boot and REST APIs.
- Implement stronger server-side role-based authorization.
- Enhance reporting and recruitment analytics.
- Add automated testing and improved application logging.

## Author

**Shwetha Nayak**

Information Science and Engineering Graduate

- GitHub: https://github.com/Shwetha810
- LinkedIn: https://www.linkedin.com/in/shwetha-nayak18/