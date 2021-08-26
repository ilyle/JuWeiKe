package com.dagen.storage.support;

import android.text.TextUtils;

import com.dagen.storage.bean.QueryQuestBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpJsonBuilder {

    private String command;
    private ParamsBeanX params;

    private HttpJsonBuilder(Builder builder) {
        this.command = builder.command;
        this.params = builder.params;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static class Builder {
        private String command;
        private ParamsBeanX params=new ParamsBeanX();

        public Builder setCommand(String commond){
            this.command=commond;
            return this;
        }

        public Builder setColumns(String...colums){
            if(colums.length==0)return this;
            List<String>mstrs=new ArrayList<>();
            for(int i=0;i<colums.length;i++){
                mstrs.add(colums[i]);
            }
            params.setColumns(mstrs);
            return this;
        }

        public Builder setTable(String table){
            params.setTable(table);
            return this;
        }


        public Builder setParams(List<HttpJsonBuilder.ParamsBeanX.ParamsBean.Expr1Bean>mBeans,String combine){
            if(params==null)params= new ParamsBeanX();

          if(mBeans.size()==1){
              ParamsBeanX.ParamsBean parambs=new ParamsBeanX.ParamsBean();
              parambs.setCondition(mBeans.get(0).getCondition());
              parambs.setColumn(mBeans.get(0).getColumn());
              parambs.setCombine(combine);
              params.setParams(parambs);
          }else {
              ParamsBeanX.ParamsBean parambeans=new ParamsBeanX.ParamsBean();
              for(int i=0;i<mBeans.size();i++){
                  switch (i){
                      case 0:
                          ParamsBeanX.ParamsBean.Expr1Bean bean1=new ParamsBeanX.ParamsBean.Expr1Bean();
                          bean1.setCondition(mBeans.get(i).getCondition());
                          bean1.setColumn(mBeans.get(i).getColumn());
                          parambeans.setExpr1(bean1);
                          break;
                      case 1:
                          ParamsBeanX.ParamsBean.Expr2Bean bean2=new ParamsBeanX.ParamsBean.Expr2Bean();
                          bean2.setCondition(mBeans.get(i).getCondition());
                          bean2.setColumn(mBeans.get(i).getColumn());
                          parambeans.setExpr2(bean2);
                          break;
                  }
              }
              parambeans.setCombine(combine);
              params.setParams(parambeans);
          }
          return this;
        }

        public HttpJsonBuilder build() {
            return new HttpJsonBuilder(this);
        }
    }


    public static class ParamsBeanX {
        /**
         * columns : ["ID"]
         * params : {"expr1":{"condition":"zl@nf.com","column":"EMAIL"},"expr2":{"condition":"kbzl880925","column":"PASSWORDHASH"},"combine":"and"}
         * table : USERS
         */

        private ParamsBeanX.ParamsBean params;
        private String table;
        private List<String> columns;


        public ParamsBeanX.ParamsBean getParams() {
            return params;
        }

        public void setParams(ParamsBeanX.ParamsBean params) {
            this.params = params;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public List<String> getColumns() {
            return columns;
        }

        public void setColumns(List<String> columns) {
            this.columns = columns;
        }



        public static class ParamsBean {
            /**
             * expr1 : {"condition":"zl@nf.com","column":"EMAIL"}
             * expr2 : {"condition":"kbzl880925","column":"PASSWORDHASH"}
             * combine : and
             */

            private ParamsBeanX.ParamsBean.Expr1Bean expr1;
            private ParamsBeanX.ParamsBean.Expr2Bean expr2;
            private String combine;

            private String condition;
            private String column;


            public String getCondition() {
                return condition;
            }

            public void setCondition(String condition) {
                this.condition = condition;
            }

            public String getColumn() {
                return column;
            }


            public void setColumn(String column) {
                this.column = column;
            }

            public ParamsBeanX.ParamsBean.Expr1Bean getExpr1() {
                return expr1;
            }

            public void setExpr1(ParamsBeanX.ParamsBean.Expr1Bean expr1) {
                this.expr1 = expr1;
            }

            public ParamsBeanX.ParamsBean.Expr2Bean getExpr2() {
                return expr2;
            }

            public void setExpr2(ParamsBeanX.ParamsBean.Expr2Bean expr2) {
                this.expr2 = expr2;
            }

            public String getCombine() {
                return combine;
            }

            public void setCombine(String combine) {
                this.combine = combine;
            }

            public static class Expr1Bean {
                /**
                 * condition : zl@nf.com
                 * column : EMAIL
                 */

                private String condition;
                private String column;

                public String getCondition() {
                    return condition;
                }

                public void setCondition(String condition) {
                    this.condition = condition;
                }

                public String getColumn() {
                    return column;
                }

                public void setColumn(String column) {
                    this.column = column;
                }
            }

            public static class Expr2Bean {
                /**
                 * condition : kbzl880925
                 * column : PASSWORDHASH
                 */

                private String condition;
                private String column;

                public String getCondition() {
                    return condition;
                }

                public void setCondition(String condition) {
                    this.condition = condition;
                }

                public String getColumn() {
                    return column;
                }

                public void setColumn(String column) {
                    this.column = column;
                }
            }
        }
    }


}
