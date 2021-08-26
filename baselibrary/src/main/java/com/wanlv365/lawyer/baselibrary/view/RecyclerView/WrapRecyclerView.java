package com.wanlv365.lawyer.baselibrary.view.RecyclerView;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wanlv365.lawyer.baselibrary.view.RecyclerView.CommonAdapter.WrapRecyclerAdapter;

public class WrapRecyclerView extends RecyclerView {


    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    private View mEmptyView, mLoadingView;

    // 包裹了一层的头部底部Adapter
    private WrapRecyclerAdapter mWrapRecyclerAdapter;
    // 这个是列表数据的Adapter
    private Adapter mAdapter;


    public WrapRecyclerView(Context context) {
        this(context,null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {

        // 为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
            mAdapter = null;
        }

        this.mAdapter = adapter;



        if(adapter instanceof WrapRecyclerAdapter){
            mWrapRecyclerAdapter= (WrapRecyclerAdapter) adapter;
        }else {
            mWrapRecyclerAdapter=new WrapRecyclerAdapter(adapter);


        }

        //删除的问题是因为列表的adapter改变了，但是wrapRecycleAdapter并没有修改。所以需要关联 观察者模式

        super.setAdapter(mWrapRecyclerAdapter);

        adapter.registerAdapterDataObserver(mAdapterDataObserver);

        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(this);

    }


    /**
     * 添加一个空列表数据页面
     */
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 添加一个正在加载数据的页面
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }

    /**
     * Adapter数据改变的方法
     */
    private void dataChanged() {
        if (mAdapter.getItemCount() == 0) {
            // 没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
            } else {
                mEmptyView.setVisibility(GONE);
            }
        }
    }




    //添加头部
    public void addHeaderView(View view){
        //避免重复添加
       if(mWrapRecyclerAdapter!=null){
           mWrapRecyclerAdapter.addHeaderView(view);
       }
    }

    //添加底部
    public void addFooterView(View view){
        //避免重复添加
        if(mWrapRecyclerAdapter!=null){
            mWrapRecyclerAdapter.addFooterView(view);
        }
    }


    //移除头部
    public void removeHeaderView(View view){
        //避免重复添加
        if(mWrapRecyclerAdapter!=null){
            mWrapRecyclerAdapter.removeHeaderView(view);
        }
    }

    //移除底部
    public void removeFooterView(View view){
        //避免重复添加
        if(mWrapRecyclerAdapter!=null){
            mWrapRecyclerAdapter.removeFooterView(view);
        }
    }



    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyDataSetChanged();

            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemRemoved(positionStart);

            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);

            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);

            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart,payload);

            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemInserted(positionStart);

            dataChanged();
        }
    };




}