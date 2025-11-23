package com.example.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.API.ApiClient;
import com.example.onlineshop.API.AuthApi;
import com.example.onlineshop.Domain.RegisterRequest;
import com.example.onlineshop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText etFullName, etEmail, etPassword;
    Button btnRegister;
    TextView toLogin; // Yangi qo'shildi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        toLogin = findViewById(R.id.toLogin); // Yangi qo'shildi

        // Register tugmasi bosilganda
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest request = new RegisterRequest(fullName, email, password);
            AuthApi api = ApiClient.getAuthApi();
            api.register(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to Login
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: "+response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // "Already have an account?" linki bosilganda - YANGI QO'SHILGAN KOD
        toLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // RegisterActivity ni yopish
        });
    }
}