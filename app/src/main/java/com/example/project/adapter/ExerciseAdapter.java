package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.Exercise;


import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private Context context;
    private ArrayList<Exercise> exerciseList;

    public ExerciseAdapter(Context context, ArrayList<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.nameTextView.setText(exercise.getName());
        holder.caloriesTextView.setText(String.valueOf(exercise.getCaloriesPerHour()) + " calories/hour");

        // Tải ảnh từ URL (hoặc để rỗng nếu không có ảnh)
        if (!exercise.getImg().isEmpty()) {
            Glide.with(context).load(exercise.getImg()).into(holder.exerciseImageView);
        } else {
            Glide.with(context).load(exercise.getImg()).into(holder.exerciseImageView); // Hình ảnh mặc định
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked on " + exercise.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, caloriesTextView;
        ImageView exerciseImageView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txvExercise);
            caloriesTextView = itemView.findViewById(R.id.txvCalories);
            exerciseImageView = itemView.findViewById(R.id.imvExercise);
        }
    }
}
