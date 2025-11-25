package com.example.onlineshop.Helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageHelper {public static void loadImage(Context context, String imageUrl, ImageView imageView) {
    // Agar URL http:// yoki https:// bilan boshlansa, internetdan yuklaymiz
    if (imageUrl.startsWith("http")) {
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);
    } else {
        // Aks holda drawable dan yuklaymiz
        loadImageFromDrawable(context, imageUrl, imageView);
    }
}

    public static void loadImageFromDrawable(Context context, String drawableName, ImageView imageView) {
        try {
            int drawableResourceId = context.getResources()
                    .getIdentifier(drawableName, "drawable", context.getPackageName());

            if (drawableResourceId != 0) {
                // Drawable topildi
                Glide.with(context)
                        .load(drawableResourceId)
                        .into(imageView);
            } else {
                // Drawable topilmadi, default rasmni ko'rsatamiz
                Glide.with(context)
                        .load(android.R.drawable.ic_menu_report_image)
                        .into(imageView);
            }
        } catch (Exception e) {
            // Xatolik bo'lsa, default rasm
            Glide.with(context)
                    .load(android.R.drawable.ic_menu_report_image)
                    .into(imageView);
        }
    }

    // Rasm mavjudligini tekshirish
    public static boolean isDrawableExists(Context context, String drawableName) {
        int drawableResourceId = context.getResources()
                .getIdentifier(drawableName, "drawable", context.getPackageName());
        return drawableResourceId != 0;
    }
}