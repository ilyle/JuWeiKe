package com.dagen.storage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.activity.SystemSettingActivity;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.bean.ExprBean;
import com.dagen.storage.bean.HomeBean;
import com.dagen.storage.bean.LoginBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.HttpJsonBuilder;
import com.dagen.storage.support.HttpJsonBuilder2;
import com.dagen.storage.support.OnScanFinishListener;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.support.popupwindow.CommonPopupWindow;
import com.dagen.storage.utils.Endecrypt;
import com.dagen.storage.utils.Toaster;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//登录页
public class LoginActivity extends BaseMoudleActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.iv_jzzh)
    ImageView ivJzzh;
    @BindView(R.id.iv_jzmm)
    ImageView ivJzmm;
    @BindView(R.id.iv_choose)
    ImageView ivChoose;
    @BindView(R.id.ll_jzzh)
    LinearLayout llJzzh;

    private boolean isSel, isAccountRemeber, isPwdRemeber;
    private List<HomeBean> mAccounts = new ArrayList<>();
    private CommonPopupWindow popupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void initData() {
        super.initData();

        if(!TextUtils.isEmpty(SharePreferenceUtil.getInstance().getString("url"))) {
            Contasts.BASE_URL = "http://" + SharePreferenceUtil.getInstance().getString("url") + "/MDInfaceSystem96cs/servlet/doserverdata";
        }

        setEditextFilter(etAccount, new OnScanFinishListener() {
            @Override
            public void onScanFinish(String content) {
                Toaster.showMsg("扫描成功");
            }
        });

        isAccountRemeber = SharePreferenceUtil.getInstance().getBoolean("isAccountRemeber", false);
        isPwdRemeber = SharePreferenceUtil.getInstance().getBoolean("isPwdRemeber", false);

        ivJzzh.setImageResource(isAccountRemeber ? R.drawable.img_check : R.drawable.img_uncheck);
        ivJzmm.setImageResource(isPwdRemeber ? R.drawable.img_check : R.drawable.img_uncheck);

        ivChoose.setVisibility(isAccountRemeber?View.VISIBLE:View.GONE);

        String json = SharePreferenceUtil.getInstance().getString("account");
        if(!TextUtils.isEmpty(json)) {
            List<HomeBean> accounts = jsonToList(json, HomeBean.class);
            if (accounts != null)
                mAccounts.addAll(accounts);
        }

    }

    @OnClick({R.id.tv_login, R.id.ll_jzzh, R.id.ll_jzmm, R.id.iv_choose,R.id.tv_xtsz})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_choose:
                if(mAccounts.size()==0){
                    Toaster.showMsg("暂无数据！");
                    return;
                }
                showPop();
                break;
            case R.id.tv_xtsz:
                startActivity(new Intent(this,SystemSettingActivity.class));
                break;
            case R.id.tv_login:
                if(TextUtils.isEmpty(SharePreferenceUtil.getInstance().getString("url"))){
                     showSureDialog("您还未设置erp地址，是否去设置？", new OnSureClickListener() {
                         @Override
                         public void onSure() {
                             startActivity(new Intent(LoginActivity.this,SystemSettingActivity.class));
                         }
                     });
                     return;
                }

                if (TextUtils.isEmpty(etAccount.getText().toString().trim())) {
                    Toaster.showMsg(etAccount.getHint().toString().trim());
                    return;
                }
                if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                    Toaster.showMsg(etPwd.getHint().toString().trim());
                    return;
                }

                mProgressDilog.show();
                checkDeviceId(getDevideId());

                break;
            case R.id.ll_jzzh:
                isAccountRemeber = !isAccountRemeber;
                ivJzzh.setImageResource(isAccountRemeber ? R.drawable.img_check : R.drawable.img_uncheck);
                ivChoose.setVisibility(isAccountRemeber?View.VISIBLE:View.GONE);
                SharePreferenceUtil.getInstance().saveBoolean("isAccountRemeber", isAccountRemeber).commit();
                break;
            case R.id.ll_jzmm:
                isPwdRemeber = !isPwdRemeber;
                ivJzmm.setImageResource(isPwdRemeber ? R.drawable.img_check : R.drawable.img_uncheck);
                SharePreferenceUtil.getInstance().saveBoolean("isPwdRemeber", isPwdRemeber).commit();
                break;
        }
    }

    //验证设备号的有效性
    private void checkDeviceId(String id) {
        SharePreferenceUtil.getInstance().saveString("deviceId",id).commit();
        HttpUtils.with(this)
                .url("http://bos.henlo.net:92/pda_checkshebei.jsp?code="+id)
                .post()
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        if(result.getCode()==0){
                             login(id);
                        }else {
                            Toaster.showMsg(result.getMsg());
                            mProgressDilog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }

    //登录
    private void login(String id) {

       /* List<ExprBean>mBeans=new ArrayList<>();
        mBeans.add(new ExprBean("EMAIL",etAccount.getText().toString().trim()));
        mBeans.add(new ExprBean("PASSWORDHASH",etPwd.getText().toString().trim()));
        String jsonStr=new HttpJsonBuilder2.Builder().setCommand("Query")
                .setTable("USERS")
                .setParams(mBeans,"and")
                .setColumns("ID")
                .build().toJson();*/

        Map<String, Object> params = new HashMap<>();
        params.put("user", etAccount.getText().toString().trim());
        params.put("psd",etPwd.getText().toString().trim());
        params.put("code", id);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "user_login_ard");
        data.put("params", params);

        HttpUtils.with(this)
                    .url(Contasts.BASE_URL)
                    .doJsonPost()
                    .setJson(gson.toJson(data))
                    .execute(new HttpCallBack<LoginBean>() {
                        @Override
                        public void onSuccess(LoginBean result) {
                            mProgressDilog.dismiss();
                            if(result.getCode()==200){
                                if(result.getSucceed()!=0){
                                    Toaster.showMsg(result.getValue());
                                    return;
                                }
                                int count=0;
                                for(HomeBean bean:mAccounts){
                                    if(bean.getName().equals(etAccount.getText().toString().trim())
                                            &&bean.getPwd().equals(etPwd.getText().toString().trim())
                                    ){
                                        count++;
                                    }
                                }
                                if(count==0) {//防止重复添加
                                    HomeBean bean = new HomeBean();
                                    bean.setName(etAccount.getText().toString().trim());
                                    bean.setPwd(etPwd.getText().toString().trim());
                                    mAccounts.add(bean);
                                }
                                SharePreferenceUtil.getInstance().saveString("account", gson.toJson(mAccounts)).commit();

                                // SharePreferenceUtil.getInstance().saveString("userId",result.getRows().get(0).get(0)+"").commit();
                                SharePreferenceUtil.getInstance().saveString("userId",result.getMsg().getUserid()+"").commit();
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toaster.showMsg(result.getValue());
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            super.onError(e);
                            mProgressDilog.dismiss();
                        }
                    });

    }


    //选择弹窗
    private void showPop() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_account)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(1f)//取值范围0.0f-1.0f 值越小越暗
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAsDropDown2(popupWindow, etAccount, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_account:
                RecyclerView rc_zh = view.findViewById(R.id.rc_zh);
                rc_zh.setLayoutManager(new LinearLayoutManager(this));
                rc_zh.setAdapter(new CommonRecyclerAdapter<HomeBean>(this, mAccounts, R.layout.item_zh) {
                    @Override
                    public void convert(ViewHolder holder, HomeBean item) {
                        holder.setText(R.id.tv_zh, item.getName());
                        holder.setOnIntemClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etAccount.setText(item.getName());
                                etAccount.setSelection(item.getName().length());
                                if(isPwdRemeber){
                                    etPwd.setText(item.getPwd());
                                }else {
                                    etPwd.setText("");
                                }
                                popupWindow.dismiss();
                            }
                        });
                    }

                });
                break;
        }
    }

}
