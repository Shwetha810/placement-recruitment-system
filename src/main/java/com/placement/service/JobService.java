
package com.placement.service;

import com.placement.dao.JobDAO;
import com.placement.dao.CompanyDAO;
import com.placement.model.Job;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JobService {

    private final JobDAO jobDAO = new JobDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();

    public String addJob(Job job) {

        String validation = validateJob(job);

        if (validation != null) {
            return validation;
        }

        try {

            if (companyDAO.findById(job.getCompanyId()) == null) {
                return "Company does not exist.";
            }

            boolean added = jobDAO.addJob(job);

            if (added) {
                return "Job posted successfully. Job ID: "
                        + job.getJobId();
            }

            return "Unable to post job.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public List<Job> getAllJobs() {

        try {
            return jobDAO.getAllJobs();

        } catch (SQLException e) {
            System.out.println("Unable to retrieve jobs: "
                    + e.getMessage());
            return List.of();
        }
    }

    public Job getJobById(int jobId) {

        if (jobId <= 0) {
            return null;
        }

        try {
            return jobDAO.findById(jobId);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    private String validateJob(Job job) {

        if (job == null) {
            return "Job details cannot be empty.";
        }

        if (job.getCompanyId() <= 0) {
            return "Select a valid company.";
        }

        if (job.getJobTitle() == null ||
                job.getJobTitle().isBlank()) {
            return "Job title cannot be empty.";
        }

        if (job.getJobDescription() == null ||
                job.getJobDescription().isBlank()) {
            return "Job description cannot be empty.";
        }

        if (job.getEmploymentType() == null ||
                job.getEmploymentType().isBlank()) {
            return "Employment type cannot be empty.";
        }

        if (job.getMinCgpa() == null ||
                job.getMinCgpa().doubleValue() < 0 ||
                job.getMinCgpa().doubleValue() > 10) {
            return "CGPA must be between 0 and 10.";
        }

        if (job.getMaxBacklogs() < 0) {
            return "Maximum backlogs cannot be negative.";
        }

        if (job.getGraduationYear() < 2020) {
            return "Invalid graduation year.";
        }

        if (job.getMinPackage() != null &&
                job.getMinPackage().signum() < 0) {
            return "Minimum package cannot be negative.";
        }

        if (job.getMaxPackage() != null &&
                job.getMaxPackage().signum() < 0) {
            return "Maximum package cannot be negative.";
        }

        if (job.getMinPackage() != null &&
                job.getMaxPackage() != null &&
                job.getMaxPackage().compareTo(
                        job.getMinPackage()) < 0) {
            return "Maximum package must be greater than minimum package.";
        }

        if (job.getApplicationDeadline() == null ||
                !job.getApplicationDeadline().isAfter(
                        LocalDate.now())) {
            return "Application deadline must be a future date.";
        }

        if (job.getEligibleBranches() == null ||
                job.getEligibleBranches().isEmpty()) {
            return "At least one eligible branch is required.";
        }

        if (job.getEligibleBranches().stream().anyMatch(
                branch -> branch == null ||
                        branch.isBlank())) {
            return "Branch names cannot be empty.";
        }

        if (job.getStatus() == null ||
                !(job.getStatus().equals("OPEN") ||
                        job.getStatus().equals("CLOSED"))) {
            return "Status must be OPEN or CLOSED.";
        }

        return null;
    }
}
