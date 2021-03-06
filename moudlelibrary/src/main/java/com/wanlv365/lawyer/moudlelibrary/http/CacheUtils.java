package com.wanlv365.lawyer.moudlelibrary.http;



import com.wanlv365.lawyer.moudlelibrary.db.DaoSupportFactory;
import com.wanlv365.lawyer.moudlelibrary.db.IDaoSupport;

import java.util.List;

public class CacheUtils {

    public static String getCacheData(String key){
        String resultJson=null;
        IDaoSupport daoSupport= DaoSupportFactory.getFactory().getDao(CacheData.class);
        List<CacheData> mCacheDatas=daoSupport.querySupport().SELECTION("mKeyUrl=?").SELECTIONARGS(key).query();
        if(mCacheDatas.size()!=0){
            CacheData cacheData=mCacheDatas.get(0);
            resultJson=cacheData.getResult();
        }

        return resultJson;
    }

    public static void saveCacheData(String key,String value){
        IDaoSupport daoSupport= DaoSupportFactory.getFactory().getDao(CacheData.class);
        daoSupport.delete("mKeyUrl=?",key);
        daoSupport.insert(new CacheData(key,value));
    }

}
