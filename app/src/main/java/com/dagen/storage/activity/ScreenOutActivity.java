package com.dagen.storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.OutLogInfoBean;
import com.dagen.storage.bean.ScreenBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnPickerSelectListener;
import com.dagen.storage.utils.Toaster;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//筛选
public class ScreenOutActivity extends BaseMoudleActivity {


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
    @BindView(R.id.rc_sx)
    RecyclerView rcSx;

    private List<ScreenBean.MsgBean>mBeans=new ArrayList<>();
    private int pos;
    private int status;

    @Override
    public int getLayoutId() {
        return R.layout.activity_screen_out;
    }


    @Override
    public void onTimeSelect(String time) {
        super.onTimeSelect(time);
        if(status==1) {
            mBeans.get(pos).setStartTime(time);
        }
        if(status==2) {
            mBeans.get(pos).setEndTime(time);
        }

        mBeans.get(pos).setValue(mBeans.get(pos).getStartTime()+"-"+ mBeans.get(pos).getEndTime());

        rcSx.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void initData() {
        super.initData();

        initTimePick();

        tvCommonTitle.setText("筛选");

        rcSx.setLayoutManager(new LinearLayoutManager(this));
        rcSx.setAdapter(new CommonRecyclerAdapter<ScreenBean.MsgBean>(this,mBeans,R.layout.item_screen_out) {
            @Override
            public void convert(ViewHolder holder, ScreenBean.MsgBean item) {

                       holder.getView(R.id.ll_1).setVisibility(item.getType().equals("date")?View.GONE:View.VISIBLE);
                       holder.getView(R.id.ll_2).setVisibility(item.getType().equals("date")?View.VISIBLE:View.GONE);

                       holder.getView(R.id.iv_xl).setVisibility(item.getType().equals("select")?View.VISIBLE:View.GONE);
                       EditText et=holder.getView(R.id.tv_content) ;
                       TextView tvContent=holder.getView(R.id.tv_content2) ;
                       if(item.getType().equals("select")){
                           et.setVisibility(View.GONE);
                           tvContent.setVisibility(View.VISIBLE);
                           if(item.getSelectarr().size()>0)
                           holder.setText(R.id.tv_content2,item.getSelectName());
                       }else {
                           et.setVisibility(View.VISIBLE);
                           tvContent.setVisibility(View.GONE);
                           et.setEnabled(true);
                           holder.setText(R.id.tv_content,item.getValue());
                       }


                       holder.setText(R.id.et_content,item.getStartTime());
                       holder.setText(R.id.et_content2,item.getEndTime());



                       et.addTextChangedListener(new TextWatcher() {
                           @Override
                           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                           }

                           @Override
                           public void onTextChanged(CharSequence s, int start, int before, int count) {

                           }

                           @Override
                           public void afterTextChanged(Editable s) {
                                  mBeans.get(holder.getAdapterPosition()).setValue(s.toString());
                           }
                       });

                       holder.setText(R.id.tv_name,item.getName()+":");

                       holder.getView(R.id.tv_content2).setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if(!item.getType().equals("select"))return;
                               pos=holder.getAdapterPosition();
                               pvCustomOptions.setPicker(item.getSelectarr());
                               pvCustomOptions.show();
                           }
                       });
                       holder.getView(R.id.iv_xl).setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if(!item.getType().equals("select"))return;
                               pos=holder.getAdapterPosition();
                               pvCustomOptions.setPicker(item.getSelectarr());
                               pvCustomOptions.show();
                           }
                       });
                       holder.getView(R.id.iv_xl2).setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               pos=holder.getAdapterPosition();
                               status=1;
                               mTimePicker.show();
                           }
                       });
                       holder.getView(R.id.iv_xl3).setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               pos=holder.getAdapterPosition();
                               status=2;
                               mTimePicker.show();
                           }
                       });
                       holder.setOnIntemClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if(!item.getType().equals("select"))return;
                               pos=holder.getAdapterPosition();
                               pvCustomOptions.setPicker(item.getSelectarr());
                               pvCustomOptions.show();
                           }
                       });

            }

        });

        initCustomOptionPicker(new OnPickerSelectListener() {
            @Override
            public void onPickerSelect(int options1, int options2, int options3) {
                      mBeans.get(pos).setSelectValue(mBeans.get(pos).getSelectarr().get(options1).getValue());
                      mBeans.get(pos).setSelectName(mBeans.get(pos).getSelectarr().get(options1).getName());
                      rcSx.getAdapter().notifyDataSetChanged();
            }
        });

        mProgressDilog.show();
        quest();
    }

    private void quest() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getdocnquery");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<ScreenBean>() {
                    @Override
                    public void onSuccess(ScreenBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                             mBeans.addAll(result.getMsg());

                             for(int i=0;i<mBeans.size();i++){
                                 if(mBeans.get(i).getType().equals("date")){
                                     if(!TextUtils.isEmpty(mBeans.get(i).getValue())) {
                                         String[] dates = mBeans.get(i).getValue().split("-");
                                         if(dates.length>1){
                                             mBeans.get(i).setStartTime(dates[0]);
                                             mBeans.get(i).setEndTime(dates[1]);
                                         }
                                     }
                                 }else if(mBeans.get(i).getType().equals("select")){
                                     if(mBeans.get(i).getSelectarr().size()==0)return;
                                     mBeans.get(i).setSelectValue(mBeans.get(i).getSelectarr().get(0).getValue());
                                     mBeans.get(i).setSelectName(mBeans.get(i).getSelectarr().get(0).getName());
                                 }
                             }

                             rcSx.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }

    @OnClick({R.id.iv_common_left,R.id.tv_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.tv_sure:
                Intent intent=new Intent();
                intent.putExtra("data",(Serializable) mBeans);
                setResult(RESULT_OK,intent);
                finish();
                break;

        }
    }


}
