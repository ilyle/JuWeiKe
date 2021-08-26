package com.dagen.storage.bean;

import java.util.List;

public class TmBean {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : [{"id":2525272,"no":"BBA8099124100200","qty":8},{"id":2525372,"no":"QQ2037001600111","qty":18},{"id":2282372,"no":"QQA7019135300111","qty":4},{"id":2282272,"no":"XXB032110E3G4700","qty":4},{"id":2615272,"no":"QQB0096T65300513","qty":6}]
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
         * id : 2525272
         * no : BBA8099124100200
         * qty : 8
         */

        private int id;
        private String no;
        private int qty;

        private boolean check;

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }
    }
}
