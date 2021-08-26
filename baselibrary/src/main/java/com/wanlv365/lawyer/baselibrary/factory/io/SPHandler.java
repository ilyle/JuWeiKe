package com.wanlv365.lawyer.baselibrary.factory.io;


import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;

public class SPHandler implements IOHandler{
    @Override
    public void saveString(String key, String value) {
        SharePreferenceUtil.getInstance().saveString(key,value).commit();
    }

    @Override
    public String getString(String key) {
        return SharePreferenceUtil.getInstance().getString(key);
    }
}
