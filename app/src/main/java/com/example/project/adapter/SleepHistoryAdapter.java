package com.example.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.SleepData;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SleepHistoryAdapter extends RecyclerView.Adapter<SleepHistoryAdapter.SleepViewHolder> {

    private List<SleepData> sleepDataList;
    private FirebaseFirestore db;

    public SleepHistoryAdapter(List<SleepData> sleepDataList) {
        this.sleepDataList = sleepDataList;
        this.db = FirebaseFirestore.getInstance(); // Khởi tạo Firestore
    }

    @NonNull
    @Override
    public SleepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sleep_history, parent, false);
        return new SleepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SleepViewHolder holder, int position) {
        SleepData sleepData = sleepDataList.get(position);
        holder.bind(sleepData, position, this); // Truyền adapter vào ViewHolder
    }

    @Override
    public int getItemCount() {
        return sleepDataList.size();
    }

    public static class SleepViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSleepTime;
        private TextView tvWakeUpTime;
        private TextView tvTotalSleepTime;
        private TextView tvCaloriesBurned;
        private TextView tvSleepQuality;
        public ImageButton btnDelete; // Thêm tham chiếu cho nút xóa

        public SleepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSleepTime = itemView.findViewById(R.id.tvSleepTime);
            tvWakeUpTime = itemView.findViewById(R.id.tvWakeUpTime);
            tvTotalSleepTime = itemView.findViewById(R.id.tvTotalSleepTime);
            tvCaloriesBurned = itemView.findViewById(R.id.tvCaloriesBurned);
            tvSleepQuality = itemView.findViewById(R.id.tvSleepQuality);
            btnDelete = itemView.findViewById(R.id.btnDelete); // Khởi tạo nút xóa
        }

        public void bind(SleepData sleepData, int position, SleepHistoryAdapter adapter) {
            String sleepTime = String.format("%02d:%02d", sleepData.getSleepHour(), sleepData.getSleepMinute());
            String wakeUpTime = String.format("%02d:%02d", sleepData.getWakeHour(), sleepData.getWakeMinute());
            tvSleepTime.setText(" " + sleepTime);
            tvWakeUpTime.setText(" " + wakeUpTime);
            tvTotalSleepTime.setText(String.format(" %dh %d phút", sleepData.getHoursSlept(), sleepData.getMinutesSlept()));
            tvCaloriesBurned.setText(" " + sleepData.getCaloriesBurned() + " kcal");
            tvSleepQuality.setText(" " + sleepData.getSleepQuality());

            // Xử lý sự kiện click cho nút xóa
            btnDelete.setOnClickListener(v -> deleteSleepData(sleepData.getUserId(), position, adapter)); // Sử dụng userId làm Document ID
        }

        private void deleteSleepData(String sleepDataId, int position, SleepHistoryAdapter adapter) {
            // Xóa dữ liệu khỏi Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("sleep_data").document(sleepDataId) // Sử dụng sleepDataId làm Document ID
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Xóa dữ liệu khỏi danh sách và cập nhật UI
                        adapter.sleepDataList.remove(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(itemView.getContext(), "Dữ liệu đã được xóa!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(itemView.getContext(), "Lỗi khi xóa dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

    }
}
