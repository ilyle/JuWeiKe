package com.dagen.storage.activity;

import android.content.Intent;
import android.text.TextUtils;
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
import com.dagen.storage.bean.StoreBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnScanFinishListener;
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
import butterknife.OnClick;

//选择店仓
public class SelectStoreActivity extends BaseMoudleActivity {


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

    private List<StoreBean.MsgBean> mBeans = new ArrayList<>();
    private String name="";


    @Override
    public int getLayoutId() {
        return R.layout.activity_warehousing_entry_tm_del;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("选择店仓");

        etSearch.setHint("查询店仓");

        tvSure.setVisibility(View.GONE);



        rcView.setLayoutManager(new LinearLayoutManager(this));

        rcView.setAdapter(new CommonRecyclerAdapter<StoreBean.MsgBean>(this, mBeans, R.layout.item_xzdc) {
            @Override
            public void convert(ViewHolder holder, StoreBean.MsgBean item) {
                     holder.setText(R.id.tv_tm,item.getName());
                     holder.setText(R.id.tv_sl,item.getCode());

                     holder.setOnIntemClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Intent intent=new Intent();
                             intent.putExtra("name",item.getName());
                             intent.putExtra("code",item.getCode());
                             setResult(RESULT_OK,intent);
                             finish();
                         }
                     });
            }

        });

        setEditextFilter(etSearch, new OnScanFinishListener() {
            @Override
            public void onScanFinish(String content) {
                mProgressDilog.show();
                mBeans.clear();
                quest();
            }
        });

        mProgressDilog.show();
        quest();

    }

    //获取店仓
    private void quest() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", Integer.parseInt(getIntent().getStringExtra("tableid")));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("name", etSearch.getText().toString().trim());
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getstorelist");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<StoreBean>() {
                    @Override
                    public void onSuccess(StoreBean result) {
                           mProgressDilog.dismiss();
                        if (result.getCode() == 200) {
                            mBeans.addAll(result.getMsg());
                            rcView.getAdapter().notifyDataSetChanged();
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

    @OnClick({R.id.iv_common_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;

        }
    }
}
