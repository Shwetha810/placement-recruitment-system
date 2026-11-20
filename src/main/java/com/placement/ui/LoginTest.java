
package com.placement.ui;

import com.placement.dao.StudentProfileDAO;
import com.placement.model.User;
import com.placement.service.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        StudentProfileDAO profileDAO = new StudentProfileDAO();

        System.out.println("\n===== PLACEMENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");

        String choice = sc.nextLine();

        if (!choice.equals("1")) {
            System.out.println("Exiting...");
            return;
        }

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try {
            User user = userService.login(email, password);

            if (user == null) {
                System.out.println("\nInvalid email or password.");
                return;
            }

            System.out.println("\nLogin successful!");
            System.out.println("Welcome, " + user.getFullName());
            System.out.println("Role: " + user.getRole());

            switch (user.getRole().toUpperCase()) {

                case "STUDENT":

                    int studentId = profileDAO.getStudentIdByUserId(
                            user.getUserId());

                    if (studentId == -1) {
                        System.out.println(
                                "Student profile not found. Please complete your profile first.");
                        return;
                    }

                    studentDashboard(sc, studentId);
                    break;

                case "RECRUITER":
                    recruiterDashboard();
                    break;

                case "ADMIN":
                    adminDashboard();
                    break;

                default:
                    System.out.println("Invalid user role.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void studentDashboard(Scanner sc, int studentId) {

        while (true) {

            System.out.println("\n===== STUDENT DASHBOARD =====");
            System.out.println("1. View Eligible Jobs");
            System.out.println("2. Manage Applications");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    EligibilityTest.runForStudent(studentId);
                    break;

                case "2":
                    ApplicationTest.runForStudent(studentId);
                    break;

                case "3":
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void recruiterDashboard() {

        System.out.println("\n===== RECRUITER DASHBOARD =====");
        RecruiterTest.main(new String[0]);
    }

    private static void adminDashboard() {

        System.out.println("\n===== ADMIN DASHBOARD =====");
        System.out.println("Welcome to the Admin Dashboard.");
        System.out.println("Admin management features will be added next.");
        System.out.println("Logged out.");
    }
}
