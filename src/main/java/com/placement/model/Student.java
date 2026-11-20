
package com.placement.model;

public class Student {

    private int studentId;
    private int userId;
    private String rollNumber;
    private String branch;
    private double cgpa;
    private int backlogs;
    private int graduationYear;
    private String phone;

    public Student() {
    }

    public Student(int userId, String rollNumber,
                   String branch, double cgpa,
                   int backlogs, int graduationYear,
                   String phone) {

        this.userId = userId;
        this.rollNumber = rollNumber;
        this.branch = branch;
        this.cgpa = cgpa;
        this.backlogs = backlogs;
        this.graduationYear = graduationYear;
        this.phone = phone;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getUserId() {
        return userId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getBranch() {
        return branch;
    }

    public double getCgpa() {
        return cgpa;
    }

    public int getBacklogs() {
        return backlogs;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public String getPhone() {
        return phone;
    }
}
