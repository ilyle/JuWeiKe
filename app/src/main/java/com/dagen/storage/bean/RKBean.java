package com.dagen.storage.bean;

public class RKBean {

    /**
     * succeed : -1
     * value : ispdt
     * code : 200
     * msg : {"ifpdt":"Y"}
     */

    private int succeed;
    private String value;
    private int code;
    private MsgBean msg;

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

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * ifpdt : Y
         */

        private String ifpdt;

        public String getIfpdt() {
            return ifpdt;
        }

        public void setIfpdt(String ifpdt) {
            this.ifpdt = ifpdt;
        }
    }
}
