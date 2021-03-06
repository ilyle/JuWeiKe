package com.dagen.storage;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.activity.GodownInventoryOutLogHomeActivity;
import com.dagen.storage.activity.StockQueryForSpecActivity;
import com.dagen.storage.activity.StockQueryForTmActivity;
import com.dagen.storage.activity.SystemSettingActivity;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.HomeMoudleBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.update.UpdateBean;
import com.dagen.storage.update.UpdateDialog;
import com.dagen.storage.update.UpdateManager;
import com.dagen.storage.update.UpdateProgressDialog;
import com.dagen.storage.utils.AppUtils;
import com.dagen.storage.utils.ImageLoader;
import com.dagen.storage.utils.Toaster;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.OnDownloadListener;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;
import com.wanlv365.lawyer.baselibrary.view.dialog.AlertDialog;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseMoudleActivity {

    @BindView(R.id.rc_home)
    RecyclerView rcHome;
    @BindView(R.id.tv_name)
    TextView tvName;

    private List<HomeMoudleBean.MsgBean> mBeans = new ArrayList<>();

    private UpdateProgressDialog progressDialog;
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    progressDialog.setProgress(msg.arg1);
                    break;
            }

        }
    };
    private String paths = "";
    private boolean isForm;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        super.initData();

        if (SharePreferenceUtil.getInstance().getBoolean("autoUpdate", false)) {
            checkVersion();
        }

        rcHome.setLayoutManager(new LinearLayoutManager(this));
        rcHome.setAdapter(new CommonRecyclerAdapter<HomeMoudleBean.MsgBean>(this, mBeans, R.layout.item_home) {
            @Override
            public void convert(ViewHolder holder, HomeMoudleBean.MsgBean item) {
                holder.setText(R.id.tv_title, item.getName());
                RecyclerView rcMoudle = holder.getView(R.id.rc_moudle);
                rcMoudle.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                rcMoudle.setAdapter(new CommonRecyclerAdapter<HomeMoudleBean.MsgBean.TableBean>(MainActivity.this, item.getTable(), R.layout.item_home_moudle) {
                    @Override
                    public void convert(ViewHolder holder, HomeMoudleBean.MsgBean.TableBean item) {
                        holder.setText(R.id.tv_name, item.getName());
                        ImageLoader.loagImg(MainActivity.this, item.getImg(), holder.getView(R.id.iv_img));
                        holder.setOnIntemClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = null;
                                switch (item.getType()) {
                                    case "cwup":
                                    case "standard":
                                    case "cwdown":
                                    case "cwmove":
                                    case "cwinv":
                                    case "outin":
                                        intent = new Intent(MainActivity.this, GodownInventoryOutLogHomeActivity.class);
                                        intent.putExtra("tableid", item.getTableid());
                                        intent.putExtra("type", item.getType());
                                        intent.putExtra("isAdd", item.getIs_permitadd());
                                        intent.putExtra("name", item.getName());
                                        startActivity(intent);
                                        break;
                                    case "skuquery":
                                        intent = new Intent(MainActivity.this, StockQueryForSpecActivity.class);
                                        intent.putExtra("tableid", item.getTableid());
                                        intent.putExtra("title", item.getName());
                                        startActivity(intent);
                                        // showChooseDialog();
                                        break;
                                    case "????????????":
                                        startActivity(new Intent(MainActivity.this, SystemSettingActivity.class));
                                        break;
                                }
                            }
                        });

                    }

                });
            }

        });

        mProgressDilog.show();
        questMoudle();
    }

    private void questMoudle() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", SharePreferenceUtil.getInstance().getString("userId"));
        params.put("code", SharePreferenceUtil.getInstance().getString("deviceId"));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pda_painelview");
        data.put("params", params);
        HttpUtils.with(this)
                .url("http://bos.henlo.net:92/pda_painelview.jsp")
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<HomeMoudleBean>() {
                    @Override
                    public void onSuccess(HomeMoudleBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            tvName.setText(result.getAppname());
                            mBeans.addAll(result.getMsg());
                            rcHome.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }


    private void showChooseDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.dialog_del)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        dialog.setText(R.id.tv_tm_del, "???????????????");
        dialog.setText(R.id.tv_kh_del, "???????????????");

        dialog.setOnClickListener(R.id.tv_tm_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, StockQueryForTmActivity.class));
            }
        });

        dialog.setOnClickListener(R.id.tv_kh_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, StockQueryForSpecActivity.class));
            }
        });

        dialog.show();
    }

    private void checkWritePermission(UpdateDialog dialog, String url) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // ???????????????????????????
                            dialog.dismiss();
                            progressDialog = new UpdateProgressDialog(MainActivity.this);
                            progressDialog.show();
                            downloadFile(url);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // ????????????????????????????????????????????????????????????Never ask again???,??????????????????????????????????????????????????????????????????

                        } else {
                            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????

                        }
                    }
                });
    }


    //????????????
    public void checkVersion() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", SharePreferenceUtil.getInstance().getString("userId"));
        params.put("code", SharePreferenceUtil.getInstance().getString("deviceId"));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_checkversion");
        data.put("params", params);

        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<UpdateBean>() {
                    @Override
                    public void onSuccess(UpdateBean result) {
                        if (result.getCode() == 200 && result.getSucceed() == 0) {
                            if (UpdateManager.getInatance().checkVersion(MainActivity.this, result.getMsg().getVersion())) {
                                showUpdateDialog(result.getMsg());
                            } else {

                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                    }
                });
    }


    //???????????????
    private void showUpdateDialog(final UpdateBean.MsgBean data) {
        final UpdateDialog dialog = new UpdateDialog(this, true);
        dialog.setContent(data.getContent());
        dialog.setVersionName(data.getName());
        dialog.setOnUpdateClickListener(new UpdateDialog.OnUpdateClickListener() {
            @Override
            public void onUpdate() {
                checkWritePermission(dialog, data.getDownloadurl());

            }
        });
        dialog.show();
    }


    //??????apk
    private void downloadFile(String url) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "stroage";
        MyApplication.isDownLoad = true;
        HttpUtils.with(this).download(url, path, "stroage.apk", new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                paths = file.getAbsolutePath();
                progressDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean haveInstallPermission = getPackageManager().canRequestPackageInstalls();
                    if (!haveInstallPermission) {
                        isForm = true;
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        //????????????apk???URI???????????????intent??????????????????????????????????????????????????????????????????????????????????????????????????????
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        intent.setData(packageURI);
                        startActivityForResult(intent, 10086);
                    } else {
                        UpdateManager.getInatance().installApk(MainActivity.this, file);
                    }

                } else {
                    UpdateManager.getInatance().installApk(MainActivity.this, file);
                }
            }

            @Override
            public void onDownloading(int progress) {
                Message msg = mHander.obtainMessage();
                msg.arg1 = (int) (progress);
                msg.what = 111;
                mHander.sendMessage(msg);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                progressDialog.dismiss();
                Log.i("tag", "????????????====" + e.getMessage());
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!isForm) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            boolean haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {
                if (requestCode == 10086) {
                    UpdateManager.getInatance().installApk(MainActivity.this, new File(paths));
                }
            } else {
                Toaster.showMsg("Android8.0???????????????????????????????????????????????????????????????");
            }

        }

        isForm = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
