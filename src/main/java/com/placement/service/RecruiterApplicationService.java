
package com.placement.service;

import com.placement.dao.RecruiterApplicationDAO;
import com.placement.model.Applicant;
import com.placement.model.ApplicationStatistics;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class RecruiterApplicationService {

    private final RecruiterApplicationDAO dao =
            new RecruiterApplicationDAO();

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "UNDER_REVIEW",
            "SHORTLISTED",
            "REJECTED"
    );

    public List<Applicant> getApplicants(int jobId, int recruiterId) {

        if (jobId <= 0 || recruiterId <= 0) {
            return List.of();
        }

        try {
            return dao.getApplicantsByJobId(jobId, recruiterId);
        } catch (SQLException e) {
            System.out.println("Unable to retrieve applicants: "
                    + e.getMessage());
            return List.of();
        }
    }

    public String updateStatus(
            int applicationId, int recruiterId, String status) {

        if (applicationId <= 0 || recruiterId <= 0) {
            return "Invalid application or recruiter ID.";
        }

        if (status == null
                || !ALLOWED_STATUSES.contains(status.trim().toUpperCase())) {
            return "Invalid application status.";
        }

        try {
            boolean updated = dao.updateApplicationStatus(
                    applicationId,
                    recruiterId,
                    status.trim().toUpperCase());

            return updated
                    ? "Application status updated successfully."
                    : "Application not found under your jobs or cannot be updated.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public ApplicationStatistics getStatistics(
            int jobId, int recruiterId) {

        if (jobId <= 0 || recruiterId <= 0) {
            return null;
        }

        try {
            return dao.getApplicationStatistics(jobId, recruiterId);
        } catch (SQLException e) {
            System.out.println("Unable to retrieve statistics: "
                    + e.getMessage());
            return null;
        }
    }
}
