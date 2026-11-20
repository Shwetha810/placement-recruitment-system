
package com.placement.ui;

import com.placement.model.Company;
import com.placement.service.CompanyService;

import java.util.List;
import java.util.Scanner;

public class CompanyTest {

    private static final CompanyService companyService =
            new CompanyService();

    public static void main(String[] args) {
        run(new Scanner(System.in));
    }

    public static void run(Scanner sc) {

        while (true) {

            System.out.println("\n===== COMPANY MANAGEMENT =====");
            System.out.println("1. Add Company");
            System.out.println("2. View All Companies");
            System.out.println("3. Search Company");
            System.out.println("4. View Company by ID");
            System.out.println("5. Update Company");
            System.out.println("6. Delete Company");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {

                case 1:
                    addCompany(sc);
                    break;

                case 2:
                    viewAllCompanies();
                    break;

                case 3:
                    searchCompany(sc);
                    break;

                case 4:
                    viewCompanyById(sc);
                    break;

                case 5:
                    updateCompany(sc);
                    break;

                case 6:
                    deleteCompany(sc);
                    break;

                case 7:
                    System.out.println("Returning to Admin Dashboard.");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addCompany(Scanner sc) {

        System.out.println("\n===== ADD COMPANY =====");

        System.out.print("Enter company name: ");
        String name = sc.nextLine();

        System.out.print("Enter location: ");
        String location = sc.nextLine();

        System.out.print("Enter website: ");
        String website = sc.nextLine();

        System.out.print("Enter industry: ");
        String industry = sc.nextLine();

        Company company = new Company(
                name,
                emptyToNull(location),
                emptyToNull(website),
                emptyToNull(industry)
        );

        System.out.println(companyService.addCompany(company));
    }

    private static void viewAllCompanies() {

        System.out.println("\n===== ALL COMPANIES =====");

        List<Company> companies =
                companyService.getAllCompanies();

        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        for (Company company : companies) {
            displayCompany(company);
        }
    }

    private static void searchCompany(Scanner sc) {

        System.out.println("\n===== SEARCH COMPANY =====");

        System.out.print("Enter company name to search: ");
        String name = sc.nextLine();

        List<Company> companies =
                companyService.searchCompanies(name);

        if (companies.isEmpty()) {
            System.out.println("No matching companies found.");
            return;
        }

        for (Company company : companies) {
            displayCompany(company);
        }
    }

    private static void viewCompanyById(Scanner sc) {

        System.out.println("\n===== VIEW COMPANY =====");

        System.out.print("Enter company ID: ");

        int id = readCompanyId(sc);

        if (id == -1) {
            return;
        }

        Company company =
                companyService.getCompanyById(id);

        if (company == null) {
            System.out.println("Company not found.");
        } else {
            displayCompany(company);
        }
    }

    private static void updateCompany(Scanner sc) {

        System.out.println("\n===== UPDATE COMPANY =====");

        System.out.print("Enter company ID: ");

        int id = readCompanyId(sc);

        if (id == -1) {
            return;
        }

        Company existing =
                companyService.getCompanyById(id);

        if (existing == null) {
            System.out.println("Company not found.");
            return;
        }

        System.out.println("Enter updated details:");

        System.out.print("Company name: ");
        String name = sc.nextLine();

        System.out.print("Location: ");
        String location = sc.nextLine();

        System.out.print("Website: ");
        String website = sc.nextLine();

        System.out.print("Industry: ");
        String industry = sc.nextLine();

        Company updatedCompany = new Company(
                name,
                emptyToNull(location),
                emptyToNull(website),
                emptyToNull(industry)
        );

        updatedCompany.setCompanyId(id);

        System.out.println(
                companyService.updateCompany(updatedCompany)
        );
    }

    private static void deleteCompany(Scanner sc) {

        System.out.println("\n===== DELETE COMPANY =====");

        System.out.print("Enter company ID: ");

        int id = readCompanyId(sc);

        if (id == -1) {
            return;
        }

        System.out.print("Are you sure? (yes/no): ");
        String confirmation = sc.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    companyService.deleteCompany(id)
            );

        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static int readCompanyId(Scanner sc) {

        try {
            int id = Integer.parseInt(sc.nextLine().trim());

            if (id <= 0) {
                System.out.println("Enter a valid company ID.");
                return -1;
            }

            return id;

        } catch (NumberFormatException e) {
            System.out.println("Invalid company ID.");
            return -1;
        }
    }

    private static String emptyToNull(String value) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }

    private static void displayCompany(Company company) {

        System.out.println("\n--------------------------------");
        System.out.println("Company ID   : " + company.getCompanyId());
        System.out.println("Company Name : " + company.getCompanyName());
        System.out.println("Location     : " + company.getLocation());
        System.out.println("Website      : " + company.getWebsite());
        System.out.println("Industry     : " + company.getIndustry());
        System.out.println("--------------------------------");
    }
}
