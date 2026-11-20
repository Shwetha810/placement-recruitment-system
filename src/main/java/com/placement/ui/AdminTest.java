
package com.placement.ui;

import com.placement.model.StudentDetails;
import com.placement.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class AdminTest {

    private static final Scanner sc = new Scanner(System.in);

    private static final StudentService studentService =
            new StudentService();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. View All Registered Students");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    viewAllStudents();
                    break;

                case 2:
                    System.out.println(
                            "Logging out from Admin Dashboard."
                    );
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllStudents() {

        List<StudentDetails> students =
                studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("\nNo registered students found.");
            return;
        }

        System.out.println("\n===== REGISTERED STUDENTS =====");

        for (StudentDetails student : students) {

            System.out.println("--------------------------------");
            System.out.println("Student ID      : "
                    + student.getStudentId());
            System.out.println("User ID         : "
                    + student.getUserId());
            System.out.println("Name            : "
                    + student.getFullName());
            System.out.println("Email           : "
                    + student.getEmail());
            System.out.println("Roll Number     : "
                    + student.getRollNumber());
            System.out.println("Branch          : "
                    + student.getBranch());
            System.out.println("CGPA            : "
                    + student.getCgpa());
            System.out.println("Backlogs        : "
                    + student.getBacklogs());
            System.out.println("Graduation Year : "
                    + student.getGraduationYear());
            System.out.println("Phone           : "
                    + student.getPhone());
        }

        System.out.println("--------------------------------");
        System.out.println("Total Students: " + students.size());
    }
}
