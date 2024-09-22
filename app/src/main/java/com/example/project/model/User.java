package com.example.project.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private int gender; // 0:Nu 1:Nam
    private int age;
    private int height;
    private int weight;
    private int heart_rate;
    private int blood_pressure;
    private int blood_sugar;
    private int cholesterol;
    private String medical_history;
    private String chronic_disease;
    private String vaccination;
    private String medical_interventions;
    private int exerciseFrequency;// 0:Khong 1:Nhe 2:Vua 3:Nang
    private String email;

    public User() {
        this.id = "";
        this.name = "";
        this.gender = 0;
        this.age = 0;
        this.height = 0;
        this.weight = 0;
        this.heart_rate = 0;
        this.blood_pressure = 0;
        this.blood_sugar = 0;
        this.cholesterol = 0;
        this.medical_history = "";
        this.chronic_disease = "";
        this.vaccination = "";
        this.medical_interventions = "";
        this.exerciseFrequency = 0;
        this.email = "";
    }
    public User(String name, String email,String UserID){
        this.name = name;
        this.email = email;
        this.id = UserID;
    }
    public User(String id, String email, String name, int gender, int age, int height,int exerciseFrequency, int weight, int heart_rate, int blood_pressure, int blood_sugar, int cholesterol,String medical_history, String chronic_disease, String vaccination, String medical_interventions) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.heart_rate = heart_rate;
        this.blood_pressure = blood_pressure;
        this.blood_sugar = blood_sugar;
        this.cholesterol = cholesterol;
        this.medical_history = medical_history;
        this.chronic_disease = chronic_disease;
        this.vaccination = vaccination;
        this.medical_interventions = medical_interventions;
        this.exerciseFrequency = exerciseFrequency;
        this.email = email;
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

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate ) {this.heart_rate = heart_rate;}

    public int getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(int blood_pressure) {this.blood_pressure = blood_pressure;}

    public int getBlood_sugar() {
        return blood_sugar;
    }

    public void setBlood_sugar(int blood_sugar) {this.blood_sugar = blood_sugar;}

    public int getCholesterol() {
        return cholesterol;
    }
    public void setCholesterol(int cholesterol) {this.cholesterol = cholesterol;}

    public String getMedical_history() {return medical_history;}
    public void setMedical_history(String medical_history) {this.medical_history = medical_history;}

    public String getChronic_disease() {return chronic_disease;}
    public void setChronic_disease(String chronic_disease) {this.chronic_disease = chronic_disease;}

    public String getVaccination() {return vaccination;}
    public void setVaccination(String vaccination) {this.vaccination = vaccination;}

    public String getMedical_interventions() {return medical_interventions;}
    public void setMedical_interventions(String Medical_interventions) {this.medical_interventions = Medical_interventions;}

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
    public float BMICal() {
        return (float) Math.floor(this.weight/(this.height*0.01*this.height*0.01)* 100)/100;
    }
    public int TTDECal() {
        float bmr;
        if(this.gender==0) {
            bmr = (float) (655+(9.6*this.weight)+(1.8*this.height)-(4.7*this.age));
        } else {
            bmr = (float) (66 + (13.7*this.weight)+(5 *this.height)-(6.8*this.age));
        }
        if(exerciseFrequency == 0)
            return (int) Math.floor(bmr*1.2);
        else if(exerciseFrequency == 1)
            return (int) Math.floor(bmr*1.375);
        else if(exerciseFrequency == 2)
            return (int) Math.floor(bmr*1.725);
        else if(exerciseFrequency == 3)
            return (int) Math.floor(bmr*1.9);
        return 0;
    }
    public int WaterCal(){
        int water = 0;
        if(this.gender == 1){
            if(exerciseFrequency == 0)
                water += 35*this.weight + (exerciseFrequency + 1.2)*500;
            else if(exerciseFrequency == 1)
                water += 35*this.weight + (exerciseFrequency + 0.35)*500;
            else if(exerciseFrequency == 2)
                water += 35*this.weight + (exerciseFrequency - 0.5)*500;
            else if(exerciseFrequency == 3)
                water += 35*this.weight + (exerciseFrequency - 1)*500;

        }
        else {
            if(exerciseFrequency ==0)
                water += 31*this.weight + (exerciseFrequency + 1.2)*500;
            else if(exerciseFrequency == 1)
                water += 31*this.weight + (exerciseFrequency + 0.35)*500;
            else if(exerciseFrequency == 2)
                water += 31*this.weight + (exerciseFrequency - 0.5)*500;
            else if(exerciseFrequency == 3)
                water += 31*this.weight + (exerciseFrequency - 1)*500;
        }
        return water;
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
                ", heart_rate=" + heart_rate +
                ", blood_pressure=" + blood_pressure +
                ", blood_sugar=" + blood_sugar +
                ", cholesterol=" + cholesterol +
                ", medical_history='" + medical_history + '\'' +
                ", chronic_disease='" + chronic_disease + '\'' +
                ", vaccination='" + vaccination + '\'' +
                ", Medical_interventions='" + medical_interventions + '\'' +
                ", exerciseFrequency=" + exerciseFrequency +
                ", email='" + email + '\'' +
                '}';
    }
}
