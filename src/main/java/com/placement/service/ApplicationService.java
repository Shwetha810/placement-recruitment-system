
package com.placement.service;

import com.placement.dao.ApplicationDAO;
import com.placement.dao.EligibilityDAO;
import com.placement.model.Application;
import com.placement.model.Applicant;
import com.placement.model.ApplicationStatistics;
import com.placement.model.Job;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ApplicationService {

    private final ApplicationDAO applicationDAO;
    private final EligibilityDAO eligibilityDAO;

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "UNDER_REVIEW",
            "SHORTLISTED",
            "REJECTED"
    );

    public ApplicationService() {
        this.applicationDAO = new ApplicationDAO();
        this.eligibilityDAO = new EligibilityDAO();
    }

    public String applyForJob(int studentId, int jobId) {

        if (studentId <= 0 || jobId <= 0) {
            return "Invalid student ID or job ID.";
        }

        try {

            List<Job> eligibleJobs =
                    eligibilityDAO.getEligibleJobs(studentId);

            boolean eligible = eligibleJobs.stream()
                    .anyMatch(job -> job.getJobId() == jobId);

            if (!eligible) {
                return "You are not eligible for this job, "
                        + "or the job is not available.";
            }

            Application application = new Application();
            application.setStudentId(studentId);
            application.setJobId(jobId);

            int applicationId =
                    applicationDAO.applyForJob(application);

            if (applicationId > 0) {
                return "Application submitted successfully. Application ID: "
                        + applicationId;
            }

            return "Unable to submit application.";

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                return "You have already applied for this job.";
            }

            return "Database error while submitting application: "
                    + e.getMessage();
        }
    }

    public List<Application> getMyApplications(int studentId) {

        if (studentId <= 0) {
            return List.of();
        }

        try {
            return applicationDAO.getApplicationsByStudentId(studentId);

        } catch (SQLException e) {
            System.out.println("Error retrieving applications: "
                    + e.getMessage());
            return List.of();
        }
    }

    public String withdrawApplication(
            int applicationId, int studentId) {

        if (applicationId <= 0 || studentId <= 0) {
            return "Invalid application ID or student ID.";
        }

        try {

            boolean withdrawn =
                    applicationDAO.withdrawApplication(
                            applicationId, studentId);

            if (withdrawn) {
                return "Application withdrawn successfully.";
            }

            return "Application not found or cannot be withdrawn.";

        } catch (SQLException e) {
            return "Database error while withdrawing application: "
                    + e.getMessage();
        }
    }

    public List<Applicant> getApplicantsByJobId(int jobId) {

        if (jobId <= 0) {
            return List.of();
        }

        try {
            return applicationDAO.getApplicantsByJobId(jobId);

        } catch (SQLException e) {
            System.out.println("Error retrieving applicants: "
                    + e.getMessage());
            return List.of();
        }
    }

    public String updateApplicationStatus(
            int applicationId, String status) {

        if (applicationId <= 0) {
            return "Invalid application ID.";
        }

        if (status == null || status.isBlank()) {
            return "Application status cannot be empty.";
        }

        String normalizedStatus =
                status.trim().toUpperCase(Locale.ROOT);

        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            return "Invalid application status. Allowed values: "
                    + ALLOWED_STATUSES;
        }

        try {

            boolean updated =
                    applicationDAO.updateApplicationStatus(
                            applicationId, normalizedStatus);

            if (updated) {
                return "Application status updated successfully.";
            }

            return "Application not found or status cannot be updated.";

        } catch (SQLException e) {
            return "Database error while updating application status: "
                    + e.getMessage();
        }
    }

    public ApplicationStatistics getApplicationStatistics(int jobId) {

        if (jobId <= 0) {
            return null;
        }

        try {
            return applicationDAO.getApplicationStatistics(jobId);

        } catch (SQLException e) {
            System.out.println("Error retrieving statistics: "
                    + e.getMessage());
            return null;
        }
    }
}
