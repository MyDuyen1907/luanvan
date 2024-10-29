package com.example.project.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String name;
    private int gender; // 0:Nu 1:Nam
    private int age;
    private int height;
    private int weight;
    private int exerciseFrequency;// 0:Khong 1:Nhe 2:Vua 3:Nang
    private String email;
    private String role;
    private List<String> activityHistory;

    public User() {
        this.id = "";
        this.name = "";
        this.gender = 0;
        this.age = 0;
        this.height = 0;
        this.weight = 0;
        this.exerciseFrequency = 0;
        this.email = "";
    }
    public User(String name, String email,String UserID, String role){
        this.name = name;
        this.email = email;
        this.id = UserID;
        this.role = role;

    }
    public User(String id, String email, String name, int gender, int age, int height,int exerciseFrequency, int weight) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.exerciseFrequency = exerciseFrequency;
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getUserID() {
        return id;
    }
    public void setUserID(String userId) {
        this.id = userId;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(int exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }
    public List<String> getActivityHistory() {
        return activityHistory;
    }
    public void setActivityHistory(List<String> activityHistory) {
        this.activityHistory = activityHistory;
    }
    //BMI được chia cân nặng/ chiều cao*chiều cao
    public float BMICal() {
        return (float) Math.round(this.weight / (this.height * 0.01 * this.height * 0.01) * 100) / 100;
    }
    public int calculateMaxCalories() {
        double maxCalories;
        if (gender == 0) {
            // Lượng calo cho nữ
            maxCalories = (6.25 * height) + (10 * weight) - (5 * age) - 161;
        } else {
            // Lượng calo cho nam
            maxCalories = (6.25 * height) + (10 * weight) - (5 * age) + 5;
        }
        return (int) Math.round(maxCalories); // Chuyển đổi sang kiểu int sau khi làm tròn
    }


    public int TTDECal() {
        float bmr;
        // Tính BMR dựa trên giới tính
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

    public int WaterCal() {
        int baseWater;

        // Xác định lượng nước cơ bản dựa trên giới tính
        if (this.gender == 1) {
            baseWater = 35 * this.weight;  // Nam
        } else {
            baseWater = 31 * this.weight;  // Nữ
        }

        // Tính lượng nước bổ sung dựa trên tần suất tập thể dục
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
                ", exerciseFrequency=" + exerciseFrequency +
                ", email='" + email + '\'' +
                '}';
    }
}
