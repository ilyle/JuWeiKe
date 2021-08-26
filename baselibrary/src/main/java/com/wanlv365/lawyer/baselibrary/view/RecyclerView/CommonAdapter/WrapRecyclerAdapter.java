package com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter;


import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//头部和底部包裹adapter

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private RecyclerView.Adapter mAdapter;

    private SparseArray<View> mHeaders,mFooters;//头部和底部的集合

    private static int BASE_HEADER_KEY=1000000;
    private static int BASE_FOOTER_KEY=2000000;

    public WrapRecyclerAdapter(RecyclerView.Adapter adapter){
        this.mAdapter=adapter;
        mHeaders=new SparseArray();
        mFooters=new SparseArray();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // viewType 可能就是 SparseArray 的key
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaders.get(viewType);
            return createFooterHeaderViewHolder(headerView);
        }

        if (isFooterViewType(viewType)) {
            View footerView = mFooters.get(viewType);
            return createFooterHeaderViewHolder(footerView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);

    }


    //创建头部和底部的viewholder
    private RecyclerView.ViewHolder createFooterHeaderViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {};
    }


    /**
     * 是不是底部类型
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooters.indexOfKey(viewType);
        return position >= 0;
    }


    /**
     * 是不是头部类型
     */
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaders.indexOfKey(viewType);
        return position >= 0;
    }


    /**
     * 是不是底部位置
     */
    private boolean isFooterPosition(int position) {
        return position >= (mHeaders.size() + mAdapter.getItemCount());
    }

    /**
     * 是不是头部位置
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaders.size();
    }



    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return mHeaders.keyAt(position);
        }
        if (isFooterPosition(position)) {
            // 直接返回position位置的key
            position = position - mHeaders.size() - mAdapter.getItemCount();
            return mFooters.keyAt(position);
        }
        // 返回列表Adapter的getItemViewType
        position = position - mHeaders.size();
        return mAdapter.getItemViewType(position);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        // 计算一下位置
        int mposition = position - mHeaders.size();//支持多布局
        mAdapter.onBindViewHolder(holder,mposition);
      //  mAdapter.onBindViewHolder(holder, getItemViewType(mposition));

    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount()+mHeaders.size()+mFooters.size();
    }


    /**
     * 获取列表的Adapter
     */
    private RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    // 添加头部
    public void addHeaderView(View view) {
        int position = mHeaders.indexOfValue(view);
        if (position < 0) {
            mHeaders.put(BASE_HEADER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    // 添加底部
    public void addFooterView(View view) {
        int position = mFooters.indexOfValue(view);
        if (position < 0) {
            mFooters.put(BASE_FOOTER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    // 移除头部
    public void removeHeaderView(View view) {
        int index = mHeaders.indexOfValue(view);
        if (index < 0) return;
        mHeaders.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooters.indexOfValue(view);
        if (index < 0) return;
        mFooters.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recycler
     * @version 1.0
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }


}
