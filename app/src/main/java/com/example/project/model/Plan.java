package com.example.project.model;

public class Plan {
    private String description;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
    private String userId;
    private String Id;

    // Constructor rỗng cho Firestore
    public Plan() {}

    // Constructor đầy đủ
    public Plan(String description, String startTime, String endTime, String startDate, String endDate, String userId, String Id) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this .userId = userId;
        this.Id = Id;
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
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getId() {
        return Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    @Override
    public String toString() {
        return "Plan{" +
                "description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", userId='" + userId + '\'' +
                ", Id='" + Id + '\'' +
                '}';
    }
}