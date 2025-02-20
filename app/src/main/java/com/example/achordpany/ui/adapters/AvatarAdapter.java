package com.example.achordpany.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achordpany.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {
    private Context context;
    private List<String> avatarList;
    private OnAvatarClickListener listener;

    public interface OnAvatarClickListener {
        void onAvatarClick(String avatarPath);
    }

    public AvatarAdapter(Context context, List<String> avatarList, OnAvatarClickListener listener) {
        this.context = context;
        this.avatarList = avatarList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_avatar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String avatarPath = avatarList.get(position);
        loadImageFromAssets(avatarPath, holder.avatarImage);

        holder.itemView.setOnClickListener(v -> {
            Log.d("AvatarAdapter", "Avatar selected: " + avatarPath);
            listener.onAvatarClick(avatarPath);
        });
    }

    @Override
    public int getItemCount() {
        Log.d("AvatarAdapter", "Total avatars in adapter: " + avatarList.size());
        return avatarList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.avatarImage);
        }
    }

    // Load image from assets folder
    private void loadImageFromAssets(String filePath, ImageView imageView) {
        try {
            InputStream inputStream = context.getAssets().open("profile_images/" + filePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            inputStream.close();
        } catch (IOException e) {
            Log.e("AvatarAdapter", "Error loading avatar: " + e.getMessage(), e);
        }
    }
}
