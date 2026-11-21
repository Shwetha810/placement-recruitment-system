
package com.placement.ui;

import com.placement.model.Applicant;
import com.placement.model.ApplicationStatistics;
import com.placement.model.Job;
import com.placement.service.RecruiterApplicationService;
import com.placement.service.RecruiterJobService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RecruiterTest {

    private final RecruiterJobService jobService =
            new RecruiterJobService();

    private final RecruiterApplicationService applicationService =
            new RecruiterApplicationService();

    private final int recruiterId;

    private RecruiterTest(int recruiterId) {
        this.recruiterId = recruiterId;
    }

    public static void run(Scanner sc, int recruiterId) {

        if (recruiterId <= 0) {
            System.out.println("Invalid recruiter account.");
            return;
        }

        new RecruiterTest(recruiterId).showMenu(sc);
    }

    private void showMenu(Scanner sc) {

        while (true) {

            System.out.println("\n==================================");
            System.out.println("       RECRUITER DASHBOARD");
            System.out.println("==================================");
            System.out.println("1. View My Job Postings");
            System.out.println("2. Post New Job");
            System.out.println("3. Update My Job Details");
            System.out.println("4. Close My Job");
            System.out.println("5. Reopen My Job");
            System.out.println("6. Delete My Job");
            System.out.println("7. Search My Jobs");
            System.out.println("8. View Applicants");
            System.out.println("9. Update Application Status");
            System.out.println("10. View Application Statistics");
            System.out.println("11. Logout");
            System.out.println("==================================");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> viewMyJobs();
                case "2" -> postJob(sc);
                case "3" -> updateJob(sc);
                case "4" -> changeJobStatus(sc, true);
                case "5" -> changeJobStatus(sc, false);
                case "6" -> deleteJob(sc);
                case "7" -> searchJobs(sc);
                case "8" -> viewApplicants(sc);
                case "9" -> updateApplicationStatus(sc);
                case "10" -> viewStatistics(sc);
                case "11" -> {
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewMyJobs() {
        displayJobs(jobService.getMyJobs(recruiterId));
    }

    private void postJob(Scanner sc) {

        try {
            Job job = readJobDetails(sc);

            System.out.println("\n"
                    + jobService.addJob(job, recruiterId));

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        }
    }

    private void updateJob(Scanner sc) {

        try {
            System.out.print("Enter Job ID to update: ");
            int jobId = Integer.parseInt(sc.nextLine().trim());

            Job existing = jobService.getMyJob(jobId, recruiterId);

            if (existing == null) {
                System.out.println("Job not found under your account.");
                return;
            }

            System.out.println("\nEnter updated job details:");

            Job updated = readJobDetails(sc);
            updated.setJobId(jobId);
            updated.setStatus(existing.getStatus());

            System.out.println("\n"
                    + jobService.updateJob(updated, recruiterId));

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        }
    }

    private void changeJobStatus(Scanner sc, boolean close) {

        try {
            System.out.print("Enter Job ID: ");
            int jobId = Integer.parseInt(sc.nextLine().trim());

            String result = close
                    ? jobService.closeJob(jobId, recruiterId)
                    : jobService.reopenJob(jobId, recruiterId);

            System.out.println("\n" + result);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private void deleteJob(Scanner sc) {

        try {
            System.out.print("Enter Job ID to delete: ");
            int jobId = Integer.parseInt(sc.nextLine().trim());

            Job job = jobService.getMyJob(jobId, recruiterId);

            if (job == null) {
                System.out.println("Job not found under your account.");
                return;
            }

            displayJob(job);

            System.out.print("Confirm deletion? (YES/NO): ");

            if (!sc.nextLine().trim().equalsIgnoreCase("YES")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            System.out.println("\n"
                    + jobService.deleteJob(jobId, recruiterId));

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private void searchJobs(Scanner sc) {

        System.out.print("Enter job title or location: ");
        String keyword = sc.nextLine();

        displayJobs(jobService.searchJobs(recruiterId, keyword));
    }

    private void viewApplicants(Scanner sc) {

        try {
            System.out.print("Enter Job ID: ");
            int jobId = Integer.parseInt(sc.nextLine().trim());

            Job job = jobService.getMyJob(jobId, recruiterId);

            if (job == null) {
                System.out.println("Job not found under your account.");
                return;
            }

            List<Applicant> applicants =
                    applicationService.getApplicants(jobId, recruiterId);

            if (applicants.isEmpty()) {
                System.out.println("No applicants found for this job.");
                return;
            }

            System.out.println("\nTotal Applicants: " + applicants.size());

            for (Applicant applicant : applicants) {
                System.out.println("\n----------------------------");
                System.out.println("Application ID: "
                        + applicant.getApplicationId());
                System.out.println("Student Name: "
                        + applicant.getFullName());
                System.out.println("Email: " + applicant.getEmail());
                System.out.println("Roll Number: "
                        + applicant.getRollNumber());
                System.out.println("Branch: " + applicant.getBranch());
                System.out.println("CGPA: " + applicant.getCgpa());
                System.out.println("Backlogs: " + applicant.getBacklogs());
                System.out.println("Graduation Year: "
                        + applicant.getGraduationYear());
                System.out.println("Phone: " + applicant.getPhone());
                System.out.println("Status: "
                        + applicant.getApplicationStatus());
                System.out.println("Applied At: "
                        + applicant.getAppliedAt());
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private void updateApplicationStatus(Scanner sc) {

        try {
            System.out.print("Enter Application ID: ");
            int applicationId = Integer.parseInt(sc.nextLine().trim());

            System.out.println("\nSelect New Status:");
            System.out.println("1. UNDER_REVIEW");
            System.out.println("2. SHORTLISTED");
            System.out.println("3. REJECTED");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            String status = switch (choice) {
                case "1" -> "UNDER_REVIEW";
                case "2" -> "SHORTLISTED";
                case "3" -> "REJECTED";
                default -> null;
            };

            if (status == null) {
                System.out.println("Invalid status choice.");
                return;
            }

            System.out.println("\n"
                    + applicationService.updateStatus(
                            applicationId, recruiterId, status));

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Application ID.");
        }
    }

    private void viewStatistics(Scanner sc) {

        try {
            System.out.print("Enter Job ID: ");
            int jobId = Integer.parseInt(sc.nextLine().trim());

            if (jobService.getMyJob(jobId, recruiterId) == null) {
                System.out.println("Job not found under your account.");
                return;
            }

            ApplicationStatistics statistics =
                    applicationService.getStatistics(jobId, recruiterId);

            if (statistics == null) {
                System.out.println("Unable to retrieve statistics.");
                return;
            }

            System.out.println("\n===== APPLICATION STATISTICS =====");
            System.out.println("Job ID: " + jobId);
            System.out.println("Total Applications: "
                    + statistics.getTotalApplications());
            System.out.println("Applied: " + statistics.getApplied());
            System.out.println("Under Review: "
                    + statistics.getUnderReview());
            System.out.println("Shortlisted: "
                    + statistics.getShortlisted());
            System.out.println("Rejected: " + statistics.getRejected());
            System.out.println("Withdrawn: " + statistics.getWithdrawn());

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid Job ID.");
        }
    }

    private Job readJobDetails(Scanner sc) {

        Job job = new Job();

        System.out.print("Enter Company ID: ");
        job.setCompanyId(Integer.parseInt(sc.nextLine().trim()));

        System.out.print("Enter Job Title: ");
        job.setJobTitle(sc.nextLine());

        System.out.print("Enter Job Description: ");
        job.setJobDescription(sc.nextLine());

        System.out.print("Enter Employment Type (FULL_TIME/INTERNSHIP): ");
        job.setEmploymentType(sc.nextLine().trim().toUpperCase());

        System.out.print("Enter Location: ");
        job.setLocation(sc.nextLine());

        System.out.print("Enter Minimum CGPA: ");
        job.setMinCgpa(new BigDecimal(sc.nextLine().trim()));

        System.out.print("Enter Maximum Backlogs: ");
        job.setMaxBacklogs(Integer.parseInt(sc.nextLine().trim()));

        System.out.print("Enter Graduation Year: ");
        job.setGraduationYear(Integer.parseInt(sc.nextLine().trim()));

        System.out.print("Enter Minimum Package (or press Enter): ");
        String minPackage = sc.nextLine().trim();

        if (!minPackage.isEmpty()) {
            job.setMinPackage(new BigDecimal(minPackage));
        }

        System.out.print("Enter Maximum Package (or press Enter): ");
        String maxPackage = sc.nextLine().trim();

        if (!maxPackage.isEmpty()) {
            job.setMaxPackage(new BigDecimal(maxPackage));
        }

        System.out.print("Enter Application Deadline (YYYY-MM-DD): ");
        job.setApplicationDeadline(
                LocalDate.parse(sc.nextLine().trim()));

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

    private void displayJobs(List<Job> jobs) {

        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs found.");
            return;
        }

        for (Job job : jobs) {
            displayJob(job);
        }

        System.out.println("\nTotal jobs found: " + jobs.size());
    }

    private void displayJob(Job job) {

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
        System.out.println("Application Deadline: "
                + job.getApplicationDeadline());
        System.out.println("Status: " + job.getStatus());
        System.out.println("Eligible Branches: "
                + job.getEligibleBranches());
        System.out.println("Description: " + job.getJobDescription());
        System.out.println("--------------------------------");
    }
}
