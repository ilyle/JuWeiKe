package com.dagen.storage.bean;

import java.util.List;

public class HomeListBean {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : [{"id":44876,"tot_dbname1":"总件数","tot_dbcname1":"200","tot_dbname2":"总款数","tot_dbcname2":"2","tot_dbname3":"操作人","tot_dbcname3":"赵林","list":[{"name":"单号","value":"docno"},{"name":"单据日期","value":"billdate"}]}]
     */

    private int succeed;
    private String value;
    private int code;
    private List<MsgBean> msg;

    public int getSucceed() {
        return succeed;
    }

    public void setSucceed(int succeed) {
        this.succeed = succeed;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * id : 44876
         * tot_dbname1 : 总件数
         * tot_dbcname1 : 200
         * tot_dbname2 : 总款数
         * tot_dbcname2 : 2
         * tot_dbname3 : 操作人
         * tot_dbcname3 : 赵林
         * list : [{"name":"单号","value":"docno"},{"name":"单据日期","value":"billdate"}]
         */

        private int id;
        private String tot_dbname1;
        private String tot_dbcname1;
        private String tot_dbname2;
        private String tot_dbcname2;
        private String tot_dbname3;
        private String tot_dbcname3;
        private List<ListBean> list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTot_dbname1() {
            return tot_dbname1;
        }

        public void setTot_dbname1(String tot_dbname1) {
            this.tot_dbname1 = tot_dbname1;
        }

        public String getTot_dbcname1() {
            return tot_dbcname1;
        }

        public void setTot_dbcname1(String tot_dbcname1) {
            this.tot_dbcname1 = tot_dbcname1;
        }

        public String getTot_dbname2() {
            return tot_dbname2;
        }

        public void setTot_dbname2(String tot_dbname2) {
            this.tot_dbname2 = tot_dbname2;
        }

        public String getTot_dbcname2() {
            return tot_dbcname2;
        }

        public void setTot_dbcname2(String tot_dbcname2) {
            this.tot_dbcname2 = tot_dbcname2;
        }

        public String getTot_dbname3() {
            return tot_dbname3;
        }

        public void setTot_dbname3(String tot_dbname3) {
            this.tot_dbname3 = tot_dbname3;
        }

        public String getTot_dbcname3() {
            return tot_dbcname3;
        }

        public void setTot_dbcname3(String tot_dbcname3) {
            this.tot_dbcname3 = tot_dbcname3;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * name : 单号
             * value : docno
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
