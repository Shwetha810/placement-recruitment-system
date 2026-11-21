
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

    private static final JobService jobService = new JobService();

    public static void main(String[] args) {
        run(new Scanner(System.in));
    }

    public static void run(Scanner sc) {

        while (true) {

            System.out.println("\n===== JOB MANAGEMENT =====");
            System.out.println("1. Post New Job");
            System.out.println("2. View All Jobs");
            System.out.println("3. View Job By ID");
            System.out.println("4. Update Job Details");
            System.out.println("5. Close Job Posting");
            System.out.println("6. Reopen Job Posting");
            System.out.println("7. Delete Job Posting");
            System.out.println("8. Search Jobs By Company");
            System.out.println("9. Search Jobs By Location");
            System.out.println("10. Filter Jobs");
            System.out.println("11. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    postJob(sc);
                    break;

                case "2":
                    viewAllJobs();
                    break;

                case "3":
                    viewJobById(sc);
                    break;

                case "4":
                    updateJob(sc);
                    break;

                case "5":
                    changeJobStatus(sc, true);
                    break;

                case "6":
                    changeJobStatus(sc, false);
                    break;

                case "7":
                    deleteJob(sc);
                    break;

                case "8":
                    searchByCompany(sc);
                    break;

                case "9":
                    searchByLocation(sc);
                    break;

                case "10":
                    filterJobs(sc);
                    break;

                case "11":
                    System.out.println("Returning to Admin Dashboard.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void postJob(Scanner sc) {

        try {
            Job job = readJobDetails(sc);
            job.setStatus("OPEN");

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

    private static Job readJobDetails(Scanner sc) {

        Job job = new Job();

        System.out.print("Enter Company ID: ");
        job.setCompanyId(Integer.parseInt(sc.nextLine()));

        System.out.print("Enter Job Title: ");
        job.setJobTitle(sc.nextLine());

        System.out.print("Enter Job Description: ");
        job.setJobDescription(sc.nextLine());

        System.out.print("Enter Employment Type (FULL_TIME/INTERNSHIP): ");
        job.setEmploymentType(sc.nextLine().trim().toUpperCase());

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

        System.out.print("Enter Eligible Branches (comma-separated): ");

        List<String> branches = Arrays.stream(
                        sc.nextLine().split(","))
                .map(String::trim)
                .filter(branch -> !branch.isEmpty())
                .distinct()
                .toList();

        job.setEligibleBranches(branches);

        return job;
    }

    private static void updateJob(Scanner sc) {

        try {
            System.out.print("Enter Job ID to update: ");
            int jobId = Integer.parseInt(sc.nextLine());

            Job existingJob = jobService.getJobById(jobId);

            if (existingJob == null) {
                System.out.println("Job not found.");
                return;
            }

            System.out.println("\nEnter the updated job details:");

            Job updatedJob = readJobDetails(sc);
            updatedJob.setJobId(jobId);
            updatedJob.setStatus(existingJob.getStatus());

            System.out.println("\n" + jobService.updateJob(updatedJob));

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD format.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void changeJobStatus(Scanner sc, boolean close) {

        try {
            System.out.print("Enter Job ID: ");
            int jobId = Integer.parseInt(sc.nextLine());

            String result = close
                    ? jobService.closeJob(jobId)
                    : jobService.reopenJob(jobId);

            System.out.println("\n" + result);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private static void deleteJob(Scanner sc) {

        try {
            System.out.print("Enter Job ID to delete: ");
            int jobId = Integer.parseInt(sc.nextLine());

            Job job = jobService.getJobById(jobId);

            if (job == null) {
                System.out.println("Job not found.");
                return;
            }

            displayJob(job);

            System.out.print("Are you sure you want to delete this job? (YES/NO): ");
            String confirmation = sc.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("YES")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            System.out.println("\n" + jobService.deleteJob(jobId));

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private static void searchByCompany(Scanner sc) {

        System.out.print("Enter company name to search: ");
        String companyName = sc.nextLine();

        List<Job> jobs = jobService.searchByCompanyName(companyName);
        displayJobs(jobs);
    }

    private static void searchByLocation(Scanner sc) {

        System.out.print("Enter location to search: ");
        String location = sc.nextLine();

        List<Job> jobs = jobService.searchByLocation(location);
        displayJobs(jobs);
    }

    private static void filterJobs(Scanner sc) {

        System.out.println("\nEmployment Type:");
        System.out.println("1. All");
        System.out.println("2. FULL_TIME");
        System.out.println("3. INTERNSHIP");
        System.out.print("Choose employment type: ");

        String typeChoice = sc.nextLine();

        String employmentType;

        switch (typeChoice) {
            case "1":
                employmentType = null;
                break;
            case "2":
                employmentType = "FULL_TIME";
                break;
            case "3":
                employmentType = "INTERNSHIP";
                break;
            default:
                System.out.println("Invalid employment type choice.");
                return;
        }

        System.out.println("\nJob Status:");
        System.out.println("1. All");
        System.out.println("2. OPEN");
        System.out.println("3. CLOSED");
        System.out.print("Choose job status: ");

        String statusChoice = sc.nextLine();

        String status;

        switch (statusChoice) {
            case "1":
                status = null;
                break;
            case "2":
                status = "OPEN";
                break;
            case "3":
                status = "CLOSED";
                break;
            default:
                System.out.println("Invalid status choice.");
                return;
        }

        List<Job> jobs = jobService.filterJobs(employmentType, status);
        displayJobs(jobs);
    }

    private static void viewAllJobs() {
        displayJobs(jobService.getAllJobs());
    }

    private static void viewJobById(Scanner sc) {

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

    private static void displayJobs(List<Job> jobs) {

        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs found.");
            return;
        }

        for (Job job : jobs) {
            displayJob(job);
        }

        System.out.println("\nTotal jobs found: " + jobs.size());
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
