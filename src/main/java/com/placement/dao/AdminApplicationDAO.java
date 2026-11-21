
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.AdminApplication;
import com.placement.model.ApplicationStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminApplicationDAO {

    private static final String BASE_QUERY = """
            SELECT a.application_id,
                   a.student_id,
                   u.full_name,
                   u.email,
                   sp.roll_number,
                   sp.branch,
                   sp.cgpa,
                   sp.backlogs,
                   sp.graduation_year,
                   sp.phone,
                   a.job_id,
                   jp.job_title,
                   c.company_name,
                   jp.location,
                   a.application_status,
                   a.applied_at
            FROM applications a
            JOIN student_profiles sp
                ON a.student_id = sp.student_id
            JOIN users u
                ON sp.user_id = u.user_id
            JOIN job_postings jp
                ON a.job_id = jp.job_id
            JOIN companies c
                ON jp.company_id = c.company_id
            """;

    public List<AdminApplication> getAllApplications()
            throws SQLException {

        return executeApplicationQuery(
                BASE_QUERY + " ORDER BY a.applied_at DESC",
                null
        );
    }

    public List<AdminApplication> getApplicationsByJobId(int jobId)
            throws SQLException {

        String sql = BASE_QUERY
                + " WHERE a.job_id = ? ORDER BY a.applied_at DESC";

        return executeApplicationQuery(sql, jobId);
    }

    public List<AdminApplication> getApplicationsByStudentId(
            int studentId) throws SQLException {

        String sql = BASE_QUERY
                + " WHERE a.student_id = ? ORDER BY a.applied_at DESC";

        return executeApplicationQuery(sql, studentId);
    }

    public List<AdminApplication> getApplicationsByStatus(String status)
            throws SQLException {

        String sql = BASE_QUERY
                + " WHERE a.application_status = ?"
                + " ORDER BY a.applied_at DESC";

        return executeApplicationQuery(sql, status);
    }

    private List<AdminApplication> executeApplicationQuery(
            String sql, Object parameter) throws SQLException {

        List<AdminApplication> applications = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (parameter instanceof Integer) {
                ps.setInt(1, (Integer) parameter);
            } else if (parameter instanceof String) {
                ps.setString(1, (String) parameter);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    AdminApplication application = new AdminApplication();

                    application.setApplicationId(
                            rs.getInt("application_id"));

                    application.setStudentId(
                            rs.getInt("student_id"));

                    application.setStudentName(
                            rs.getString("full_name"));

                    application.setEmail(
                            rs.getString("email"));

                    application.setRollNumber(
                            rs.getString("roll_number"));

                    application.setBranch(
                            rs.getString("branch"));

                    application.setCgpa(
                            rs.getBigDecimal("cgpa"));

                    application.setBacklogs(
                            rs.getInt("backlogs"));

                    application.setGraduationYear(
                            rs.getInt("graduation_year"));

                    application.setPhone(
                            rs.getString("phone"));

                    application.setJobId(
                            rs.getInt("job_id"));

                    application.setJobTitle(
                            rs.getString("job_title"));

                    application.setCompanyName(
                            rs.getString("company_name"));

                    application.setLocation(
                            rs.getString("location"));

                    application.setApplicationStatus(
                            rs.getString("application_status"));

                    if (rs.getTimestamp("applied_at") != null) {
                        application.setAppliedAt(
                                rs.getTimestamp("applied_at")
                                        .toLocalDateTime());
                    }

                    applications.add(application);
                }
            }
        }

        return applications;
    }

    public ApplicationStatistics getOverallStatistics()
            throws SQLException {

        ApplicationStatistics statistics =
                new ApplicationStatistics();

        String sql = """
                SELECT COUNT(*) AS total_applications,
                       COALESCE(SUM(application_status = 'APPLIED'), 0)
                           AS applied,
                       COALESCE(SUM(application_status = 'UNDER_REVIEW'), 0)
                           AS under_review,
                       COALESCE(SUM(application_status = 'SHORTLISTED'), 0)
                           AS shortlisted,
                       COALESCE(SUM(application_status = 'REJECTED'), 0)
                           AS rejected,
                       COALESCE(SUM(application_status = 'WITHDRAWN'), 0)
                           AS withdrawn
                FROM applications
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                statistics.setTotalApplications(
                        rs.getInt("total_applications"));

                statistics.setApplied(
                        rs.getInt("applied"));

                statistics.setUnderReview(
                        rs.getInt("under_review"));

                statistics.setShortlisted(
                        rs.getInt("shortlisted"));

                statistics.setRejected(
                        rs.getInt("rejected"));

                statistics.setWithdrawn(
                        rs.getInt("withdrawn"));
            }
        }

        return statistics;
    }
}
