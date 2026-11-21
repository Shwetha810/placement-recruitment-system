
package com.placement.ui;

import com.placement.model.AdminApplication;
import com.placement.model.ApplicationStatistics;
import com.placement.service.AdminApplicationService;

import java.util.List;
import java.util.Scanner;

public class AdminApplicationTest {

    private static final AdminApplicationService service =
            new AdminApplicationService();

    public static void run(Scanner sc) {

        while (true) {

            System.out.println("\n===== APPLICATION MANAGEMENT =====");
            System.out.println("1. View All Applications");
            System.out.println("2. View Applications By Job ID");
            System.out.println("3. View Applications By Student ID");
            System.out.println("4. Filter Applications By Status");
            System.out.println("5. View Overall Application Statistics");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    viewAllApplications();
                    break;

                case "2":
                    viewApplicationsByJobId(sc);
                    break;

                case "3":
                    viewApplicationsByStudentId(sc);
                    break;

                case "4":
                    filterApplicationsByStatus(sc);
                    break;

                case "5":
                    viewOverallStatistics();
                    break;

                case "6":
                    System.out.println(
                            "Returning to Admin Dashboard.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllApplications() {

        List<AdminApplication> applications =
                service.getAllApplications();

        displayApplications(applications);
    }

    private static void viewApplicationsByJobId(Scanner sc) {

        try {

            System.out.print("Enter Job ID: ");
            int jobId = Integer.parseInt(sc.nextLine().trim());

            List<AdminApplication> applications =
                    service.getApplicationsByJobId(jobId);

            displayApplications(applications);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private static void viewApplicationsByStudentId(Scanner sc) {

        try {

            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(sc.nextLine().trim());

            List<AdminApplication> applications =
                    service.getApplicationsByStudentId(studentId);

            displayApplications(applications);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Student ID.");
        }
    }

    private static void filterApplicationsByStatus(Scanner sc) {

        System.out.println("\nAvailable Statuses:");
        System.out.println("1. APPLIED");
        System.out.println("2. UNDER_REVIEW");
        System.out.println("3. SHORTLISTED");
        System.out.println("4. REJECTED");
        System.out.println("5. WITHDRAWN");

        System.out.print("Enter status: ");

        String status = sc.nextLine().trim().toUpperCase();

        List<AdminApplication> applications =
                service.getApplicationsByStatus(status);

        if (!List.of(
                "APPLIED",
                "UNDER_REVIEW",
                "SHORTLISTED",
                "REJECTED",
                "WITHDRAWN"
        ).contains(status)) {

            System.out.println("Invalid application status.");
            return;
        }

        displayApplications(applications);
    }

    private static void displayApplications(
            List<AdminApplication> applications) {

        if (applications.isEmpty()) {
            System.out.println("\nNo applications found.");
            return;
        }

        System.out.println("\n===== APPLICATION DETAILS =====");

        for (AdminApplication application : applications) {

            System.out.println("\n--------------------------------");
            System.out.println("Application ID : "
                    + application.getApplicationId());

            System.out.println("Application Status : "
                    + application.getApplicationStatus());

            System.out.println("Applied At : "
                    + application.getAppliedAt());

            System.out.println("\n--- STUDENT DETAILS ---");

            System.out.println("Student ID : "
                    + application.getStudentId());

            System.out.println("Name : "
                    + application.getStudentName());

            System.out.println("Email : "
                    + application.getEmail());

            System.out.println("Roll Number : "
                    + application.getRollNumber());

            System.out.println("Branch : "
                    + application.getBranch());

            System.out.println("CGPA : "
                    + application.getCgpa());

            System.out.println("Backlogs : "
                    + application.getBacklogs());

            System.out.println("Graduation Year : "
                    + application.getGraduationYear());

            System.out.println("Phone : "
                    + application.getPhone());

            System.out.println("\n--- JOB DETAILS ---");

            System.out.println("Job ID : "
                    + application.getJobId());

            System.out.println("Job Title : "
                    + application.getJobTitle());

            System.out.println("Company : "
                    + application.getCompanyName());

            System.out.println("Location : "
                    + application.getLocation());

            System.out.println("--------------------------------");
        }

        System.out.println("Total Applications: "
                + applications.size());
    }

    private static void viewOverallStatistics() {

        ApplicationStatistics statistics =
                service.getOverallStatistics();

        if (statistics == null) {
            System.out.println("Unable to retrieve statistics.");
            return;
        }

        System.out.println("\n===== OVERALL APPLICATION STATISTICS =====");

        System.out.println("Total Applications : "
                + statistics.getTotalApplications());

        System.out.println("Applied : "
                + statistics.getApplied());

        System.out.println("Under Review : "
                + statistics.getUnderReview());

        System.out.println("Shortlisted : "
                + statistics.getShortlisted());

        System.out.println("Rejected : "
                + statistics.getRejected());

        System.out.println("Withdrawn : "
                + statistics.getWithdrawn());

        System.out.println("--------------------------------");
    }
}
