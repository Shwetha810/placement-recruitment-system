
package com.placement.dao;

import com.placement.model.Job;
import com.placement.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EligibilityDAO {

    public List<Job> getEligibleJobs(int studentId)
            throws SQLException {

        List<Job> jobs = new ArrayList<>();

        String sql = """
                SELECT jp.*,
                       GROUP_CONCAT(
                           jeb.branch_name
                           ORDER BY jeb.branch_name
                           SEPARATOR ','
                       ) AS eligible_branches
                FROM student_profiles sp
                JOIN job_postings jp
                    ON sp.cgpa >= jp.min_cgpa
                    AND sp.backlogs <= jp.max_backlogs
                    AND sp.graduation_year = jp.graduation_year
                JOIN job_eligible_branches jeb
                    ON jp.job_id = jeb.job_id
                    AND UPPER(sp.branch) = UPPER(jeb.branch_name)
                WHERE sp.student_id = ?
                    AND jp.status = 'OPEN'
                    AND jp.application_deadline >= CURDATE()
                GROUP BY jp.job_id
                ORDER BY jp.created_at DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Job job = new Job();

                    job.setJobId(rs.getInt("job_id"));
                    job.setCompanyId(rs.getInt("company_id"));
                    job.setJobTitle(rs.getString("job_title"));
                    job.setJobDescription(
                            rs.getString("job_description"));
                    job.setEmploymentType(
                            rs.getString("employment_type"));
                    job.setLocation(rs.getString("location"));
                    job.setMinCgpa(rs.getBigDecimal("min_cgpa"));
                    job.setMaxBacklogs(rs.getInt("max_backlogs"));
                    job.setGraduationYear(
                            rs.getInt("graduation_year"));
                    job.setMinPackage(
                            rs.getBigDecimal("min_package"));
                    job.setMaxPackage(
                            rs.getBigDecimal("max_package"));
                    job.setApplicationDeadline(
                            rs.getDate("application_deadline")
                                    .toLocalDate());
                    job.setStatus(rs.getString("status"));

                    String branches = rs.getString("eligible_branches");

                    job.setEligibleBranches(
                            List.of(branches.split(",")));

                    jobs.add(job);
                }
            }
        }

        return jobs;
    }
}
