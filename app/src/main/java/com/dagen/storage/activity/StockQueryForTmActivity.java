package com.dagen.storage.activity;

import android.content.Intent;
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
import com.dagen.storage.bean.HomeBean;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//库存查询--按条码
public class StockQueryForTmActivity extends BaseMoudleActivity {


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

    private List<HomeBean> mBeans = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_stock_query_spec;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("库存查询-按条码筛选");

        tvKh.setText("条码");
        etSearch.setHint("请输入条码");

        llBtm.setVisibility(View.GONE);

        for (int i = 0; i < 20; i++) {
            HomeBean bean = new HomeBean();
            bean.setName("出库单" + i);
            mBeans.add(bean);
        }

        rcView.setLayoutManager(new LinearLayoutManager(this));

        rcView.setAdapter(new CommonRecyclerAdapter<HomeBean>(this, mBeans, R.layout.item_kccx_tm) {
            @Override
            public void convert(ViewHolder holder, HomeBean item) {
                if (holder.getAdapterPosition() != 0) {
                    holder.setText(R.id.tv_xh, "江苏泰州靖江上海城657店");
                    holder.setText(R.id.tv_cw, "粉红色");
                    holder.setText(R.id.tv_tm, "32");
                    holder.setText(R.id.tv_djsl, "2");
                } else {
                    holder.setText(R.id.tv_xh, "店仓");
                    holder.setText(R.id.tv_cw, "小类");
                    holder.setText(R.id.tv_tm, "库存");
                    holder.setText(R.id.tv_djsl, "零售价");
                }
                holder.getView(R.id.ll_root).setBackgroundColor(holder.getAdapterPosition() % 2 == 0 ? getResources().getColor(R.color.main_bg) : getResources().getColor(R.color.color_50f56c5c));

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
                startActivityForResult(intent, 200);
                break;
        }
    }


}
