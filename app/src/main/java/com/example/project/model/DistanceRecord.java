package com.example.project.model;

import java.util.Date;

public class DistanceRecord {
    private double distance;
    private double calories;
    private long elapsedTime;
    private String timestamp;
    private Date date;

    public DistanceRecord() {
        // Constructor rỗng cho Firestore
    }

    public DistanceRecord(double distance, double calories, long elapsedTime, String timestamp,Date date) {
        this.distance = distance;
        this.calories = calories;
        this.elapsedTime = elapsedTime;
        this.timestamp = timestamp;
        this.date = date;
    }

    // Getter và Setter
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
