
package com.placement.ui;

import com.placement.model.StudentDetails;
import com.placement.model.User;
import com.placement.service.StudentService;
import com.placement.service.RecruiterService;

import java.util.List;
import java.util.Scanner;

public class AdminTest {

    private static final Scanner sc = new Scanner(System.in);

    private static final StudentService studentService =
            new StudentService();

    private static final RecruiterService recruiterService =
            new RecruiterService();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. View All Registered Students");
            System.out.println("2. View All Recruiters");
            System.out.println("3. Manage Companies");
            System.out.println("4. Manage Job Postings");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {

                case 1:
                    viewAllStudents();
                    break;

                case 2:
                    viewAllRecruiters();
                    break;

                case 3:
                    CompanyTest.run(sc);
                    break;

                case 4:
                    JobTest.run(sc);
                    break;

                case 5:
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

    private static void viewAllRecruiters() {

        List<User> recruiters =
                recruiterService.getAllRecruiters();

        if (recruiters.isEmpty()) {
            System.out.println("\nNo registered recruiters found.");
            return;
        }

        System.out.println("\n===== REGISTERED RECRUITERS =====");

        for (User recruiter : recruiters) {

            System.out.println("--------------------------------");
            System.out.println("User ID : " + recruiter.getUserId());
            System.out.println("Name    : " + recruiter.getFullName());
            System.out.println("Email   : " + recruiter.getEmail());
            System.out.println("Role    : " + recruiter.getRole());
        }

        System.out.println("--------------------------------");
        System.out.println("Total Recruiters: " + recruiters.size());
    }
}
