package com.dagen.storage.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

public class DisplayUtils {

    //获取屏幕宽度
    public static int getScreenWidth(Context context){
        //context的方法，获取windowManager
        WindowManager windowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //获取屏幕宽度
    public static int getScreenHeight(Context context){
        //context的方法，获取windowManager
        WindowManager windowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
