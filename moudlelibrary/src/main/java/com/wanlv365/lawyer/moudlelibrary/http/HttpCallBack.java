package com.wanlv365.lawyer.moudlelibrary.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.wanlv365.lawyer.baselibrary.EngineCallBack;
import com.wanlv365.lawyer.baselibrary.HttpUtils;

import java.util.Map;

public abstract class HttpCallBack<T> implements EngineCallBack {

    private Context mContext;

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
            this.mContext=context;
           //添加公共参数
           onPreExecute();
    }


    @Override
        public void onSuccess(String result) {
           Gson gson=new Gson();
           String newResult="";
           if(result.startsWith("[")) {
                newResult = result.substring(1, result.trim().length() - 1);
               Log.i("tag", "newResult===" + newResult);
           }else {
               newResult=result;
           }
           //增加异常捕获  防止解析出错崩溃
           try {
               T t= (T) gson.fromJson(newResult, HttpUtils.analysisClazzInfo(this));
               onSuccess(t);
           }catch (Exception e){
               onError(e);
           }
    }

    @Override
    public void onError(Exception e) {
       // Toast.makeText(mContext,"网络异常",Toast.LENGTH_SHORT).show();
    }

    //子类去重写  加载进度条
    public void onPreExecute(){

    }

    //返回可以直接操作的对象
    public abstract void onSuccess(T result);
}
