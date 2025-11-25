package com.example.onlineshop.Domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PopularDomain implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("picUrl")
    private String picUrl;

    @SerializedName("review")
    private int review;

    @SerializedName("score")
    private double score;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("categoryId")
    private int categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    private int numberInCart;

    // Constructors
    public PopularDomain() {}

    public PopularDomain(int id, String title, String picUrl, int review, double score, double price, String description) {
        this.id = id;
        this.title = title;
        this.picUrl = picUrl;
        this.review = review;
        this.score = score;
        this.price = price;
        this.description = description;
        this.numberInCart = 1;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPicUrl() { return picUrl; }
    public void setPicUrl(String picUrl) { this.picUrl = picUrl; }

    public int getReview() { return review; }
    public void setReview(int review) { this.review = review; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getNumberInCart() { return numberInCart; }
    public void setNumberInCart(int numberInCart) { this.numberInCart = numberInCart; }
}