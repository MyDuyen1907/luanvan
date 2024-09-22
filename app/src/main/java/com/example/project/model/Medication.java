package com.example.project.model;

public class Medication {
    private String medicineName;
    private String dosage;
    private String time;
    private String userId;

    public Medication() {
    }

    public Medication( String medicineName, String dosage, String time, String userId) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.time = time;
        this.userId = userId;
    }


    public String getmedicineName() {
        return medicineName;
    }

    public void setmedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Medication{" +
                ",medicineName='" + medicineName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", time='" + time + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}