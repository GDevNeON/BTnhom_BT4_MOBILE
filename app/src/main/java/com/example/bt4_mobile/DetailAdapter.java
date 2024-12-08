package com.example.bt4_mobile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {
    private Context context;
    private List<String> images;

    public DetailAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoView photoView = new PhotoView(context);
        photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new ViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imagePath = images.get(position);
        Glide.with(context).load(imagePath).into(holder.photoView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PhotoView photoView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoView = (PhotoView) itemView;
        }
    }
}

