package com.dagen.storage.bean;

import java.util.List;

public class OutLogBean {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : [{"id":12,"tot_qty":0,"pdtqty":0,"operater":"root","docno":"MQS2101120001","billdate":20210112},{"id":77,"tot_qty":0,"pdtqty":0,"operater":"root","docno":"MQS2102010001","billdate":20210201},{"id":211,"tot_qty":0,"pdtqty":1,"operater":"root","docno":"MQS2103300002","billdate":20210330},{"id":210,"tot_qty":0,"pdtqty":1,"operater":"root","docno":"MQS2103300001","billdate":20210330},{"id":106,"tot_qty":0,"pdtqty":0,"operater":"root","docno":"MQS2102040004","billdate":20210204},{"id":109,"tot_qty":0,"pdtqty":0,"operater":"root","docno":"MQS2102040006","billdate":20210204},{"id":105,"tot_qty":0,"pdtqty":0,"operater":"root","docno":"MQS2102040003","billdate":20210204},{"id":110,"tot_qty":0,"pdtqty":1,"operater":"root","docno":"MQS2102040007","billdate":20210204},{"id":92,"tot_qty":0,"pdtqty":0,"operater":"root","docno":"MQS2102030008","billdate":20210203},{"id":118,"tot_qty":0,"pdtqty":1,"operater":"root","docno":"MQS2102190005","billdate":20210219},{"id":128,"tot_qty":0,"pdtqty":1,"operater":"root","docno":"MQS2102190015","billdate":20210219},{"id":123,"tot_qty":1,"pdtqty":1,"operater":"root","docno":"MQS2102190010","billdate":20210219},{"id":102,"tot_qty":0,"pdtqty":0,"operater":"root","docno":"MQS2102030018","billdate":20210203},{"id":125,"tot_qty":0,"pdtqty":1,"operater":"root","docno":"MQS2102190012","billdate":20210219},{"id":122,"tot_qty":0,"pdtqty":1,"operater":"root","docno":"MQS2102190009","billdate":20210219}]
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
         * id : 12
         * tot_qty : 0
         * pdtqty : 0
         * operater : root
         * docno : MQS2101120001
         * billdate : 20210112
         */

        private int id;
        private int tot_qty;
        private int pdtqty;
        private String operater;
        private String docno;
        private int billdate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTot_qty() {
            return tot_qty;
        }

        public void setTot_qty(int tot_qty) {
            this.tot_qty = tot_qty;
        }

        public int getPdtqty() {
            return pdtqty;
        }

        public void setPdtqty(int pdtqty) {
            this.pdtqty = pdtqty;
        }

        public String getOperater() {
            return operater;
        }

        public void setOperater(String operater) {
            this.operater = operater;
        }

        public String getDocno() {
            return docno;
        }

        public void setDocno(String docno) {
            this.docno = docno;
        }

        public int getBilldate() {
            return billdate;
        }

        public void setBilldate(int billdate) {
            this.billdate = billdate;
        }
    }
}
