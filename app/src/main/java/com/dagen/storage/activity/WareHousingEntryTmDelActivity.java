package com.dagen.storage.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.bean.HomeBean;
import com.dagen.storage.bean.MatrixBean;
import com.dagen.storage.bean.TmBean;
import com.dagen.storage.bean.WareHousingInsetBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.utils.Toaster;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//入库单 条码删除
public class WareHousingEntryTmDelActivity extends BaseMoudleActivity {


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
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.rc_view)
    RecyclerView rcView;

    private List<TmBean.MsgBean> mBeans = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_warehousing_entry_tm_del;
    }

    @Override
    public void initData() {
        super.initData();
        if(getIntent().getStringExtra("from")!=null){
            tvCommonTitle.setText("按款号删除");
        }else {
            tvCommonTitle.setText("按条码删除");
        }

        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(new CommonRecyclerAdapter<TmBean.MsgBean>(this, mBeans, R.layout.item_tm_del) {
            @Override
            public void convert(ViewHolder holder, TmBean.MsgBean item) {
                        holder.setText(R.id.tv_tm,item.getNo());
                        holder.setText(R.id.tv_sl,"数量:"+item.getQty());
                        holder.setImageResource(R.id.iv_sel,item.isCheck()?R.drawable.blue_check:R.drawable.blue_uncheck);
                        holder.setOnIntemClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item.setCheck(!item.isCheck());
                                rcView.getAdapter().notifyDataSetChanged();
                            }
                        });
            }

        });

        mProgressDilog.show();
        quest();

    }

    private void quest() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", getIntent().getIntExtra("tableid",0));
        params.put("rowid", getIntent().getIntExtra("id",0));
        params.put("alias", "");
        if(getIntent().getStringExtra("from")!=null) {
            params.put("pdt", "");
        }else {
            params.put("alias", "");
        }
        Map<String, Object> data = new HashMap<>();
        if(getIntent().getStringExtra("from")!=null) {
            data.put("method", "boxno_getpdtitem_ard");
        }else {
            data.put("method", "boxno_getaliasitem_ard");
        }
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<TmBean>() {
                    @Override
                    public void onSuccess(TmBean result) {
                        mProgressDilog.dismiss();
                        if(result.getCode()==200&&result.getSucceed()==0){
                            mBeans.addAll(result.getMsg());
                            rcView.getAdapter().notifyDataSetChanged();
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

    @OnClick({R.id.iv_common_left,R.id.tv_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.tv_sure:
                StringBuilder builder=new StringBuilder();
                for(TmBean.MsgBean bean:mBeans){
                    if(bean.isCheck()){
                        builder.append(bean.getNo()).append(",");
                    }
                }
                if(builder.length()<1){
                    Toaster.showMsg("请至少选择一项！");
                    return;
                }
                 mProgressDilog.show();
                 delForTm(builder.substring(0,builder.length()-1));
                break;

        }
    }

    private void delForTm(String tm) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", getIntent().getIntExtra("tableid",0));
        params.put("rowid", getIntent().getIntExtra("id",0));
        if(getIntent().getStringExtra("from")!=null) {
            params.put("pdt", tm);
        }else {
            params.put("alias", tm);
        }
        Map<String, Object> data = new HashMap<>();
        if(getIntent().getStringExtra("from")!=null) {
            data.put("method", "boxno_deletepdtitem_ard");
        }else {
            data.put("method", "boxno_deletealiasitem_ard");
        }
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        mProgressDilog.dismiss();
                        if(result.getCode()==200&&result.getSucceed()==0){
                           setResult(RESULT_OK);
                           finish();
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

}
