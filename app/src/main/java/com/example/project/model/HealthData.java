package com.example.project.model;

public class HealthData {
    private float systolic;
    private float diastolic;
    private float bloodSugar;
    private float bloodSugarPP;
    private float cholesterol;
    private String userId;
    private String status;
    private String date;

    public HealthData() {
        // Cần thiết cho Firestore
    }

    public HealthData(float systolic, float diastolic, float bloodSugar, float bloodSugarPP, float cholesterol, String userId, String status, String date) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.bloodSugar = bloodSugar;
        this.bloodSugarPP = bloodSugarPP;
        this.cholesterol = cholesterol;
        this.userId = userId;
        this.status = status;
        this.date = date;
    }

    // Getter và Setter
    public float getSystolic() {
        return systolic;
    }

    public void setSystolic(float systolic) {
        this.systolic = systolic;
    }

    public float getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(float diastolic) {
        this.diastolic = diastolic;
    }

    public float getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(float bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public float getBloodSugarPP() {
        return bloodSugarPP;
    }

    public void setBloodSugarPP(float bloodSugarPP) {
        this.bloodSugarPP = bloodSugarPP;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
