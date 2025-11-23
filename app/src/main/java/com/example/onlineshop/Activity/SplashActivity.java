package com.example.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferences dan tokenni olish
        String token = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                .getString("ACCESS_TOKEN", null);

        if(token != null && !token.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish(); // SplashActivity ni yopish
    }
}
