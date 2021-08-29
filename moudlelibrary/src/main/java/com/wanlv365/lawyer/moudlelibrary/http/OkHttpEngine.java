package com.wanlv365.lawyer.moudlelibrary.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.wanlv365.lawyer.baselibrary.EngineCallBack;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.IHttpEngine;
import com.wanlv365.lawyer.baselibrary.OnDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//okhttp 引擎
public class OkHttpEngine implements IHttpEngine {

     OkHttpClient.Builder builder= new OkHttpClient.Builder();
    private String resultJson;
    private OkHttpClient mOkhttpClient=null;
    private Interceptor mInterceptor;

    public void setNetWorkIntercept(Interceptor intercept){
        this.mInterceptor=intercept;
        builder.addInterceptor(mInterceptor);
        mOkhttpClient=builder.build();
    }


    @Override
    public void get(Context context, final boolean cache, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String joinUrl = HttpUtils.joinParams(url, params);

        Log.i("Okhttp--GET->请求参数:   ", joinUrl);

        if (cache) {//判断需不需要缓存
            resultJson = CacheUtils.getCacheData(joinUrl);
            if (!TextUtils.isEmpty(resultJson)) {
                //需要缓存  数据库有缓存
                Log.i("Okhttp--->缓存:   ", "有缓存，已读到缓存");
                callBack.onSuccess(resultJson);
            }

        }

        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .method("GET", null)
                .build();

        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();


                //每次获取到的数据 先比对上一次内容

                if (cache) {
                    if (result.equals(resultJson)) {//数据一致  直接返回  不刷新界面
                        Log.i("Okhttp--->缓存:   ", "缓存数据一致");
                        return;
                    }

                }


                Log.i("Okhttp--->返回结果:   ", result);

                //不一致  成功回调  刷新界面
                callBack.onSuccess(result);


                if (cache) {//缓存数据
                    CacheUtils.saveCacheData(joinUrl, result);
                }
            }
        });

    }

    @Override
    public void post(final Context context, final boolean cache, String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String joinUrl = HttpUtils.joinParams(url, params);

        Log.i("Okhttp--POST->请求参数:   ", joinUrl);

        if (cache) {//判断需不需要缓存
            resultJson = CacheUtils.getCacheData(joinUrl);
            if (!TextUtils.isEmpty(resultJson)) {
                //需要缓存  数据库有缓存
                Log.i("Okhttp--->缓存:   ", "有缓存，已读到缓存");
                callBack.onSuccess(resultJson);
            }

        }


        RequestBody requestBody = appendBody(params);

        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        Headers requestHeaders = request.headers();
        int headerSize=requestHeaders.size();
        for(int i=0;i<headerSize;i++){
            String headerName=requestHeaders.name(i);
            String headerValue=requestHeaders.get(headerName);
            Log.i("tag","请求头==="+headerName+"===="+headerValue);
        }



        mOkhttpClient.newCall(request).enqueue(new Callback() {

            Handler mainHandler = new Handler(context.getMainLooper());

            @Override
            public void onFailure(Call call,final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (cache) {
                    if (result.equals(resultJson)) {//数据一致  直接返回  不刷新界面
                        Log.i("Okhttp--->缓存:   ", "缓存数据一致");
                        return;
                    }

                }

                Log.i("Okhttp--->返回结果:   ", result);

                if(result.startsWith("<html>")){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(new Exception());
                        }
                    });
                    return;
                }

                //每次获取到的数据 先比对上一次内容

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });

                if (cache) {//缓存数据
                    CacheUtils.saveCacheData(joinUrl, result);
                }

            }
        });


    }

    @Override
    public void doPost(final Context context, String url, Map<String, Object> param, Map<String, String> header, final EngineCallBack callBack) {
        final String joinUrl = HttpUtils.joinParams(url, param);
        Log.i("OkHttp--POST-> ", joinUrl);
        RequestBody requestBody = appendBody(param);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
             //   .addHeader("Ns-Session", header.get("token"))
                .build();


        Headers requestHeaders = request.headers();
        int headerSize=requestHeaders.size();
        for(int i=0;i<headerSize;i++){
            String headerName=requestHeaders.name(i);
            String headerValue=requestHeaders.get(headerName);
            Log.i("tag","请求头==="+headerName+"===="+headerValue);
        }

        mOkhttpClient.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(context.getMainLooper());

            @Override
            public void onFailure(Call call,final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i("OkHttp--->Result ", result);

                if(result.startsWith("<html>")){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(new Exception());
                        }
                    });
                    return;
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });

    }

    @Override
    public void doPostJson(final Context context, String url, String json, String token, final EngineCallBack callBack) {
        final String joinUrl = HttpUtils.joinParams(url, null);
        Log.i("OkHttp--->json参数: ", json);
        Log.i("OkHttp--->token: ", token);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
             //   .addHeader("Ns-Session", token)
                .build();
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(context.getMainLooper());

            @Override
            public void onFailure(Call call,final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i("OkHttp--->Result ", result);

                if(result.startsWith("<html>")){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(new Exception());
                        }
                    });
                    return;
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });
    }

    private RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("","")//解决参数不能为空的bug
                .setType(MultipartBody.FORM);
        appendParams(builder, params);
        return builder.build();
    }


    //添加参数
    private void appendParams(MultipartBody.Builder builder, Map<String, Object> params) {

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                // builder.addFormDataPart(key,params.get(key)+"");
                Object value = params.get(key);

                if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                } else if (value instanceof List) {//代表提交的是list集合

                    try {
                        List<File> files = (List<File>) value;

                        for (int i = 0; i < files.size(); i++) {
                            File file = files.get(i);
                            builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    builder.addFormDataPart(key, value + "");
                }

            }
        }

    }

    //猜测文件的类型
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contTypeFor = fileNameMap.getContentTypeFor(path);

        if (contTypeFor == null) {
            contTypeFor = "application/octet_stream";
        }

        return contTypeFor;
    }

  //文件下载
    @Override
    public void download(String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        //异步请求
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                //储存下载文件的目录
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);

                try {

                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    //下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    listener.onDownloadFailed(e);
                }finally {

                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }

                }


            }
        });

    }

}
