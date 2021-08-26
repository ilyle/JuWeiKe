package com.dagen.storage.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dagen.storage.R;
import com.dagen.storage.utils.DisplayUtils;


public class UpdateProgressDialog extends Dialog {

    private TextView tvProgress;
    private ProgressBar progressBar;

    public UpdateProgressDialog(@NonNull Context context) {
        super(context, R.style.UpdateDialog);
        setCanceledOnTouchOutside(false);
        View view= LayoutInflater.from(context).inflate(R.layout.update_progress_dialog,null);
        setContentView(view);
        initView(view);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = DisplayUtils.getScreenWidth(context);
        getWindow().setAttributes(params);

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
        setCancelable(false);

    }

    private void initView(View view) {
        tvProgress=(TextView) view.findViewById(R.id.tv_progress);
        progressBar=(ProgressBar) view.findViewById(R.id.upgrade_progressbar);
    }


    public void setProgress(int arg1) {
        tvProgress.setText(arg1+"%");
        progressBar.setProgress(arg1);
    }
}
