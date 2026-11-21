
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.Applicant;
import com.placement.model.ApplicationStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecruiterApplicationDAO {

    public List<Applicant> getApplicantsByJobId(
            int jobId, int recruiterId) throws SQLException {

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
                JOIN job_postings jp ON a.job_id = jp.job_id
                JOIN student_profiles sp ON a.student_id = sp.student_id
                JOIN users u ON sp.user_id = u.user_id
                WHERE a.job_id = ?
                  AND jp.recruiter_id = ?
                ORDER BY a.applied_at DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, recruiterId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Applicant applicant = new Applicant();

                    applicant.setApplicationId(
                            rs.getInt("application_id"));
                    applicant.setStudentId(rs.getInt("student_id"));
                    applicant.setJobId(rs.getInt("job_id"));
                    applicant.setFullName(rs.getString("full_name"));
                    applicant.setEmail(rs.getString("email"));
                    applicant.setRollNumber(rs.getString("roll_number"));
                    applicant.setBranch(rs.getString("branch"));
                    applicant.setCgpa(rs.getBigDecimal("cgpa"));
                    applicant.setBacklogs(rs.getInt("backlogs"));
                    applicant.setGraduationYear(
                            rs.getInt("graduation_year"));
                    applicant.setPhone(rs.getString("phone"));
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
            int applicationId, int recruiterId, String status)
            throws SQLException {

        String sql = """
                UPDATE applications a
                JOIN job_postings jp ON a.job_id = jp.job_id
                SET a.application_status = ?
                WHERE a.application_id = ?
                  AND jp.recruiter_id = ?
                  AND a.application_status IN (
                      'APPLIED', 'UNDER_REVIEW', 'SHORTLISTED'
                  )
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, applicationId);
            ps.setInt(3, recruiterId);

            return ps.executeUpdate() > 0;
        }
    }

    public ApplicationStatistics getApplicationStatistics(
            int jobId, int recruiterId) throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total_applications,
                       SUM(a.application_status = 'APPLIED') AS applied,
                       SUM(a.application_status = 'UNDER_REVIEW') AS under_review,
                       SUM(a.application_status = 'SHORTLISTED') AS shortlisted,
                       SUM(a.application_status = 'REJECTED') AS rejected,
                       SUM(a.application_status = 'WITHDRAWN') AS withdrawn
                FROM applications a
                JOIN job_postings jp ON a.job_id = jp.job_id
                WHERE a.job_id = ?
                  AND jp.recruiter_id = ?
                """;

        ApplicationStatistics statistics =
                new ApplicationStatistics();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, recruiterId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    statistics.setTotalApplications(
                            rs.getInt("total_applications"));
                    statistics.setApplied(rs.getInt("applied"));
                    statistics.setUnderReview(
                            rs.getInt("under_review"));
                    statistics.setShortlisted(
                            rs.getInt("shortlisted"));
                    statistics.setRejected(rs.getInt("rejected"));
                    statistics.setWithdrawn(rs.getInt("withdrawn"));
                }
            }
        }

        return statistics;
    }
}
