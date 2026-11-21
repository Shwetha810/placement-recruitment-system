
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.Job;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecruiterJobDAO {

    public boolean addJob(Job job, int recruiterId) throws SQLException {

        String sql = """
                INSERT INTO job_postings
                (company_id, recruiter_id, job_title, job_description,
                 employment_type, location, min_cgpa, max_backlogs,
                 graduation_year, min_package, max_package,
                 application_deadline, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int jobId;

                try (PreparedStatement ps = con.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, job.getCompanyId());
                    ps.setInt(2, recruiterId);
                    ps.setString(3, job.getJobTitle());
                    ps.setString(4, job.getJobDescription());
                    ps.setString(5, job.getEmploymentType());
                    ps.setString(6, job.getLocation());
                    ps.setBigDecimal(7, job.getMinCgpa());
                    ps.setInt(8, job.getMaxBacklogs());
                    ps.setInt(9, job.getGraduationYear());
                    ps.setBigDecimal(10, job.getMinPackage());
                    ps.setBigDecimal(11, job.getMaxPackage());
                    ps.setDate(12, Date.valueOf(job.getApplicationDeadline()));
                    ps.setString(13, "OPEN");

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("Unable to generate job ID.");
                        }
                        jobId = rs.getInt(1);
                    }
                }

                insertBranches(con, jobId, job.getEligibleBranches());
                job.setJobId(jobId);

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<Job> getJobsByRecruiter(int recruiterId)
            throws SQLException {

        String sql = """
                SELECT * FROM job_postings
                WHERE recruiter_id = ?
                ORDER BY created_at DESC
                """;

        List<Job> jobs = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, recruiterId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = mapJob(rs);
                    job.setEligibleBranches(
                            getBranches(con, job.getJobId()));
                    jobs.add(job);
                }
            }
        }

        return jobs;
    }

    public Job findOwnedJob(int jobId, int recruiterId)
            throws SQLException {

        String sql = """
                SELECT * FROM job_postings
                WHERE job_id = ? AND recruiter_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, recruiterId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Job job = mapJob(rs);
                    job.setEligibleBranches(
                            getBranches(con, jobId));
                    return job;
                }
            }
        }

        return null;
    }

    public boolean updateJob(Job job, int recruiterId)
            throws SQLException {

        String sql = """
                UPDATE job_postings SET
                    company_id = ?,
                    job_title = ?,
                    job_description = ?,
                    employment_type = ?,
                    location = ?,
                    min_cgpa = ?,
                    max_backlogs = ?,
                    graduation_year = ?,
                    min_package = ?,
                    max_package = ?,
                    application_deadline = ?
                WHERE job_id = ? AND recruiter_id = ?
                """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int rows;

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, job.getCompanyId());
                    ps.setString(2, job.getJobTitle());
                    ps.setString(3, job.getJobDescription());
                    ps.setString(4, job.getEmploymentType());
                    ps.setString(5, job.getLocation());
                    ps.setBigDecimal(6, job.getMinCgpa());
                    ps.setInt(7, job.getMaxBacklogs());
                    ps.setInt(8, job.getGraduationYear());
                    ps.setBigDecimal(9, job.getMinPackage());
                    ps.setBigDecimal(10, job.getMaxPackage());
                    ps.setDate(11, Date.valueOf(job.getApplicationDeadline()));
                    ps.setInt(12, job.getJobId());
                    ps.setInt(13, recruiterId);

                    rows = ps.executeUpdate();
                }

                if (rows == 0) {
                    con.rollback();
                    return false;
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM job_eligible_branches WHERE job_id = ?")) {
                    ps.setInt(1, job.getJobId());
                    ps.executeUpdate();
                }

                insertBranches(con, job.getJobId(),
                        job.getEligibleBranches());

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public boolean updateStatus(int jobId, int recruiterId, String status)
            throws SQLException {

        String sql = """
                UPDATE job_postings
                SET status = ?
                WHERE job_id = ? AND recruiter_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, jobId);
            ps.setInt(3, recruiterId);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteJob(int jobId, int recruiterId)
            throws SQLException {

        String checkSql = """
                SELECT COUNT(*) FROM applications
                WHERE job_id = ?
                """;

        String deleteSql = """
                DELETE FROM job_postings
                WHERE job_id = ? AND recruiter_id = ?
                """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int applicationCount;

                try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                    ps.setInt(1, jobId);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        applicationCount = rs.getInt(1);
                    }
                }

                if (applicationCount > 0) {
                    con.rollback();
                    throw new SQLException(
                            "This job cannot be deleted because students have applied.");
                }

                int rows;

                try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
                    ps.setInt(1, jobId);
                    ps.setInt(2, recruiterId);
                    rows = ps.executeUpdate();
                }

                con.commit();
                return rows > 0;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<Job> searchJobs(int recruiterId, String keyword)
            throws SQLException {

        String sql = """
                SELECT * FROM job_postings
                WHERE recruiter_id = ?
                AND (LOWER(job_title) LIKE LOWER(?)
                     OR LOWER(location) LIKE LOWER(?))
                ORDER BY created_at DESC
                """;

        List<Job> jobs = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String search = "%" + keyword + "%";
            ps.setInt(1, recruiterId);
            ps.setString(2, search);
            ps.setString(3, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = mapJob(rs);
                    job.setEligibleBranches(
                            getBranches(con, job.getJobId()));
                    jobs.add(job);
                }
            }
        }

        return jobs;
    }

    private List<String> getBranches(Connection con, int jobId)
            throws SQLException {

        List<String> branches = new ArrayList<>();

        String sql = """
                SELECT branch_name
                FROM job_eligible_branches
                WHERE job_id = ?
                ORDER BY branch_name
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    branches.add(rs.getString("branch_name"));
                }
            }
        }

        return branches;
    }

    private void insertBranches(Connection con, int jobId,
                                List<String> branches)
            throws SQLException {

        String sql = """
                INSERT INTO job_eligible_branches (job_id, branch_name)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (String branch : branches) {
                ps.setInt(1, jobId);
                ps.setString(2, branch);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private Job mapJob(ResultSet rs) throws SQLException {

        Job job = new Job();

        job.setJobId(rs.getInt("job_id"));
        job.setCompanyId(rs.getInt("company_id"));
        job.setRecruiterId(rs.getInt("recruiter_id"));
        job.setJobTitle(rs.getString("job_title"));
        job.setJobDescription(rs.getString("job_description"));
        job.setEmploymentType(rs.getString("employment_type"));
        job.setLocation(rs.getString("location"));
        job.setMinCgpa(rs.getBigDecimal("min_cgpa"));
        job.setMaxBacklogs(rs.getInt("max_backlogs"));
        job.setGraduationYear(rs.getInt("graduation_year"));
        job.setMinPackage(rs.getBigDecimal("min_package"));
        job.setMaxPackage(rs.getBigDecimal("max_package"));

        Date deadline = rs.getDate("application_deadline");
        job.setApplicationDeadline(
                deadline == null ? null : deadline.toLocalDate());

        job.setStatus(rs.getString("status"));

        return job;
    }
}
