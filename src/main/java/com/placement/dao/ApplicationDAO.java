
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.Application;
import com.placement.model.Applicant;
import com.placement.model.ApplicationStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    public int applyForJob(Application application)
            throws SQLException {

        String sql = """
                INSERT INTO applications
                (student_id, job_id, application_status)
                VALUES (?, ?, 'APPLIED')
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, application.getStudentId());
            ps.setInt(2, application.getJobId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }

        return 0;
    }

    public List<Application> getApplicationsByStudentId(
            int studentId) throws SQLException {

        List<Application> applications = new ArrayList<>();

        String sql = """
                SELECT a.application_id,
                       a.student_id,
                       a.job_id,
                       a.application_status,
                       a.applied_at,
                       jp.job_title,
                       c.company_name
                FROM applications a
                JOIN job_postings jp
                    ON a.job_id = jp.job_id
                JOIN companies c
                    ON jp.company_id = c.company_id
                WHERE a.student_id = ?
                ORDER BY a.applied_at DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Application application = new Application();

                    application.setApplicationId(
                            rs.getInt("application_id"));

                    application.setStudentId(
                            rs.getInt("student_id"));

                    application.setJobId(
                            rs.getInt("job_id"));

                    application.setApplicationStatus(
                            rs.getString("application_status"));

                    if (rs.getTimestamp("applied_at") != null) {
                        application.setAppliedAt(
                                rs.getTimestamp("applied_at")
                                        .toLocalDateTime());
                    }

                    application.setJobTitle(
                            rs.getString("job_title"));

                    application.setCompanyName(
                            rs.getString("company_name"));

                    applications.add(application);
                }
            }
        }

        return applications;
    }

    public boolean withdrawApplication(
            int applicationId, int studentId) throws SQLException {

        String sql = """
                UPDATE applications
                SET application_status = 'WITHDRAWN'
                WHERE application_id = ?
                  AND student_id = ?
                  AND application_status IN ('APPLIED', 'UNDER_REVIEW')
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            ps.setInt(2, studentId);

            return ps.executeUpdate() > 0;
        }
    }

    public List<Applicant> getApplicantsByJobId(int jobId)
            throws SQLException {

        List<Applicant> applicants = new ArrayList<>();

        String sql = """
                SELECT a.application_id,
                       a.student_id,
                       a.job_id,
                       a.application_status,
                       a.applied_at,
                       u.full_name,
                       u.email,
                       sp.roll_number,
                       sp.branch,
                       sp.cgpa,
                       sp.backlogs,
                       sp.graduation_year,
                       sp.phone
                FROM applications a
                JOIN student_profiles sp
                    ON a.student_id = sp.student_id
                JOIN users u
                    ON sp.user_id = u.user_id
                WHERE a.job_id = ?
                ORDER BY a.applied_at DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Applicant applicant = new Applicant();

                    applicant.setApplicationId(
                            rs.getInt("application_id"));

                    applicant.setStudentId(
                            rs.getInt("student_id"));

                    applicant.setJobId(
                            rs.getInt("job_id"));

                    applicant.setFullName(
                            rs.getString("full_name"));

                    applicant.setEmail(
                            rs.getString("email"));

                    applicant.setRollNumber(
                            rs.getString("roll_number"));

                    applicant.setBranch(
                            rs.getString("branch"));

                    applicant.setCgpa(
                            rs.getBigDecimal("cgpa"));

                    applicant.setBacklogs(
                            rs.getInt("backlogs"));

                    applicant.setGraduationYear(
                            rs.getInt("graduation_year"));

                    applicant.setPhone(
                            rs.getString("phone"));

                    applicant.setApplicationStatus(
                            rs.getString("application_status"));

                    if (rs.getTimestamp("applied_at") != null) {
                        applicant.setAppliedAt(
                                rs.getTimestamp("applied_at")
                                        .toLocalDateTime());
                    }

                    applicants.add(applicant);
                }
            }
        }

        return applicants;
    }

    public boolean updateApplicationStatus(
            int applicationId, String status) throws SQLException {

        String sql = """
                UPDATE applications
                SET application_status = ?
                WHERE application_id = ?
                  AND application_status IN (
                      'APPLIED',
                      'UNDER_REVIEW',
                      'SHORTLISTED'
                  )
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, applicationId);

            return ps.executeUpdate() > 0;
        }
    }

    public ApplicationStatistics getApplicationStatistics(int jobId)
            throws SQLException {

        ApplicationStatistics statistics =
                new ApplicationStatistics();

        String sql = """
                SELECT COUNT(*) AS total_applications,
                       SUM(application_status = 'APPLIED') AS applied,
                       SUM(application_status = 'UNDER_REVIEW') AS under_review,
                       SUM(application_status = 'SHORTLISTED') AS shortlisted,
                       SUM(application_status = 'REJECTED') AS rejected,
                       SUM(application_status = 'WITHDRAWN') AS withdrawn
                FROM applications
                WHERE job_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {

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
        }

        return statistics;
    }
}
