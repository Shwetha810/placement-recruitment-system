
package com.placement.service;

import com.placement.dao.UserDAO;
import com.placement.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecruiterService {

    private final UserDAO userDAO = new UserDAO();

    public List<User> getAllRecruiters() {

        try {
            return userDAO.getAllRecruiters();

        } catch (SQLException e) {
            System.out.println("Unable to retrieve recruiters: "
                    + e.getMessage());

            return new ArrayList<>();
        }
    }
}
