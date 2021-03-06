package com.dagen.storage.bean;

import java.util.List;

public class OutLogInfoBean {

    /**
     * msg : {"jycw":"无","jyalias":"无","img":"","qty":0,"qtyplan":1,"row1":"visible","row2":"visible","row3":"visible","row4":"visible","totqty":0,"pdtqty":1,"description":"由电商拣货单JHD-20210219-00005生成"}
     * succeed : 0
     * value : ok
     * code : 200
     * item : [{"alias":"3190980611110","cw":"B","qty":0,"qtyplan":1}]
     */

    private MsgBean msg;
    private int succeed;
    private String value;
    private int code;
    private List<ItemBean> item;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

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

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class MsgBean {
        /**
         * jycw : 无
         * jyalias : 无
         * img :
         * qty : 0
         * qtyplan : 1
         * row1 : visible
         * row2 : visible
         * row3 : visible
         * row4 : visible
         * totqty : 0
         * pdtqty : 1
         * description : 由电商拣货单JHD-20210219-00005生成
         */

        private String jycw;
        private String jyalias;
        private String img;
        private int qty;
        private int qtyplan;
        private String row1;
        private String row2;
        private String row3;
        private String row4;
        private int totqty;
        private int pdtqty;
        private int totallqty;
        private int totsoqty;
        private int lock_status;
        private String description;
        private String ifover;
        private String mptvalue; // 建议条码的品名

        public int getLock_status() {
            return lock_status;
        }

        public void setLock_status(int lock_status) {
            this.lock_status = lock_status;
        }

        public String getIfover() {
            return ifover;
        }

        public int getTotallqty() {
            return totallqty;
        }

        public int getTotsoqty() {
            return totsoqty;
        }

        public void setTotsoqty(int totsoqty) {
            this.totsoqty = totsoqty;
        }

        public void setTotallqty(int totallqty) {
            this.totallqty = totallqty;
        }

        public void setIfover(String ifover) {
            this.ifover = ifover;
        }

        public String getJycw() {
            return jycw;
        }

        public void setJycw(String jycw) {
            this.jycw = jycw;
        }

        public String getJyalias() {
            return jyalias;
        }

        public void setJyalias(String jyalias) {
            this.jyalias = jyalias;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getQtyplan() {
            return qtyplan;
        }

        public void setQtyplan(int qtyplan) {
            this.qtyplan = qtyplan;
        }

        public String getRow1() {
            return row1;
        }

        public void setRow1(String row1) {
            this.row1 = row1;
        }

        public String getRow2() {
            return row2;
        }

        public void setRow2(String row2) {
            this.row2 = row2;
        }

        public String getRow3() {
            return row3;
        }

        public void setRow3(String row3) {
            this.row3 = row3;
        }

        public String getRow4() {
            return row4;
        }

        public void setRow4(String row4) {
            this.row4 = row4;
        }

        public int getTotqty() {
            return totqty;
        }

        public void setTotqty(int totqty) {
            this.totqty = totqty;
        }

        public int getPdtqty() {
            return pdtqty;
        }

        public void setPdtqty(int pdtqty) {
            this.pdtqty = pdtqty;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMptvalue() {
            return mptvalue;
        }

        public void setMptvalue(String mptvalue) {
            this.mptvalue = mptvalue;
        }
    }

    public static class ItemBean {
        /**
         * itemid: 806,
         * alias : 3190980611110
         * cw : B
         * qty : 0
         * qtyplan : 1
         */

        private int itemid; // 明细id
        private String alias; // 条码
        private String cw; // 储位
        private int qty;
        private int qtyplan;
        private int qtybook;
        private String mptvalue; // 品名
        private String name1;
        private String value1;
        private String name2;
        private String value2;
        private String name3;
        private String value3;

        public int getQtybook() {
            return qtybook;
        }

        public void setQtybook(int qtybook) {
            this.qtybook = qtybook;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getCw() {
            return cw;
        }

        public void setCw(String cw) {
            this.cw = cw;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getQtyplan() {
            return qtyplan;
        }

        public void setQtyplan(int qtyplan) {
            this.qtyplan = qtyplan;
        }

        public String getMptvalue() {
            return mptvalue;
        }

        public void setMptvalue(String mptvalue) {
            this.mptvalue = mptvalue;
        }

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getName3() {
            return name3;
        }

        public void setName3(String name3) {
            this.name3 = name3;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }
    }
}
