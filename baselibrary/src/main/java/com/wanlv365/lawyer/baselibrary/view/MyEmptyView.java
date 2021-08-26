package com.wanlv365.lawyer.baselibrary.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.banzhi.statusmanager.interfaces.ViewLoader;
import com.wanlv365.lawyer.baselibrary.R;

public class MyEmptyView extends ViewLoader {
    private Context mContext;

    public MyEmptyView(Context context) {
        super(context);
        this.mContext=context;
    }

    @Override
    protected View createView() {
        return LayoutInflater.from(mContext).inflate(R.layout.empty_layout,null,false);

    }

    public static int dp2px(Context context, float value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,context.getResources().getDisplayMetrics());
    }
}
