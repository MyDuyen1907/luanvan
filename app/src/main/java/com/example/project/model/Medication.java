package com.example.project.model;

import java.util.Date;

public class Medication {
    private String medicineName;
    private String dosage;
    private String time;
    private String reminder;
    private String userId;
    private String note;
    private String Id;
    private String date;
    private Date lastUpdated; // Thời gian cập nhật cuối cùng

    public Medication() {
        // Đặt lastUpdated thành thời gian hiện tại khi khởi tạo
        this.lastUpdated = new Date();
    }

    public Medication(String medicineName, String dosage, String time, String userId, String reminder, String note, String Id, Date date) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.time = time;
        this.userId = userId;
        this.reminder = reminder;
        this.note = note;
        this.Id = Id;
        this.date = date.toString();
        this.lastUpdated = new Date(); // Đặt thời gian hiện tại khi tạo đối tượng
    }

    // Các getter và setter hiện có
    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
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

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    // Không thay đổi các phương thức cũ, chỉ thêm getter cho lastUpdated

    @Override
    public String toString() {
        return "Medication{" +
                "medicineName='" + medicineName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", time='" + time + '\'' +
                ", userId='" + userId + '\'' +
                ", reminder='" + reminder + '\'' +
                ", note='" + note + '\'' +
                ", Id='" + Id + '\'' +
                ", date=" + date +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
