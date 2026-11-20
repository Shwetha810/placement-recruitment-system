
package com.placement.ui;

import com.placement.model.Applicant;
import com.placement.model.ApplicationStatistics;
import com.placement.service.ApplicationService;

import java.util.List;
import java.util.Scanner;

public class RecruiterTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ApplicationService applicationService =
                new ApplicationService();

        boolean running = true;

        while (running) {

            System.out.println("\n==================================");
            System.out.println("   RECRUITER APPLICATION SYSTEM");
            System.out.println("==================================");
            System.out.println("1. View Applicants by Job ID");
            System.out.println("2. Update Application Status");
            System.out.println("3. View Application Statistics");
            System.out.println("4. Exit");
            System.out.println("==================================");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("\nNo more input available. Exiting...");
                break;
            }

            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.print("Enter Job ID: ");

                    if (!sc.hasNextInt()) {
                        running = false;
                        break;
                    }

                    int jobId = sc.nextInt();

                    List<Applicant> applicants =
                            applicationService.getApplicantsByJobId(jobId);

                    if (applicants.isEmpty()) {
                        System.out.println("No applicants found.");
                    } else {

                        System.out.println("\nTotal Applicants: "
                                + applicants.size());

                        for (Applicant applicant : applicants) {

                            System.out.println("\n----------------------------");
                            System.out.println("Application ID: "
                                    + applicant.getApplicationId());
                            System.out.println("Student Name: "
                                    + applicant.getFullName());
                            System.out.println("Email: "
                                    + applicant.getEmail());
                            System.out.println("Roll Number: "
                                    + applicant.getRollNumber());
                            System.out.println("Branch: "
                                    + applicant.getBranch());
                            System.out.println("CGPA: "
                                    + applicant.getCgpa());
                            System.out.println("Backlogs: "
                                    + applicant.getBacklogs());
                            System.out.println("Graduation Year: "
                                    + applicant.getGraduationYear());
                            System.out.println("Phone: "
                                    + applicant.getPhone());
                            System.out.println("Status: "
                                    + applicant.getApplicationStatus());
                            System.out.println("Applied At: "
                                    + applicant.getAppliedAt());
                        }
                    }
                    break;

                case 2:

                    System.out.print("Enter Application ID: ");

                    if (!sc.hasNextInt()) {
                        running = false;
                        break;
                    }

                    int applicationId = sc.nextInt();

                    System.out.println("\nSelect New Status:");
                    System.out.println("1. UNDER_REVIEW");
                    System.out.println("2. SHORTLISTED");
                    System.out.println("3. REJECTED");
                    System.out.print("Enter choice: ");

                    if (!sc.hasNextInt()) {
                        running = false;
                        break;
                    }

                    int statusChoice = sc.nextInt();

                    String status = switch (statusChoice) {
                        case 1 -> "UNDER_REVIEW";
                        case 2 -> "SHORTLISTED";
                        case 3 -> "REJECTED";
                        default -> null;
                    };

                    if (status == null) {
                        System.out.println("Invalid status choice.");
                    } else {
                        System.out.println(
                                applicationService.updateApplicationStatus(
                                        applicationId, status));
                    }
                    break;

                case 3:

                    System.out.print("Enter Job ID: ");

                    if (!sc.hasNextInt()) {
                        running = false;
                        break;
                    }

                    int statisticsJobId = sc.nextInt();

                    ApplicationStatistics statistics =
                            applicationService.getApplicationStatistics(
                                    statisticsJobId);

                    if (statistics == null) {
                        System.out.println("Unable to retrieve statistics.");
                    } else {

                        System.out.println("\n==================================");
                        System.out.println("     APPLICATION STATISTICS");
                        System.out.println("==================================");
                        System.out.println("Job ID: " + statisticsJobId);
                        System.out.println("----------------------------------");
                        System.out.println("Total Applications: "
                                + statistics.getTotalApplications());
                        System.out.println("Applied: "
                                + statistics.getApplied());
                        System.out.println("Under Review: "
                                + statistics.getUnderReview());
                        System.out.println("Shortlisted: "
                                + statistics.getShortlisted());
                        System.out.println("Rejected: "
                                + statistics.getRejected());
                        System.out.println("Withdrawn: "
                                + statistics.getWithdrawn());
                        System.out.println("==================================");
                    }
                    break;

                case 4:
                    System.out.println(
                            "Exiting Recruiter Application System...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}
