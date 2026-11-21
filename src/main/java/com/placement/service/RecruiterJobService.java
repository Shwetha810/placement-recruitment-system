
package com.placement.service;

import com.placement.dao.CompanyDAO;
import com.placement.dao.RecruiterJobDAO;
import com.placement.model.Job;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RecruiterJobService {

    private final RecruiterJobDAO recruiterJobDAO =
            new RecruiterJobDAO();

    private final CompanyDAO companyDAO = new CompanyDAO();

    public String addJob(Job job, int recruiterId) {

        String validation = validateJob(job);

        if (validation != null) {
            return validation;
        }

        if (recruiterId <= 0) {
            return "Invalid recruiter account.";
        }

        try {
            if (companyDAO.findById(job.getCompanyId()) == null) {
                return "Company does not exist.";
            }

            boolean added = recruiterJobDAO.addJob(job, recruiterId);

            return added
                    ? "Job posted successfully. Job ID: " + job.getJobId()
                    : "Unable to post job.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public List<Job> getMyJobs(int recruiterId) {

        try {
            return recruiterJobDAO.getJobsByRecruiter(recruiterId);
        } catch (SQLException e) {
            System.out.println("Unable to retrieve your jobs: "
                    + e.getMessage());
            return List.of();
        }
    }

    public Job getMyJob(int jobId, int recruiterId) {

        if (jobId <= 0 || recruiterId <= 0) {
            return null;
        }

        try {
            return recruiterJobDAO.findOwnedJob(jobId, recruiterId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    public String updateJob(Job job, int recruiterId) {

        String validation = validateJob(job);

        if (validation != null) {
            return validation;
        }

        if (job.getJobId() <= 0) {
            return "Invalid job ID.";
        }

        try {
            if (recruiterJobDAO.findOwnedJob(
                    job.getJobId(), recruiterId) == null) {
                return "Job not found under your account.";
            }

            if (companyDAO.findById(job.getCompanyId()) == null) {
                return "Company does not exist.";
            }

            return recruiterJobDAO.updateJob(job, recruiterId)
                    ? "Job details updated successfully."
                    : "Unable to update job.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public String closeJob(int jobId, int recruiterId) {
        return changeStatus(jobId, recruiterId, "CLOSED");
    }

    public String reopenJob(int jobId, int recruiterId) {
        return changeStatus(jobId, recruiterId, "OPEN");
    }

    private String changeStatus(
            int jobId, int recruiterId, String status) {

        if (jobId <= 0) {
            return "Invalid job ID.";
        }

        try {
            Job job = recruiterJobDAO.findOwnedJob(jobId, recruiterId);

            if (job == null) {
                return "Job not found under your account.";
            }

            if (status.equals(job.getStatus())) {
                return "Job is already " + status + ".";
            }

            return recruiterJobDAO.updateStatus(
                    jobId, recruiterId, status)
                    ? "Job status changed to " + status + "."
                    : "Unable to update job status.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public String deleteJob(int jobId, int recruiterId) {

        if (jobId <= 0) {
            return "Invalid job ID.";
        }

        try {
            if (recruiterJobDAO.findOwnedJob(jobId, recruiterId) == null) {
                return "Job not found under your account.";
            }

            return recruiterJobDAO.deleteJob(jobId, recruiterId)
                    ? "Job deleted successfully."
                    : "Unable to delete job.";

        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public List<Job> searchJobs(int recruiterId, String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        try {
            return recruiterJobDAO.searchJobs(
                    recruiterId, keyword.trim());
        } catch (SQLException e) {
            System.out.println("Unable to search jobs: "
                    + e.getMessage());
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

        if (job.getJobTitle() == null || job.getJobTitle().isBlank()) {
            return "Job title cannot be empty.";
        }

        if (job.getJobDescription() == null
                || job.getJobDescription().isBlank()) {
            return "Job description cannot be empty.";
        }

        if (job.getEmploymentType() == null
                || job.getEmploymentType().isBlank()) {
            return "Employment type cannot be empty.";
        }

        if (job.getLocation() == null || job.getLocation().isBlank()) {
            return "Location cannot be empty.";
        }

        if (job.getMinCgpa() == null
                || job.getMinCgpa().doubleValue() < 0
                || job.getMinCgpa().doubleValue() > 10) {
            return "CGPA must be between 0 and 10.";
        }

        if (job.getMaxBacklogs() < 0) {
            return "Maximum backlogs cannot be negative.";
        }

        if (job.getGraduationYear() < 2020) {
            return "Invalid graduation year.";
        }

        if (job.getMinPackage() != null
                && job.getMinPackage().signum() < 0) {
            return "Minimum package cannot be negative.";
        }

        if (job.getMaxPackage() != null
                && job.getMaxPackage().signum() < 0) {
            return "Maximum package cannot be negative.";
        }

        if (job.getMinPackage() != null
                && job.getMaxPackage() != null
                && job.getMaxPackage().compareTo(
                        job.getMinPackage()) < 0) {
            return "Maximum package must be greater than minimum package.";
        }

        if (job.getApplicationDeadline() == null
                || !job.getApplicationDeadline().isAfter(LocalDate.now())) {
            return "Application deadline must be a future date.";
        }

        if (job.getEligibleBranches() == null
                || job.getEligibleBranches().isEmpty()) {
            return "At least one eligible branch is required.";
        }

        if (job.getEligibleBranches().stream().anyMatch(
                branch -> branch == null || branch.isBlank())) {
            return "Branch names cannot be empty.";
        }

        return null;
    }
}
