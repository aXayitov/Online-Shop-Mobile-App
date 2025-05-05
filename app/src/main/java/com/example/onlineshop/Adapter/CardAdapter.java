package com.example.onlineshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.onlineshop.Domain.PopularDomain;
import com.example.onlineshop.Helper.ChangeNumberItemsListener;
import com.example.onlineshop.Helper.ManagmentCart;
import com.example.onlineshop.databinding.ViewholderCardBinding;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Viewholder> {
    ArrayList<PopularDomain> items;
    ViewholderCardBinding binding;
    Context context;
    ChangeNumberItemsListener changeNumberItemsListener;
    ManagmentCart managmentCart;

    // Constructor
    public CardAdapter(ArrayList<PopularDomain> items, ChangeNumberItemsListener listener) {
        this.items = items;
        this.changeNumberItemsListener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ViewholderCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        managmentCart = new ManagmentCart(context);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        binding.titleTxt.setText(items.get(position).getTitle());
        binding.feeEachItem.setText("$" + items.get(position).getPrice());
        binding.totalEachItem.setText("$" + Math.round(items.get(position).getNumberInCart() * items.get(position).getPrice()));
        binding.numberItemTxt.setText(String.valueOf(items.get(position).getNumberInCart()));

        int drawableResourced = holder.itemView.getResources().getIdentifier(items.get(position).getPicUrl(),
                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourced)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(binding.pic);

        binding.plusCartBtn.setOnClickListener(v -> {
            managmentCart.plusNumberItem(items, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });
        });

        binding.minusCartBtn.setOnClickListener(v -> {
            managmentCart.minusNumberItem(items, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder class
    public static class Viewholder extends RecyclerView.ViewHolder {
       Viewholder(ViewholderCardBinding binding){super(binding.getRoot());}
    }
}
