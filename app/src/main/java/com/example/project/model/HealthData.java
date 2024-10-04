package com.example.project.model;

public class HealthData {
    private float systolic;
    private float diastolic;
    private float bloodSugar;
    private float bloodSugarPP;
    private float cholesterol;
    private String id;
    private String userId;

    public HealthData() {
        // Cần thiết cho Firestore
    }

    public HealthData(float systolic, float diastolic, float bloodSugar, float bloodSugarPP, float cholesterol, String id, String userId) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.bloodSugar = bloodSugar;
        this.bloodSugarPP = bloodSugarPP;
        this.cholesterol = cholesterol;
        this.id = id;
        this.userId = userId;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}

