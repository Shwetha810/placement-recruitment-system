
package com.placement;

import com.placement.model.User;
import com.placement.model.Student;
import com.placement.service.AuthService;
import com.placement.service.StudentService;

import java.sql.SQLException;
import java.util.Scanner;

public class StudentTest {

    private static final Scanner sc = new Scanner(System.in);

    private static final AuthService authService = new AuthService();
    private static final StudentService studentService = new StudentService();

    public static void main(String[] args) {

        System.out.println("===== STUDENT LOGIN =====");

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        User user;

        try {
            user = authService.login(email, password);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return;
        }

        if (user == null) {
            System.out.println("Invalid email or password.");
            return;
        }

        if (!"STUDENT".equals(user.getRole())) {
            System.out.println("Access denied. Student account required.");
            return;
        }

        System.out.println("\nWelcome, " + user.getFullName());

        while (true) {

            System.out.println("\n===== STUDENT DASHBOARD =====");
            System.out.println("1. Create Student Profile");
            System.out.println("2. View Student Profile");
            System.out.println("3. Update Student Profile");
            System.out.println("4. Logout");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            switch (choice) {

                case 1:
                    createProfile(user);
                    break;

                case 2:
                    viewProfile(user);
                    break;

                case 3:
                    updateProfile(user);
                    break;

                case 4:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static Student readStudentDetails(int userId) {

        System.out.print("Enter roll number / USN: ");
        String rollNumber = sc.nextLine();

        System.out.print("Enter branch: ");
        String branch = sc.nextLine();

        double cgpa;

        try {
            System.out.print("Enter CGPA: ");
            cgpa = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid CGPA.");
            return null;
        }

        int backlogs;

        try {
            System.out.print("Enter number of backlogs: ");
            backlogs = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid backlog count.");
            return null;
        }

        int graduationYear;

        try {
            System.out.print("Enter graduation year: ");
            graduationYear = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid graduation year.");
            return null;
        }

        System.out.print("Enter phone number (optional): ");
        String phone = sc.nextLine().trim();

        if (phone.isEmpty()) {
            phone = null;
        }

        return new Student(
                userId,
                rollNumber,
                branch,
                cgpa,
                backlogs,
                graduationYear,
                phone
        );
    }

    private static void createProfile(User user) {

        System.out.println("\n===== CREATE PROFILE =====");

        Student student = readStudentDetails(user.getUserId());

        if (student == null) {
            return;
        }

        String result = studentService.createProfile(student);

        System.out.println(result);
    }

    private static void viewProfile(User user) {

        System.out.println("\n===== STUDENT PROFILE =====");

        Student student = studentService.getProfile(user.getUserId());

        if (student == null) {
            System.out.println("No student profile found.");
            return;
        }

        displayProfile(student, user);
    }

    private static void updateProfile(User user) {

        System.out.println("\n===== UPDATE PROFILE =====");

        Student existing = studentService.getProfile(user.getUserId());

        if (existing == null) {
            System.out.println("Create your profile first.");
            return;
        }

        System.out.println("Enter updated details:");

        Student updated = readStudentDetails(user.getUserId());

        if (updated == null) {
            return;
        }

        String result = studentService.updateProfile(updated);

        System.out.println(result);
    }

    private static void displayProfile(Student student, User user) {

        System.out.println("--------------------------------");
        System.out.println("Student ID       : " + student.getStudentId());
        System.out.println("Name             : " + user.getFullName());
        System.out.println("Email            : " + user.getEmail());
        System.out.println("Roll Number      : " + student.getRollNumber());
        System.out.println("Branch           : " + student.getBranch());
        System.out.println("CGPA             : " + student.getCgpa());
        System.out.println("Backlogs         : " + student.getBacklogs());
        System.out.println("Graduation Year  : " + student.getGraduationYear());
        System.out.println("Phone            : " + student.getPhone());
        System.out.println("--------------------------------");
    }
}
