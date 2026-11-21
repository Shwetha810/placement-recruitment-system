
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

        while (true) {

            System.out.println("\n==================================");
            System.out.println("   PLACEMENT MANAGEMENT SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    login(sc, userService, profileDAO);
                    break;

                case "2":
                    RegisterTest.main(new String[0]);
                    break;

                case "3":
                    System.out.println("\nThank you for using the system.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void login(Scanner sc,
                              UserService userService,
                              StudentProfileDAO profileDAO) {

        System.out.println("\n===== LOGIN =====");

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
                            user.getUserId()
                    );

                    if (studentId == -1) {
                        System.out.println(
                                "Student profile not found. "
                                + "Please complete your profile first."
                        );
                        return;
                    }

                    studentDashboard(sc, studentId);
                    break;

                case "RECRUITER":

                    RecruiterTest.run(sc, user.getUserId());
                    break;

                case "ADMIN":

                    AdminTest.run(sc);
                    break;

                default:

                    System.out.println("Invalid user role.");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " + e.getMessage()
            );
        }
    }

    private static void studentDashboard(Scanner sc, int studentId) {

        while (true) {

            System.out.println("\n===== STUDENT DASHBOARD =====");
            System.out.println("1. View Eligible Jobs");
            System.out.println("2. Manage Applications");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    EligibilityTest.runForStudent(studentId);
                    break;

                case "2":
                    ApplicationTest.runForStudent(studentId);
                    break;

                case "3":
                    System.out.println(
                            "\nLogged out successfully."
                    );
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
