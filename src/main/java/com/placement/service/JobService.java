
package com.placement.service;

import com.placement.dao.JobDAO;
import com.placement.dao.CompanyDAO;
import com.placement.model.Job;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

            job.setStatus(job.getStatus().trim().toUpperCase(Locale.ROOT));

            boolean added = jobDAO.addJob(job);

            if (added) {
                return "Job posted successfully. Job ID: "
                        + job.getJobId();
            }

            return "Unable to post job.";

        } catch (SQLException e) {
            return "Database error while posting job: " + e.getMessage();
        }
    }

    public String updateJob(Job job) {

        if (job == null) {
            return "Job details cannot be empty.";
        }

        if (job.getJobId() <= 0) {
            return "Invalid job ID.";
        }

        String validation = validateJob(job);

        if (validation != null) {
            return validation;
        }

        try {

            if (jobDAO.findById(job.getJobId()) == null) {
                return "Job not found.";
            }

            if (companyDAO.findById(job.getCompanyId()) == null) {
                return "Company does not exist.";
            }

            job.setStatus(job.getStatus().trim().toUpperCase(Locale.ROOT));

            boolean updated = jobDAO.updateJob(job);

            return updated
                    ? "Job details updated successfully."
                    : "Unable to update job.";

        } catch (SQLException e) {
            return "Database error while updating job: " + e.getMessage();
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

            if (status.equalsIgnoreCase(job.getStatus())) {
                return "Job is already " + status + ".";
            }

            boolean updated = jobDAO.updateJobStatus(jobId, status);

            return updated
                    ? "Job status changed to " + status + " successfully."
                    : "Unable to update job status.";

        } catch (SQLException e) {
            return "Database error while changing job status: "
                    + e.getMessage();
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

        if (employmentType != null && employmentType.isBlank()) {
            return List.of();
        }

        String normalizedType = employmentType == null
                ? null
                : employmentType.trim();

        String normalizedStatus = status == null
                ? null
                : status.trim().toUpperCase(Locale.ROOT);

        if (normalizedStatus != null &&
                !normalizedStatus.equals("OPEN") &&
                !normalizedStatus.equals("CLOSED")) {
            return List.of();
        }

        try {
            return jobDAO.filterJobs(normalizedType, normalizedStatus);

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

        if (job.getJobTitle().trim().length() > 150) {
            return "Job title cannot exceed 150 characters.";
        }

        if (job.getJobDescription() == null ||
                job.getJobDescription().isBlank()) {
            return "Job description cannot be empty.";
        }

        if (job.getEmploymentType() == null ||
                job.getEmploymentType().isBlank()) {
            return "Employment type cannot be empty.";
        }

        if (job.getEmploymentType().trim().length() > 50) {
            return "Employment type cannot exceed 50 characters.";
        }

        if (job.getLocation() == null ||
                job.getLocation().isBlank()) {
            return "Location cannot be empty.";
        }

        if (job.getLocation().trim().length() > 100) {
            return "Location cannot exceed 100 characters.";
        }

        if (job.getMinCgpa() == null ||
                job.getMinCgpa().doubleValue() < 0 ||
                job.getMinCgpa().doubleValue() > 10) {
            return "CGPA must be between 0 and 10.";
        }

        if (job.getMaxBacklogs() < 0) {
            return "Maximum backlogs cannot be negative.";
        }

        int currentYear = Year.now().getValue();

        if (job.getGraduationYear() < 2000 ||
                job.getGraduationYear() > currentYear + 6) {
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
            return "Maximum package must be greater than or equal to minimum package.";
        }

        if (job.getApplicationDeadline() == null) {
            return "Application deadline is required.";
        }

        if (!job.getApplicationDeadline().isAfter(LocalDate.now())) {
            return "Application deadline must be a future date.";
        }

        if (job.getEligibleBranches() == null ||
                job.getEligibleBranches().isEmpty()) {
            return "At least one eligible branch is required.";
        }

        Set<String> branches = new HashSet<>();

        for (String branch : job.getEligibleBranches()) {

            if (branch == null || branch.isBlank()) {
                return "Branch names cannot be empty.";
            }

            String normalizedBranch =
                    branch.trim().toUpperCase(Locale.ROOT);

            if (!branches.add(normalizedBranch)) {
                return "Duplicate eligible branch: " + branch;
            }
        }

        if (job.getStatus() == null ||
                (!job.getStatus().trim().equalsIgnoreCase("OPEN") &&
                 !job.getStatus().trim().equalsIgnoreCase("CLOSED"))) {
            return "Status must be OPEN or CLOSED.";
        }

        return null;
    }
}
