
package com.placement.ui;

import com.placement.model.Job;
import com.placement.service.EligibilityService;

import java.util.List;

public class EligibilityTest {

    private static final EligibilityService service =
            new EligibilityService();

    public static void main(String[] args) {

        java.util.Scanner sc = new java.util.Scanner(System.in);

        System.out.println("\n===== STUDENT JOB ELIGIBILITY =====");
        System.out.print("Enter Student ID: ");

        try {
            int studentId = Integer.parseInt(sc.nextLine());

            if (studentId <= 0) {
                System.out.println("Invalid student ID.");
                return;
            }

            runForStudent(studentId);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid student ID.");
        }
    }

    public static void runForStudent(int studentId) {

        System.out.println("\n===== ELIGIBLE JOB POSTINGS =====");

        List<Job> jobs = service.getEligibleJobs(studentId);

        if (jobs.isEmpty()) {
            System.out.println("No eligible jobs found for this student.");
            return;
        }

        System.out.println("Eligible Jobs Found: " + jobs.size());

        for (Job job : jobs) {

            System.out.println("\n--------------------------------");
            System.out.println("Job ID: " + job.getJobId());
            System.out.println("Company ID: " + job.getCompanyId());
            System.out.println("Job Title: " + job.getJobTitle());
            System.out.println("Employment Type: " + job.getEmploymentType());
            System.out.println("Location: " + job.getLocation());
            System.out.println("Minimum CGPA: " + job.getMinCgpa());
            System.out.println("Maximum Backlogs: " + job.getMaxBacklogs());
            System.out.println("Graduation Year: " + job.getGraduationYear());
            System.out.println("Eligible Branches: " + job.getEligibleBranches());
            System.out.println("Application Deadline: " + job.getApplicationDeadline());
            System.out.println("Status: " + job.getStatus());
            System.out.println("--------------------------------");
        }
    }
}
