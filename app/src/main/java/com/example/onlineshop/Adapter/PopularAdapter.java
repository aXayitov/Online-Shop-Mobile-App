package com.example.onlineshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.onlineshop.Activity.DetailActivity;
import com.example.onlineshop.Domain.PopularDomain;
import com.example.onlineshop.Helper.ImageHelper;
import com.example.onlineshop.databinding.ViewholderPupListBinding;

import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    ArrayList<PopularDomain> items;
    Context context;

    public PopularAdapter(ArrayList<PopularDomain> items) {
        this.items = items;
    }

    // Yangi constructor - List qabul qiladi
    public PopularAdapter(List<PopularDomain> items) {
        this.items = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderPupListBinding binding = ViewholderPupListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        PopularDomain item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeTxt.setText("$" + item.getPrice());
        holder.binding.scoreTxt.setText(String.valueOf(item.getScore()));
        holder.binding.reviewTxt.setText(String.valueOf(item.getReview()));

        // Yangi usul - ImageHelper orqali rasm yuklash
        ImageHelper.loadImageFromDrawable(context, item.getPicUrl(), holder.binding.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<PopularDomain> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPupListBinding binding;

        public Viewholder(ViewholderPupListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}