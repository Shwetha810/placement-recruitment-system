
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    public boolean addJob(Job job) throws SQLException {

        String jobQuery = "INSERT INTO job_postings "
                + "(company_id, job_title, job_description, "
                + "employment_type, location, min_cgpa, "
                + "max_backlogs, graduation_year, min_package, "
                + "max_package, application_deadline, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String branchQuery = "INSERT INTO job_eligible_branches "
                + "(job_id, branch_name) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int jobId;

                try (PreparedStatement ps = con.prepareStatement(
                        jobQuery, Statement.RETURN_GENERATED_KEYS)) {

                    setJobParameters(ps, job);

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("Unable to generate job ID.");
                        }

                        jobId = rs.getInt(1);
                        job.setJobId(jobId);
                    }
                }

                insertBranches(con, jobId, job.getEligibleBranches());

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

    public List<Job> getAllJobs() throws SQLException {

        String query = "SELECT * FROM job_postings "
                + "ORDER BY created_at DESC";

        return executeJobQuery(query);
    }

    public Job findById(int jobId) throws SQLException {

        String query = "SELECT * FROM job_postings WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Job job = mapJob(rs);
                    job.setEligibleBranches(getBranches(jobId));
                    return job;
                }
            }
        }

        return null;
    }

    public boolean updateJob(Job job) throws SQLException {

        String query = "UPDATE job_postings SET "
                + "company_id = ?, job_title = ?, job_description = ?, "
                + "employment_type = ?, location = ?, min_cgpa = ?, "
                + "max_backlogs = ?, graduation_year = ?, "
                + "min_package = ?, max_package = ?, "
                + "application_deadline = ?, status = ? "
                + "WHERE job_id = ?";

        String deleteBranches = "DELETE FROM job_eligible_branches "
                + "WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int updatedRows;

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    setJobParameters(ps, job);
                    ps.setInt(13, job.getJobId());
                    updatedRows = ps.executeUpdate();
                }

                if (updatedRows == 0) {
                    con.rollback();
                    return false;
                }

                try (PreparedStatement ps =
                             con.prepareStatement(deleteBranches)) {
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

    public boolean updateJobStatus(int jobId, String status)
            throws SQLException {

        String query = "UPDATE job_postings SET status = ? "
                + "WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, status);
            ps.setInt(2, jobId);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteJob(int jobId) throws SQLException {

        String checkQuery = "SELECT COUNT(*) FROM applications "
                + "WHERE job_id = ?";

        String deleteQuery = "DELETE FROM job_postings WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int applicationCount;

                try (PreparedStatement ps =
                             con.prepareStatement(checkQuery)) {

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

                int deletedRows;

                try (PreparedStatement ps =
                             con.prepareStatement(deleteQuery)) {

                    ps.setInt(1, jobId);
                    deletedRows = ps.executeUpdate();
                }

                con.commit();
                return deletedRows > 0;

            } catch (SQLException e) {
                con.rollback();
                throw e;

            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<Job> searchByCompanyName(String companyName)
            throws SQLException {

        String query = "SELECT j.* FROM job_postings j "
                + "JOIN companies c ON j.company_id = c.company_id "
                + "WHERE LOWER(c.company_name) LIKE LOWER(?) "
                + "ORDER BY j.created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, "%" + companyName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return mapJobList(rs);
            }
        }
    }

    public List<Job> searchByLocation(String location)
            throws SQLException {

        String query = "SELECT * FROM job_postings "
                + "WHERE LOWER(location) LIKE LOWER(?) "
                + "ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, "%" + location + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return mapJobList(rs);
            }
        }
    }

    public List<Job> filterJobs(String employmentType, String status)
            throws SQLException {

        StringBuilder query = new StringBuilder(
                "SELECT * FROM job_postings WHERE 1 = 1");

        if (employmentType != null) {
            query.append(" AND employment_type = ?");
        }

        if (status != null) {
            query.append(" AND status = ?");
        }

        query.append(" ORDER BY created_at DESC");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(query.toString())) {

            int parameterIndex = 1;

            if (employmentType != null) {
                ps.setString(parameterIndex++, employmentType);
            }

            if (status != null) {
                ps.setString(parameterIndex, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return mapJobList(rs);
            }
        }
    }

    public List<String> getBranches(int jobId) throws SQLException {

        List<String> branches = new ArrayList<>();

        String query = "SELECT branch_name "
                + "FROM job_eligible_branches "
                + "WHERE job_id = ? ORDER BY branch_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    branches.add(rs.getString("branch_name"));
                }
            }
        }

        return branches;
    }

    private List<Job> executeJobQuery(String query) throws SQLException {

        List<Job> jobs = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Job job = mapJob(rs);
                job.setEligibleBranches(getBranches(job.getJobId()));
                jobs.add(job);
            }
        }

        return jobs;
    }

    private List<Job> mapJobList(ResultSet rs) throws SQLException {

        List<Job> jobs = new ArrayList<>();

        while (rs.next()) {
            Job job = mapJob(rs);
            job.setEligibleBranches(getBranches(job.getJobId()));
            jobs.add(job);
        }

        return jobs;
    }

    private void setJobParameters(PreparedStatement ps, Job job)
            throws SQLException {

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
        ps.setString(12, job.getStatus());
    }

    private void insertBranches(Connection con, int jobId,
                                List<String> branches)
            throws SQLException {

        String query = "INSERT INTO job_eligible_branches "
                + "(job_id, branch_name) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {

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
