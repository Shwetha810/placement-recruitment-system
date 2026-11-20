
package com.placement.model;

public class Company {

    private int companyId;
    private String companyName;
    private String location;
    private String website;
    private String industry;

    public Company() {
    }

    public Company(String companyName, String location,
                   String website, String industry) {

        this.companyName = companyName;
        this.location = location;
        this.website = website;
        this.industry = industry;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getIndustry() {
        return industry;
    }
}
