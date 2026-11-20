
package com.placement.ui;

import com.placement.model.Job;
import com.placement.service.EligibilityService;

import java.util.List;
import java.util.Scanner;

public class EligibilityTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== STUDENT JOB ELIGIBILITY =====");
        System.out.print("Enter Student ID: ");

        try {
            int studentId = Integer.parseInt(sc.nextLine());
            runForStudent(studentId);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid student ID.");
        }
    }

    public static void runForStudent(int studentId) {

        EligibilityService service = new EligibilityService();

        System.out.println("\n===== STUDENT JOB ELIGIBILITY =====");

        List<Job> jobs = service.getEligibleJobs(studentId);

        if (jobs.isEmpty()) {
            System.out.println("\nNo eligible jobs found for this student.");
            return;
        }

        System.out.println("\nEligible Jobs Found: " + jobs.size());

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
