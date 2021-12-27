package com.dagen.storage.base;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dagen.storage.R;
import com.dagen.storage.bean.OutLogInfoBean;
import com.dagen.storage.support.NewlineFilter;
import com.dagen.storage.support.OnModifyClickListener;
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
import com.wanlv365.lawyer.baselibrary.base.BaseActivity;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.dialog.AlertDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseMoudleActivity extends BaseActivity {

    public OptionsPickerView pvCustomOptions;
    public TimePickerView mTimePicker;
    public MediaPlayer mMediaPlayer = new MediaPlayer();


    public int userid = 0;
    public int tableid = 0;
    public int rowid = 0;

    @Override
    public void initData() {
        String useridStr = SharePreferenceUtil.getInstance().getString("userId");
        if (!TextUtils.isEmpty(useridStr)) {
            userid = Integer.parseInt(useridStr);
        }
        String tableidStr = getIntent().getStringExtra("tableid");
        if (!TextUtils.isEmpty(tableidStr)) {
            tableid = Integer.parseInt(getIntent().getStringExtra("tableid"));
        }
        rowid = getIntent().getIntExtra("id", 0);
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
                if (keyEvent != null && i==0 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
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

        /*et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && keyCode==KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (listener != null) listener.onScanFinish(et.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });*/
    }

    public void setEditextFilter(EditText et, OnScanFinishListener2 listener) {
        boolean isScan = et.getText().toString().contains("\n");

        NewlineFilter filter = new NewlineFilter();
        et.setFilters(new InputFilter[]{filter});

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                /*if (keyEvent != null && i==0 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (listener != null) listener.onScanFinish(et.getText().toString().trim());
                    return true;
                }*/

                if (i == 6) {
                    KeyboardUtils.hideSoftInput(BaseMoudleActivity.this);
                    if (listener != null) listener.onInputFinish(et.getText().toString().trim());
                    return true;
                }

                return false;
            }
        });

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && keyCode==KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (listener != null) listener.onScanFinish(et.getText().toString().trim());
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

    /**
     * 显示修改明细对话框
     */
    public void showItemModifyDialog(OutLogInfoBean.ItemBean item, OnModifyClickListener modifyListener, OnSureClickListener deleteListerner) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.popup_item_modify)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.setText(R.id.tv_name1, item.getName1());
        dialog.setText(R.id.tv_name2, item.getName2());
        dialog.setText(R.id.tv_name3, item.getName3());
        dialog.setText(R.id.tv_value1, item.getValue1());
        dialog.setText(R.id.tv_value2, item.getValue2());
        dialog.setText(R.id.tv_value3, item.getValue3());

        // 获取焦点
        EditText et = dialog.getView(R.id.tv_value3);
        et.postDelayed(() -> {
            et.requestFocus();
            et.setSelectAllOnFocus(true);
            et.selectAll();
        }, 500);

        dialog.setOnClickListener(R.id.tv_modify, view -> {
            dialog.dismiss();
            if (modifyListener != null) {
                int qty = Integer.parseInt(et.getText().toString().trim());
                modifyListener.onSure(qty);
            }
        });
        dialog.setOnClickListener(R.id.tv_delete, view -> {
            dialog.dismiss();
            if (deleteListerner != null) deleteListerner.onSure();
        });
        dialog.show();
    }

    /**
     * 显示错误提示框
     */
    public void showErrorTipsDialog(String content, OnSureClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.dialog_commit_single)
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

        // 播放错误提示声
        playFailedTips();
    }

    public void playSuccessTips() {
        try {
            AssetFileDescriptor fd = getAssets().openFd("success.mp3");
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playFailedTips() {
        try {
            AssetFileDescriptor fd = getAssets().openFd("failed.mp3");
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
