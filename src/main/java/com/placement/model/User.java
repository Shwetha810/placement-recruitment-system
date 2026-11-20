
package com.placement.model;

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String passwordHash;
    private String role;

    public User() {
    }

    public User(String fullName, String email,
                String passwordHash, String role) {

        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(int userId, String fullName,
                String email, String passwordHash,
                String role) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
