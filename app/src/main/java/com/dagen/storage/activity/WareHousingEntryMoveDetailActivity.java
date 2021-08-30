package com.dagen.storage.activity;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.bean.OutLogInfoBean;
import com.dagen.storage.bean.WareHousingEntryBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnScanFinishListener;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.utils.AppUtils;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

//移库单详情页
public class WareHousingEntryMoveDetailActivity extends BaseMoudleActivity {


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
    @BindView(R.id.rl_expand)
    RelativeLayout rlExpand;
    @BindView(R.id.tv_zsmsl)
    TextView tvZsmsl;

    private List<OutLogInfoBean.ItemBean> mBeans = new ArrayList<>();
    private OutLogInfoBean.MsgBean bean;
    private String transfertype = "out";//in/out(上架转移，下架转移)
    private int lock_status = 1;// 1：下架中，2：上架中

    @Override
    public int getLayoutId() {
        return R.layout.activity_warehousing_entry_detail;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("详情");

        etScam.requestFocus();

        tvSure.setText(lock_status == 1 ? "下架完成" : "上架完成");

        setEditextFilter(etScam, new OnScanFinishListener() {
            @Override
            public void onScanFinish(String content) {
                showPopup(content);
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
        rcView.setAdapter(new CommonRecyclerAdapter<OutLogInfoBean.ItemBean>(this, mBeans, R.layout.item_rksj_detail) {
            @Override
            public void convert(ViewHolder holder, OutLogInfoBean.ItemBean item) {
                if (holder.getAdapterPosition() != 0) {
                    holder.setText(R.id.tv_xh, (holder.getAdapterPosition()) + "");
                    holder.setText(R.id.tv_cw, item.getCw());
                    holder.setText(R.id.tv_tm, item.getAlias());
                    holder.setText(R.id.tv_djsl, item.getQtyplan() + "");
                    holder.setText(R.id.tv_xjsl, item.getQty() + "");
                } else {
                    holder.setText(R.id.tv_xjsl, lock_status == 1 ? "下架数量" : "上架数量");
                }

                holder.setOnIntemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showModifyPopup(item);
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


                            lock_status = result.getMsg().getLock_status();

                            tvSure.setText(lock_status == 1 ? "下架完成" : "上架完成");

                            int count = 0;

                            if (lock_status == 1) {
                                tvZsmsl.setText("下架数量总和：" + count);
                            } else {
                                tvZsmsl.setText("上架数量总和：" + count);
                            }

                            for (int i = 0; i < result.getItem().size(); i++) {
                                count += result.getItem().get(i).getQty();
                            }

                            if (lock_status == 1) {
                                tvZsmsl.setText("下架数量总和：" + count);
                            } else {
                                tvZsmsl.setText("上架数量总和：" + count);
                            }


                            mBeans.add(new OutLogInfoBean.ItemBean());
                            mBeans.addAll(result.getItem());
                            rcView.getAdapter().notifyDataSetChanged();

                            bean = result.getMsg();
                            // setData(result.getMsg());
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


    //插入明细
    private void insert(String tm, String cw, int sl) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("alias", tm);
        params.put("cw", cw);
        params.put("qty", sl);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getdocnoscan");
        data.put("transfertype", transfertype);
        //  data.put("params--userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        //  data.put("params--code", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        etScam.setText("");
                        etScam.postDelayed(() -> {
                            etScam.requestFocus();
                        }, 500);
                        //   mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            Toaster.showMsg("插入成功！");
                            playSuccessTips();
                            mBeans.clear();
                            quest();
                        } else {
                            mProgressDilog.dismiss();
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

    //获取储位
    private void getCw(String tm, TextView tv) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("alias", tm);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getupjycw");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        //   mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            tv.setText(result.getMsg());
                        } else {
                            mProgressDilog.dismiss();
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

    //下架完成
    private void lock() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_lock");
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

                            transfertype = "in";

                            mBeans.clear();
                            quest();
                            Toaster.showMsg("提交成功！");

                            //finish();
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

    private void showPopup(String tm) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.popup_rksj)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.setText(R.id.tv_tm, tm);
        EditText etScan = dialog.getView(R.id.et_scam);
        EditText etSl = dialog.getView(R.id.et_sjsl);
        TextView tvCw = dialog.getView(R.id.tv_jycw);
        TextView tv_sl_title = dialog.getView(R.id.tv_sl_title);
        LinearLayout ll_jycw = dialog.getView(R.id.ll_jycw);


        etSl.setSelection(etSl.getText().length());

        if (getIntent().getStringExtra("tableid").equals("24404")) {
            ll_jycw.setVisibility(View.GONE);
            tv_sl_title.setText("数量: ");
        } else {
            getCw(tm, tvCw);
        }

        dialog.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(TextUtils.isEmpty(tvCw.getText().toString().trim())){
                    getCw(tm, tvCw);
                    return;
                }*/
                if (TextUtils.isEmpty(etSl.getText().toString().trim())) {
                    Toaster.showMsg("数量不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etScan.getText().toString().trim())) {
                    Toaster.showMsg("实际储位不能为空");
                    return;
                }

               /* if(!etScan.getText().toString().trim().equals(tvCw.getText().toString().trim())){
                    Toaster.showMsg("实际储位与建议储位不一致");
                    return;
                }*/
                dialog.dismiss();
                mProgressDilog.show();
                insert(tm, etScan.getText().toString().trim(), Integer.parseInt(etSl.getText().toString().trim()));
            }
        });
        dialog.show();
    }


    private void showModifyPopup(OutLogInfoBean.ItemBean bean) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.popup_modify)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.setText(R.id.tv_kh, bean.getAlias());


        EditText etSl = dialog.getView(R.id.et_sl);
        etSl.setText(bean.getQty() + "");
        etSl.setSelection(etSl.length());

        dialog.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSl.getText().toString().trim())) {
                    Toaster.showMsg("请输入数量！");
                    return;
                }
                dialog.dismiss();
                mProgressDilog.show();
                modify(bean.getAlias(), Integer.parseInt(etSl.getText().toString().trim()));
            }
        });

        dialog.show();
    }

    //修改明细数量
    private void modify(String id, int qty) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("itemid", id);
        params.put("qty", qty);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "boxno_updateqty_ard");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<CommBean>() {
                    @Override
                    public void onSuccess(CommBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200 && result.getSucceed() == 0) {
                            quest();
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

    @OnClick({R.id.iv_common_left, R.id.rl_expand, R.id.tv_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;

            case R.id.tv_sure:
                if (mBeans.size() == 0) {
                    Toaster.showMsg("请先插入再提交");
                    return;
                }
                mProgressDilog.show();
                if (lock_status == 1) {
                    lock();
                } else {
                    commit();
                }
                break;

            case R.id.rl_expand:
                if (llMore.getVisibility() == View.GONE) {
                    llMore.setVisibility(View.VISIBLE);
                    tvExpand.setText("收起信息");

                    Drawable drawableLeft = getResources().getDrawable(
                            R.drawable.sj_up_blue);

                    tvExpand.setCompoundDrawablesWithIntrinsicBounds(null,

                            null, drawableLeft, null);

                } else {
                    llMore.setVisibility(View.GONE);
                    tvExpand.setText("更多信息");

                    Drawable drawableLeft = getResources().getDrawable(
                            R.drawable.sj_down_blue);

                    tvExpand.setCompoundDrawablesWithIntrinsicBounds(null,

                            null, drawableLeft, null);
                }
                break;

        }
    }


}
