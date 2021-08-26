package com.dagen.storage.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.OnDownloadListener;

import java.io.File;


//版本升级manager
public class UpdateManager {

    private static UpdateManager manager;

    private UpdateManager(){

    }

    public static UpdateManager getInatance(){
        if(manager==null){
            manager=new UpdateManager();
        }
        return manager;
    }

    /**
     * @return 当前应用的版本号
     */
    public int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @return 当前应用的版本名称
     */
    public String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 提示版本更新的对话框
     */
    public void showDialogUpdate(Context context, boolean force) {
        UpdateDialog dialog = new UpdateDialog(context, force);
        dialog.show();
    }


    /**
     * 下载apk
     *
     */
    public void downloadFile(Context context, String url) {
        String path= Environment.getExternalStorageDirectory()+ File.separator+"XingKe";
        HttpUtils.with(context).download(url, path, "XingKe.apk", new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {

            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed(Exception e) {

            }
        });

    }


    public boolean checkVersion(Context context, int code) {
        if(getVersionCode(context)<code){
            return true;
        }
        return false;
    }


    //安装apk 兼容7.0
    public void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) { //Android 7.0及以上
            // 参数2 清单文件中provider节点里面的authorities ; 参数3  共享的文件,即apk包的file类
            Uri apkUri = FileProvider.getUriForFile(context, "com.dagen.storage.fileProvider", file);
            Log.e("tag", "apkUri :" + apkUri.toString());
            //对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


}
