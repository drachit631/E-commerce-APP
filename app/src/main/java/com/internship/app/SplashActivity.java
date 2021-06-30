package com.internship.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    AlphaAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setBackgroundDrawableResource(R.drawable.gradient_bg1);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.splash_imageview);
        animation = new AlphaAnimation(0,1);
        animation.setDuration(2500);
        imageView.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2800);
    }
}