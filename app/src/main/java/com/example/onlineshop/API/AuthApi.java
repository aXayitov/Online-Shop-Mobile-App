package com.example.onlineshop.API;

import com.example.onlineshop.Domain.AuthResponse;
import com.example.onlineshop.Domain.LoginRequest;
import com.example.onlineshop.Domain.RefreshRequest;
import com.example.onlineshop.Domain.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("api/auth/register")
    Call<Void> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("api/auth/refresh")
    Call<AuthResponse> refresh(@Body RefreshRequest request);

    @GET("api/auth/me")
    Call<AuthResponse> getMe(@Header("Authorization") String token);
}
