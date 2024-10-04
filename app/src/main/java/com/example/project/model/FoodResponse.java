package com.example.project.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodResponse {
    @SerializedName("foods")
    private List<Food> foods;

    public List<Food> getFoods() {
        return foods;
    }

    public class Food {
        @SerializedName("description")
        private String description;

        @SerializedName("foodNutrients")
        private List<Nutrient> foodNutrients;

        public String getDescription() {
            return description;
        }

        public List<Nutrient> getFoodNutrients() {
            return foodNutrients;
        }
    }

    public class Nutrient {
        @SerializedName("nutrientName")
        private String nutrientName;

        @SerializedName("value")
        private float value;

        @SerializedName("unitName")
        private String unitName;

        public String getNutrientName() {
            return nutrientName;
        }

        public float getValue() {
            return value;
        }

        public String getUnitName() {
            return unitName;
        }
    }
}
