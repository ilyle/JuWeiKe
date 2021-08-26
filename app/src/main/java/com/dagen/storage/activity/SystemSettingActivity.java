package com.dagen.storage.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.update.UpdateManager;
import com.dagen.storage.utils.Toaster;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//系统设置
public class SystemSettingActivity extends BaseMoudleActivity {


    @BindView(R.id.iv_common_left)
    ImageView ivCommonLeft;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.iv_common_right)
    ImageView ivCommonRight;
    @BindView(R.id.tv_common_right)
    TextView tvCommonRight;
    @BindView(R.id.rl_common_title)
    RelativeLayout rlCommonTitle;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    @BindView(R.id.tv_sbbs)
    TextView tvSbbs;
    @BindView(R.id.et_erp)
    EditText etErp;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_bbh)
    TextView tvBbh;

    private boolean isOpen;

    @Override
    public int getLayoutId() {
        return R.layout.activity_system_setting;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("系统设置");
        tvSbbs.setText(getDevideId());

        if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance().getString("url"))) {
            etErp.setText(SharePreferenceUtil.getInstance().getString("url"));
            etErp.setSelection(etErp.getText().length());
        }

        isOpen = SharePreferenceUtil.getInstance().getBoolean("autoUpdate", false);
        ivSwitch.setImageResource(isOpen ? R.drawable.cb_check : R.drawable.cb_uncheck);


        tvBbh.setText("V" + UpdateManager.getInatance().getVersionName(this));
    }

    @OnClick({R.id.iv_switch, R.id.iv_common_left, R.id.tv_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.iv_switch:
                isOpen = !isOpen;
                ivSwitch.setImageResource(isOpen ? R.drawable.cb_check : R.drawable.cb_uncheck);
                SharePreferenceUtil.getInstance().saveBoolean("autoUpdate", isOpen).commit();
                break;
            case R.id.tv_sure:
                if (TextUtils.isEmpty(etErp.getText().toString().trim())) {
                    Toaster.showMsg("请输入ERP地址");
                    return;
                }
                mProgressDilog.show();
              //  Contasts.BASE_URL = "http://" + etErp.getText().toString().trim() + "/MDInfaceSystem96cs/servlet/doserverdata";
                Contasts.BASE_URL = etErp.getText().toString().trim();

                try {
                    checkUrl();

                } catch (Exception e) {
                    mProgressDilog.dismiss();
                    if (e instanceof IllegalArgumentException) {
                        Toaster.showMsg("无效地址！");
                    }
                }

                break;

        }
    }


    //检查erp地址是否有效
    private void checkUrl() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_test");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            SharePreferenceUtil.getInstance().saveString("url", etErp.getText().toString().trim()).commit();
                            finish();
                        } else {
                            Toaster.showMsg(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                        Toaster.showMsg("无效的erp地址，请检查！");
                    }
                });
    }

}
