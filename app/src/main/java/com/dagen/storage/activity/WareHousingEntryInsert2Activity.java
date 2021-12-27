package com.dagen.storage.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.CommBean;
import com.dagen.storage.bean.HomeBean;
import com.dagen.storage.bean.MaixferBean;
import com.dagen.storage.bean.MatrixBean;
import com.dagen.storage.bean.OutLogBean;
import com.dagen.storage.bean.TmListBean;
import com.dagen.storage.bean.WareHousingInsetBean;
import com.dagen.storage.support.Contasts;
import com.dagen.storage.support.OnSureClickListener;
import com.dagen.storage.utils.Toaster;
import com.wanlv365.lawyer.baselibrary.HttpUtils;
import com.wanlv365.lawyer.baselibrary.utils.SharePreferenceUtil;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.CommonRecyclerAdapter;
import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.ViewHolder;
import com.wanlv365.lawyer.moudlelibrary.http.HttpCallBack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mob.commons.eventrecoder.EventRecorder.clear;

//入库单-商品新增
public class WareHousingEntryInsert2Activity extends BaseMoudleActivity {


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

    private List<WareHousingInsetBean> mBeans = new ArrayList<>();

    private Map<Integer, List<EditText>> maps = new HashMap<>();

    private int lastPos = 1;
    private int lastNum = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ware_hosing_insert2;
    }

    @Override
    public void initData() {
        super.initData();
        tvCommonTitle.setText("入库单");

        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(new CommonRecyclerAdapter<WareHousingInsetBean>(this, mBeans, R.layout.item_rkxz) {
            @Override
            public void convert(ViewHolder holder, WareHousingInsetBean item) {
                holder.setIsRecyclable(false);

                holder.setText(R.id.et_1, item.getEtText1());
                holder.setText(R.id.et_2, item.getEtText2());
                holder.setText(R.id.et_3, item.getEtText3());
                holder.setText(R.id.et_4, item.getEtText4());
                holder.setText(R.id.et_5, item.getEtText5());
                holder.setText(R.id.et_6, item.getEtText6());
                holder.setText(R.id.et_7, item.getEtText7());
                holder.setText(R.id.et_8, item.getEtText8());
                holder.setText(R.id.et_9, item.getEtText9());

                holder.setText(R.id.tv_1, item.getTv1());
                holder.setText(R.id.tv_2, item.getTv2());
                holder.setText(R.id.tv_3, item.getTv3());
                holder.setText(R.id.tv_4, item.getTv4());
                holder.setText(R.id.tv_5, item.getTv5());
                holder.setText(R.id.tv_6, item.getTv6());
                holder.setText(R.id.tv_7, item.getTv7());
                holder.setText(R.id.tv_8, item.getTv8());
                holder.setText(R.id.tv_9, item.getTv9());

                holder.setText(R.id.tv_total, item.getTotal());
                holder.setText(R.id.tv_total_top, item.getTitleTotal());


                List<EditText> mEts = new ArrayList<>();
                EditText et1 = holder.getView(R.id.et_1);
                EditText et2 = holder.getView(R.id.et_2);
                EditText et3 = holder.getView(R.id.et_3);
                EditText et4 = holder.getView(R.id.et_4);
                EditText et5 = holder.getView(R.id.et_5);
                EditText et6 = holder.getView(R.id.et_6);
                EditText et7 = holder.getView(R.id.et_7);
                EditText et8 = holder.getView(R.id.et_8);
                EditText et9 = holder.getView(R.id.et_9);


                setInputListener(et1, mEts, holder.getAdapterPosition());
                setInputListener(et2, mEts, holder.getAdapterPosition());
                setInputListener(et3, mEts, holder.getAdapterPosition());
                setInputListener(et4, mEts, holder.getAdapterPosition());
                setInputListener(et5, mEts, holder.getAdapterPosition());
                setInputListener(et6, mEts, holder.getAdapterPosition());
                setInputListener(et7, mEts, holder.getAdapterPosition());
                setInputListener(et8, mEts, holder.getAdapterPosition());
                setInputListener(et9, mEts, holder.getAdapterPosition());


                holder.getView(R.id.tv_1).setVisibility(TextUtils.isEmpty(item.getTv1()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_2).setVisibility(TextUtils.isEmpty(item.getTv2()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_3).setVisibility(TextUtils.isEmpty(item.getTv3()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_4).setVisibility(TextUtils.isEmpty(item.getTv4()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_5).setVisibility(TextUtils.isEmpty(item.getTv5()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_6).setVisibility(TextUtils.isEmpty(item.getTv6()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_7).setVisibility(TextUtils.isEmpty(item.getTv7()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_8).setVisibility(TextUtils.isEmpty(item.getTv8()) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.tv_9).setVisibility(TextUtils.isEmpty(item.getTv9()) ? View.GONE : View.VISIBLE);


                if (holder.getAdapterPosition() == 0) {
                    holder.getView(R.id.tv_title).setBackgroundColor(getResources().getColor(R.color.color_30a952));
                    holder.setText(R.id.tv_title, "尺寸");
                    holder.setText(R.id.tv_total_top, "总计");

                    holder.getView(R.id.tv_add).setVisibility(View.GONE);
                    holder.getView(R.id.tv_total).setVisibility(View.GONE);
                    holder.getView(R.id.et_1).setVisibility(View.GONE);
                    holder.getView(R.id.et_2).setVisibility(View.GONE);
                    holder.getView(R.id.et_3).setVisibility(View.GONE);
                    holder.getView(R.id.et_4).setVisibility(View.GONE);
                    holder.getView(R.id.et_5).setVisibility(View.GONE);
                    holder.getView(R.id.et_6).setVisibility(View.GONE);
                    holder.getView(R.id.et_7).setVisibility(View.GONE);
                    holder.getView(R.id.et_8).setVisibility(View.GONE);
                    holder.getView(R.id.et_9).setVisibility(View.GONE);


                } else {
                    holder.getView(R.id.tv_title).setBackgroundColor(getResources().getColor(R.color.color_478DFE));
                    holder.setText(R.id.tv_title, item.getName());


                    holder.getView(R.id.tv_add).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_total).setVisibility(View.VISIBLE);
                      /*  holder.getView(R.id.et_1).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_2).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_3).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_4).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_5).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_6).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_7).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_8).setVisibility(View.VISIBLE);
                        holder.getView(R.id.et_9).setVisibility(View.VISIBLE);*/


                    holder.getView(R.id.et_1).setVisibility(TextUtils.isEmpty(item.getTv1()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_2).setVisibility(TextUtils.isEmpty(item.getTv2()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_3).setVisibility(TextUtils.isEmpty(item.getTv3()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_4).setVisibility(TextUtils.isEmpty(item.getTv4()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_5).setVisibility(TextUtils.isEmpty(item.getTv5()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_6).setVisibility(TextUtils.isEmpty(item.getTv6()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_7).setVisibility(TextUtils.isEmpty(item.getTv7()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_8).setVisibility(TextUtils.isEmpty(item.getTv8()) ? View.GONE : View.VISIBLE);
                    holder.getView(R.id.et_9).setVisibility(TextUtils.isEmpty(item.getTv9()) ? View.GONE : View.VISIBLE);

                    if (et1.getVisibility() == View.VISIBLE) {
                        mEts.add(et1);
                    }
                    if (et2.getVisibility() == View.VISIBLE) {
                        mEts.add(et2);
                    }
                    if (et3.getVisibility() == View.VISIBLE) {
                        mEts.add(et3);
                    }
                    if (et4.getVisibility() == View.VISIBLE) {
                        mEts.add(et4);
                    }
                    if (et5.getVisibility() == View.VISIBLE) {
                        mEts.add(et5);
                    }
                    if (et6.getVisibility() == View.VISIBLE) {
                        mEts.add(et6);
                    }
                    if (et7.getVisibility() == View.VISIBLE) {
                        mEts.add(et7);
                    }
                    if (et8.getVisibility() == View.VISIBLE) {
                        mEts.add(et8);
                    }
                    if (et9.getVisibility() == View.VISIBLE) {
                        mEts.add(et9);
                    }
                    maps.put(holder.getAdapterPosition(), mEts);

                }
            }

        });


        mProgressDilog.show();
        quest();

    }


    private void quest() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", getIntent().getIntExtra("tableid", 0));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("pdt", getIntent().getStringExtra("name"));
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_getpdtmaixfer_ard");
        data.put("params", params);
        HttpUtils.with(this)
                .url(Contasts.BASE_URL)
                .doJsonPost()
                .setJson(gson.toJson(data))
                .execute(new HttpCallBack<MatrixBean>() {
                    @Override
                    public void onSuccess(MatrixBean result) {
                        mProgressDilog.dismiss();

                        WareHousingInsetBean bean = new WareHousingInsetBean();
                        bean.setName("尺寸");

                        // 种类个数
                        int itemSize = 0;
                        for (int i = 0; i < result.getMsg().size(); i++) {
                            int size = result.getMsg().get(i).getColorlist().size();
                            if (size > itemSize) {
                                itemSize = size;
                            }
                        }
                        List<WareHousingInsetBean> itemList = new ArrayList<>(itemSize);
                        for (int i = 0; i < itemSize; i++) {
                            itemList.add(new WareHousingInsetBean());
                        }

                        for (int i = 0; i < result.getMsg().size(); i++) {

                            for (int j = 0; j < result.getMsg().get(i).getColorlist().size(); j++) {
                                WareHousingInsetBean item = itemList.get(j);
                                item.setName(result.getMsg().get(i).getColorlist().get(j).getColor());


                                switch (i) {
                                    case 0:
                                        bean.setTv1(result.getMsg().get(i).getSize());

                                        item.setTv1(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid1(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 1:
                                        bean.setTv2(result.getMsg().get(i).getSize());

                                        item.setTv2(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid2(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 2:
                                        bean.setTv3(result.getMsg().get(i).getSize());

                                        item.setTv3(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid3(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 3:
                                        bean.setTv4(result.getMsg().get(i).getSize());

                                        item.setTv4(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid4(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 4:
                                        bean.setTv5(result.getMsg().get(i).getSize());

                                        item.setTv5(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid5(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 5:
                                        bean.setTv6(result.getMsg().get(i).getSize());

                                        item.setTv6(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid6(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 6:
                                        bean.setTv7(result.getMsg().get(i).getSize());

                                        item.setTv7(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid7(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 7:
                                        bean.setTv8(result.getMsg().get(i).getSize());

                                        item.setTv8(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid8(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                    case 8:
                                        bean.setTv9(result.getMsg().get(i).getSize());

                                        item.setTv9(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
                                        item.setAliasid9(result.getMsg().get(i).getColorlist().get(j).getAliasid());
                                        break;
                                }
                            }

                        }

                        //  bean.setTotal("99");
                        mBeans.add(bean);
                        mBeans.addAll(itemList);


//                        for (int i = 0; i < result.getMsg().size(); i++) {
//                            int titleToatl = 0;
//                            for (int j = 0; j < result.getMsg().get(i).getColorlist().size(); j++) {
//                                WareHousingInsetBean bean1 = new WareHousingInsetBean();
//                                bean1.setName(result.getMsg().get(i).getColorlist().get(j).getColor());
//                                bean1.setTotal("0");
//
//                                titleToatl += result.getMsg().get(i).getColorlist().get(j).getSku();
//
//                                switch (j) {
//                                    case 0:
//                                        bean1.setTv1(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//
//                                        bean1.setAliasid1(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 1:
//                                        bean1.setTv2(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//
//                                        bean1.setAliasid2(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 2:
//                                        bean1.setTv3(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//                                        bean1.setAliasid3(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 3:
//                                        bean1.setTv4(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//                                        bean1.setAliasid4(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 4:
//                                        bean1.setTv5(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//                                        bean1.setAliasid5(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 5:
//                                        bean1.setTv6(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//                                        bean1.setAliasid6(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 6:
//                                        bean1.setTv7(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//                                        bean1.setAliasid7(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 7:
//                                        bean1.setTv8(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//                                        bean1.setAliasid8(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                    case 8:
//                                        bean1.setTv9(result.getMsg().get(i).getColorlist().get(j).getSku() + "");
//                                        bean1.setAliasid9(result.getMsg().get(i).getColorlist().get(j).getAliasid());
//                                        break;
//                                }
//
//                                //  bean.setTotal("99");
//                                mBeans.add(bean1);
//                            }
//
//                            mBeans.get(i + 1).setTitleTotal(titleToatl + "");
//                        }


                        rcView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        mProgressDilog.dismiss();
                        showErrorTipsDialog(e.getMessage(), null);
                    }
                });
    }

    private void setInputListener(EditText et1, List<EditText> mEts, int adapterPosition) {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (lastPos != adapterPosition) {
                    addIndex = "";
                    lastNum = 1;
                }

                lastPos = adapterPosition;

                jsTotal(mEts, adapterPosition, et1, s.toString());

            }
        });

        // 焦点改变
        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (lastPos != adapterPosition) {
                        addIndex = "";
                        lastNum = 1;
                        lastPos = adapterPosition;
                    }


                }
            }
        });
    }


    public void jsTotal(List<EditText> mEts, int adapterPosition, EditText et1, String s) {
        int total = 0;
        for (int i = 0; i < mEts.size(); i++) {
            if (!TextUtils.isEmpty(mEts.get(i).getText().toString().trim())) {
                total += Integer.parseInt(mEts.get(i).getText().toString().trim());
            }
        }
        // Log.i("tag",et1.getId()+"");
        mBeans.get(adapterPosition).setTotal(total + "");


        switch (et1.getId()) {
            case R.id.et_1:
                mBeans.get(adapterPosition).setEtText1(s.toString().trim());
                break;
            case R.id.et_2:
                mBeans.get(adapterPosition).setEtText2(s.toString().trim());
                break;
            case R.id.et_3:
                mBeans.get(adapterPosition).setEtText3(s.toString().trim());
                break;
            case R.id.et_4:
                mBeans.get(adapterPosition).setEtText4(s.toString().trim());
                break;
            case R.id.et_5:
                mBeans.get(adapterPosition).setEtText5(s.toString().trim());
                break;
            case R.id.et_6:
                mBeans.get(adapterPosition).setEtText6(s.toString().trim());
                break;
            case R.id.et_7:
                mBeans.get(adapterPosition).setEtText7(s.toString().trim());
                break;
            case R.id.et_8:
                mBeans.get(adapterPosition).setEtText8(s.toString().trim());
                break;
            case R.id.et_9:
                mBeans.get(adapterPosition).setEtText9(s.toString().trim());
                break;
        }

        if (rcView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && (rcView.isComputingLayout() == false)) {
            rcView.getAdapter().notifyDataSetChanged();
        }
    }


    @OnClick({R.id.iv_common_left, R.id.tv_as, R.id.tv_clear, R.id.tv_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left:
                finish();
                break;
            case R.id.tv_as:
                if (TextUtils.isEmpty(addIndex)) {
                    addNum();
                } else {
                    addNumForLast();
                }
                break;
            case R.id.tv_clear:
                clearNum();
                break;
            case R.id.tv_sure:
                mProgressDilog.show();
                save();
                break;

        }
    }

    //private List<MaixferBean> maixferBeans = new ArrayList<>();

    private void save() {
        if (mBeans.size() < 1) {
            mProgressDilog.dismiss();
            return;
        }
        List<MaixferBean> maixferBeans = new ArrayList<>();
        for (int i = 1; i < mBeans.size(); i++) {
            if (mBeans.get(i).getAliasid1() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText1())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid1());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText1()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid2() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText2())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid2());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText2()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid3() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText3())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid3());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText3()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid4() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText4())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid4());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText4()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid5() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText5())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid5());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText5()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid6() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText6())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid6());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText6()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid7() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText7())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid7());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText7()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid8() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText8())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid8());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText8()));
                maixferBeans.add(bean);
            }
            if (mBeans.get(i).getAliasid9() > 0 && !TextUtils.isEmpty(mBeans.get(i).getEtText9())) {
                MaixferBean bean = new MaixferBean();
                bean.setAliasid(mBeans.get(i).getAliasid9());
                bean.setQty(Integer.parseInt(mBeans.get(i).getEtText9()));
                maixferBeans.add(bean);
            }

            //  bean.setQty(mBeans.get(i).getEtText1());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userid", Integer.parseInt(SharePreferenceUtil.getInstance().getString("userId")));
        params.put("tableid", getIntent().getIntExtra("tableid", 0));
        params.put("rowid", getIntent().getIntExtra("id", 0));
        params.put("maixfer", maixferBeans);
        Map<String, Object> data = new HashMap<>();
        data.put("method", "pdacw_savepdtmaixfer_ard");
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
                                playSoundAndVirate();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                showSureDialog(result.getValue(), new OnSureClickListener() {
                                    @Override
                                    public void onSure() {

                                    }
                                });
                            }
                        } else {
                            showSureDialog(result.getValue(), new OnSureClickListener() {
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

    private void clearNum() {
        List<EditText> mEts = maps.get(lastPos);
        for (int i = 0; i < mEts.size(); i++) {
            mEts.get(i).setText("");
            EditText et1 = mEts.get(i);
            switch (et1.getId()) {
                case R.id.et_1:
                    mBeans.get(lastPos).setEtText1("");
                    break;
                case R.id.et_2:
                    mBeans.get(lastPos).setEtText2("");
                    break;
                case R.id.et_3:
                    mBeans.get(lastPos).setEtText3("");
                    break;
                case R.id.et_4:
                    mBeans.get(lastPos).setEtText4("");
                    break;
                case R.id.et_5:
                    mBeans.get(lastPos).setEtText5("");
                    break;
                case R.id.et_6:
                    mBeans.get(lastPos).setEtText6("");
                    break;
                case R.id.et_7:
                    mBeans.get(lastPos).setEtText7("");
                    break;
                case R.id.et_8:
                    mBeans.get(lastPos).setEtText8("");
                    break;
                case R.id.et_9:
                    mBeans.get(lastPos).setEtText9("");
                    break;
            }

        }

        int total = 0;
        for (int i = 0; i < mEts.size(); i++) {
            if (!TextUtils.isEmpty(mEts.get(i).getText().toString().trim())) {
                total += Integer.parseInt(mEts.get(i).getText().toString().trim());
            }
        }

        mBeans.get(lastPos).setTotal(total + "");

        if (rcView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && (rcView.isComputingLayout() == false)) {
            rcView.getAdapter().notifyDataSetChanged();
        }

        // 清除lastNum;
        lastNum = 0;
        addIndex = "";
    }

    private String addIndex = "";

    private void addNum() {
        List<EditText> mEts = maps.get(lastPos);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mEts.size(); i++) {
            if (TextUtils.isEmpty(mEts.get(i).getText().toString().trim())) {
                builder.append(i).append(",");
                mEts.get(i).setText(lastNum + "");
                EditText et1 = mEts.get(i);
                switch (et1.getId()) {
                    case R.id.et_1:
                        mBeans.get(lastPos).setEtText1(lastNum + "");
                        break;
                    case R.id.et_2:
                        mBeans.get(lastPos).setEtText2(lastNum + "");
                        break;
                    case R.id.et_3:
                        mBeans.get(lastPos).setEtText3(lastNum + "");
                        break;
                    case R.id.et_4:
                        mBeans.get(lastPos).setEtText4(lastNum + "");
                        break;
                    case R.id.et_5:
                        mBeans.get(lastPos).setEtText5(lastNum + "");
                        break;
                    case R.id.et_6:
                        mBeans.get(lastPos).setEtText6(lastNum + "");
                        break;
                    case R.id.et_7:
                        mBeans.get(lastPos).setEtText7(lastNum + "");
                        break;
                    case R.id.et_8:
                        mBeans.get(lastPos).setEtText8(lastNum + "");
                        break;
                    case R.id.et_9:
                        mBeans.get(lastPos).setEtText9(lastNum + "");
                        break;
                }


            }
        }

        addIndex = builder.substring(0, builder.toString().length() - 1);

        int total = 0;
        for (int i = 0; i < mEts.size(); i++) {
            if (!TextUtils.isEmpty(mEts.get(i).getText().toString().trim())) {
                total += Integer.parseInt(mEts.get(i).getText().toString().trim());
            }
        }

        mBeans.get(lastPos).setTotal(total + "");

        if (rcView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && (rcView.isComputingLayout() == false)) {
            rcView.getAdapter().notifyDataSetChanged();
        }
    }


    private void addNumForLast() {
        lastNum++;
        List<EditText> mEts = maps.get(lastPos);
        if (!TextUtils.isEmpty(addIndex)) {
            String[] indexArr = addIndex.split(",");
            for (int i = 0; i < indexArr.length; i++) {
                mEts.get(Integer.parseInt(indexArr[i])).setText(lastNum + "");
                EditText et1 = mEts.get(Integer.parseInt(indexArr[i]));
                switch (et1.getId()) {
                    case R.id.et_1:
                        mBeans.get(lastPos).setEtText1(lastNum + "");
                        break;
                    case R.id.et_2:
                        mBeans.get(lastPos).setEtText2(lastNum + "");
                        break;
                    case R.id.et_3:
                        mBeans.get(lastPos).setEtText3(lastNum + "");
                        break;
                    case R.id.et_4:
                        mBeans.get(lastPos).setEtText4(lastNum + "");
                        break;
                    case R.id.et_5:
                        mBeans.get(lastPos).setEtText5(lastNum + "");
                        break;
                    case R.id.et_6:
                        mBeans.get(lastPos).setEtText6(lastNum + "");
                        break;
                    case R.id.et_7:
                        mBeans.get(lastPos).setEtText7(lastNum + "");
                        break;
                    case R.id.et_8:
                        mBeans.get(lastPos).setEtText8(lastNum + "");
                        break;
                    case R.id.et_9:
                        mBeans.get(lastPos).setEtText9(lastNum + "");
                        break;
                }

            }
        }
        int total = 0;
        for (int i = 0; i < mEts.size(); i++) {
            if (!TextUtils.isEmpty(mEts.get(i).getText().toString().trim())) {
                total += Integer.parseInt(mEts.get(i).getText().toString().trim());
            }
        }
        mBeans.get(lastPos).setTotal(total + "");
        if (rcView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && (rcView.isComputingLayout() == false)) {
            rcView.getAdapter().notifyDataSetChanged();
        }
    }


}
