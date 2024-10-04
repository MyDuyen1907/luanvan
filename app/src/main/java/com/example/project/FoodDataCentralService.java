package com.example.project;


import com.example.project.model.FoodResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodDataCentralService {
    @GET("foods/search")
    Call<FoodResponse> searchFood(
            @Query("query") String foodName,
            @Query("api_key") String apiKey
    );
}
