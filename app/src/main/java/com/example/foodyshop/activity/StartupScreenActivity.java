package com.example.foodyshop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodyshop.R;
import com.example.foodyshop.helper.Helper;
import com.jaeger.library.StatusBarUtil;

public class StartupScreenActivity extends AppCompatActivity {

    private static final String FIRST_TIME_OPEN_APP = "first_time_open_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);
        SharedPreferences shared = Helper.getSharedPreferences(this);
        if (shared.getBoolean(FIRST_TIME_OPEN_APP, true)) {
            shared.edit().putBoolean(FIRST_TIME_OPEN_APP, false).apply();
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        } else {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
            LottieAnimationView animationView = findViewById(R.id.image_on_startup);
            animationView.playAnimation();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }, 3000);
        }
    }
}