
package com.placement.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Job {

    private int recruiterId;
    private int jobId;
    private int companyId;
    private String jobTitle;
    private String jobDescription;
    private String employmentType;
    private String location;
    private BigDecimal minCgpa;
    private int maxBacklogs;
    private int graduationYear;
    private BigDecimal minPackage;
    private BigDecimal maxPackage;
    private LocalDate applicationDeadline;
    private String status;
    private List<String> eligibleBranches;

    public Job() {
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(BigDecimal minCgpa) {
        this.minCgpa = minCgpa;
    }

    public int getMaxBacklogs() {
        return maxBacklogs;
    }

    public void setMaxBacklogs(int maxBacklogs) {
        this.maxBacklogs = maxBacklogs;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public BigDecimal getMinPackage() {
        return minPackage;
    }

    public void setMinPackage(BigDecimal minPackage) {
        this.minPackage = minPackage;
    }

    public BigDecimal getMaxPackage() {
        return maxPackage;
    }

    public void setMaxPackage(BigDecimal maxPackage) {
        this.maxPackage = maxPackage;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getEligibleBranches() {
        return eligibleBranches;
    }

    public void setEligibleBranches(List<String> eligibleBranches) {
        this.eligibleBranches = eligibleBranches;
    }

    public int getRecruiterId() {
    return recruiterId;
}

public void setRecruiterId(int recruiterId) {
    this.recruiterId = recruiterId;
}
}
