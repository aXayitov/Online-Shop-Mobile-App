package com.example.onlineshop.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlineshop.Adapter.CardAdapter;
import com.example.onlineshop.Helper.ChangeNumberItemsListener;
import com.example.onlineshop.Helper.ManagmentCart;
import com.example.onlineshop.R;
import com.example.onlineshop.databinding.ActivityCardBinding;

public class CardActivity extends AppCompatActivity {
    private ManagmentCart managmentCart;
    ActivityCardBinding binding;
    double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);

        setVariable();
        initList();
        calculatorCart();
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = CardActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CardActivity.this, R.color.purple_Dark));
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty()){
            showEmptyCartDialog();
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scroll.setVisibility(View.VISIBLE);

            binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.cartView.setAdapter(new CardAdapter(managmentCart.getListCart(), () -> calculatorCart()));
        }
    }

    private void showEmptyCartDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.empty_cart_dialog);
        dialog.setCancelable(false);

        TextView title = dialog.findViewById(R.id.dialog_title);
        Button okButton = dialog.findViewById(R.id.ok_button);

        title.setText("Your Cart is Empty");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("$"+itemTotal);
        binding.taxTxt.setText("$"+tax);
        binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }
}