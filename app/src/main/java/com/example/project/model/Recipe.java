package com.example.project.model;

public class Recipe {
    private String name;
    private String description;
    private String imageUrl;

    public Recipe() {
        // Required empty constructor for Firestore
    }

    public Recipe(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}
