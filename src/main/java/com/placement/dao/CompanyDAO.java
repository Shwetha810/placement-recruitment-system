
package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {

    public boolean addCompany(Company company)
            throws SQLException {

        String query = "INSERT INTO companies "
                + "(company_name, location, website, industry) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, company.getCompanyName());
            ps.setString(2, company.getLocation());
            ps.setString(3, company.getWebsite());
            ps.setString(4, company.getIndustry());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        company.setCompanyId(rs.getInt(1));
                    }
                }

                return true;
            }
        }

        return false;
    }

    public List<Company> getAllCompanies()
            throws SQLException {

        List<Company> companies = new ArrayList<>();

        String query = "SELECT * FROM companies "
                + "ORDER BY company_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Company company = new Company(
                        rs.getString("company_name"),
                        rs.getString("location"),
                        rs.getString("website"),
                        rs.getString("industry")
                );

                company.setCompanyId(
                        rs.getInt("company_id")
                );

                companies.add(company);
            }
        }

        return companies;
    }

    public Company findById(int companyId)
            throws SQLException {

        String query = "SELECT * FROM companies "
                + "WHERE company_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, companyId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Company company = new Company(
                            rs.getString("company_name"),
                            rs.getString("location"),
                            rs.getString("website"),
                            rs.getString("industry")
                    );

                    company.setCompanyId(
                            rs.getInt("company_id")
                    );

                    return company;
                }
            }
        }

        return null;
    }

    public List<Company> searchCompanies(String name)
            throws SQLException {

        List<Company> companies = new ArrayList<>();

        String query = "SELECT * FROM companies "
                + "WHERE company_name LIKE ? "
                + "ORDER BY company_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Company company = new Company(
                            rs.getString("company_name"),
                            rs.getString("location"),
                            rs.getString("website"),
                            rs.getString("industry")
                    );

                    company.setCompanyId(
                            rs.getInt("company_id")
                    );

                    companies.add(company);
                }
            }
        }

        return companies;
    }

    public boolean updateCompany(Company company)
            throws SQLException {

        String query = "UPDATE companies SET "
                + "company_name = ?, location = ?, "
                + "website = ?, industry = ? "
                + "WHERE company_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, company.getCompanyName());
            ps.setString(2, company.getLocation());
            ps.setString(3, company.getWebsite());
            ps.setString(4, company.getIndustry());
            ps.setInt(5, company.getCompanyId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteCompany(int companyId)
            throws SQLException {

        String query = "DELETE FROM companies "
                + "WHERE company_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, companyId);

            return ps.executeUpdate() > 0;
        }
    }
}
