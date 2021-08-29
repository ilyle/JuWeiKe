package com.dagen.storage.bean;

/**
 * Created by xujie on 2021/8/29.
 * Mail : 617314917@qq.com
 */
public class CwQtyBean {

    private String cw;
    private String qty;

    public String getCw() {
        return cw;
    }

    public void setCw(String cw) {
        this.cw = cw;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return cw + "(" + qty + ")";
    }
}
