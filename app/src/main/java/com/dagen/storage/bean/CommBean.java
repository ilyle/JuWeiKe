package com.dagen.storage.bean;

public class CommBean {
    /**
     * succeed : 0
     * value : ok
     * code : 0
     * msg :
     */

    private int succeed;
    private String value;
    private int code;
    private String msg;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
