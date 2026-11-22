
package com.placement.ui;

import com.placement.model.Application;
import com.placement.service.ApplicationService;

import java.util.List;
import java.util.Scanner;

public class ApplicationTest {

    private static final ApplicationService service =
            new ApplicationService();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== JOB APPLICATION MANAGEMENT =====");
        System.out.print("Enter Student ID: ");

        try {
            int studentId = Integer.parseInt(sc.nextLine());

            if (studentId <= 0) {
                System.out.println("Invalid student ID.");
                return;
            }

            runForStudent(studentId, sc);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid student ID.");
        }
    }

    public static void runForStudent(int studentId) {
        runForStudent(studentId, new Scanner(System.in));
    }

    public static void runForStudent(int studentId, Scanner sc) {

        while (true) {

            System.out.println("\n===== APPLICATION MENU =====");
            System.out.println("1. Apply for a Job");
            System.out.println("2. View My Applications");
            System.out.println("3. Withdraw Application");
            System.out.println("4. Back to Student Dashboard");

            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    applyForJob(studentId, sc);
                    break;

                case "2":
                    viewApplications(studentId);
                    break;

                case "3":
                    withdrawApplication(studentId, sc);
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void applyForJob(int studentId, Scanner sc) {

        try {
            System.out.print("Enter Job ID: ");
            int jobId = Integer.parseInt(sc.nextLine());

            if (jobId <= 0) {
                System.out.println("Job ID must be greater than zero.");
                return;
            }

            String result = service.applyForJob(studentId, jobId);

            System.out.println("\n" + result);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private static void viewApplications(int studentId) {

        List<Application> applications =
                service.getMyApplications(studentId);

        if (applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        System.out.println("\n===== MY APPLICATIONS =====");

        for (Application application : applications) {

            System.out.println("\n--------------------------------");
            System.out.println("Application ID: "
                    + application.getApplicationId());

            System.out.println("Job ID: "
                    + application.getJobId());

            System.out.println("Job Title: "
                    + application.getJobTitle());

            System.out.println("Company: "
                    + application.getCompanyName());

            System.out.println("Application Status: "
                    + application.getApplicationStatus());

            System.out.println("Applied At: "
                    + application.getAppliedAt());

            System.out.println("--------------------------------");
        }
    }

    private static void withdrawApplication(int studentId, Scanner sc) {

        try {
            System.out.print("Enter Application ID: ");
            int applicationId = Integer.parseInt(sc.nextLine());

            if (applicationId <= 0) {
                System.out.println("Application ID must be greater than zero.");
                return;
            }

            String result = service.withdrawApplication(
                    applicationId, studentId);

            System.out.println("\n" + result);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Application ID.");
        }
    }
}
