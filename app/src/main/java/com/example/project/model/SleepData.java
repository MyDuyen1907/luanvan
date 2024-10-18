package com.example.project.model;


public class SleepData {
    private String userId; // ID người dùng
    private int sleepHour;
    private int sleepMinute;
    private int wakeHour;
    private int wakeMinute;
    private int hoursSlept;
    private int minutesSlept;
    private int caloriesBurned;
    private String sleepQuality;
    private String date;
    private String id;

    public SleepData() {
        // Constructor trống cần thiết cho Firestore
    }

    public SleepData(String userId, int sleepHour, int sleepMinute, int wakeHour, int wakeMinute,
                     int hoursSlept, int minutesSlept, int caloriesBurned, String sleepQuality, String date, String id) {
        this.userId = userId;
        this.sleepHour = sleepHour;
        this.sleepMinute = sleepMinute;
        this.wakeHour = wakeHour;
        this.wakeMinute = wakeMinute;
        this.hoursSlept = hoursSlept;
        this.minutesSlept = minutesSlept;
        this.caloriesBurned = caloriesBurned;
        this.sleepQuality = sleepQuality;
        this.date = date;
        this.id = id;
    }

    // Getter và Setter cho tất cả các thuộc tính

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getSleepHour() {
        return sleepHour;
    }

    public void setSleepHour(int sleepHour) {
        this.sleepHour = sleepHour;
    }

    public int getSleepMinute() {
        return sleepMinute;
    }

    public void setSleepMinute(int sleepMinute) {
        this.sleepMinute = sleepMinute;
    }

    public int getWakeHour() {
        return wakeHour;
    }

    public void setWakeHour(int wakeHour) {
        this.wakeHour = wakeHour;
    }

    public int getWakeMinute() {
        return wakeMinute;
    }

    public void setWakeMinute(int wakeMinute) {
        this.wakeMinute = wakeMinute;
    }

    public int getHoursSlept() {
        return hoursSlept;
    }

    public void setHoursSlept(int hoursSlept) {
        this.hoursSlept = hoursSlept;
    }

    public int getMinutesSlept() {
        return minutesSlept;
    }

    public void setMinutesSlept(int minutesSlept) {
        this.minutesSlept = minutesSlept;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(String sleepQuality) {
        this.sleepQuality = sleepQuality;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "SleepData{" +
                "userId='" + userId + '\'' +
                ", sleepHour=" + sleepHour +
                ", sleepMinute=" + sleepMinute +
                ", wakeHour=" + wakeHour +
                ", wakeMinute=" + wakeMinute +
                ", hoursSlept=" + hoursSlept +
                ", minutesSlept=" + minutesSlept +
                ", caloriesBurned=" + caloriesBurned +
                ", sleepQuality='" + sleepQuality + '\'' +
                ", date='" + date + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}