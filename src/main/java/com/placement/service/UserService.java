
package com.placement.service;

import com.placement.dao.UserDAO;
import com.placement.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public boolean registerStudent(String fullName, String email,
                                   String password, String rollNumber,
                                   String branch, BigDecimal cgpa,
                                   int backlogs, int graduationYear,
                                   String phone) throws SQLException {

        validateName(fullName);
        validateEmail(email);
        validatePassword(password);
        validateProfile(rollNumber, branch, cgpa, backlogs, graduationYear);

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

        User user = new User(
                fullName.trim(),
                email.trim().toLowerCase(),
                passwordHash,
                "STUDENT"
        );

        return userDAO.registerStudent(
                user, rollNumber.trim(), branch.trim().toUpperCase(),
                cgpa, backlogs, graduationYear, phone
        );
    }

    public boolean completeStudentProfile(String email, String password,
                                           String rollNumber, String branch,
                                           BigDecimal cgpa, int backlogs,
                                           int graduationYear, String phone)
            throws SQLException {

        User user = login(email, password);

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException("Only student accounts can have student profiles.");
        }

        validateProfile(rollNumber, branch, cgpa, backlogs, graduationYear);

        if (userDAO.hasStudentProfile(user.getUserId())) {
            throw new IllegalArgumentException("A student profile already exists for this account.");
        }

        return userDAO.addStudentProfile(
                user.getUserId(), rollNumber.trim(),
                branch.trim().toUpperCase(), cgpa,
                backlogs, graduationYear, phone
        );
    }

    public User login(String email, String password) throws SQLException {

        if (email == null || password == null ||
                email.trim().isEmpty() || password.isEmpty()) {
            return null;
        }

        User user = userDAO.findByEmail(email.trim().toLowerCase());

        if (user == null) {
            return null;
        }

        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }

        return null;
    }

    private void validateName(String fullName) {

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

    private void validateEmail(String email) {

        if (email == null ||
                !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
    }

    private void validatePassword(String password) {

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }
    }

    private void validateProfile(String rollNumber, String branch,
                                 BigDecimal cgpa, int backlogs,
                                 int graduationYear) {

        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty.");
        }

        if (branch == null || branch.trim().isEmpty()) {
            throw new IllegalArgumentException("Branch cannot be empty.");
        }

        if (cgpa == null || cgpa.compareTo(BigDecimal.ZERO) < 0 ||
                cgpa.compareTo(BigDecimal.TEN) > 0) {
            throw new IllegalArgumentException("CGPA must be between 0 and 10.");
        }

        if (backlogs < 0) {
            throw new IllegalArgumentException("Backlogs cannot be negative.");
        }

        if (graduationYear < 2000 || graduationYear > 2100) {
            throw new IllegalArgumentException("Enter a valid graduation year.");
        }
    }
}
