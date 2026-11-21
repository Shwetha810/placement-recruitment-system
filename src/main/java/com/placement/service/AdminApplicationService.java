
package com.placement.service;

import com.placement.dao.AdminApplicationDAO;
import com.placement.model.AdminApplication;
import com.placement.model.ApplicationStatistics;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class AdminApplicationService {

    private final AdminApplicationDAO adminApplicationDAO =
            new AdminApplicationDAO();

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "APPLIED",
            "UNDER_REVIEW",
            "SHORTLISTED",
            "REJECTED",
            "WITHDRAWN"
    );

    public List<AdminApplication> getAllApplications() {
        try {
            return adminApplicationDAO.getAllApplications();
        } catch (SQLException e) {
            System.out.println("Error retrieving applications: "
                    + e.getMessage());
            return List.of();
        }
    }

    public List<AdminApplication> getApplicationsByJobId(int jobId) {

        if (jobId <= 0) {
            return List.of();
        }

        try {
            return adminApplicationDAO.getApplicationsByJobId(jobId);
        } catch (SQLException e) {
            System.out.println("Error retrieving applications: "
                    + e.getMessage());
            return List.of();
        }
    }

    public List<AdminApplication> getApplicationsByStudentId(
            int studentId) {

        if (studentId <= 0) {
            return List.of();
        }

        try {
            return adminApplicationDAO.getApplicationsByStudentId(
                    studentId);
        } catch (SQLException e) {
            System.out.println("Error retrieving applications: "
                    + e.getMessage());
            return List.of();
        }
    }

    public List<AdminApplication> getApplicationsByStatus(String status) {

        if (status == null) {
            return List.of();
        }

        String normalizedStatus = status.trim().toUpperCase();

        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            return List.of();
        }

        try {
            return adminApplicationDAO.getApplicationsByStatus(
                    normalizedStatus);
        } catch (SQLException e) {
            System.out.println("Error retrieving applications: "
                    + e.getMessage());
            return List.of();
        }
    }

    public ApplicationStatistics getOverallStatistics() {

        try {
            return adminApplicationDAO.getOverallStatistics();
        } catch (SQLException e) {
            System.out.println("Error retrieving statistics: "
                    + e.getMessage());
            return null;
        }
    }
}
