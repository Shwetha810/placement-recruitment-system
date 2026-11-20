
package com.placement.service;

import com.placement.dao.EligibilityDAO;
import com.placement.model.Job;

import java.sql.SQLException;
import java.util.List;

public class EligibilityService {

    private final EligibilityDAO eligibilityDAO = new EligibilityDAO();

    public List<Job> getEligibleJobs(int studentId) {

        if (studentId <= 0) {
            System.out.println("Invalid student ID.");
            return List.of();
        }

        try {
            return eligibilityDAO.getEligibleJobs(studentId);

        } catch (SQLException e) {
            System.out.println("Unable to check eligibility: "
                    + e.getMessage());
            return List.of();
        }
    }
}
