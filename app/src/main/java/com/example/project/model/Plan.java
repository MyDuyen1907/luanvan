package com.example.project.model;

public class Plan {
    private String description;
    private String start;
    private String end;

    public Plan(String description, String start, String end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "description='" + description + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';

    }
}
