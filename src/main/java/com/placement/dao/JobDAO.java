
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
                    ps.setDate(11, Date.valueOf(
                            job.getApplicationDeadline()));
                    ps.setString(12, job.getStatus());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Unable to generate job ID.");
                        }

                        jobId = rs.getInt(1);
                        job.setJobId(jobId);
                    }
                }

                try (PreparedStatement ps =
                        con.prepareStatement(branchQuery)) {

                    for (String branch : job.getEligibleBranches()) {

                        ps.setInt(1, jobId);
                        ps.setString(2, branch);
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

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

        List<Job> jobs = new ArrayList<>();

        String query = "SELECT * FROM job_postings "
                + "ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Job job = mapJob(rs);
                job.setEligibleBranches(
                        getBranches(job.getJobId()));

                jobs.add(job);
            }
        }

        return jobs;
    }

    public Job findById(int jobId) throws SQLException {

        String query = "SELECT * FROM job_postings "
                + "WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Job job = mapJob(rs);

                    job.setEligibleBranches(
                            getBranches(jobId));

                    return job;
                }
            }
        }

        return null;
    }

    public List<String> getBranches(int jobId)
            throws SQLException {

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
        job.setApplicationDeadline(
                rs.getDate("application_deadline").toLocalDate());
        job.setStatus(rs.getString("status"));

        return job;
    }
}
