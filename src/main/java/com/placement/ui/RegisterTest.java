
package com.placement.ui;

import com.placement.service.UserService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class RegisterTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        System.out.println("\n===== USER REGISTRATION =====");
        System.out.println("1. Register New Student");
        System.out.println("2. Complete Existing Student Profile");
        System.out.println("3. Register New Recruiter");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");

        String choice = sc.nextLine();

        try {
            switch (choice) {

                case "1":
                    registerNewStudent(sc, userService);
                    break;

                case "2":
                    completeExistingProfile(sc, userService);
                    break;

                case "3":
                    registerNewRecruiter(sc, userService);
                    break;

                case "4":
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "\nDuplicate email, roll number, or existing profile."
                );
            } else {
                System.out.println(
                        "\nDatabase error: " + e.getMessage()
                );
            }
        }
    }

    private static void registerNewStudent(Scanner sc,
                                           UserService userService)
            throws SQLException {

        System.out.println("\n--- New Student Details ---");

        System.out.print("Full name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        StudentDetails details = readStudentDetails(sc);

        boolean result = userService.registerStudent(
                name, email, password,
                details.rollNumber, details.branch,
                details.cgpa, details.backlogs,
                details.graduationYear, details.phone
        );

        if (result) {
            System.out.println(
                    "\nStudent registration completed successfully!"
            );
            System.out.println(
                    "User account and academic profile created."
            );
        }
    }

    private static void completeExistingProfile(Scanner sc,
                                                UserService userService)
            throws SQLException {

        System.out.println("\n--- Complete Existing Student Profile ---");

        System.out.print("Registered email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        StudentDetails details = readStudentDetails(sc);

        boolean result = userService.completeStudentProfile(
                email, password,
                details.rollNumber, details.branch,
                details.cgpa, details.backlogs,
                details.graduationYear, details.phone
        );

        if (result) {
            System.out.println(
                    "\nStudent profile created successfully!"
            );
        }
    }

    private static void registerNewRecruiter(Scanner sc,
                                             UserService userService)
            throws SQLException {

        System.out.println("\n--- New Recruiter Details ---");

        System.out.print("Full name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        boolean result = userService.registerRecruiter(
                name, email, password
        );

        if (result) {
            System.out.println(
                    "\nRecruiter registration completed successfully!"
            );
            System.out.println("Recruiter account created.");
            System.out.println("You can now log in as a recruiter.");
        }
    }

    private static StudentDetails readStudentDetails(Scanner sc) {

        StudentDetails details = new StudentDetails();

        System.out.print("Roll number: ");
        details.rollNumber = sc.nextLine();

        System.out.print("Branch (ISE/CSE/ECE etc.): ");
        details.branch = sc.nextLine();

        System.out.print("CGPA: ");
        details.cgpa = new BigDecimal(sc.nextLine());

        System.out.print("Number of backlogs: ");
        details.backlogs = Integer.parseInt(sc.nextLine());

        System.out.print("Graduation year: ");
        details.graduationYear = Integer.parseInt(sc.nextLine());

        System.out.print("Phone number (optional): ");
        details.phone = sc.nextLine();

        if (details.phone.trim().isEmpty()) {
            details.phone = null;
        }

        return details;
    }

    private static class StudentDetails {
        String rollNumber;
        String branch;
        BigDecimal cgpa;
        int backlogs;
        int graduationYear;
        String phone;
    }
}
