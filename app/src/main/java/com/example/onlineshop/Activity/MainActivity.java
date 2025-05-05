package com.example.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlineshop.Adapter.PopularAdapter;
import com.example.onlineshop.Domain.PopularDomain;
import com.example.onlineshop.R;
import com.example.onlineshop.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        statusBarColor();
        initRecyclerView();
        bottomNavigation();
    }

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CardActivity.class)));
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("T-shirt black", "item_1", 15, 4, 500, "Immerce yourself in a world af vibrant visuals and\n"+
                " immersive sound with the monitor.\n"+
                " Its cutting-edge monitor technology brings every\n"+
                " scene to life with striking clarity and rich colors."));
        items.add(new PopularDomain("Smart Watch", "item_2", 10, 4.5, 450, "Immerce yourself in a world af vibrant visuals and\n"+
                " immersive sound with the monitor.\n"+
                " Its cutting-edge monitor technology brings every\n"+
                " scene to life with striking clarity and rich colors."));
        items.add(new PopularDomain("Phone", "item_3", 3, 4.9, 800, "Immerce yourself in a world af vibrant visuals and\n"+
                " immersive sound with the monitor.\n"+
                " Its cutting-edge monitor technology brings every\n"+
                " scene to life with striking clarity and rich colors."));

        binding.PopularView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.PopularView.setAdapter(new PopularAdapter(items));
    }
}