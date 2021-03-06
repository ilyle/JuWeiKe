package com.dagen.storage.support;

import android.util.Log;

import com.dagen.storage.MyApplication;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

//网络拦截器
public class MyNetWorkIntercept implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
              //  .addHeader("Ns-Session", SharePreferenceUtil.getInstance().getString("token"))
                .build();

        Log.e("tag","请求头==="+request.headers());

        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (!bodyEncoded(response.headers())&&!MyApplication.isDownLoad) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                return response;
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String resultCode=jsonObject.getString("result_code");
                    if(resultCode.equals("-1")){
                           /* Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent. FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            ActivityManager.getInstance().finishAllButCurrent();*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //得到所需的string，开始判断是否异常
                //***********************do something*****************************

            }

        }

        MyApplication.isDownLoad=false;

        return response;
    }


    private boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }
}
