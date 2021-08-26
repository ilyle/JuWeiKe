package com.dagen.storage.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dagen.storage.R;
import com.dagen.storage.base.BaseMoudleActivity;
import com.dagen.storage.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//入库单-商品新增
public class WareHousingEntryInsertActivity extends BaseMoudleActivity {




    @Override
    public int getLayoutId() {
        return R.layout.activity_ware_hosing_insert;
    }

    @Override
    public void initData() {
        super.initData();
    }

   /* @OnClick({R.id.iv_switch})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_common_left:
                finish();
                break;

        }
    }*/

    private List<String> getRowDataList(List<DataBean> result) {
        List<String> data = new ArrayList<>();
        for (DataBean dataBean : result) {
            data.add(dataBean.size);
        }
        data.add("行总计");
        return data;
    }

    private List<String> getColumnDataList(List<DataBean> result) {
        List<String> data = new ArrayList<>();
        for (DataBean dataBean : result) {
            for (ListBean listBean : dataBean.colorlist) {
                if (!data.contains(listBean.color)) {
                    data.add(listBean.color);
                }
            }
        }
        return data;
    }

    private List<AdapterBean> getAdapterList(List<DataBean> result) {
        List<AdapterBean> data = new ArrayList<>();
        for (DataBean dataBean : result) {
            for (ListBean listBean : dataBean.colorlist) {
                int index = -1;
                for (int i = 0; i < data.size(); i++) {
                    AdapterBean datum = data.get(i);
                    if (datum.color.equals(listBean.color)) {
                        index = i;
                        break;
                    }
                }
                AdapterListBean adapterListBean = new AdapterListBean();
                adapterListBean.size = dataBean.size;
                adapterListBean.sku = listBean.sku;
                adapterListBean.aliasid = listBean.aliasid;
                if (index >= 0) {
                    data.get(index).adapterList.add(adapterListBean);
                } else {
                    AdapterBean adapterBean = new AdapterBean();
                    adapterBean.color = listBean.color;
                    adapterBean.adapterList.add(adapterListBean);
                    data.add(adapterBean);
                }
            }
        }
        return data;
    }

    class DataBean {
        String size;
        int saleprice;
        String salepricename;
        List<ListBean> colorlist;
    }

    public class ListBean {
        public  String color;
        public int aliasid;
        public int sku;
    }

   public class AdapterBean {
       public String color;
       public List<AdapterListBean> adapterList = new ArrayList<>();
    }

    public class AdapterListBean {
        public  String size;
        public  int aliasid;
        public int sku;
        public int custom = -1;
    }

}
