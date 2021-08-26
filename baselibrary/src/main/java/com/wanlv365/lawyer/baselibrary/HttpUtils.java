package com.wanlv365.lawyer.baselibrary;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static IHttpEngine mHttpEngine = null;//默认引擎

   // private String url = "https://api.nicosoft.org/";
    private String url = "";


    private static final int GET_TYPE = 1111;
    private static final int POST_TYPE = 2222;
    private static final int DO_POST_TYPE = 3333;
    private static final int DO_POST_JSON_TYPE = 4444;

    private Context mContext;
    private Map<String, Object> mParams;
    private Map<String, String> header;
    private String json;

    private int mType = POST_TYPE;//默认post请求

    private boolean mCache = false;//是否读取缓存

    private String token = "";

    private HttpUtils(Context context) {
        this.mContext = context;
        mParams = new HashMap<>();
        header = new HashMap<>();
    }


    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils doJsonPost() {
        mType = DO_POST_JSON_TYPE;
        return this;
    }

    public HttpUtils doPost() {
        mType = DO_POST_TYPE;
        return this;
    }

    //请求方式
    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    public HttpUtils url(String url) {
        this.url = this.url + url;
        return this;
    }

    public HttpUtils urlForAll(String url) {
        this.url = url;
        return this;
    }

    //是否配置缓存
    public HttpUtils cache(boolean isCache) {
        this.mCache = isCache;
        return this;
    }


    //添加参数
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    //添加参数
    public HttpUtils addParams(Map<String, Object> mParams) {
        mParams.putAll(mParams);
        return this;
    }

    //添加回调
    public void execute(EngineCallBack callBack) {
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }
        //执行之前的回调
        callBack.onPreExecute(mContext, mParams);
        //判断执行方法
        if (mType == POST_TYPE) {
            post(url, mParams, callBack);
        }
        if (mType == GET_TYPE) {
            get(url, mParams, callBack);
        }
        if (mType == DO_POST_TYPE) {
            doPost(url, mParams, callBack);
        }
        if (mType == DO_POST_JSON_TYPE) {
            doJson(url, json, callBack);
        }
    }

    public void execute() {
        execute(null);
    }

    //可以在application里面初始化一个引擎
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }


    //每次可以自带引擎
    public HttpUtils exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
        return this;
    }


    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mContext, mCache, url, params, callBack);
    }


    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mContext, mCache, url, params, callBack);
    }

    private void doPost(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.doPost(mContext, url, params, header, callBack);
    }

    private void doJson(String url, String json, EngineCallBack callBack) {
        mHttpEngine.doPostJson(mContext, url, json, token, callBack);
    }

    public void download(String url, final String destFileDir, final String destFileName, final OnDownloadListener listener){
        mHttpEngine.download(url,destFileDir,destFileName,listener);
    }

    //get请求拼接url
    public static String joinParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }
        StringBuffer sb = new StringBuffer(url);
        if (!url.contains("?")) {
            sb.append("?");
        } else {
            if (!url.endsWith("?")) {
                sb.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();

    }

    //解析一个类上面的class信息
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    // 添加请求头
    public HttpUtils setHeader(String key, String val) {
        header.put(key, val);
        return this;
    }

    // 设置token
    public HttpUtils setToken(String token) {
        this.token = token;
        return this;
    }

    // 设置json
    public HttpUtils setJson(String json) {
        this.json = json;
        return this;
    }

}
