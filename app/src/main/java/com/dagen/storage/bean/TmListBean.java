package com.dagen.storage.bean;

import java.util.List;

public class TmListBean {


    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : [{"name":"000019","value":"0-0","pdtid":9021},{"name":"001","value":"0-0","pdtid":9022},{"name":"00101","value":"---","pdtid":9023},{"name":"00201","value":"---","pdtid":9024},{"name":"0320000107","value":"","pdtid":46375},{"name":"0321000107","value":"","pdtid":46376},{"name":"10B00001","value":"ZR-ZR","pdtid":45889},{"name":"122333","value":"","pdtid":46378},{"name":"1234567","value":"","pdtid":46363},{"name":"123457","value":"","pdtid":46377}]
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
         * name : 000019
         * value : 0-0
         * pdtid : 9021
         */

        private String name;
        private String value;
        private int pdtid;

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

        public int getPdtid() {
            return pdtid;
        }

        public void setPdtid(int pdtid) {
            this.pdtid = pdtid;
        }
    }
}
