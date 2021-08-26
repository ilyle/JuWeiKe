package com.wanlv365.lawyer.baselibrary.view.RecyclerView.Decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


//ListView样式的通用分割线
public class LinearLayoutItemDecoration extends RecyclerView.ItemDecoration{

    private Drawable mDivider;

    public LinearLayoutItemDecoration(Context context,int drawableResId){

        //获取drawable
        mDivider= ContextCompat.getDrawable(context, drawableResId);
    }


    //留出分割线的位置
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position=parent.getChildAdapterPosition(view);
        if(position!=0){
            outRect.top=mDivider.getIntrinsicHeight();
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount=parent.getChildCount();

        Rect rect=new Rect();

        rect.left=parent.getPaddingLeft();
        rect.right=parent.getWidth()-parent.getPaddingRight();


        for(int i=1;i<childCount;i++){
            rect.bottom=parent.getChildAt(i).getTop();
            rect.top=rect.bottom-mDivider.getIntrinsicHeight();
            mDivider.setBounds(rect);

            mDivider.draw(c);
        }


    }
}
