package com.dagen.storage.utils;

import android.content.Context;

import com.dagen.storage.bean.CommBean;
import com.dagen.storage.support.Contasts;
import com.google.gson.Gson;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xujie on 2021/8/27.
 * Mail : 617314917@qq.com
 */
public class NetworkHelper {

    private static volatile NetworkHelper sInstance;

    private NetworkHelper() {
    }

    //单例模式
    public static NetworkHelper getInstance() {
        if (sInstance == null) {
            synchronized (NetworkHelper.class) {
                if (sInstance == null) {
                    sInstance = new NetworkHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * 删除明细
     * boxno_deletecwitem_ard
     */
    public void deleteItem(Context context, int userid, int tableid, int rowid, int itemid, HttpCallBack<CommBean> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        params.put("tableid", tableid);
        params.put("rowid", rowid);
        params.put("itemid", itemid);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "boxno_deletecwitem_ard");
        data.put("params", params);
        HttpUtils.with(context)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(new Gson().toJson(data))
                .execute(callBack);
    }

    /**
     * 修改明细
     * boxno_updateqty_ard
     */
    public void updateQty(Context context, int userid, int tableid, int rowid, int itemid, int qty, HttpCallBack<CommBean> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        params.put("tableid", tableid);
        params.put("rowid", rowid);
        params.put("itemid", itemid);
        params.put("qty", qty);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "boxno_updateqty_ard");
        data.put("params", params);
        HttpUtils.with(context)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(new Gson().toJson(data))
                .execute(callBack);
    }

    /**
     *
     */
    public void getUpAliasList(Context context, int userid, int tableid, int rowid, String alias, HttpCallBack<String> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        params.put("tableid", tableid);
        params.put("rowid", rowid);
        params.put("alias", alias);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getupaliaslist");
        data.put("params", params);
        HttpUtils.with(context)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(new Gson().toJson(data))
                .execute(callBack);
    }
}
