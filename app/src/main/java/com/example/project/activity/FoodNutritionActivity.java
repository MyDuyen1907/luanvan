package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.ApiClient;
import com.example.project.FoodDataCentralService;
import com.example.project.R;
import com.example.project.model.FoodResponse;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodNutritionActivity extends AppCompatActivity {
    private static final String API_KEY = "wJWNKjpHsk4Q7a0g4uTTIJ8keNMEpivatgbYWC05";
    private AutoCompleteTextView editTextFood;
    private TextView textViewResult;
    private Button btnBackFoodNutrition, buttonSearch;
    private ImageButton add_food;

    // Tạo danh sách ánh xạ giữa món ăn tiếng Việt và tiếng Anh
    private Map<String, String> foodTranslationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        editTextFood = findViewById(R.id.editTextFood);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button btnBackFoodNutrition = findViewById(R.id.btn_back_food_nutrition);
        add_food = findViewById(R.id.add_food);

        // Khởi tạo ánh xạ giữa món ăn tiếng Việt và tiếng Anh
        foodTranslationMap = new HashMap<>();
        foodTranslationMap.put("Táo", "Apple");
        foodTranslationMap.put("Chuối", "Banana");
        foodTranslationMap.put("Cam", "Orange");
        foodTranslationMap.put("Ức gà nướng", "Grilled chicken breast");
        foodTranslationMap.put("Cơm chiên", "Fried rice");
        foodTranslationMap.put("Cá hồi nướng", "Grilled salmon");
        foodTranslationMap.put("Súp bông cải xanh", "Broccoli soup");
        foodTranslationMap.put("Trứng ốp la", "Fried eggs");
        foodTranslationMap.put("Sữa chua", "Yogurt");
        foodTranslationMap.put("Bít tết bò", "Beef steak");
        foodTranslationMap.put("Mỳ ống sốt cà chua", "Pasta with tomato sauce");
        foodTranslationMap.put("Bơ nghiền", "Mashed avocado");
        foodTranslationMap.put("Hạnh nhân rang", "Roasted almonds");
        foodTranslationMap.put("Cà rốt luộc", "Boiled carrots");
        foodTranslationMap.put("Khoai tây nghiền", "Mashed potatoes");
        foodTranslationMap.put("Rau bina xào", "Stir-fried spinach");
        foodTranslationMap.put("Cà chua nấu chín", "Cooked tomatoes");
        foodTranslationMap.put("Dâu tây trộn đường", "Strawberries with sugar");
        foodTranslationMap.put("Việt quất tươi", "Fresh blueberries");
        foodTranslationMap.put("Dưa chuột muối", "Pickled cucumber");
        foodTranslationMap.put("Cháo yến mạch", "Oatmeal porridge");
        foodTranslationMap.put("Sữa chua Hy Lạp với mật ong", "Greek yogurt with honey");
        foodTranslationMap.put("Phô mai Cheddar nướng", "Grilled cheddar cheese");
        foodTranslationMap.put("Đậu phụ xào", "Stir-fried tofu");
        foodTranslationMap.put("Tôm chiên xù", "Fried shrimp");
        foodTranslationMap.put("Quinoa luộc", "Boiled quinoa");
        foodTranslationMap.put("Bơ đậu phộng với bánh mì", "Peanut butter with bread");
        foodTranslationMap.put("Sô cô la đen đun chảy", "Melted dark chocolate");
        foodTranslationMap.put("Salad rau diếp", "Lettuce salad");
        foodTranslationMap.put("Cải bắp muối", "Pickled cabbage");
        foodTranslationMap.put("Khoai lang nướng", "Roasted sweet potatoes");
        foodTranslationMap.put("Bí ngô nướng", "Roasted pumpkin");
        foodTranslationMap.put("Măng tây hấp", "Steamed asparagus");
        foodTranslationMap.put("Nấm xào tỏi", "Mushrooms stir-fried with garlic");
        foodTranslationMap.put("Gà nướng BBQ", "BBQ grilled chicken");
        foodTranslationMap.put("Lẩu nấm", "Mushroom hotpot");
        foodTranslationMap.put("Hamburger phô mai", "Cheeseburger");
        foodTranslationMap.put("Nước dừa tươi", "Fresh coconut water");
        foodTranslationMap.put("Bánh mì kẹp thịt", "Meat sandwich");
        foodTranslationMap.put("Mì xào bò", "Stir-fried noodles with beef");
        foodTranslationMap.put("Pizza phô mai", "Cheese pizza");



        List<String> foodSuggestions = Arrays.asList(
                "Táo", "Chuối", "Cam", "Ức gà nướng", "Cơm chiên", "Cá hồi nướng", "Súp bông cải xanh", "Trứng ốp la", "Sữa", "Bít tết bò",
                "Mỳ ống sốt cà chua", "Bơ nghiền", "Hạnh nhân rang", "Cà rốt luộc", "Khoai tây nghiền", "Rau bina xào", "Cà chua nấu chín", "Dâu tây trộn đường",
                "Việt quất tươi", "Dưa chuột muối", "Cháo yến mạch", "Sữa chua Hy Lạp với mật ong", "Phô mai Cheddar nướng", "Đậu phụ xào", "Tôm chiên xù",
                "Quinoa luộc", "Ức gà tây nướng", "Bơ đậu phộng với bánh mì", "Sô cô la đen đun chảy", "Salad rau diếp", "Cải bắp muối", "Đậu Hà Lan xào",
                "Khoai lang nướng", "Bí ngô nướng", "Bí xanh hấp", "Nấm xào tỏi", "Hạt chia", "Óc chó", "Dâu rừng", "Đậu lăng hầm",
                "Gạo lứt luộc", "Súp lơ xào", "Măng tây hấp", "Ớt chuông nướng", "Lê", "Dưa hấu", "Nho", "Dứa", "Xoài",
                "Kiwi", "Sườn heo nướng", "Cá mòi hộp", "Đậu gà nấu", "Hummus", "Hạt điều rang", "Nước dừa tươi", "Đậu đen hầm",
                "Thịt cừu nướng", "Hamburger phô mai", "Tỏi nướng", "Gừng xào", "Cà tím nướng", "Lựu", "Đào", "Mận", "Phô mai Cottage",
                "Hạt hướng dương rang",
                // Thêm các món ăn từ FoodData Central
                "Chả giò", "Phở", "Bánh mì", "Bún", "Mì Quảng", "Hủ tiếu", "Bánh xèo", "Thịt kho trứng", "Canh chua", "Cá kho tộ",
                "Bò bía", "Gà xào sả ớt", "Bánh tét", "Bánh chưng", "Cá nướng", "Nộm", "Mì xào", "Cá chiên giòn", "Thịt xông khói",
                "Salad trái cây", "Nui xào", "Cà ri gà", "Cháo gà", "Bún riêu", "Gỏi đu đủ", "Chả cá", "Xôi gà", "Gà rán",
                "Mực xào chua ngọt", "Bò kho", "Bánh cuốn", "Cháo lòng", "Nộm bò", "Bánh bao", "Bún chả", "Bánh tráng trộn"
        );



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, foodSuggestions);
        editTextFood.setAdapter(adapter);

        buttonSearch.setOnClickListener(v -> {
            String foodName = editTextFood.getText().toString().trim();
            if (!foodName.isEmpty()) {
                // Kiểm tra xem tên món ăn có cần dịch sang tiếng Anh không
                if (foodTranslationMap.containsKey(foodName)) {
                    foodName = foodTranslationMap.get(foodName);  // Dịch sang tiếng Anh
                }
                fetchNutritionData(foodName);
            }
        });

        btnBackFoodNutrition.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
        add_food.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ScanBarcodeActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void fetchNutritionData(String foodName) {
        try {
            // Mã hóa URL để xử lý ký tự đặc biệt
            String encodedFoodName = URLEncoder.encode(foodName, "UTF-8");
            FoodDataCentralService service = ApiClient.getClient().create(FoodDataCentralService.class);
            Call<FoodResponse> call = service.searchFood(encodedFoodName, API_KEY);

            call.enqueue(new Callback<FoodResponse>() {
                @Override
                public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getFoods() != null
                            && !response.body().getFoods().isEmpty()) {
                        FoodResponse.Food food = response.body().getFoods().get(0); // Lấy món ăn đầu tiên
                        StringBuilder result = new StringBuilder("Món ăn: " + food.getDescription() + "\n\n");
                        for (FoodResponse.Nutrient nutrient : food.getFoodNutrients()) {
                            if (nutrient.getNutrientName().equalsIgnoreCase("Energy")
                                    || nutrient.getNutrientName().equalsIgnoreCase("Protein")
                                    || nutrient.getNutrientName().equalsIgnoreCase("Total lipid (fat)")
                                    || nutrient.getNutrientName().equalsIgnoreCase("Carbohydrate, by difference")
                                    || nutrient.getNutrientName().equalsIgnoreCase("Iron, Fe")
                                    || nutrient.getNutrientName().equalsIgnoreCase("Vitamin A, RAE")
                                    || nutrient.getNutrientName().equalsIgnoreCase("Vitamin C, total ascorbic acid")
                                    || nutrient.getNutrientName().equalsIgnoreCase("Sugars, total")) {
                                result.append(nutrient.getNutrientName()).append(": ")
                                        .append(nutrient.getValue()).append(" ")
                                        .append(nutrient.getUnitName()).append("\n");
                            }
                        }
                        textViewResult.setText(result.toString());
                    } else {
                        textViewResult.setText("Không tìm thấy thông tin dinh dưỡng.");
                    }
                }

                @Override
                public void onFailure(Call<FoodResponse> call, Throwable t) {
                    textViewResult.setText("Lỗi: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            textViewResult.setText("Lỗi: " + e.getMessage());
        }
    }
}
