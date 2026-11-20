
package com.placement.service;

import com.placement.dao.StudentDAO;
import com.placement.model.Student;

import java.sql.SQLException;
import java.time.Year;

public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();

    public String createProfile(Student student) {

        String validation = validateStudent(student);

        if (validation != null) {
            return validation;
        }

        try {

            Student existing = studentDAO.findByUserId(
                    student.getUserId()
            );

            if (existing != null) {
                return "Student profile already exists.";
            }

            boolean created = studentDAO.createProfile(student);

            if (created) {
                return "Student profile created successfully. "
                        + "Student ID: " + student.getStudentId();
            }

            return "Unable to create student profile.";

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                return "Roll number or user ID already exists.";
            }

            return "Database error: " + e.getMessage();
        }
    }

    public Student getProfile(int userId) {

        try {
            return studentDAO.findByUserId(userId);

        } catch (SQLException e) {
            System.out.println("Unable to retrieve profile: "
                    + e.getMessage());
            return null;
        }
    }

    public String updateProfile(Student student) {

        String validation = validateStudent(student);

        if (validation != null) {
            return validation;
        }

        try {

            Student existing = studentDAO.findByUserId(
                    student.getUserId()
            );

            if (existing == null) {
                return "Student profile does not exist.";
            }

            boolean updated = studentDAO.updateProfile(student);

            if (updated) {
                return "Student profile updated successfully.";
            }

            return "No changes were made.";

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                return "Roll number is already registered.";
            }

            return "Database error: " + e.getMessage();
        }
    }

    private String validateStudent(Student student) {

        if (student.getRollNumber() == null ||
                student.getRollNumber().trim().isEmpty()) {
            return "Roll number cannot be empty.";
        }

        if (student.getBranch() == null ||
                student.getBranch().trim().isEmpty()) {
            return "Branch cannot be empty.";
        }

        if (!Double.isFinite(student.getCgpa()) ||
                student.getCgpa() < 0 ||
                student.getCgpa() > 10) {
                return "CGPA must be between 0 and 10.";
        }

        if (student.getBacklogs() < 0) {
            return "Backlogs cannot be negative.";
        }

        int currentYear = Year.now().getValue();

        if (student.getGraduationYear() < 2000 ||
                student.getGraduationYear() > currentYear + 6) {
            return "Invalid graduation year.";
        }

        String phone = student.getPhone();

        if (phone != null && !phone.isBlank() &&
                !phone.matches("[0-9]{10,15}")) {
            return "Phone number must contain 10 to 15 digits.";
        }

        return null;
    }
}
