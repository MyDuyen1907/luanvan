package com.example.project.adapter;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import  com.example.project.activity.DishDetailActivity;
import  com.example.project.model.Dish;
import  com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LikedDishAdapter extends RecyclerView.Adapter<LikedDishAdapter.DishViewHolder>{


    private Context context;
    List<Dish> DishList;
    FirebaseStorage storage;
    FirebaseFirestore db;

    public LikedDishAdapter(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public void setDishList(List<Dish> dishList) {
        DishList = dishList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dish_view, parent,false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = this.DishList.get(position);

        holder.name.setText(dish.getName());
        holder.kcal.setText(Double.toString(dish.getCalo()));
        holder.protein.setText(Double.toString(dish.getProtein()));
        holder.fat.setText(Double.toString(dish.getFat()));
        holder.carbs.setText(Double.toString(dish.getCarb()));
        holder.favButton.setChecked(true);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDishDetail(dish);
            }
        });

        // get image of the dish from firebase storage
        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(dish.getImg());
        try {
            File localFile = File.createTempFile("images", "jpg");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Loi", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Remove from to favorite list
        holder.favButton.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                FirebaseAuth auth =  FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if(isChecked) {
                }
                else{
                    db.collection("favorite").document(user.getUid())
                            .update("listID", FieldValue.arrayRemove(dish.getId()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Removed from favorite");
                                    DishList.remove(holder.getBindingAdapterPosition());
                                    setDishList(DishList);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            }
        }));

    }

    private void showDishDetail(Dish dish) {
        Intent intent = new Intent(context, DishDetailActivity.class);
        intent.putExtra("dish", dish);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(DishList != null)
            return DishList.size();
        return 0;
    }

    public class DishViewHolder extends RecyclerView.ViewHolder{
        private CardView cv;
        private ImageView img;
        private TextView name, kcal, carbs, protein, fat;
        private ToggleButton favButton;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.txvName);
            kcal=itemView.findViewById(R.id.txvKcal);
            carbs=itemView.findViewById(R.id.txvCarbs);
            protein=itemView.findViewById(R.id.txvProtein);
            fat=itemView.findViewById(R.id.txvFat);
            cv=itemView.findViewById(R.id.cv);
            favButton = itemView.findViewById(R.id.favButton);
        }
    }


}
