
package com.placement.ui;

import com.placement.model.Job;
import com.placement.service.JobService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class JobTest {

    private static final Scanner sc = new Scanner(System.in);
    private static final JobService jobService = new JobService();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== JOB MANAGEMENT =====");
            System.out.println("1. Post New Job");
            System.out.println("2. View All Jobs");
            System.out.println("3. View Job By ID");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    postJob();
                    break;

                case "2":
                    viewAllJobs();
                    break;

                case "3":
                    viewJobById();
                    break;

                case "4":
                    System.out.println("Exiting Job Management.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void postJob() {

        try {

            Job job = new Job();

            System.out.print("Enter Company ID: ");
            job.setCompanyId(Integer.parseInt(sc.nextLine()));

            System.out.print("Enter Job Title: ");
            job.setJobTitle(sc.nextLine());

            System.out.print("Enter Job Description: ");
            job.setJobDescription(sc.nextLine());

            System.out.print("Enter Employment Type (FULL_TIME/INTERNSHIP): ");
            job.setEmploymentType(sc.nextLine());

            System.out.print("Enter Location: ");
            job.setLocation(sc.nextLine());

            System.out.print("Enter Minimum CGPA: ");
            job.setMinCgpa(new BigDecimal(sc.nextLine()));

            System.out.print("Enter Maximum Backlogs: ");
            job.setMaxBacklogs(Integer.parseInt(sc.nextLine()));

            System.out.print("Enter Graduation Year: ");
            job.setGraduationYear(Integer.parseInt(sc.nextLine()));

            System.out.print("Enter Minimum Package (or press Enter): ");
            String minPackage = sc.nextLine();

            if (!minPackage.isBlank()) {
                job.setMinPackage(new BigDecimal(minPackage));
            }

            System.out.print("Enter Maximum Package (or press Enter): ");
            String maxPackage = sc.nextLine();

            if (!maxPackage.isBlank()) {
                job.setMaxPackage(new BigDecimal(maxPackage));
            }

            System.out.print("Enter Application Deadline (YYYY-MM-DD): ");
            job.setApplicationDeadline(LocalDate.parse(sc.nextLine()));

            job.setStatus("OPEN");

            System.out.print("Enter Eligible Branches (comma-separated): ");
            List<String> branches = Arrays.stream(sc.nextLine().split(","))
                    .map(String::trim)
                    .filter(branch -> !branch.isEmpty())
                    .distinct()
                    .toList();

            job.setEligibleBranches(branches);

            String result = jobService.addJob(job);

            System.out.println("\n" + result);

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD format.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllJobs() {

        List<Job> jobs = jobService.getAllJobs();

        if (jobs.isEmpty()) {
            System.out.println("No jobs found.");
            return;
        }

        for (Job job : jobs) {
            displayJob(job);
        }
    }

    private static void viewJobById() {

        try {

            System.out.print("Enter Job ID: ");
            int jobId = Integer.parseInt(sc.nextLine());

            Job job = jobService.getJobById(jobId);

            if (job == null) {
                System.out.println("Job not found.");
            } else {
                displayJob(job);
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private static void displayJob(Job job) {

        System.out.println("\n--------------------------------");
        System.out.println("Job ID: " + job.getJobId());
        System.out.println("Company ID: " + job.getCompanyId());
        System.out.println("Job Title: " + job.getJobTitle());
        System.out.println("Employment Type: " + job.getEmploymentType());
        System.out.println("Location: " + job.getLocation());
        System.out.println("Minimum CGPA: " + job.getMinCgpa());
        System.out.println("Maximum Backlogs: " + job.getMaxBacklogs());
        System.out.println("Graduation Year: " + job.getGraduationYear());
        System.out.println("Minimum Package: " + job.getMinPackage());
        System.out.println("Maximum Package: " + job.getMaxPackage());
        System.out.println("Application Deadline: " + job.getApplicationDeadline());
        System.out.println("Status: " + job.getStatus());
        System.out.println("Eligible Branches: " + job.getEligibleBranches());
        System.out.println("Description: " + job.getJobDescription());
        System.out.println("--------------------------------");
    }
}
