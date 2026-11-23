
CREATE DATABASE IF NOT EXISTS placement_management;

USE placement_management;

-- 1. Users
CREATE TABLE users (
    user_id INT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 2. Student Profiles
CREATE TABLE student_profiles (
    student_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    roll_number VARCHAR(30) NOT NULL,
    branch VARCHAR(50) NOT NULL,
    cgpa DECIMAL(4,2) NOT NULL,
    backlogs INT NOT NULL DEFAULT 0,
    graduation_year INT NOT NULL,
    phone VARCHAR(15) DEFAULT NULL,
    PRIMARY KEY (student_id),
    UNIQUE KEY user_id (user_id),
    UNIQUE KEY roll_number (roll_number),
    CONSTRAINT student_profiles_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT student_profiles_chk_1
        CHECK (cgpa >= 0 AND cgpa <= 10),
    CONSTRAINT student_profiles_chk_2
        CHECK (backlogs >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 3. Companies
CREATE TABLE companies (
    company_id INT NOT NULL AUTO_INCREMENT,
    company_name VARCHAR(150) NOT NULL,
    location VARCHAR(100) DEFAULT NULL,
    website VARCHAR(255) DEFAULT NULL,
    industry VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 4. Company Recruiters
CREATE TABLE company_recruiters (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    company_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY user_id (user_id),
    KEY company_id (company_id),
    CONSTRAINT company_recruiters_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT company_recruiters_ibfk_2
        FOREIGN KEY (company_id) REFERENCES companies(company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 5. Jobs
CREATE TABLE jobs (
    job_id INT NOT NULL AUTO_INCREMENT,
    company_id INT NOT NULL,
    job_title VARCHAR(150) NOT NULL,
    job_description TEXT,
    min_cgpa DECIMAL(4,2) NOT NULL,
    max_backlogs INT NOT NULL,
    graduation_year INT NOT NULL,
    package_lpa DECIMAL(6,2) DEFAULT NULL,
    application_deadline DATE NOT NULL,
    job_status VARCHAR(20) DEFAULT 'OPEN',
    PRIMARY KEY (job_id),
    KEY company_id (company_id),
    CONSTRAINT jobs_ibfk_1
        FOREIGN KEY (company_id) REFERENCES companies(company_id),
    CONSTRAINT jobs_chk_1
        CHECK (min_cgpa >= 0 AND min_cgpa <= 10),
    CONSTRAINT jobs_chk_2
        CHECK (max_backlogs >= 0),
    CONSTRAINT jobs_chk_3
        CHECK (package_lpa IS NULL OR package_lpa >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 6. Job Postings
CREATE TABLE job_postings (
    job_id INT NOT NULL AUTO_INCREMENT,
    company_id INT NOT NULL,
    recruiter_id INT DEFAULT NULL,
    job_title VARCHAR(150) NOT NULL,
    job_description TEXT NOT NULL,
    employment_type VARCHAR(50) NOT NULL,
    location VARCHAR(100) DEFAULT NULL,
    min_cgpa DECIMAL(4,2) NOT NULL,
    max_backlogs INT NOT NULL DEFAULT 0,
    graduation_year INT NOT NULL,
    min_package DECIMAL(8,2) DEFAULT NULL,
    max_package DECIMAL(8,2) DEFAULT NULL,
    application_deadline DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (job_id),
    KEY company_id (company_id),
    KEY idx_job_postings_recruiter_id (recruiter_id),
    CONSTRAINT fk_job_postings_recruiter
        FOREIGN KEY (recruiter_id) REFERENCES users(user_id),
    CONSTRAINT job_postings_ibfk_1
        FOREIGN KEY (company_id) REFERENCES companies(company_id),
    CONSTRAINT job_postings_chk_1
        CHECK (min_cgpa >= 0 AND min_cgpa <= 10),
    CONSTRAINT job_postings_chk_2
        CHECK (max_backlogs >= 0),
    CONSTRAINT job_postings_chk_3
        CHECK (min_package IS NULL OR min_package >= 0),
    CONSTRAINT job_postings_chk_4
        CHECK (max_package IS NULL OR max_package >= 0),
    CONSTRAINT job_postings_chk_5
        CHECK (
            min_package IS NULL
            OR max_package IS NULL
            OR max_package >= min_package
        ),
    CONSTRAINT job_postings_chk_6
        CHECK (status IN ('OPEN', 'CLOSED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 7. Job Eligible Branches
CREATE TABLE job_eligible_branches (
    id INT NOT NULL AUTO_INCREMENT,
    job_id INT NOT NULL,
    branch_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY job_id (job_id, branch_name),
    CONSTRAINT fk_job_eligible_branches_job
        FOREIGN KEY (job_id)
        REFERENCES job_postings(job_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 8. Applications
CREATE TABLE applications (
    application_id INT NOT NULL AUTO_INCREMENT,
    student_id INT NOT NULL,
    job_id INT NOT NULL,
    applied_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    application_status VARCHAR(30) DEFAULT 'APPLIED',
    PRIMARY KEY (application_id),
    UNIQUE KEY student_id (student_id, job_id),
    KEY fk_application_job (job_id),
    CONSTRAINT applications_ibfk_1
        FOREIGN KEY (student_id)
        REFERENCES student_profiles(student_id),
    CONSTRAINT fk_application_job
        FOREIGN KEY (job_id)
        REFERENCES job_postings(job_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 9. Recruitment Rounds
CREATE TABLE recruitment_rounds (
    round_id INT NOT NULL AUTO_INCREMENT,
    job_id INT NOT NULL,
    round_name VARCHAR(100) NOT NULL,
    round_number INT NOT NULL,
    round_date DATE DEFAULT NULL,
    PRIMARY KEY (round_id),
    UNIQUE KEY job_id (job_id, round_number),
    CONSTRAINT recruitment_rounds_ibfk_1
        FOREIGN KEY (job_id) REFERENCES jobs(job_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- 10. Application Round Results
CREATE TABLE application_round_results (
    result_id INT NOT NULL AUTO_INCREMENT,
    application_id INT NOT NULL,
    round_id INT NOT NULL,
    result_status VARCHAR(30) NOT NULL,
    feedback TEXT,
    PRIMARY KEY (result_id),
    UNIQUE KEY application_id (application_id, round_id),
    KEY round_id (round_id),
    CONSTRAINT application_round_results_ibfk_1
        FOREIGN KEY (application_id)
        REFERENCES applications(application_id),
    CONSTRAINT application_round_results_ibfk_2
        FOREIGN KEY (round_id)
        REFERENCES recruitment_rounds(round_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
