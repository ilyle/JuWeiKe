package com.wanlv365.lawyer.baselibrary.base;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.banzhi.statusmanager.StatusManager;
import com.google.gson.Gson;
import com.wanlv365.lawyer.baselibrary.ActivityManager;
import com.wanlv365.lawyer.baselibrary.utils.DisplayUtils;
import com.wanlv365.lawyer.baselibrary.utils.StatusBarUtil;
import com.wanlv365.lawyer.baselibrary.view.LoadProgressDialog;
import com.wanlv365.lawyer.baselibrary.view.MyEmptyView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {


    private Unbinder unbinder;
    public StatusManager helper;

    public LoadProgressDialog mProgressDilog;//加载等待进度条

    public Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().attch(this);
        gson=new Gson();
        setContentView(getLayoutId());
        unbinder= ButterKnife.bind(this);
        if(mProgressDilog==null) {
            mProgressDilog = new LoadProgressDialog(this, "加载中");
        }

        helper = new StatusManager.Builder(this,getLayoutView())
                .setEmptyView(new MyEmptyView(this)).
                 build();

        helper.init(this);
        initData();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DisplayUtils.hideInputWhenTouchOtherView(this, ev, null);
        return super.dispatchTouchEvent(ev); }

    public abstract void initData();

    public abstract int getLayoutId();

    //获取子类的控件  展示空布局时用
    public  View getLayoutView(){
          return null;
    }

    //状态栏高度占位
    public void resetTittleHeight(TextView tv){
        ViewGroup.LayoutParams params=(ViewGroup.LayoutParams) tv.getLayoutParams();
        params.height= StatusBarUtil.getStatusBarHeight(this);
        tv.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivityManager.getInstance().detach(this);
    }

}
