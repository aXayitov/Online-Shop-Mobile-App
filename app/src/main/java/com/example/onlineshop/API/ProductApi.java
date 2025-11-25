package com.example.onlineshop.API;

import com.example.onlineshop.Domain.PopularDomain;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {

    @GET("api/products")
    Call<List<PopularDomain>> getAllProducts();

    @GET("api/products/popular")
    Call<List<PopularDomain>> getPopularProducts();

    @GET("api/products/{id}")
    Call<PopularDomain> getProductById(@Path("id") int productId);

    @GET("api/products/category/{categoryId}")
    Call<List<PopularDomain>> getProductsByCategory(@Path("categoryId") int categoryId);

    @GET("api/products/search")
    Call<List<PopularDomain>> searchProducts(@Query("q") String query);
}