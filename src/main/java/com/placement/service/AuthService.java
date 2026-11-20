
package com.placement.service;

import com.placement.dao.UserDAO;
import com.placement.model.User;
import com.placement.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public String registerStudent(
            String fullName, String email, String password) {

        if (fullName == null || fullName.trim().isEmpty()) {
            return "Name cannot be empty.";
        }

        if (email == null ||
                !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "Invalid email address.";
        }

        if (password == null || password.length() < 8) {
            return "Password must contain at least 8 characters.";
        }

        email = email.trim().toLowerCase();

        try {

            User existingUser = userDAO.findByEmail(email);

            if (existingUser != null) {
                return "Email is already registered.";
            }

            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            User user = new User(
                    fullName.trim(),
                    email,
                    hashedPassword,
                    "STUDENT"
            );

            if (userDAO.registerUser(user)) {
                return "Registration successful. User ID: "
                        + user.getUserId();
            }

            return "Registration failed.";

        } catch (SQLException e) {

            if (e.getSQLState() != null
                    && e.getSQLState().startsWith("23")) {
                return "Email is already registered.";
            }

            return "Database error: " + e.getMessage();
        }
    }

    public User login(String email, String password)
            throws SQLException {

        if (email == null || password == null) {
            return null;
        }

        User user = userDAO.findByEmail(
                email.trim().toLowerCase()
        );

        if (user != null &&
                PasswordUtil.verifyPassword(
                        password, user.getPasswordHash())) {

            return user;
        }

        return null;
    }
}
