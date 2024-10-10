package com.example.project.model;

public class MealAlarm {
    private String mealType; // Loại bữa ăn (sáng, trưa, tối)
    private String time;     // Thời gian báo thức

    // Constructor mặc định (cần thiết cho Firestore)
    public MealAlarm() {
    }

    public MealAlarm(String mealType, String time) {
        this.mealType = mealType;
        this.time = time;
    }

    // Getter và Setter cho mealType
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    // Getter và Setter cho time
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
