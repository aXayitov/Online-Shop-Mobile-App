package com.example.onlineshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.API.ApiClient;
import com.example.onlineshop.API.AuthApi;
import com.example.onlineshop.Domain.UpdateProfileRequest;
import com.example.onlineshop.Domain.UserProfile;
import com.example.onlineshop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivProfile, btnBack;
    private TextView tvChangePhoto;
    private EditText etFullName, etEmail, etPhone;
    private Button btnSave, btnLogout;
    private SharedPreferences sharedPreferences;
    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        initViews();
        setupSharedPreferences();
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews() {
        ivProfile = findViewById(R.id.ivProfile);
        tvChangePhoto = findViewById(R.id.tvChangePhoto);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        authApi = ApiClient.getAuthApi();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> goBackToMain());

        tvChangePhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Change photo clicked", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> updateProfile());

        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserProfile() {
        String token = sharedPreferences.getString("token", "");
        String cachedName = sharedPreferences.getString("fullName", "");

        // Avval cached nomni ko'rsatish
        if (!cachedName.isEmpty()) {
            etFullName.setText(cachedName);
        }

        if (token.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Log.d("PROFILE_DEBUG", "Token: " + token);

        String authHeader = "Bearer " + token;
        Call<UserProfile> call = authApi.getProfile(authHeader);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile user = response.body();
                    displayUserProfile(user);
                    saveUserProfileToCache(user);
                } else {
                    handleProfileLoadError(response);
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserProfile(UserProfile user) {
        etFullName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone() != null ? user.getPhone() : "");
    }

    private void saveUserProfileToCache(UserProfile user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", user.getFullName());
        editor.putString("email", user.getEmail());
        if (user.getPhone() != null) {
            editor.putString("phone", user.getPhone());
        }
        editor.apply();
    }

    private void updateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        if (!validateInput(fullName, email)) {
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest(fullName, email, phone);
        String authHeader = "Bearer " + token;

        Call<Void> call = authApi.updateProfile(authHeader, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    handleUpdateSuccess(fullName, email, phone);
                } else {
                    handleUpdateError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String fullName, String email) {
        if (fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill name and email fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void handleUpdateSuccess(String fullName, String email, String phone) {
        // Yangi ma'lumotlarni cache ga saqlash
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", fullName);
        editor.putString("email", email);
        if (!phone.isEmpty()) {
            editor.putString("phone", phone);
        }
        editor.apply();

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        goBackToMain();
    }

    private void handleProfileLoadError(Response<UserProfile> response) {
        if (response.code() == 401) {
            Toast.makeText(ProfileActivity.this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            logout();
        } else {
            Toast.makeText(ProfileActivity.this, "Failed to load profile: " + response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpdateError(Response<Void> response) {
        if (response.code() == 401) {
            Toast.makeText(ProfileActivity.this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            logout();
        } else {
            Toast.makeText(ProfileActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    private void goBackToMain() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBackToMain();
    }
}