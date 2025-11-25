package com.example.onlineshop.API;

import com.example.onlineshop.Domain.CategoryDomain;
import com.example.onlineshop.Domain.PopularDomain;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryApi {
    @GET("api/categories")
    Call<List<CategoryDomain>> getCategories();
}
