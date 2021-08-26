package com.dagen.storage.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.dagen.storage.R;
import com.dagen.storage.utils.DisplayUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;


//更新版本dialog
public class UpdateDialog extends Dialog {

    private TextView tvVersionName,tvContent,tvCancel,tvUpdate;
    private View viewSx;
    private OnUpdateClickListener mOnUpdateClickListener;

    public UpdateDialog(@NonNull Context context) {
        this(context,false);
    }

    public UpdateDialog(@NonNull Context context, boolean force) {
        super(context, R.style.UpdateDialog);
        setCanceledOnTouchOutside(!force);
        View view= LayoutInflater.from(context).inflate(R.layout.update_dialog,null);
        setContentView(view);
        initView(view);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = DisplayUtils.getScreenWidth(context)- DensityUtil.dp2px(100);
        getWindow().setAttributes(params);


        if(force) {//强制更新
            OnKeyListener keylistener = new OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            setOnKeyListener(keylistener);

            tvCancel.setVisibility(View.GONE);
            viewSx.setVisibility(View.GONE);

        }

        setCancelable(!force);


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnUpdateClickListener!=null){
                    mOnUpdateClickListener.onUpdate();
                }
            }
        });

    }

    private void initView(View view) {
        tvVersionName=(TextView)view.findViewById(R.id.tv_update_versionname);
        tvContent=(TextView)view.findViewById(R.id.tv_update_content);
        tvCancel=(TextView)view.findViewById(R.id.tv_cancel);
        tvUpdate=(TextView)view.findViewById(R.id.tv_update);
        viewSx=view.findViewById(R.id.view_sx);

        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setVersionName(String versionName) {
        tvVersionName.setText(versionName);
    }

    public interface OnUpdateClickListener{
        void onUpdate();
    }

    public void setOnUpdateClickListener(OnUpdateClickListener mOnUpdateClickListener) {
        this.mOnUpdateClickListener = mOnUpdateClickListener;
    }
}
