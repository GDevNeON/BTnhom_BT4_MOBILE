package com.example.bt4_mobile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private final Context context;
    private final List<String> images;
    private final OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(String imagePath); // Chỉ cần trả về đường dẫn của ảnh được chọn
    }

    public GridAdapter(Context context, List<String> images, OnImageClickListener listener) {
        this.context = context;
        this.images = images;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = images.get(position);

        Log.d("DEBUG", "Loading image: " + imagePath);
        Glide.with(context)
                .load(imagePath)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> listener.onImageClick(imagePath));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}

