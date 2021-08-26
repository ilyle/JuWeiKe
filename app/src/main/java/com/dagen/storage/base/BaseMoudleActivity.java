package com.dagen.storage.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dagen.storage.LoginActivity;
import com.dagen.storage.MainActivity;
import com.dagen.storage.R;
import com.dagen.storage.SplashActivity;
import com.dagen.storage.activity.GodownInventoryOutLogHomeActivity;
import com.dagen.storage.support.NewlineFilter;
import com.dagen.storage.support.OnPickerSelectListener;
import com.dagen.storage.support.OnScanFinishListener;
import com.dagen.storage.support.OnScanFinishListener2;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.utils.AppUtils;
import com.dagen.storage.utils.Endecrypt;
import com.dagen.storage.utils.KeyboardUtils;
import com.dagen.storage.utils.PickViewUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wanlv365.lawyer.baselibrary.base.BaseActivity;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.dialog.AlertDialog;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.functions.Consumer;

public class BaseMoudleActivity extends BaseActivity {

    public OptionsPickerView pvCustomOptions;
    public TimePickerView mTimePicker;

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    //设置输入框的过滤及输入完成回调
    public void setEditextFilter(EditText et, OnScanFinishListener listener) {

        NewlineFilter filter = new NewlineFilter();
        et.setFilters(new InputFilter[]{filter});

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (listener != null) listener.onScanFinish(et.getText().toString().trim());
                    return true;
                }

                if (i == 6) {
                    KeyboardUtils.hideSoftInput(BaseMoudleActivity.this);
                    if (listener != null) listener.onScanFinish(et.getText().toString().trim());
                    return true;
                }

                return false;
            }
        });
    }

    public void setEditextFilter(EditText et, OnScanFinishListener2 listener) {
        boolean isScan = et.getText().toString().contains("\n");

        NewlineFilter filter = new NewlineFilter();
        et.setFilters(new InputFilter[]{filter});

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (listener != null) listener.onScanFinish(et.getText().toString().trim());
                    return true;
                }

                if (i == 6) {
                    KeyboardUtils.hideSoftInput(BaseMoudleActivity.this);
                    if (listener != null) listener.onInputFinish(et.getText().toString().trim());
                    return true;
                }

                return false;
            }
        });
    }

    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(new Gson().fromJson(elem, cls));
        }
        return list;
    }

    public void showSureDialog(String content, OnSureClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.dialog_commit)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        dialog.setText(R.id.tv_content, content);

        dialog.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) listener.onSure();
            }
        });

        dialog.show();
    }

    public void showSingleSureDialog(String content, OnSureClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.dialog_commit_single)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        dialog.setText(R.id.tv_content, content);

        dialog.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) listener.onSure();
            }
        });

        dialog.show();
    }


    public void playSoundAndVirate() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Vibrator vib = (Vibrator) BaseMoudleActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(500);

                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//系统自带提示音
                Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
                rt.play();

            }
        }, 100);


    }


    //初始化时间选择器
    public void initTimePick() {
        PickViewUtils mPickViewUtils = new PickViewUtils();
        mTimePicker = mPickViewUtils.initYearMonthDayPicker(this);
        mPickViewUtils.setTimePickListener(new PickViewUtils.TimePickListener() {
            @Override
            public void onTimeSel(String year) {
                onTimeSelect(year);
            }
        });
    }

    //子类复写此方法获取选中的时间
    public void onTimeSelect(String time) {
    }


    @SuppressLint("MissingPermission")
    public String getDevideId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String uniqueId = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            uniqueId = tm.getImei();


        } else {
            uniqueId = tm.getDeviceId();
        }

        if (TextUtils.isEmpty(uniqueId)) {
            uniqueId = getIMEINew(this);
        }

        return uniqueId;
    }

    /**
     * Pseudo-Unique ID, 这个在任何Android手机中都有效 解决手机中IMEI获取不到情况，兼容所有手机
     */
    public static String getIMEINew(Context context) {
        //we make this look like a valid IMEI
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }


    //sip_sign = MD5(sip_appkey + sip_timestamp + MD5(appSecret))
    //生成系统级参数
    public String createSystemParamers(String appkey, String secret) {
        SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        a.setLenient(false);
        String tt = a.format(new Date());

        String sip_sign = Endecrypt.MD5(appkey + tt + Endecrypt.MD5(secret));

        StringBuilder builder = new StringBuilder();
        builder.append("sip_appkey=")
                .append(Endecrypt.getEncodeStr(appkey))
                .append("&sip_timestamp=")
                .append(Endecrypt.getEncodeStr(tt))
                .append("&sip_sign=")
                .append(Endecrypt.getEncodeStr(sip_sign));
        Log.i("tag", "系统级参数==" + builder.toString());
        return builder.toString();
    }

    public void initCustomOptionPicker(OnPickerSelectListener listener) {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (listener != null) listener.onPickerSelect(options1, option2, options3);
            }
        })
                .setLayoutRes(R.layout.item_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });


                    }
                })
                .setContentTextSize(18)
                .isDialog(false)
                .build();
    }


}
