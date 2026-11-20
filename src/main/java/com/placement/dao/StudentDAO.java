
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class StudentDAO {

    public boolean createProfile(Student student)
            throws SQLException {

        String query = "INSERT INTO student_profiles "
                + "(user_id, roll_number, branch, cgpa, "
                + "backlogs, graduation_year, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, student.getUserId());
            ps.setString(2, student.getRollNumber());
            ps.setString(3, student.getBranch());
            ps.setDouble(4, student.getCgpa());
            ps.setInt(5, student.getBacklogs());
            ps.setInt(6, student.getGraduationYear());
            ps.setString(7, student.getPhone());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        student.setStudentId(rs.getInt(1));
                    }
                }

                return true;
            }
        }

        return false;
    }

    public Student findByUserId(int userId)
            throws SQLException {

        String query = "SELECT * FROM student_profiles "
                + "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Student student = new Student(
                            rs.getInt("user_id"),
                            rs.getString("roll_number"),
                            rs.getString("branch"),
                            rs.getDouble("cgpa"),
                            rs.getInt("backlogs"),
                            rs.getInt("graduation_year"),
                            rs.getString("phone")
                    );

                    student.setStudentId(
                            rs.getInt("student_id")
                    );

                    return student;
                }
            }
        }

        return null;
    }

    public boolean updateProfile(Student student)
            throws SQLException {

        String query = "UPDATE student_profiles SET "
                + "roll_number = ?, branch = ?, cgpa = ?, "
                + "backlogs = ?, graduation_year = ?, phone = ? "
                + "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, student.getRollNumber());
            ps.setString(2, student.getBranch());
            ps.setDouble(3, student.getCgpa());
            ps.setInt(4, student.getBacklogs());
            ps.setInt(5, student.getGraduationYear());
            ps.setString(6, student.getPhone());
            ps.setInt(7, student.getUserId());

            return ps.executeUpdate() > 0;
        }
    }
}
