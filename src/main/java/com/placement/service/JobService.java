
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

    public String updateJob(Job job) {

        String validation = validateJob(job);

        if (validation != null) {
            return validation;
        }

        if (job.getJobId() <= 0) {
            return "Invalid job ID.";
        }

        try {
            if (jobDAO.findById(job.getJobId()) == null) {
                return "Job not found.";
            }

            if (companyDAO.findById(job.getCompanyId()) == null) {
                return "Company does not exist.";
            }

            boolean updated = jobDAO.updateJob(job);

            return updated
                    ? "Job details updated successfully."
                    : "Unable to update job.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public String closeJob(int jobId) {
        return changeJobStatus(jobId, "CLOSED");
    }

    public String reopenJob(int jobId) {
        return changeJobStatus(jobId, "OPEN");
    }

    private String changeJobStatus(int jobId, String status) {

        if (jobId <= 0) {
            return "Invalid job ID.";
        }

        try {
            Job job = jobDAO.findById(jobId);

            if (job == null) {
                return "Job not found.";
            }

            if (status.equals(job.getStatus())) {
                return "Job is already " + status + ".";
            }

            boolean updated = jobDAO.updateJobStatus(jobId, status);

            return updated
                    ? "Job status changed to " + status + " successfully."
                    : "Unable to update job status.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public String deleteJob(int jobId) {

        if (jobId <= 0) {
            return "Invalid job ID.";
        }

        try {
            if (jobDAO.findById(jobId) == null) {
                return "Job not found.";
            }

            boolean deleted = jobDAO.deleteJob(jobId);

            return deleted
                    ? "Job deleted successfully."
                    : "Unable to delete job.";

        } catch (SQLException e) {
            return "Unable to delete job: " + e.getMessage();
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

    public List<Job> searchByCompanyName(String companyName) {

        if (companyName == null || companyName.isBlank()) {
            return List.of();
        }

        try {
            return jobDAO.searchByCompanyName(companyName.trim());

        } catch (SQLException e) {
            System.out.println("Unable to search jobs: " + e.getMessage());
            return List.of();
        }
    }

    public List<Job> searchByLocation(String location) {

        if (location == null || location.isBlank()) {
            return List.of();
        }

        try {
            return jobDAO.searchByLocation(location.trim());

        } catch (SQLException e) {
            System.out.println("Unable to search jobs: " + e.getMessage());
            return List.of();
        }
    }

    public List<Job> filterJobs(String employmentType, String status) {

        if (employmentType != null &&
                employmentType.isBlank()) {
            return List.of();
        }

        if (status != null &&
                !status.equals("OPEN") &&
                !status.equals("CLOSED")) {
            return List.of();
        }

        try {
            return jobDAO.filterJobs(employmentType, status);

        } catch (SQLException e) {
            System.out.println("Unable to filter jobs: " + e.getMessage());
            return List.of();
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

        if (job.getLocation() == null ||
                job.getLocation().isBlank()) {
            return "Location cannot be empty.";
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
                !job.getApplicationDeadline().isAfter(LocalDate.now())) {
            return "Application deadline must be a future date.";
        }

        if (job.getEligibleBranches() == null ||
                job.getEligibleBranches().isEmpty()) {
            return "At least one eligible branch is required.";
        }

        if (job.getEligibleBranches().stream().anyMatch(
                branch -> branch == null || branch.isBlank())) {
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
