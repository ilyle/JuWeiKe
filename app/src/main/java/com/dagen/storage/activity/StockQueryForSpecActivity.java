package com.dagen.storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.HomeBean;
import com.dagen.storage.bean.StockQueryBean;
import com.dagen.storage.bean.TableQuery;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnScanFinishListener;
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

//库存查询--按款号
public class StockQueryForSpecActivity extends BaseMoudleActivity {


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
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    @BindView(R.id.tv_dc_name)
    TextView tvDcName;
    @BindView(R.id.tv_xzdc)
    TextView tvXzdc;
    @BindView(R.id.ll_btm)
    LinearLayout llBtm;
    @BindView(R.id.tv_kh)
    TextView tvKh;
    @BindView(R.id.table)
    SmartTable table;

    private List<StockQueryBean.MsgBean.ListBean> mBeans = new ArrayList<>();
    private String sku = "2100100611117";
    private String storecode = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_stock_query_spec;
    }

    @Override
    public void initData() {
        super.initData();
        //tvCommonTitle.setText("库存查询-按条码筛选");

        tvCommonTitle.setText(getIntent().getStringExtra("title"));
       /* for (int i = 0; i < 20; i++) {
            HomeBean bean = new HomeBean();
            bean.setName("出库单" + i);
            mBeans.add(bean);
        }*/

        rcView.setLayoutManager(new LinearLayoutManager(this));

        rcView.setAdapter(new CommonRecyclerAdapter<StockQueryBean.MsgBean.ListBean>(this, mBeans, R.layout.item_kccx_kh2) {
            @Override
            public void convert(ViewHolder holder, StockQueryBean.MsgBean.ListBean item) {
                /*if (holder.getAdapterPosition() != 0) {
                    holder.setText(R.id.tv_xh, "CW001");
                    holder.setText(R.id.tv_cw, "粉红色");
                    holder.setText(R.id.tv_tm, "32");
                    holder.setText(R.id.tv_djsl, "2");
                } else {
                    holder.setText(R.id.tv_xh, "条码");
                    holder.setText(R.id.tv_cw, "颜色");
                    holder.setText(R.id.tv_tm, "尺寸");
                    holder.setText(R.id.tv_djsl, "库存");
                }*/
                holder.getView(R.id.tv_1).setVisibility(TextUtils.isEmpty(item.getRow1())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_2).setVisibility(TextUtils.isEmpty(item.getRow2())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_3).setVisibility(TextUtils.isEmpty(item.getRow3())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_4).setVisibility(TextUtils.isEmpty(item.getRow4())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_5).setVisibility(TextUtils.isEmpty(item.getRow5())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_6).setVisibility(TextUtils.isEmpty(item.getRow6())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_7).setVisibility(TextUtils.isEmpty(item.getRow7())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_8).setVisibility(TextUtils.isEmpty(item.getRow8())?View.GONE:View.VISIBLE);
                holder.getView(R.id.tv_9).setVisibility(TextUtils.isEmpty(item.getRow9())?View.GONE:View.VISIBLE);

                holder.setText(R.id.tv_1,item.getRow1());
                holder.setText(R.id.tv_2,item.getRow2());
                holder.setText(R.id.tv_3,item.getRow3());
                holder.setText(R.id.tv_4,item.getRow4());
                holder.setText(R.id.tv_5,item.getRow5());
                holder.setText(R.id.tv_6,item.getRow6());
                holder.setText(R.id.tv_7,item.getRow7());
                holder.setText(R.id.tv_8,item.getRow8());
                holder.setText(R.id.tv_9,item.getRow9());

                holder.getView(R.id.ll_root).setBackgroundColor(holder.getAdapterPosition() % 2 == 0 ? getResources().getColor(R.color.main_bg) : getResources().getColor(R.color.color_50f56c5c));

            }

        });

        setEditextFilter(etSearch, new OnScanFinishListener() {
            @Override
            public void onScanFinish(String content) {
                sku = content;
                mProgressDilog.show();
                quest();
            }
        });

        mProgressDilog.show();
        quest();
    }


    //查询
    private void quest() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("sku", sku);
        params.put("storecode", storecode);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_querysku");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<StockQueryBean>() {
                    @Override
                    public void onSuccess(StockQueryBean result) {
                        mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                           /* StockQueryBean.MsgBean.ListBean bean=new StockQueryBean.MsgBean.ListBean();
                            bean.setRow1(result.getMsg().getRow1());
                            bean.setRow2(result.getMsg().getRow2());
                            bean.setRow3(result.getMsg().getRow3());
                            bean.setRow4(result.getMsg().getRow4());
                            bean.setRow5(result.getMsg().getRow5());
                            bean.setRow6(result.getMsg().getRow6());
                            bean.setRow7(result.getMsg().getRow7());
                            bean.setRow8(result.getMsg().getRow8());
                            bean.setRow9(result.getMsg().getRow9());
                            mBeans.add(bean);
                            mBeans.addAll(result.getMsg().getList());

                            rcView.getAdapter().notifyDataSetChanged();*/

                          /*  Column<String> row1,row2;
                            row1 = new Column<>(result.getMsg().getRow1(), "row1");
                            row2 = new Column<>(result.getMsg().getRow2(), "row2");
                            Column<String> row3= new Column<>(result.getMsg().getRow3(), "row3");
                            Column<String> row4= new Column<>(result.getMsg().getRow4(), "row4");
                            Column<String> row5= new Column<>(result.getMsg().getRow5(), "row5");
                            Column<String> row6 = new Column<>(result.getMsg().getRow6(), "row6");
                            Column<String> row7 = new Column<>(result.getMsg().getRow7(), "row7");
                            Column<String> row8 = new Column<>(result.getMsg().getRow8(), "row8");
                            Column<String> row9 = new Column<>(result.getMsg().getRow9(), "row9");*/
                            List<Column>mTitles=new ArrayList<>();
                            if(!TextUtils.isEmpty(result.getMsg().getRow1())){
                                mTitles.add(new Column<>(result.getMsg().getRow1(), "row1"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow2())){
                                mTitles.add(new Column<>(result.getMsg().getRow2(), "row2"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow3())){
                                mTitles.add(new Column<>(result.getMsg().getRow3(), "row3"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow4())){
                                mTitles.add(new Column<>(result.getMsg().getRow4(), "row4"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow5())){
                                mTitles.add(new Column<>(result.getMsg().getRow5(), "row5"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow6())){
                                mTitles.add(new Column<>(result.getMsg().getRow6(), "row6"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow7())){
                                mTitles.add(new Column<>(result.getMsg().getRow7(), "row7"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow8())){
                                mTitles.add(new Column<>(result.getMsg().getRow8(), "row8"));
                            }
                            if(!TextUtils.isEmpty(result.getMsg().getRow9())){
                                mTitles.add(new Column<>(result.getMsg().getRow9(), "row9"));
                            }




                            List<TableQuery> list = new ArrayList<>();
                            for(int i=0;i<result.getMsg().getList().size();i++){
                                list.add(new TableQuery(
                                        result.getMsg().getList().get(i).getRow1(),
                                        result.getMsg().getList().get(i).getRow2(),
                                        result.getMsg().getList().get(i).getRow3(),
                                        result.getMsg().getList().get(i).getRow4(),
                                        result.getMsg().getList().get(i).getRow5(),
                                        result.getMsg().getList().get(i).getRow6(),
                                        result.getMsg().getList().get(i).getRow7(),
                                        result.getMsg().getList().get(i).getRow8(),
                                        result.getMsg().getList().get(i).getRow9()
                                        ));
                            }

                           // TableData<TableQuery> tableData = new TableData<>("", list, row1,row2,row3,row4,row5,row6);
                            TableData<TableQuery> tableData = new TableData<>("", list, mTitles);
                            table.setTableData(tableData);
                            tableData.setShowCount(false);
                            table.getConfig().setShowTableTitle(false);
                            table.getConfig().setShowXSequence(false);
                            table.getConfig().setShowYSequence(false);
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

    @OnClick({R.id.iv_common_left, R.id.tv_xzdc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.tv_xzdc:
                Intent intent = new Intent(this, SelectStoreActivity.class);
                intent.putExtra("tableid", getIntent().getStringExtra("tableid"));
                startActivityForResult(intent, 600);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (600 == requestCode && RESULT_OK == resultCode) {
            tvDcName.setText(data.getStringExtra("name"));
            storecode = data.getStringExtra("code");
            mProgressDilog.show();
            quest();
        }
    }


}
