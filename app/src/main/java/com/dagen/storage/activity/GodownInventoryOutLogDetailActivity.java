package com.dagen.storage.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.dagen.storage.bean.OutLogInfoBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnScanFinishListener;
import com.dagen.storage.support.OnScanFinishListener2;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.utils.AppUtils;
import com.dagen.storage.utils.ImageLoader;
import com.dagen.storage.utils.Toaster;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;
import com.wanlv365.lawyer.baselibrary.view.dialog.AlertDialog;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//出库下架单详情页
public class GodownInventoryOutLogDetailActivity extends BaseMoudleActivity {
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
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    @BindView(R.id.et_scam)
    EditText etScam;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.tv_expand)
    TextView tvExpand;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_smtm)
    LinearLayout llSmtm;
    @BindView(R.id.ll_tm)
    LinearLayout llTm;
    @BindView(R.id.ll_jycw)
    LinearLayout llJycw;
    @BindView(R.id.ll_sl)
    LinearLayout llSl;
    @BindView(R.id.rl_expand)
    RelativeLayout rlExpand;
    @BindView(R.id.tv_tm)
    TextView tvTm;
    @BindView(R.id.tv_cw)
    TextView tvCw;
    @BindView(R.id.tv_sl)
    TextView tvSl;
    @BindView(R.id.tv_zdzjs)
    TextView tvZdzjs;
    @BindView(R.id.tv_zdzks)
    TextView tvZdzks;
    @BindView(R.id.tv_bz)
    TextView tvBz;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_zdjsl)
    TextView tvZdjsl;
    @BindView(R.id.tv_zsmsl)
    TextView tvZsmsl;

    private List<OutLogInfoBean.ItemBean> mBeans = new ArrayList<>();
    private OutLogInfoBean.MsgBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_godown_inventory_outlog_detail;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("详情");

        etScam.requestFocus();

        setEditextFilter(etScam, new OnScanFinishListener() {
            @Override
            public void onScanFinish(String content) {

                if(bean==null)return;

                if(content.equals(bean.getJyalias())){

                    /*if(bean.getQty()==0){
                        Toaster.showMsg("下架数量为0");
                        return;
                    }*/
                    if(bean.getQtyplan()==1) {
                        mProgressDilog.show();
                        insert(content,bean.getJycw(),bean.getQty());
                    }else {
                        showPopup(content,bean.getQtyplan()-bean.getQty());
                    }
                }else {
                    Toaster.showMsg("扫描条码与建议条码不一致！");
                }

            }
        });


        rcView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        rcView.setHasFixedSize(true);
        rcView.setNestedScrollingEnabled(false);
        rcView.setAdapter(new CommonRecyclerAdapter<OutLogInfoBean.ItemBean>(this, mBeans, R.layout.item_ckxjd_detail) {
            @Override
            public void convert(ViewHolder holder, OutLogInfoBean.ItemBean item) {
                if (holder.getAdapterPosition() != 0) {
                    holder.setText(R.id.tv_xh, (holder.getAdapterPosition()) + "");
                    holder.setText(R.id.tv_cw, item.getCw());
                    holder.setText(R.id.tv_tm, item.getAlias());
                    holder.setText(R.id.tv_djsl, item.getQtyplan() + "");
                    holder.setText(R.id.tv_xjsl, item.getQty() + "");
                }
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
        data.put("method", "pdacw_getdocnoview");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<OutLogInfoBean>() {
                    @Override
                    public void onSuccess(OutLogInfoBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            mBeans.add(new OutLogInfoBean.ItemBean());
                            mBeans.addAll(result.getItem());
                            rcView.getAdapter().notifyDataSetChanged();
                            setData(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }


    //插入明细
    private void insert(String tm,String cw,int sl) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("alias", tm);
        params.put("cw", cw);
        params.put("qty", sl);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getdocnoscan");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                     //   mProgressDilog.dismiss();
                        etScam.setText("");
                        if (result.getCode() == 200) {
                           Toaster.showMsg("插入成功！");
                           playSoundAndVirate();
                         /*  mBeans.clear();
                           quest();*/
                         nextCode();
                        }else {
                            mProgressDilog.dismiss();
                            Toaster.showMsg(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }

    //下一个条码
    private void nextCode() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_nextalias");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        //   mProgressDilog.dismiss();
                        mBeans.clear();
                        quest();
                        if (result.getCode() == 200) {

                        }else {
                            mProgressDilog.dismiss();
                            Toaster.showMsg(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }

    //提交
    private void commit() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_submit");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        mProgressDilog.dismiss();
                        etScam.setText("");
                        if (result.getCode() == 200) {
                          /*  mBeans.clear();
                            quest();*/
                            Toaster.showMsg("提交成功！");
                            setResult(RESULT_OK);
                            finish();
                        }else {
                            showSureDialog(result.getMsg(), new OnSureClickListener() {
                                @Override
                                public void onSure() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }


    //设置数据
    private void setData(OutLogInfoBean.MsgBean msg) {
        bean=msg;
        llSmtm.setVisibility(msg.getRow1().equals("visible") ? View.VISIBLE : View.GONE);
        llTm.setVisibility(msg.getRow2().equals("visible") ? View.VISIBLE : View.GONE);
        llJycw.setVisibility(msg.getRow3().equals("visible") ? View.VISIBLE : View.GONE);
        llSl.setVisibility(msg.getRow4().equals("visible") ? View.VISIBLE : View.GONE);

        tvTm.setText(msg.getJyalias());
        tvCw.setText(msg.getJycw());
        tvSl.setText(msg.getQty() + "");
        tvZdzjs.setText(msg.getQtyplan() + "");
        tvZdzks.setText(msg.getTotqty() + "");
        tvBz.setText(msg.getDescription());

        ImageLoader.loagImg(this, msg.getImg(), ivImg);

        int djTotal=0;
        int xjSl=0;

        for(int i=0;i<mBeans.size();i++){
            djTotal+=mBeans.get(i).getQtyplan();
            xjSl+=mBeans.get(i).getQty();
        }

        tvZdjsl.setText("总单据数量："+msg.getTotsoqty());
     //   tvZsmsl.setText("总扫描数量："+msg.getTotqty());
        tvZsmsl.setText("下架数量总和："+msg.getTotallqty());

    }

    //显示弹窗
    private void showPopup(String tm,int count) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.popup_ckxj)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.setText(R.id.tv_tm,tm);
        dialog.setText(R.id.tv_jycw,bean.getJycw());
        dialog.setText(R.id.et_xjsl,count+"");

        EditText etScan=dialog.getView(R.id.et_scam);
        EditText etSl=dialog.getView(R.id.et_xjsl);

       setEditextFilter(etScan, new OnScanFinishListener() {
           @Override
           public void onScanFinish(String content) {
               dialog.dismiss();
               mProgressDilog.show();
               insert(tm,etScan.getText().toString().trim(), Integer.parseInt(etSl.getText().toString().trim()));
           }
       });

        dialog.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etScan.getText().toString().trim())){
                    Toaster.showMsg("实际储位不能为空");
                    return;
                }

                if(!etScan.getText().toString().trim().equals(bean.getJycw())){
                    Toaster.showMsg("实际储位与建议储位不一致");
                    return;
                }
                dialog.dismiss();
                mProgressDilog.show();
                insert(tm,etScan.getText().toString().trim(), Integer.parseInt(etSl.getText().toString().trim()));
            }
        });

        dialog.show();
    }

    @OnClick({R.id.iv_common_left, R.id.rl_expand,R.id.tv_next,R.id.tv_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.tv_next:
                if(bean==null){
                    Toaster.showMsg("网络错误！");
                    return;
                }
                if(bean.getIfover().equals("Y")){
                   showSureDialog("单子已扫完，是否提交？", new OnSureClickListener() {
                       @Override
                       public void onSure() {
                           mProgressDilog.show();
                           commit();
                       }
                   });
                    return;
                }
                mProgressDilog.show();
                nextCode();
                break;
            case R.id.tv_sure:

                if(mBeans.size()==1){
                    Toaster.showMsg("请先插入再提交");
                    return;
                }

               /* if((mBeans.size()-1)!=bean.getTotqty()){
                    Toaster.showMsg("请全部扫描完再提交");
                    return;
                }*/
                mProgressDilog.show();
               commit();
                break;
            case R.id.rl_expand:
                if (llMore.getVisibility() == View.GONE) {
                    changeExpand(true,"收起信息",R.drawable.sj_up_blue);
                } else {
                    changeExpand(false, "更多信息", R.drawable.sj_down_blue);
                }
                break;

        }
    }


    //折叠收起切换
    private void changeExpand(boolean visaible,String text,int resId){
        llMore.setVisibility(visaible?View.VISIBLE:View.GONE);
        tvExpand.setText(text);

        Drawable drawableLeft = getResources().getDrawable(
                resId);

        tvExpand.setCompoundDrawablesWithIntrinsicBounds(null,

                null, drawableLeft, null);
    }


}
