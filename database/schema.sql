USE placement_management;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_profiles (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    roll_number VARCHAR(30) NOT NULL UNIQUE,
    branch VARCHAR(50) NOT NULL,
    cgpa DECIMAL(4,2) NOT NULL,
    backlogs INT NOT NULL DEFAULT 0,
    graduation_year INT NOT NULL,
    phone VARCHAR(15),

    FOREIGN KEY (user_id)
    REFERENCES users(user_id),

    CHECK (cgpa >= 0 AND cgpa <= 10),
    CHECK (backlogs >= 0)
);

CREATE TABLE companies (
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(150) NOT NULL,
    location VARCHAR(100),
    website VARCHAR(255),
    industry VARCHAR(100)
);

CREATE TABLE company_recruiters (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    company_id INT NOT NULL,

    UNIQUE (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (company_id) REFERENCES companies(company_id)
);

CREATE TABLE jobs (
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT NOT NULL,
    job_title VARCHAR(150) NOT NULL,
    job_description TEXT,
    min_cgpa DECIMAL(4,2) NOT NULL,
    max_backlogs INT NOT NULL,
    graduation_year INT NOT NULL,
    package_lpa DECIMAL(6,2),
    application_deadline DATE NOT NULL,
    job_status VARCHAR(20) DEFAULT 'OPEN',

    FOREIGN KEY (company_id)
    REFERENCES companies(company_id),

    CHECK (min_cgpa >= 0 AND min_cgpa <= 10),
    CHECK (max_backlogs >= 0),
    CHECK (package_lpa IS NULL OR package_lpa >= 0)
);

CREATE TABLE job_eligible_branches (
    id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT NOT NULL,
    branch_name VARCHAR(50) NOT NULL,

    UNIQUE (job_id, branch_name),
    FOREIGN KEY (job_id)
    REFERENCES jobs(job_id)
);

CREATE TABLE applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    job_id INT NOT NULL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    application_status VARCHAR(30) DEFAULT 'APPLIED',

    UNIQUE (student_id, job_id),

    FOREIGN KEY (student_id)
    REFERENCES student_profiles(student_id),

    FOREIGN KEY (job_id)
    REFERENCES jobs(job_id)
);

CREATE TABLE recruitment_rounds (
    round_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT NOT NULL,
    round_name VARCHAR(100) NOT NULL,
    round_number INT NOT NULL,
    round_date DATE,

    UNIQUE (job_id, round_number),

    FOREIGN KEY (job_id)
    REFERENCES jobs(job_id)
);

CREATE TABLE application_round_results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    application_id INT NOT NULL,
    round_id INT NOT NULL,
    result_status VARCHAR(30) NOT NULL,
    feedback TEXT,

    UNIQUE (application_id, round_id),

    FOREIGN KEY (application_id)
    REFERENCES applications(application_id),

    FOREIGN KEY (round_id)
    REFERENCES recruitment_rounds(round_id)
);