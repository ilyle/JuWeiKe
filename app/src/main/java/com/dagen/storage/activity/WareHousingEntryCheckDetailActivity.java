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
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnScanFinishListener;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.utils.AppUtils;
import com.dagen.storage.utils.NetworkHelper;
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

//储位盘点单详情页
public class WareHousingEntryCheckDetailActivity extends BaseMoudleActivity {


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
    private String diffreason = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_warehousing_entry_detail;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("详情");

        etScam.requestFocus();

        if (getIntent().getStringExtra("tableid").equals("24404")) {
            tvSure.setText("下架完成");
        }

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

                holder.getView(R.id.tv_djsl).setVisibility(getIntent().getStringExtra("tableid").equals("24426") ? View.VISIBLE : View.GONE);

                if (holder.getAdapterPosition() != 0) {
                    holder.setText(R.id.tv_xh, (holder.getAdapterPosition()) + "");
                    holder.setText(R.id.tv_cw, item.getCw());
                    holder.setText(R.id.tv_tm, item.getAlias());
                    holder.setText(R.id.tv_djsl, item.getQtybook() + "");
                    holder.setText(R.id.tv_xjsl, item.getQty() + "");
                } else {
                    if (getIntent().getStringExtra("tableid").equals("24426")) {
                        holder.setText(R.id.tv_xjsl, "盘点数量");
                        holder.setText(R.id.tv_djsl, "账面数");

                    } else {
                        holder.setText(R.id.tv_xjsl, "下架数量");
                    }
                }
                holder.setOnIntemLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showItemModifyDialog(item,
                                (qty) -> modifyItem(item.getItemid(), qty),
                                () -> deleteItem(item.getItemid()));
                        return false;
                    }
                });
            }

        });

        mProgressDilog.show();
        quest();
    }

    /**
     * 修改明细明细
     *
     * @param itemid 物品条码
     * @param qty    数量
     */
    private void modifyItem(int itemid, int qty) {
        mProgressDilog.show();
        NetworkHelper.getInstance().updateQty(this, userid, tableid, rowid, itemid, qty, new HttpCallBack<CommBean>() {
            @Override
            public void onSuccess(CommBean result) {
                mProgressDilog.dismiss();
                if (result.getCode() == 200) {
                    // 刷新界面
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

    /**
     * 删除明细
     *
     * @param itemid 条码
     */
    private void deleteItem(int itemid) {
        mProgressDilog.show();
        NetworkHelper.getInstance().deleteItem(this, userid, tableid, rowid, itemid, new HttpCallBack<CommBean>() {
            @Override
            public void onSuccess(CommBean result) {
                mProgressDilog.dismiss();
                if (result.getCode() == 200) {
                    // 刷新界面
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


                            int count = 0;

                            if (getIntent().getStringExtra("tableid").equals("24426")) {
                                tvZsmsl.setText("盘点数量总和：" + count);
                            } else {
                                tvZsmsl.setText("下架数量总和：" + count);
                            }


                            for (int i = 0; i < result.getItem().size(); i++) {
                                count += result.getItem().get(i).getQty();
                            }

                            if (getIntent().getStringExtra("tableid").equals("24426")) {
                                tvZsmsl.setText("盘点数量总和：" + count);
                            } else {
                                tvZsmsl.setText("下架数量总和：" + count);
                            }

                            mBeans.clear();
                            mBeans.add(new OutLogInfoBean.ItemBean());
                            mBeans.addAll(result.getItem());
                            rcView.getAdapter().notifyDataSetChanged();

                            bean = result.getMsg();
                            // setData(result.getMsg());
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
        params.put("diffreason", diffreason);
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

                        if (result.getSucceed() == -2) {
                            showChayiDialog(result.getMsg());
                            return;
                        }

                        if (result.getCode() == 200) {
                          /*  mBeans.clear();
                            quest();*/

                            Toaster.showMsg("提交成功！");
                            setResult(RESULT_OK);
                            finish();
                        } else {
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


    private void showChayiDialog(String msg) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.popup_cx)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.setText(R.id.tv_title, msg);
        EditText et = dialog.getView(R.id.et_sl);
        dialog.getView(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getView(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et.getText().toString().trim())) {
                    Toaster.showMsg("请填写原因");
                    return;
                }
                dialog.dismiss();
                diffreason = et.getText().toString().trim();
                mProgressDilog.show();
                commit();
            }
        });
        dialog.show();
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
        LinearLayout ll_sjcw = dialog.getView(R.id.ll_sjcw);


        etSl.setSelection(etSl.getText().length());

        ll_jycw.setVisibility(View.GONE);
        tv_sl_title.setText("数量: ");

        if (getIntent().getStringExtra("tableid").equals("24426")) {
            ll_sjcw.setVisibility(View.GONE);
        }

        dialog.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etSl.getText().toString().trim())) {
                    Toaster.showMsg("下架架数量不能为空");
                    return;
                }


                if (!getIntent().getStringExtra("tableid").equals("24426") && TextUtils.isEmpty(etScan.getText().toString().trim())) {
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
                commit();
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
