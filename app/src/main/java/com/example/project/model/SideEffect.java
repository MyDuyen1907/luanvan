package com.example.project.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SideEffect {
    private String description;
    private long timestamp;
    private String userId;

    public SideEffect() {}

    public SideEffect(String description, long timestamp, String userId){
        this.description = description;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return "SideEffect{" +
                "description='" + description + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

