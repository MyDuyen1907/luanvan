package com.example.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.SideEffect;

import java.util.List;

public class SideEffectAdapter extends RecyclerView.Adapter<SideEffectAdapter.SideEffectViewHolder> {

    private List<SideEffect> sideEffectList;

    public SideEffectAdapter(List<SideEffect> sideEffectList) {
        this.sideEffectList = sideEffectList;
    }

    @NonNull
    @Override
    public SideEffectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_side_effect, parent, false);
        return new SideEffectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SideEffectViewHolder holder, int position) {
        SideEffect sideEffect = sideEffectList.get(position);
        holder.tvSideEffect.setText(sideEffect.getDescription());
        holder.tvTimestamp.setText(sideEffect.getFormattedTimestamp());
    }

    @Override
    public int getItemCount() {
        return sideEffectList.size();
    }

    public static class SideEffectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSideEffect, tvTimestamp;

        public SideEffectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSideEffect = itemView.findViewById(R.id.tvSideEffect);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}

