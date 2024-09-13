package com.example.project.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SideEffect {
    private String description;
    private long timestamp;

    public SideEffect() {}

    public SideEffect(String description, long timestamp) {
        this.description = description;
        this.timestamp = timestamp;
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
}

