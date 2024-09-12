package com.example.project.model;

public class SleepData {
    private String startSleepTime;
    private String wakeUpTime;
    private String sleepQuality;
    private int awakenings;
    private String sleepCycles;
    private float sleepDurationDeep;
    private float sleepDurationRem;
    private float sleepDurationLight;
    private String userId;

    // Constructor rỗng yêu cầu bởi Firebase
    public SleepData() {
        // Firebase yêu cầu constructor rỗng
    }

    // Constructor với tất cả các trường
    public SleepData(String startSleepTime, String wakeUpTime, String sleepQuality, int awakenings, String sleepCycles, float sleepDurationDeep, float sleepDurationRem, float sleepDurationLight, String userId) {
        this.startSleepTime = startSleepTime;
        this.wakeUpTime = wakeUpTime;
        this.sleepQuality = sleepQuality;
        this.awakenings = awakenings;
        this.sleepCycles = sleepCycles;
        this.sleepDurationDeep = sleepDurationDeep;
        this.sleepDurationRem = sleepDurationRem;
        this.sleepDurationLight = sleepDurationLight;
        this.userId = userId;
    }

    // Getters và Setters
    public String getStartSleepTime() {
        return startSleepTime;
    }

    public void setStartSleepTime(String startSleepTime) {
        this.startSleepTime = startSleepTime;
    }

    public String getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(String wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    public String getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(String sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public int getAwakenings() {
        return awakenings;
    }

    public void setAwakenings(int awakenings) {
        this.awakenings = awakenings;
    }

    public String getSleepCycles() {
        return sleepCycles;
    }

    public void setSleepCycles(String sleepCycles) {
        this.sleepCycles = sleepCycles;
    }

    public float getSleepDurationDeep() {
        return sleepDurationDeep;
    }

    public void setSleepDurationDeep(float sleepDurationDeep) {
        this.sleepDurationDeep = sleepDurationDeep;
    }

    public float getSleepDurationRem() {
        return sleepDurationRem;
    }

    public void setSleepDurationRem(float sleepDurationRem) {
        this.sleepDurationRem = sleepDurationRem;
    }

    public float getSleepDurationLight() {
        return sleepDurationLight;
    }

    public void setSleepDurationLight(float sleepDurationLight) {
        this.sleepDurationLight = sleepDurationLight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SleepData{" +
                "startSleepTime='" + startSleepTime + '\'' +
                ", wakeUpTime='" + wakeUpTime + '\'' +
                ", sleepQuality='" + sleepQuality + '\'' +
                ", awakenings=" + awakenings +
                ", sleepCycles='" + sleepCycles + '\'' +
                ", sleepDurationDeep=" + sleepDurationDeep +
                ", sleepDurationRem=" + sleepDurationRem +
                ", sleepDurationLight=" + sleepDurationLight +
                ", userId='" + userId + '\'' +
                '}';
    }
}
