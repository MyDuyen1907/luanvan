package com.example.project;

import com.example.project.model.FoodResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApiService {
    @GET("foods/search")
    Call<FoodResponse> getFoodData(
            @Query("query") String foodName,
            @Query("api_key") String apiKey
    );
}
