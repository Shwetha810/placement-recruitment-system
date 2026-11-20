
package com.placement.service;

import com.placement.dao.CompanyDAO;
import com.placement.model.Company;

import java.sql.SQLException;
import java.util.List;

public class CompanyService {

    private final CompanyDAO companyDAO = new CompanyDAO();

    public String addCompany(Company company) {

        String validation = validateCompany(company);

        if (validation != null) {
            return validation;
        }

        try {

            boolean added = companyDAO.addCompany(company);

            if (added) {
                return "Company added successfully. Company ID: "
                        + company.getCompanyId();
            }

            return "Unable to add company.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public List<Company> getAllCompanies() {

        try {
            return companyDAO.getAllCompanies();

        } catch (SQLException e) {
            System.out.println("Unable to retrieve companies: "
                    + e.getMessage());
            return List.of();
        }
    }

    public Company getCompanyById(int companyId) {

        if (companyId <= 0) {
            return null;
        }

        try {
            return companyDAO.findById(companyId);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    public List<Company> searchCompanies(String name) {

        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }

        try {
            return companyDAO.searchCompanies(name.trim());

        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
            return List.of();
        }
    }

    public String updateCompany(Company company) {

        String validation = validateCompany(company);

        if (validation != null) {
            return validation;
        }

        if (company.getCompanyId() <= 0) {
            return "Invalid company ID.";
        }

        try {

            Company existing = companyDAO.findById(
                    company.getCompanyId()
            );

            if (existing == null) {
                return "Company not found.";
            }

            boolean updated = companyDAO.updateCompany(company);

            return updated
                    ? "Company updated successfully."
                    : "No changes were made.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    public String deleteCompany(int companyId) {

        if (companyId <= 0) {
            return "Invalid company ID.";
        }

        try {

            Company existing = companyDAO.findById(companyId);

            if (existing == null) {
                return "Company not found.";
            }

            boolean deleted = companyDAO.deleteCompany(companyId);

            return deleted
                    ? "Company deleted successfully."
                    : "Unable to delete company.";

        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    private String validateCompany(Company company) {

        if (company == null) {
            return "Company details cannot be empty.";
        }

        if (company.getCompanyName() == null ||
                company.getCompanyName().trim().isEmpty()) {
            return "Company name cannot be empty.";
        }

        if (company.getCompanyName().trim().length() > 150) {
            return "Company name cannot exceed 150 characters.";
        }

        if (company.getLocation() != null &&
                company.getLocation().length() > 100) {
            return "Location cannot exceed 100 characters.";
        }

        if (company.getWebsite() != null &&
                !company.getWebsite().isBlank() &&
                !company.getWebsite().matches(
                        "^https?://.+\\..+")) {
            return "Enter a valid website URL.";
        }

        if (company.getIndustry() != null &&
                company.getIndustry().length() > 100) {
            return "Industry cannot exceed 100 characters.";
        }

        return null;
    }
}
