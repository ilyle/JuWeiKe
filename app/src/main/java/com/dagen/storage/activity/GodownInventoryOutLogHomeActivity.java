package com.dagen.storage.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.bean.HomeListBean;
import com.dagen.storage.bean.OutLogBean;
import com.dagen.storage.bean.QueryArrBean;
import com.dagen.storage.bean.ScreenBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnScanFinishListener;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.utils.Toaster;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

//出库下架单列表页
public class GodownInventoryOutLogHomeActivity extends BaseMoudleActivity {


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
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_sx)
    LinearLayout llSx;

    private List<OutLogBean.MsgBean> mBeans = new ArrayList<>();
    private List<HomeListBean.MsgBean> mLists = new ArrayList<>();
    private String docno = "";
    private List<QueryArrBean> mQueryArrs = new ArrayList<>();

    private List<ScreenBean.MsgBean> mScreens = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_godown_inventory_outlog_home;
    }

    @Override
    public void initData() {
        super.initData();

        if (getIntent().getStringExtra("isAdd").equals("Y")) {
            //tvCommonTitle.setText("入库上架单");
            ivCommonRight.setImageResource(R.drawable.add);
            ivCommonRight.setVisibility(View.VISIBLE);
        } else {
            ivCommonRight.setVisibility(View.GONE);
        }
        tvCommonTitle.setText(getIntent().getStringExtra("title"));


        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(new CommonRecyclerAdapter<HomeListBean.MsgBean>(this, mLists, R.layout.item_ckxjd_list_2) {
            @Override
            public void convert(ViewHolder holder, HomeListBean.MsgBean item) {

                holder.setText(R.id.tv_zjs, item.getTot_dbcname1() + "");
                holder.setText(R.id.tv_zks, item.getTot_dbcname2() + "");
                holder.setText(R.id.tv_czr, item.getTot_dbcname3());

                holder.setText(R.id.tv_zjs_title, item.getTot_dbname1() + "");
                holder.setText(R.id.tv_zks_title, item.getTot_dbname2() + "");
                holder.setText(R.id.tv_czr_title, item.getTot_dbname3());

                RecyclerView rc_mx = holder.getView(R.id.rc_mx);
                rc_mx.setLayoutManager(new LinearLayoutManager(GodownInventoryOutLogHomeActivity.this));
                rc_mx.setAdapter(new CommonRecyclerAdapter<HomeListBean.MsgBean.ListBean>(GodownInventoryOutLogHomeActivity.this, item.getList(), R.layout.item_mx) {
                    @Override
                    public void convert(ViewHolder holder, HomeListBean.MsgBean.ListBean items) {
                        holder.setText(R.id.tv_dh_title, items.getName());
                        holder.setText(R.id.tv_dh, items.getValue());
                        holder.setOnIntemClickListener(v -> onItemClick2(v, item));
                    }

                });

                holder.setOnIntemClickListener(v -> onItemClick2(v, item));
            }

        });


        setEditextFilter(etSearch, new OnScanFinishListener() {
            @Override
            public void onScanFinish(String content) {
                docno = content;
                mBeans.clear();
                rcView.getAdapter().notifyDataSetChanged();
                mProgressDilog.show();
                mLists.clear();
                quest();
            }
        });

        refreshLayout.setEnableLoadMore(false);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                docno = "";
                etSearch.setText("");
                mBeans.clear();
                mQueryArrs.clear();
                mLists.clear();
                quest();
            }
        });

        mProgressDilog.show();
        quest();
    }

    /* private void quest() {
         Map<String, Object> params = new HashMap<>();
         params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
         params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
         params.put("docno", docno);
         params.put("queryarr", mQueryArrs);
         Map<String, Object> data = new HashMap<>();
         data.put("method", "pdacw_getdocnolist");
         data.put("params", params);
         HttpUtils.with(this)
                 .url(Contasts.BASE_URL)
                 .doJsonPost()
                 .setJson(gson.toJson(data))
                 .execute(new HttpCallBack<OutLogBean>() {
                     @Override
                     public void onSuccess(OutLogBean result) {
                         refreshLayout.finishRefresh();
                         mProgressDilog.dismiss();
                         if (result.getCode() == 200) {
                             mBeans.addAll(result.getMsg());
                             rcView.getAdapter().notifyDataSetChanged();

                            *//* if(mBeans.size()==0){
                                helper.showEmpty();
                            }else {
                                helper.showContent();
                            }*//*

                        }else {
                            Toaster.showMsg(result.getValue());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        refreshLayout.finishRefresh();
                        mProgressDilog.dismiss();
                    }
                });
    }*/
    private void quest() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("docno", docno);
        params.put("queryarr", mQueryArrs);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getdocnolist_test");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<HomeListBean>() {
                    @Override
                    public void onSuccess(HomeListBean result) {
                        refreshLayout.finishRefresh();
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            mLists.addAll(result.getMsg());
                            rcView.getAdapter().notifyDataSetChanged();

                           /* if(mBeans.size()==0){
                                helper.showEmpty();
                            }else {
                                helper.showContent();
                            }*/

                        } else {
                            mLists.clear();
                            rcView.getAdapter().notifyDataSetChanged();
                            showErrorTipsDialog(result.getMsg().toString(), null);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mLists.clear();
                        rcView.getAdapter().notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                        mProgressDilog.dismiss();
                        showErrorTipsDialog(e.getMessage(), null);
                    }
                });
    }

    private void onItemClick2(View v, HomeListBean.MsgBean item) {
        switch (getIntent().getStringExtra("type")) {
            case "cwup":
            {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryInLogDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivityForResult(intent, 500);
                break;
            }
            case "cwdown":
            {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, GodownInventoryOutLogDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivityForResult(intent, 500);
                break;
            }
            case "cwmove":
            {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryMoveDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivityForResult(intent, 500);
                break;
            }
            case "cwinv":{
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryCheckDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivityForResult(intent, 500);
                break;
            }
            case "outin": {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivityForResult(intent, 500);
                break;
            }
        }
    }

    private void onItemClick(View v, HomeListBean.MsgBean item) {
        switch (getIntent().getStringExtra("tableid")) {
            case "24408": // 入库上架单
            case "24622": // 归还上架单
            case "24628": // 退货上架单
            {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryInLogDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                startActivityForResult(intent, 500);
                break;
            }
            case "24411": // 出库下架单
            case "24444": // 手动下架单
            {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, GodownInventoryOutLogDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                startActivityForResult(intent, 500);
                break;
            }
            case "24426": {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryCheckDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                startActivityForResult(intent, 500);
                break;
            }
            case "24404": {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryMoveDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                startActivityForResult(intent, 500);
                break;
            }
            case "15036": {
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                startActivityForResult(intent, 500);
                break;
            }
            default: {
//                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, GodownInventoryOutLogDetailActivity.class);
//                intent.putExtra("id", item.getId());
//                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
//                startActivityForResult(intent, 500);
                break;
            }
        }
    }

    @OnClick({R.id.iv_common_left, R.id.ll_sx, R.id.iv_common_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.iv_common_right:

                Intent intent1 = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingInLogAddActivity.class);
                intent1.putExtra("tableid", getIntent().getStringExtra("tableid"));
                intent1.putExtra("type", getIntent().getStringExtra("type"));
                startActivityForResult(intent1, 500);
                break;
            case R.id.ll_sx:
                Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, ScreenOutActivity.class);
                //  intent.putExtra("id",item.getId());
                intent.putExtra("id", "");
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivityForResult(intent, 600);
                break;

        }
    }


    private void questScreen() {
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
                        if (result.getCode() == 200) {
                            mScreens.addAll(result.getMsg());
                            for (int i = 0; i < mScreens.size(); i++) {
                                if (mScreens.get(i).getType().equals("select")) {
                                    if (mScreens.get(i).getSelectarr().size() == 0) return;
                                    mScreens.get(i).setSelectName(mScreens.get(i).getSelectarr().get(0).getName());
                                    mScreens.get(i).setSelectValue(mScreens.get(i).getSelectarr().get(0).getValue());
                                }
                            }

                            add();
                        } else {
                            showErrorTipsDialog(result.getMsg().toString(), null);
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


    private void add() {
        mQueryArrs.clear();
        for (int i = 0; i < mScreens.size(); i++) {
            if (mScreens.get(i).getType().equals("select")) {
                mQueryArrs.add(new QueryArrBean(mScreens.get(i).getDname(), mScreens.get(i).getSelectValue()));
            } else {
                mQueryArrs.add(new QueryArrBean(mScreens.get(i).getDname(), mScreens.get(i).getValue()));
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
                            if (result.getSucceed() == 0) {
                                Intent intent = null;
                                if (getIntent().getStringExtra("tableid").equals("24404")) {
                                    intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryMoveDetailActivity.class);
                                } else {
                                    intent = new Intent(GodownInventoryOutLogHomeActivity.this, WareHousingEntryCheckDetailActivity.class);
                                }
                                intent.putExtra("id", Integer.parseInt(result.getValue()));
                                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                                startActivityForResult(intent, 500);
                                // setResult(RESULT_OK);
                                //  finish();
                            } else {
                                showErrorTipsDialog(result.getMsg(), null);
                            }
                        } else {
                            showErrorTipsDialog(result.getMsg(), null);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        mBeans.clear();
        mLists.clear();
        quest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (500 == requestCode && RESULT_OK == resultCode) {
          /*  mBeans.clear();
            quest();*/
        }
        if (600 == requestCode && RESULT_OK == resultCode) {
            mProgressDilog.show();
            mQueryArrs.clear();
            List<ScreenBean.MsgBean> mScreens = (List<ScreenBean.MsgBean>) data.getSerializableExtra("data");
            for (ScreenBean.MsgBean bean : mScreens) {
                if (bean.getType().equals("select")) {
                    mQueryArrs.add(new QueryArrBean(bean.getDname(), bean.getSelectValue()));
                } else {
                    mQueryArrs.add(new QueryArrBean(bean.getDname(), bean.getValue()));
                }
            }
            mBeans.clear();
            mLists.clear();
            quest();
        }
    }
}
