
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Existing registration method used by AuthService
    public boolean registerUser(User user) throws SQLException {

        String query = "INSERT INTO users "
                + "(full_name, email, password_hash, role) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setUserId(rs.getInt(1));
                    }
                }
                return true;
            }
        }

        return false;
    }

    // New registration with student profile
    public boolean registerStudent(User user, String rollNumber,
                                   String branch, BigDecimal cgpa,
                                   int backlogs, int graduationYear,
                                   String phone) throws SQLException {

        String userQuery = "INSERT INTO users "
                + "(full_name, email, password_hash, role) "
                + "VALUES (?, ?, ?, ?)";

        String profileQuery = "INSERT INTO student_profiles "
                + "(user_id, roll_number, branch, cgpa, backlogs, "
                + "graduation_year, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int userId;

                try (PreparedStatement ps = con.prepareStatement(
                        userQuery, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, user.getFullName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPasswordHash());
                    ps.setString(4, "STUDENT");

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "User ID could not be generated."
                            );
                        }

                        userId = rs.getInt(1);
                        user.setUserId(userId);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(profileQuery)) {

                    ps.setInt(1, userId);
                    ps.setString(2, rollNumber);
                    ps.setString(3, branch);
                    ps.setBigDecimal(4, cgpa);
                    ps.setInt(5, backlogs);
                    ps.setInt(6, graduationYear);
                    ps.setString(7, phone);

                    ps.executeUpdate();
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

    // Add profile to an existing user
    public boolean addStudentProfile(int userId, String rollNumber,
                                     String branch, BigDecimal cgpa,
                                     int backlogs, int graduationYear,
                                     String phone) throws SQLException {

        String query = "INSERT INTO student_profiles "
                + "(user_id, roll_number, branch, cgpa, backlogs, "
                + "graduation_year, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, rollNumber);
            ps.setString(3, branch);
            ps.setBigDecimal(4, cgpa);
            ps.setInt(5, backlogs);
            ps.setInt(6, graduationYear);
            ps.setString(7, phone);

            return ps.executeUpdate() > 0;
        }
    }

    // Check whether a student profile exists
    public boolean hasStudentProfile(int userId) throws SQLException {

        String query = "SELECT student_id FROM student_profiles WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Find user by email
    public User findByEmail(String email) throws SQLException {

        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("role")
                    );
                }
            }
        }

        return null;
    }

    // Retrieve all registered recruiters
    public List<User> getAllRecruiters() throws SQLException {

        List<User> recruiters = new ArrayList<>();

        String query = "SELECT user_id, full_name, email, role "
                + "FROM users "
                + "WHERE role = 'RECRUITER' "
                + "ORDER BY user_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                User recruiter = new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        null,
                        rs.getString("role")
                );

                recruiters.add(recruiter);
            }
        }

        return recruiters;
    }
}
