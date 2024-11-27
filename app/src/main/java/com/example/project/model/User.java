package com.example.project.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String name;
    private int gender; // 0:Nu 1:Nam
    private int age;
    private int height;
    private int weight;
    private int exerciseFrequency; // 0:Khong 1:Nhe 2:Vua 3:Nang
    private int aim;
    private String email;
    private String role;
    private Date lastUpdated; // Thời gian cập nhật cuối cùng

    public User() {
        this.id = "";
        this.name = "";
        this.gender = 0;
        this.age = 0;
        this.height = 0;
        this.weight = 0;
        this.aim = 0;
        this.exerciseFrequency = 0;
        this.email = "";
        this.lastUpdated = new Date(); // Cập nhật lần đầu
    }

    public User(String name, String email, String userID, String role) {
        this.name = name;
        this.email = email;
        this.id = userID;
        this.role = role;
        this.lastUpdated = new Date();
    }

    public User(String id, String email, String name, int gender, int age, int height, int exerciseFrequency, int weight, int aim) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.aim = aim;
        this.exerciseFrequency = exerciseFrequency;
        this.email = email;
        this.lastUpdated = new Date(); // Cập nhật lần đầu
    }

    // Cập nhật thời gian mỗi khi có thay đổi
    private void updateLastUpdated() {
        this.lastUpdated = new Date();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        updateLastUpdated();
    }

    public String getUserID() {
        return id;
    }

    public void setUserID(String userId) {
        this.id = userId;
        updateLastUpdated();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        updateLastUpdated();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updateLastUpdated();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateLastUpdated();
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
        updateLastUpdated();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        updateLastUpdated();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        updateLastUpdated();
    }
    public int getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
        updateLastUpdated();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        updateLastUpdated();
    }

    public int getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(int exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
        updateLastUpdated();
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    // Tính toán chỉ số BMI
    public float BMICal() {
        return (float) Math.round(this.weight / (this.height * 0.01 * this.height * 0.01) * 100) / 100;
    }

    // Tính lượng calo tối đa
    public int calculateMaxCalories() {
        double maxCalories;
        if (gender == 0) {
            // Lượng calo cho nữ
            maxCalories = (6.25 * height) + (10 * weight) - (5 * age) - 161;
        } else {
            // Lượng calo cho nam
            maxCalories = (6.25 * height) + (10 * weight) - (5 * age) + 5;
        }
        return (int) Math.round(maxCalories);
    }

    // Tính TDEE
    public int TTDECal() {
        float bmr;
        if (this.gender == 0) {
            // Nữ
            bmr = (float) (655 + (9.6 * this.weight) + (1.8 * this.height) - (4.7 * this.age));
        } else {
            // Nam
            bmr = (float) (66 + (13.7 * this.weight) + (5 * this.height) - (6.8 * this.age));
        }

        // Tính TDEE dựa trên mức độ hoạt động
        switch (this.exerciseFrequency) {
            case 0:
                return (int) Math.floor(bmr * 1.2);
            case 1:
                return (int) Math.floor(bmr * 1.375);
            case 2:
                return (int) Math.floor(bmr * 1.725);
            case 3:
                return (int) Math.floor(bmr * 1.9);
            default:
                throw new IllegalArgumentException("Exercise frequency không hợp lệ");
        }
    }

    // Tính lượng nước cần thiết
    public int WaterCal() {
        int baseWater;

        if (this.gender == 1) {
            baseWater = 35 * this.weight;
        } else {
            baseWater = 31 * this.weight;
        }

        int extraWater;
        switch (this.exerciseFrequency) {
            case 0:
                extraWater = (int) (1.2 * 500);
                break;
            case 1:
                extraWater = (int) (0.35 * 500);
                break;
            case 2:
                extraWater = (int) (-0.5 * 500);
                break;
            case 3:
                extraWater = (int) (-1 * 500);
                break;
            default:
                throw new IllegalArgumentException("Exercise frequency không hợp lệ");
        }

        return baseWater + extraWater;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", aim=" + aim +
                ", exerciseFrequency=" + exerciseFrequency +
                ", email='" + email + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
