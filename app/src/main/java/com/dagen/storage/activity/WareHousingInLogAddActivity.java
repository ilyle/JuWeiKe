package com.dagen.storage.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.bean.OutLogBean;
import com.dagen.storage.bean.QueryArrBean;
import com.dagen.storage.bean.ScreenBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnPickerSelectListener;
import com.dagen.storage.support.OnSureClickListener;
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
import butterknife.OnClick;

//入库单新增
public class WareHousingInLogAddActivity extends BaseMoudleActivity {


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
    private List<QueryArrBean>mQueryArrs=new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_screen_out;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("新增");

        rcSx.setLayoutManager(new LinearLayoutManager(this));
        rcSx.setAdapter(new CommonRecyclerAdapter<ScreenBean.MsgBean>(this,mBeans,R.layout.item_screen_out) {
            @Override
            public void convert(ViewHolder holder, ScreenBean.MsgBean item) {
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
                     // holder.setText(R.id.tv_content,item.getValue());

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
                      mBeans.get(pos).setSelectName(mBeans.get(pos).getSelectarr().get(options1).getName());
                      mBeans.get(pos).setSelectValue(mBeans.get(pos).getSelectarr().get(options1).getValue());
                      rcSx.getAdapter().notifyDataSetChanged();

            }
        });

        mProgressDilog.show();
        quest();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mBeans.clear();
        quest();
    }

    private void quest() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_newdocnquery");
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
                                 if(mBeans.get(i).getType().equals("select")){
                                     if(mBeans.get(i).getSelectarr().size()==0)return;
                                     mBeans.get(i).setSelectName(mBeans.get(i).getSelectarr().get(0).getName());
                                     mBeans.get(i).setSelectValue(mBeans.get(i).getSelectarr().get(0).getValue());
                                 }
                             }

                             rcSx.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                        showErrorTipsDialog(e.getMessage(),null);
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
                mProgressDilog.show();
                add();
                break;

        }
    }

    private void add() {
        mQueryArrs.clear();
        for(int i=0;i<mBeans.size();i++){
            if(mBeans.get(i).getType().equals("select")){
                mQueryArrs.add(new QueryArrBean(mBeans.get(i).getDname(), mBeans.get(i).getSelectValue()));
            }else {
                mQueryArrs.add(new QueryArrBean(mBeans.get(i).getDname(), mBeans.get(i).getValue()));
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("docno", "");
        params.put("queryarr", mQueryArrs);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_adddocno");
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
                              if(result.getSucceed()==0){
                                  Intent intent=null;
                                  if(getIntent().getStringExtra("tableid").equals("24408")) {
                                      intent = new Intent(WareHousingInLogAddActivity.this, WareHousingEntryInLogDetailActivity.class);
                                  }else if(getIntent().getStringExtra("tableid").equals("24404")){
                                      intent = new Intent(WareHousingInLogAddActivity.this, WareHousingEntryMoveDetailActivity.class);


                                  }else if(getIntent().getStringExtra("tableid").equals("24426")){
                                  intent = new Intent(WareHousingInLogAddActivity.this, WareHousingEntryCheckDetailActivity.class);

                                  }else if(getIntent().getStringExtra("tableid").equals("24444")){
                                  intent = new Intent(WareHousingInLogAddActivity.this, WareHousingEntryCheckDetailActivity.class);
                                  }

                                  else {
                                      intent = new Intent(WareHousingInLogAddActivity.this, WareHousingEntryInLogDetailActivity.class);
                                  }
                                  intent.putExtra("id", Integer.parseInt(result.getValue()));
                                  intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                                  startActivityForResult(intent, 500);
                                 // setResult(RESULT_OK);
                                //  finish();
                              }else {
                                  showErrorTipsDialog(result.getValue(), null);
                              }
                        }else {
                           showErrorTipsDialog(result.getValue(), null);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                        showErrorTipsDialog(e.getMessage(), null);
                    }
                });
    }


}
