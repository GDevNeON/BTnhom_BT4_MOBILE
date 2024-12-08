package com.example.bt4_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> imageList = new ArrayList<>();
    private int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // request quyền
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 trở lên
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else {
                loadImages();
            }
        } else {
            // Android 12 trở xuống
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                loadImages();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Quyền đã được cấp", Toast.LENGTH_SHORT).show();
            loadImages();
        } else {
            Toast.makeText(this, "Cấp quyền không thành công", Toast.LENGTH_SHORT).show();
        }
    }


    private List<String> getImagesFromPictures(Context context) {
        List<String> imageList = new ArrayList<>();
        Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media.RELATIVE_PATH + " LIKE ?";
        String[] selectionArgs = new String[]{"%Pictures%"};

        String[] projection = {MediaStore.Images.Media.DATA};

        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    imageList.add(path);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("DEBUG", "Fetching images from MediaStore...");
        Log.d("DEBUG", "Found " + imageList.size() + " images.");
        return imageList;
    }

    private void loadImages() {
        // Gán danh sách ảnh
        imageList = getImagesFromPictures(this);
        Log.d("DEBUG", "Image List size: " + imageList.size());  // Thêm log kiểm tra số lượng ảnh

        if (imageList.isEmpty()) {
            Toast.makeText(this, "Không có ảnh trong thư mục Pictures", Toast.LENGTH_SHORT).show();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        GridAdapter adapter = new GridAdapter(this, imageList, (imagePath) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("IMAGE_PATH", imagePath);
            intent.putStringArrayListExtra("IMAGES", new ArrayList<>(imageList));
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);  // Đảm bảo gọi setAdapter
    }
}