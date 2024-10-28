package com.example.project.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String id;
    private String userID;
    private String userName;
    private String email;
    private String password;
    private String role;
    private String recentActivity;

    public Account() {
    }

    public Account(String id, String userID, String userName, String email, String password, String role, String recentActivity) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.recentActivity = recentActivity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
    public String getRecentActivity() {
        return recentActivity;
    }
    public void setRecentActivity(String recentActivity) {
        this.recentActivity = recentActivity;
    }
}
