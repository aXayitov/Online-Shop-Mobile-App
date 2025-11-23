package com.example.onlineshop.Domain;

public class RefreshRequest {
    private String token;
    private String refreshToken;

    // Getters & Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
