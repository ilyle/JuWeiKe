package com.dagen.storage.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.HomeBean;
import com.dagen.storage.bean.OutLogBean;
import com.dagen.storage.bean.QueryArrBean;
import com.dagen.storage.support.OnScanFinishListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//入库上架单列表页
public class WareHousingEntryInLogHomeActivity extends BaseMoudleActivity {


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

    private List<OutLogBean.MsgBean> mBeans = new ArrayList<>();
    private String docno = "";
    private List<QueryArrBean>mQueryArrs=new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_godown_inventory_outlog_home;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("入库上架单");
        ivCommonRight.setVisibility(View.VISIBLE);
        ivCommonRight.setImageResource(R.drawable.add);

       /* for(int i=0;i<20;i++){
            HomeBean bean=new HomeBean();
            bean.setName("出库单"+i);
            mBeans.add(bean);
        }*/


        /*rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(new CommonRecyclerAdapter<OutLogBean.MsgBean>(this, mBeans, R.layout.item_ckxjd_list) {
            @Override
            public void convert(ViewHolder holder, OutLogBean.MsgBean item) {
                holder.setText(R.id.tv_dh, item.getDocno());
                holder.setText(R.id.tv_rq, item.getBilldate() + "");
                holder.setText(R.id.tv_zjs, item.getTot_qty() + "");
                holder.setText(R.id.tv_zks, item.getPdtqty() + "");
                holder.setText(R.id.tv_czr, item.getOperater());
                holder.setOnIntemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GodownInventoryOutLogHomeActivity.this, GodownInventoryOutLogDetailActivity.class);
                        intent.putExtra("id",item.getId());
                        intent.putExtra("tableid",getIntent().getStringExtra("tableid"));
                        startActivity(intent);
                    }
                });
            }

        });

        setEditextFilter(etSearch, new OnScanFinishListener() {
            @Override
            public void onScanFinish(String content) {
                docno=content;
                mBeans.clear();
                quest();
            }
        });

        refreshLayout.setEnableLoadMore(false);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                docno="";
                etSearch.setText("");
                mBeans.clear();
                quest();
            }
        });

        mProgressDilog.show();
        quest();*/
    }

    @OnClick({ R.id.iv_common_left,R.id.ll_sx })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.ll_sx:
                Intent intent=new Intent(this,ScreenOutActivity.class);
                startActivity(intent);
                break;

        }
    }

}
