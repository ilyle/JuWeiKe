package com.dagen.storage.support;

import android.util.Log;

import com.dagen.storage.bean.ExprBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HttpJsonBuilder2 {

    private String command;
    private String id;
    private List<ExprBean>beans;
    private String combine;
    private String table;
    private List<String> columns;


    private HttpJsonBuilder2(Builder builder) {
        this.command = builder.command;
        this.id=builder.id;
        this.beans=builder.beans;
        this.combine=builder.combine;
        this.table=builder.table;
        this.columns=builder.columns;
    }

    public String toJson() {
        try {
            JSONObject obj1=new JSONObject();
            obj1.put("command",command);

            JSONObject obj2=new JSONObject();
            obj2.put("params",HttpJsonBuilder2.buildParam(beans,"and"));
            obj2.put("table",table);

            JSONArray array=new JSONArray();
            for(int i=0;i<columns.size();i++){
                array.put(i,columns.get(i));
            }
            obj2.put("columns",array);
            obj1.put("params",obj2);

           return  "["+obj1.toString()+"]";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject buildParam(List<ExprBean>mBeans, String combine){
        if(mBeans.size()==1){
            JSONObject object=new JSONObject();
            try {
                object.put("condition",mBeans.get(0).getCondition());
                object.put("column",mBeans.get(0).getColumn());
                object.put("combine",combine);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }else {
            JSONObject object=new JSONObject();
            try {
                for(int i=0;i<mBeans.size();i++){
                    JSONObject object1=new JSONObject();
                    object1.put("condition",mBeans.get(i).getCondition());
                    object1.put("column",mBeans.get(i).getColumn());
                    object.put("expr"+(i+1),object1);
                }

                object.put("combine",combine);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }
    }

    public static class Builder {
        private String command;
        private String id;
        private List<ExprBean>beans;
        private String combine;
        private String table;
        private List<String> columns;

        public Builder setCommand(String commond){
            this.command=commond;
            return this;
        }
        public Builder setId(String id){
            this.id=id;
            return this;
        }

        public Builder setColumns(String...colums){
            if(colums.length==0)return this;
            List<String>mstrs=new ArrayList<>();
            for(int i=0;i<colums.length;i++){
                mstrs.add(colums[i]);
            }
            this.columns=mstrs;
            return this;
        }

        public Builder setTable(String table){
           this.table=table;
            return this;
        }

        public Builder setParams(List<ExprBean>mBeans, String combine){
            this.beans=mBeans;
            this.combine=combine;
          return this;
        }

        public HttpJsonBuilder2 build() {
            return new HttpJsonBuilder2(this);
        }
    }





}
