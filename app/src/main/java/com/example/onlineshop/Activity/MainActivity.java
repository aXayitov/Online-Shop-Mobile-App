package com.example.onlineshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.Adapter.CategoryAdapter;
import com.example.onlineshop.Adapter.PopularAdapter;
import com.example.onlineshop.API.ApiClient;
import com.example.onlineshop.API.CategoryApi;
import com.example.onlineshop.API.ProductApi;
import com.example.onlineshop.Domain.CategoryDomain;
import com.example.onlineshop.Domain.PopularDomain;
import com.example.onlineshop.R;
import com.example.onlineshop.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private PopularAdapter popularAdapter;
    private CategoryAdapter categoryAdapter;
    private ArrayList<PopularDomain> popularItems;
    private List<CategoryDomain> categoryItems;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Listlarni initialize qilish
        popularItems = new ArrayList<>();
        categoryItems = new ArrayList<>();

        statusBarColor();
        initRecyclerViews();
        bottomNavigation();
        loadUserName();

        // Faqat backend dan ma'lumot yuklash
        loadDataFromBackend();
    }

    private void loadDataFromBackend() {
        Log.d(TAG, "Loading data ONLY from backend...");

        // Categories ni yuklash
        loadCategoriesFromBackend();

        // Products ni yuklash
        binding.getRoot().postDelayed(() -> {
            loadProductsFromBackend();
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserName();
    }

    private void loadUserName() {
        String userName = sharedPreferences.getString("fullName", "Isroilov Temur");
        binding.textView2.setText(userName);
    }

    private void initRecyclerViews() {
        // Popular products RecyclerView
        binding.PopularView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularAdapter = new PopularAdapter(popularItems);
        binding.PopularView.setAdapter(popularAdapter);

        // Categories RecyclerView
        RecyclerView categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categoryItems);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        setupCategoryClick();
        setupSearch();
    }

    private void setupCategoryClick() {
        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(CategoryDomain category) {
                loadProductsByCategory(category.getId());
            }
        });
    }

    private void loadProductsByCategory(int categoryId) {
        Log.d(TAG, "Loading products for category: " + categoryId);
        ProductApi productApi = ApiClient.getProductApi();
        Call<List<PopularDomain>> call = productApi.getProductsByCategory(categoryId);

        call.enqueue(new Callback<List<PopularDomain>>() {
            @Override
            public void onResponse(Call<List<PopularDomain>> call, Response<List<PopularDomain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Products by category loaded: " + response.body().size());
                    popularItems.clear();
                    popularItems.addAll(response.body());
                    popularAdapter.notifyDataSetChanged();

                    if (response.body().isEmpty()) {
                        Toast.makeText(MainActivity.this, "No products in this category", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Loaded " + response.body().size() + " products", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "❌ Failed to load products by category. Code: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<PopularDomain>> call, Throwable t) {
                Log.e(TAG, "❌ Error loading products by category: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                showEmptyState();
            }
        });
    }

    private void loadCategoriesFromBackend() {
        Log.d(TAG, "Loading categories from backend...");
        CategoryApi categoryApi = ApiClient.getCategoryApi();
        Call<List<CategoryDomain>> call = categoryApi.getCategories();

        call.enqueue(new Callback<List<CategoryDomain>>() {
            @Override
            public void onResponse(Call<List<CategoryDomain>> call, Response<List<CategoryDomain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Categories loaded from backend: " + response.body().size());
                    categoryItems.clear();
                    categoryItems.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();

                    if (response.body().isEmpty()) {
                        showEmptyCategories();
                    }
                } else {
                    Log.e(TAG, "❌ Failed to load categories. Code: " + response.code());
                    showEmptyCategories();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDomain>> call, Throwable t) {
                Log.e(TAG, "❌ Error loading categories: " + t.getMessage());
                showEmptyCategories();
            }
        });
    }

    private void loadProductsFromBackend() {
        Log.d(TAG, "Loading products from backend...");
        ProductApi productApi = ApiClient.getProductApi();
        Call<List<PopularDomain>> call = productApi.getPopularProducts();

        call.enqueue(new Callback<List<PopularDomain>>() {
            @Override
            public void onResponse(Call<List<PopularDomain>> call, Response<List<PopularDomain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Products loaded from backend: " + response.body().size());
                    popularItems.clear();
                    popularItems.addAll(response.body());
                    popularAdapter.notifyDataSetChanged();

                    // Ma'lumotlarni log qilamiz
                    for (PopularDomain product : response.body()) {
                        Log.d(TAG, "📦 Product: " + product.getId() + " - " + product.getTitle() +
                                " - $" + product.getPrice() + " - Image: " + product.getPicUrl());
                    }

                    if (response.body().isEmpty()) {
                        showEmptyProducts();
                    } else {
                        Toast.makeText(MainActivity.this, "Products loaded successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "❌ Failed to load products. Code: " + response.code());
                    showEmptyProducts();
                }
            }

            @Override
            public void onFailure(Call<List<PopularDomain>> call, Throwable t) {
                Log.e(TAG, "❌ Error loading products: " + t.getMessage());
                showEmptyProducts();
            }
        });
    }

    private void showEmptyState() {
        // Bo'sh holatni ko'rsatish
        popularItems.clear();
        popularAdapter.notifyDataSetChanged();
        Toast.makeText(this, "No products available", Toast.LENGTH_LONG).show();
    }

    private void showEmptyCategories() {
        // Bo'sh kategoriyalar
        categoryItems.clear();
        categoryAdapter.notifyDataSetChanged();
        Toast.makeText(this, "No categories available", Toast.LENGTH_LONG).show();
    }

    private void showEmptyProducts() {
        // Bo'sh productlar
        popularItems.clear();
        popularAdapter.notifyDataSetChanged();
        Toast.makeText(this, "No products available", Toast.LENGTH_LONG).show();
    }

    private void setupSearch() {
        binding.editTextText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String query = binding.editTextText.getText().toString().trim();
                    if (!query.isEmpty()) {
                        searchProducts(query);
                    }
                }
            }
        });
    }

    private void searchProducts(String query) {
        ProductApi productApi = ApiClient.getProductApi();
        Call<List<PopularDomain>> call = productApi.searchProducts(query);

        call.enqueue(new Callback<List<PopularDomain>>() {
            @Override
            public void onResponse(Call<List<PopularDomain>> call, Response<List<PopularDomain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    popularItems.clear();
                    popularItems.addAll(response.body());
                    popularAdapter.notifyDataSetChanged();

                    if (response.body().isEmpty()) {
                        Toast.makeText(MainActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PopularDomain>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CardActivity.class)));

        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }
}