package com.dagen.storage.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dagen.storage.R;

public class ImageLoader {

    public static void loagImg(Context context, String url, ImageView imageView){
         Glide.with(context).load(url).placeholder(R.drawable.default_img).error(R.drawable.default_img).into(imageView);
    }

    public static void loagPhoto(Context context, String url, ImageView imageView){
         Glide.with(context).load(url).placeholder(R.drawable.default_photo).error(R.drawable.default_photo).into(imageView);
    }

}
