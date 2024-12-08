package com.example.bt4_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private ViewPager2 viewPager;
    private List<String> imageList;
    private long lastFlingTime = 0;
    private static final long FLING_COOLDOWN = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String currentImagePath = getIntent().getStringExtra("IMAGE_PATH");
        imageList = getIntent().getStringArrayListExtra("IMAGES");

        if (currentImagePath == null || imageList == null || imageList.isEmpty()) {
            Toast.makeText(this, "Không tải được ảnh", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewPager = findViewById(R.id.viewPager);
        DetailAdapter adapter = new DetailAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        // Chuyển đến ảnh đang được chọn
        int currentIndex = imageList.indexOf(currentImagePath);
        if (currentIndex != -1) {
            viewPager.setCurrentItem(currentIndex, false);
        }

        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setAlpha(1 - Math.abs(position));
                page.setScaleY(0.8f + (1 - Math.abs(position)) * 0.2f);
            }
        });

        // Cấu hình GestureDetector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Kiểm tra số ngón tay
                if (e2.getPointerCount() >= 3) {
                    // Tính toán di chuyển của các ngón tay
                    float deltaX = e2.getX() - e1.getX();
                    float deltaY = e2.getY() - e1.getY();

                    // Kiểm tra độ dài của vuốt (tính theo hướng ngang)
                    if (Math.abs(deltaY) < Math.abs(deltaX) && Math.abs(deltaX) > 100) {
                        if (deltaX > 0) {
                            goToNextImage();
                        } else {
                            goToPreviousImage();
                        }
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                // Xử lý sự kiện onDown để tránh ACTION_CANCEL
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log sự kiện touch để xem chi tiết
        Log.d("DEBUG", "Pointer count: " + event.getPointerCount());

        // Đẩy sự kiện lên GestureDetector để xử lý
        gestureDetector.onTouchEvent(event);

        // Kiểm tra sự kiện multi-touch trong onTouchEvent
        if (event.getPointerCount() >= 3) {
            float deltaX = event.getX(2) - event.getX(0); // Sử dụng các ngón tay thứ 0 và thứ 2
            if (Math.abs(deltaX) > 100) {
                if (deltaX > 0) {
                    goToNextImage();
                } else {
                    goToPreviousImage();
                }
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private void goToNextImage() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFlingTime < FLING_COOLDOWN) {
            return; // Ignore this fling if it was too soon
        }
        lastFlingTime = currentTime;

        // Chuyển đến ảnh tiếp theo
        int currentIndex = viewPager.getCurrentItem();
        if (currentIndex < imageList.size() - 1) {
            viewPager.setCurrentItem(currentIndex + 1, true);
        }
    }

    private void goToPreviousImage() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFlingTime < FLING_COOLDOWN) {
            return; // Ignore this fling if it was too soon
        }
        lastFlingTime = currentTime;

        // Chuyển đến ảnh trước đó
        int currentIndex = viewPager.getCurrentItem();
        if (currentIndex > 0) {
            viewPager.setCurrentItem(currentIndex - 1, true);
        }
    }
}
