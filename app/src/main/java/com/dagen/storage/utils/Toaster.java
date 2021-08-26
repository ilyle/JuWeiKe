package com.dagen.storage.utils;

import android.widget.Toast;

import com.dagen.storage.MyApplication;


public class Toaster {
    public static void showMsg(String text){
        Toast.makeText(MyApplication.getInstance(),text,Toast.LENGTH_SHORT).show();
    }
}
