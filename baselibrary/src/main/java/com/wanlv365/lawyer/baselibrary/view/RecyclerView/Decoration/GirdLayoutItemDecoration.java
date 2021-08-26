package com.wanlv365.lawyer.baselibrary.view.RecyclerView.Decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//GrideView样式的通用分割线
public class GirdLayoutItemDecoration extends RecyclerView.ItemDecoration{

    private Drawable mDivider;


    public GirdLayoutItemDecoration(Context context, int drawableResId){

        //获取drawable
        mDivider= ContextCompat.getDrawable(context, drawableResId);
    }


    //留出分割线的位置
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //留出分割线位置  右边 和下边
       /* outRect.bottom=mDivider.getIntrinsicHeight();
        outRect.right=mDivider.getIntrinsicWidth();*/


        //留出分割线位置  右边 和下边 最底部不用留  最右边也不用留

        int bottom=mDivider.getIntrinsicHeight();
        int right=mDivider.getIntrinsicWidth();

        if(isLastColoum(view,parent)){
            right=0;
        }

        if(isLastRow(view,parent)){
            bottom=0;

        }



        outRect.right=right;
        outRect.bottom=bottom;


    }

    //最后一列
    private boolean isLastColoum(View view, RecyclerView parent) {

        //当前位置
        int currentPosition= ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        //获取列数
        int spanCount=getSpanCount(parent);


        return (currentPosition+1)%spanCount==0;

    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager manager=parent.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager=(GridLayoutManager) manager;
            return gridLayoutManager.getSpanCount();
        }

        return 1;
    }

    //最后一行 当前位置+1>（行数-1）*列数
    private boolean isLastRow(View view, RecyclerView parent) {
        //当前位置
        int currentPosition= ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        //获取列数
        int spanCount=getSpanCount(parent);

        //获取行数 总条目/列数

        int rowNum=parent.getAdapter().getItemCount()%spanCount==0
                ?parent.getAdapter().getItemCount()/spanCount
                :(parent.getAdapter().getItemCount()/spanCount+1);

        return (currentPosition+1)>(rowNum-1)*spanCount;
    }



    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorzital(c,parent);
        drawVertical(c,parent);
    }

    //绘制垂直方向
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int childCount=parent.getChildCount();

        for(int i=0;i<childCount;i++){
            View childView=parent.getChildAt(i);
            RecyclerView.LayoutParams params=(RecyclerView.LayoutParams) childView.getLayoutParams();

            int left=childView.getRight()+params.rightMargin;
            int right=left+mDivider.getIntrinsicWidth();

            int top=childView.getTop()-params.topMargin;
            int bottom=childView.getBottom()+params.bottomMargin;

            mDivider.setBounds(left,top,right,bottom);

            mDivider.draw(canvas);

        }
    }

    //绘制水平方向
    private void drawHorzital(Canvas canvas,RecyclerView parent) {
        int childCount=parent.getChildCount();

        for(int i=0;i<childCount;i++){
            View childView=parent.getChildAt(i);


            RecyclerView.LayoutParams params=(RecyclerView.LayoutParams) childView.getLayoutParams();

            int left=childView.getLeft()-params.leftMargin;
            int right=childView.getRight()+mDivider.getIntrinsicWidth()+params.rightMargin;

            int top=childView.getBottom()+params.bottomMargin;
            int bottom=top+mDivider.getIntrinsicHeight();

            mDivider.setBounds(left,top,right,bottom);


            if(!isLastRow(childView,parent))
            mDivider.draw(canvas);

        }
    }
}
