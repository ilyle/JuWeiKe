package com.dagen.storage.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.bean.HomeBean;
import com.dagen.storage.bean.RKBean;
import com.dagen.storage.bean.TableWare;
import com.dagen.storage.bean.TmListBean;
import com.dagen.storage.bean.WareHousingEntryBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnScanFinishListener2;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.support.popupwindow.CommonPopupWindow;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//入库单activity
public class WareHousingEntryActivity extends BaseMoudleActivity implements CommonPopupWindow.ViewInterface {


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
    @BindView(R.id.tv_del)
    TextView tvDel;
    @BindView(R.id.ll_btm)
    LinearLayout llBtm;
    @BindView(R.id.et_scam)
    EditText etScam;
    @BindView(R.id.et_hj)
    EditText etHj;
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.tv_zdjsl)
    TextView tvZdjsl;
    @BindView(R.id.tv_zsmsl)
    TextView tvZsmsl;

    private List<HomeBean> mBeans = new ArrayList<>();
    private List<HomeBean> mAccounts = new ArrayList<>();
    private CommonPopupWindow popupWindow;

    private List<TmListBean.MsgBean> mTms = new ArrayList<>();
    private int index;
    private String diffreason="";
    private String ifcontrim="N";


    @Override
    public int getLayoutId() {
        return R.layout.activity_warehousing_entry;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("入库单");

        for (int i = 0; i < 20; i++) {
            HomeBean bean = new HomeBean();
            bean.setName("出库单" + i);
            mBeans.add(bean);
        }

        rcView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        rcView.setHasFixedSize(true);
        rcView.setNestedScrollingEnabled(false);
        rcView.setAdapter(new CommonRecyclerAdapter<HomeBean>(this, mBeans, R.layout.item_rkd_list) {
            @Override
            public void convert(ViewHolder holder, HomeBean item) {
                if (holder.getAdapterPosition() != 0) {
                    holder.setText(R.id.tv_xh, "CW001");
                    holder.setText(R.id.tv_cw, "粉红色");
                    holder.setText(R.id.tv_tm, "32");
                    holder.setText(R.id.tv_djsl, "2");
                    holder.setText(R.id.tv_xjsl, "200");
                }
                holder.getView(R.id.ll_root).setBackgroundColor(holder.getAdapterPosition() % 2 == 0 ? getResources().getColor(R.color.main_bg) : getResources().getColor(R.color.color_50f56c5c));

                holder.setOnIntemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showPopup();
                    }
                });
            }

        });

        String json = SharePreferenceUtil.getInstance().getString("account");
        List<HomeBean> accounts = jsonToList(json, HomeBean.class);
        if (accounts != null)
            mAccounts.addAll(accounts);

        setEditextFilter(etScam, new OnScanFinishListener2() {
            @Override
            public void onScanFinish(String content) {
                mProgressDilog.show();
                questScan(content);
              /* if(ifcontrim.equals("Y")){
                   showPopup(content);
               }else {
                   mProgressDilog.show();
                   insert(content,"",1);
                   //getCw(content);
               }*/
            }

            @Override
            public void onInputFinish(String content) {
                mProgressDilog.show();
                mTms.clear();
                questTmList(content);

            }
        });

        mProgressDilog.show();
        quest();
    }


    private void showPopup(String tm) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.popup_rkd)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.setText(R.id.tv_tm, tm);
        EditText etScan = dialog.getView(R.id.et_scam);
        EditText etSl = dialog.getView(R.id.et_sjsl);

        etSl.setSelection(etSl.getText().length());
        //getCw(tm, tvCw);

        dialog.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(TextUtils.isEmpty(tvCw.getText().toString().trim())){
                    getCw(tm, tvCw);
                    return;
                }*/
                if (TextUtils.isEmpty(etSl.getText().toString().trim())) {
                    Toaster.showMsg("入库数量不能为空");
                    return;
                }
              /*  if (TextUtils.isEmpty(etScan.getText().toString().trim())) {
                    Toaster.showMsg("实际储位不能为空");
                    return;
                }*/

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

    //获取储位
    private void getCw(String tm) {
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
                            insert(tm,"",1);
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
                        //   mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            Toaster.showMsg("插入成功！");
                            playSoundAndVirate();
                            mBeans.clear();
                            quest();
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

    private void questScan(String pdt) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("pdt", pdt);
        params.put("qty", 1);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "boxno_scan_ard_new");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<RKBean>() {
                    @Override
                    public void onSuccess(RKBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            if (result.getSucceed() == 0) {
                               // quest();

                                if(ifcontrim.equals("Y")){
                                    showPopup(pdt);
                                }else {
                                    mProgressDilog.show();
                                    insert(pdt,"",1);
                                    //getCw(content);
                                }

                            } else if (result.getSucceed() == -1) {
                                Intent intent = new Intent(WareHousingEntryActivity.this, WareHousingEntryInsert2Activity.class);
                                intent.putExtra("name", pdt);
                                intent.putExtra("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
                                intent.putExtra("id", getIntent().getIntExtra("id", 0));
                                startActivityForResult(intent, 600);
                                popupWindow.dismiss();
                            } else {
                                Toaster.showMsg(result.getValue());
                            }
                        } else {
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

    private void questTmList(String text) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("pdt", text);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_pdtlist_ard");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<TmListBean>() {
                    @Override
                    public void onSuccess(TmListBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            mTms.addAll(result.getMsg());
                            showPop();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }

    //查询
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
                .execute(new HttpCallBack<WareHousingEntryBean>() {
                    @Override
                    public void onSuccess(WareHousingEntryBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {

                            tvZdjsl.setText("总数量："+result.getTotqty());
                            tvZsmsl.setText("扫描数量："+result.getTotqtyscan());

                            ifcontrim=result.getIfcontrim();

                            List<Column> mTitles = new ArrayList<>();
                            if (!TextUtils.isEmpty(result.getItem().getRow1())) {
                                mTitles.add(new Column<>(result.getItem().getRow1(), "row1"));
                            }
                            if (!TextUtils.isEmpty(result.getItem().getRow2())) {
                                mTitles.add(new Column<>(result.getItem().getRow2(), "row2"));
                                if (result.getItem().getRow2().equals("入库数")) {
                                    index = 1;
                                }
                            }
                            if (!TextUtils.isEmpty(result.getItem().getRow3())) {
                                mTitles.add(new Column<>(result.getItem().getRow3(), "row3"));
                                if (result.getItem().getRow3().equals("入库数")) {
                                    index = 2;
                                }
                            }
                            if (!TextUtils.isEmpty(result.getItem().getRow4())) {
                                mTitles.add(new Column<>(result.getItem().getRow4(), "row4"));
                                if (result.getItem().getRow4().equals("入库数")) {
                                    index = 3;
                                }
                            }
                            if (!TextUtils.isEmpty(result.getItem().getRow5())) {
                                mTitles.add(new Column<>(result.getItem().getRow5(), "row5"));
                                if (result.getItem().getRow5().equals("入库数")) {
                                    index = 4;
                                }
                            }
                            if (!TextUtils.isEmpty(result.getItem().getRow6())) {
                                mTitles.add(new Column<>(result.getItem().getRow6(), "row6"));
                                if (result.getItem().getRow6().equals("入库数")) {
                                    index = 5;
                                }
                            }
                           /* if (!TextUtils.isEmpty(result.getItem().getRow7())) {
                                mTitles.add(new Column<>(result.getItem().getRow7(), "row7"));
                            }
                            if (!TextUtils.isEmpty(result.getItem().getRow8())) {
                                mTitles.add(new Column<>(result.getItem().getRow8(), "row8"));
                            }
                            if (!TextUtils.isEmpty(result.getItem().getRow9())) {
                                mTitles.add(new Column<>(result.getItem().getRow9(), "row9"));
                            }*/


                            List<TableWare> list = new ArrayList<>();
                            for (int i = 0; i < result.getItem().getList().size(); i++) {
                                list.add(new TableWare(
                                        result.getItem().getList().get(i).getRow1(),
                                        result.getItem().getList().get(i).getRow2(),
                                        result.getItem().getList().get(i).getRow3(),
                                        result.getItem().getList().get(i).getRow4(),
                                        result.getItem().getList().get(i).getRow5(),
                                        result.getItem().getList().get(i).getRow6()
                                     /*   result.getItem().getList().get(i).getRow7(),
                                        result.getItem().getList().get(i).getRow8(),
                                        result.getItem().getList().get(i).getRow9()*/
                                ));
                            }

                            // TableData<TableQuery> tableData = new TableData<>("", list, row1,row2,row3,row4,row5,row6);
                            TableData<TableWare> tableData = new TableData<>("", list, mTitles);
                            table.setTableData(tableData);
                            tableData.setShowCount(false);
                            table.getConfig().setShowTableTitle(false);
                            table.getConfig().setShowXSequence(false);
                            table.getConfig().setShowYSequence(false);


                            tableData.setOnItemClickListener(new TableData.OnItemClickListener() {
                                @Override
                                public void onClick(Column column, String value, Object o, int col, int row) {
                                    showPopup(result.getItem().getList().get(row));
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


    @OnClick({R.id.iv_common_left, R.id.tv_sure, R.id.tv_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.tv_sure:
               /* showSureDialog("确认提交入库单？", new OnSureClickListener() {
                    @Override
                    public void onSure() {

                    }
                });*/
                mProgressDilog.show();
                commit();
                break;
            case R.id.tv_del:
                showChooseDialog();
                break;

        }
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
                        etScam.setText("");
                        mProgressDilog.dismiss();

                        if(result.getSucceed()==-2){
                            showChayiDialog(result.getMsg());
                            return;
                        }

                        if (result.getCode() == 200) {
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
        dialog.setText(R.id.tv_title,msg);
        EditText et=dialog.getView(R.id.et_sl);
        dialog.getView(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getView(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et.getText().toString().trim())){
                    Toaster.showMsg("请填写原因");
                    return;
                }
                dialog.dismiss();
                diffreason=et.getText().toString().trim();
                mProgressDilog.show();
                commit();
            }
        });
        dialog.show();
    }


    private void showPopup(WareHousingEntryBean.ItemBean.ListBean bean) {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.popup_modify)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.setText(R.id.tv_kh, bean.getRow1());
        switch (index) {
            case 1:
                dialog.setText(R.id.et_sl, bean.getRow2());
                break;
            case 2:
                dialog.setText(R.id.et_sl, bean.getRow3());
                break;
            case 3:
                dialog.setText(R.id.et_sl, bean.getRow4());
                break;
            case 4:
                dialog.setText(R.id.et_sl, bean.getRow5());
                break;
            case 5:
                dialog.setText(R.id.et_sl, bean.getRow6());
                break;
        }

        EditText etSl = dialog.getView(R.id.et_sl);
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
                modify(bean.getItemid(), Integer.parseInt(etSl.getText().toString().trim()));
            }
        });

        dialog.show();
    }

    //修改明细数量
    private void modify(int id, int qty) {
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
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                    }
                });
    }

    private void showChooseDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.dialog_del)
                .setWidthAndHeight(AppUtils.getScreenWidth(this) * 85 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();


        dialog.setOnClickListener(R.id.tv_tm_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(WareHousingEntryActivity.this, WareHousingEntryTmDelActivity.class);
                intent.putExtra("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
                intent.putExtra("id", getIntent().getIntExtra("id", 0));
                startActivityForResult(intent, 600);
            }
        });

        dialog.setOnClickListener(R.id.tv_kh_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(WareHousingEntryActivity.this, WareHousingEntryTmDelActivity.class);
                intent.putExtra("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
                intent.putExtra("id", getIntent().getIntExtra("id", 0));
                intent.putExtra("from", "kh");
                startActivityForResult(intent, 600);

            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (600 == requestCode && RESULT_OK == resultCode) {
            quest();
        }
    }

    //选择弹窗
    private void showPop() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_account)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(1f)//取值范围0.0f-1.0f 值越小越暗
                .setViewOnclickListener(this)
                .create();

        //这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //这一句代码至关重要，决定着弹出窗体后是否可以继续输入
        popupWindow.setFocusable(false);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.showAsDropDown2(popupWindow, etScam, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_account:
                RecyclerView rc_zh = view.findViewById(R.id.rc_zh);
                rc_zh.setLayoutManager(new LinearLayoutManager(this));
                rc_zh.setAdapter(new CommonRecyclerAdapter<TmListBean.MsgBean>(this, mTms, R.layout.item_tm) {
                    @Override
                    public void convert(ViewHolder holder, TmListBean.MsgBean item) {
                        holder.setText(R.id.tv_zh, item.getName());
                        holder.setOnIntemClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(WareHousingEntryActivity.this, WareHousingEntryInsert2Activity.class);
                                intent.putExtra("name", item.getName());
                                intent.putExtra("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
                                intent.putExtra("id", getIntent().getIntExtra("id", 0));
                                startActivityForResult(intent, 600);
                                popupWindow.dismiss();
                            }
                        });
                    }

                });
                break;
        }
    }


}
