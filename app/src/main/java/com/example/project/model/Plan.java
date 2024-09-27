package com.example.project.model;

public class Plan {
    private String description;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;

    // Constructor rỗng cho Firestore
    public Plan() {}

    // Constructor đầy đủ
    public Plan(String description, String startTime, String endTime, String startDate, String endDate) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter và Setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
