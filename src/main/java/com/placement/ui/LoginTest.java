
package com.placement.ui;

import com.placement.model.User;
import com.placement.model.Student;
import com.placement.service.UserService;
import com.placement.service.StudentService;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginTest {

    private static final UserService userService = new UserService();
    private static final StudentService studentService = new StudentService();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

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
                    login(sc);
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

    private static void login(Scanner sc) {

        System.out.println("\n===== LOGIN =====");

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

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
                    studentDashboard(sc, user);
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

            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void studentDashboard(Scanner sc, User user) {

        while (true) {

            Student student = studentService.getProfile(user.getUserId());

            System.out.println("\n===== STUDENT DASHBOARD =====");
            System.out.println("1. View Student Profile");
            System.out.println("2. Create Student Profile");
            System.out.println("3. Update Student Profile");
            System.out.println("4. View Eligible Jobs");
            System.out.println("5. Manage Job Applications");
            System.out.println("6. Logout");

            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    viewProfile(student, user);
                    break;

                case "2":
                    createProfile(sc, user, student);
                    break;

                case "3":
                    updateProfile(sc, user, student);
                    break;

                case "4":

                    if (!checkProfile(student)) {
                        break;
                    }

                    EligibilityTest.runForStudent(student.getStudentId());
                    break;

                case "5":

                    if (!checkProfile(student)) {
                        break;
                    }

                    ApplicationTest.runForStudent(
                            student.getStudentId(), sc
                    );
                    break;

                case "6":
                    System.out.println("\nLogged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean checkProfile(Student student) {

        if (student == null) {
            System.out.println(
                    "\nPlease create your student profile first."
            );
            return false;
        }

        return true;
    }

    private static void createProfile(
            Scanner sc, User user, Student existing) {

        System.out.println("\n===== CREATE STUDENT PROFILE =====");

        if (existing != null) {
            System.out.println("Student profile already exists.");
            return;
        }

        Student student = readStudentDetails(sc, user.getUserId());

        if (student == null) {
            return;
        }

        String result = studentService.createProfile(student);

        System.out.println("\n" + result);
    }

    private static void viewProfile(Student student, User user) {

        System.out.println("\n===== STUDENT PROFILE =====");

        if (!checkProfile(student)) {
            return;
        }

        System.out.println("--------------------------------");
        System.out.println("Student ID      : " + student.getStudentId());
        System.out.println("Name            : " + user.getFullName());
        System.out.println("Email           : " + user.getEmail());
        System.out.println("Roll Number     : " + student.getRollNumber());
        System.out.println("Branch          : " + student.getBranch());
        System.out.println("CGPA            : " + student.getCgpa());
        System.out.println("Backlogs        : " + student.getBacklogs());
        System.out.println("Graduation Year : " + student.getGraduationYear());
        System.out.println("Phone           : " + student.getPhone());
        System.out.println("--------------------------------");
    }

    private static void updateProfile(
            Scanner sc, User user, Student existing) {

        System.out.println("\n===== UPDATE STUDENT PROFILE =====");

        if (!checkProfile(existing)) {
            return;
        }

        System.out.println("Enter updated details:");

        Student updated = readStudentDetails(sc, user.getUserId());

        if (updated == null) {
            return;
        }

        String result = studentService.updateProfile(updated);

        System.out.println("\n" + result);
    }

    private static Student readStudentDetails(
            Scanner sc, int userId) {

        System.out.print("Enter roll number / USN: ");
        String rollNumber = sc.nextLine().trim();

        System.out.print("Enter branch: ");
        String branch = sc.nextLine().trim();

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
}
