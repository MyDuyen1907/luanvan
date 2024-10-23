package com.example.project.model;

import java.util.Map;

public class FoodProduct {
    private Product product;

    public Product getProduct() {
        return product;
    }

    public class Product {
        private String product_name;
        private String description;
        private String image_url;
        private Map<String, Object> nutriments;

        public String getProductName() {
            return product_name;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return image_url;
        }

        public Map<String, Object> getNutrients() {
            return nutriments;
        }
    }
}
