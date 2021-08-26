package com.dagen.storage.utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dagen.storage.R;

import java.text.SimpleDateFormat;
import java.util.Date;

//选择器工具类
public class PickViewUtils {

    //只显示年的时间选择器
    public TimePickerView initYearPicker(Context context){
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(mTimePickListener!=null){
                    mTimePickListener.onTimeSel(getTime2(date,"yyyy"));
                }

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, false, false, false, false, false})
                .isDialog(false)
                .setCancelColor(context.getResources().getColor(R.color.color_666666))
                .setSubmitColor(context.getResources().getColor(R.color.color_478DFE))
                //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        return pvTime;
    }



    //显示年月日的时间选择器
    public TimePickerView initYearMonthDayPicker(Context context){
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(mTimePickListener!=null){
                    mTimePickListener.onTimeSel(getTime2(date,"yyyyMMdd"));
                }

            }
        })

                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(false)
                .setCancelColor(context.getResources().getColor(R.color.color_666666))
                .setSubmitColor(context.getResources().getColor(R.color.color_478DFE))
                //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        return pvTime;
    }


    private  TimePickListener mTimePickListener;

    public void setTimePickListener(TimePickListener mTimePickListener) {
        this.mTimePickListener = mTimePickListener;
    }

    public interface TimePickListener{
        void onTimeSel(String year);
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private  String getTime2(Date date,String formats) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(formats);
        return format.format(date);
    }




}
