package com.example.project.model;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private String userID;
    private String userName;
    private String email;
    private String password;
    private String role;
    private Date createdDate; // Thời gian tạo tài khoản

    public Account() {
        this.createdDate = new Date(); // Cập nhật thời gian tạo khi khởi tạo đối tượng
        System.out.println("Account created on: " + createdDate);
    }

    public Account(String userID, String userName, String email, String password, String role) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdDate = new Date(); // Cập nhật thời gian tạo khi khởi tạo đối tượng
        System.out.println("Account created on: " + createdDate);
    }

    // Getter và Setter
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
