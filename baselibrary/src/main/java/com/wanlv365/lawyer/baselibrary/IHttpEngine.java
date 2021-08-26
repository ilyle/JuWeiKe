package com.wanlv365.lawyer.baselibrary;

import android.content.Context;

import java.util.Map;

//引擎规范
public interface IHttpEngine {

    //get请求
    void get(Context context, boolean cache, String url, Map<String, Object> params, EngineCallBack callBack);

    //post请求
    void post(Context context, boolean cache, String url, Map<String, Object> params, EngineCallBack callBack);

    // Post 带请求头
    void doPost(Context context, String url, Map<String, Object> param, Map<String, String> header, EngineCallBack callBack);

    // Post Json
    void doPostJson(Context context, String url, String json, String token, EngineCallBack callBack);
    //下载文件

    void download( String url,  String destFileDir,  String destFileName,  OnDownloadListener listener);

    //上传文件


    //https 添加证书

}
