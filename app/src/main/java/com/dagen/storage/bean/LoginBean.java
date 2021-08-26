package com.dagen.storage.bean;

public class LoginBean {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : {"userid":1818,"user":"zl","psd":"kbzl880925","storeid":437,"typeid":1,"msg":"0","power":"1111"}
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
         * userid : 1818
         * user : zl
         * psd : kbzl880925
         * storeid : 437
         * typeid : 1
         * msg : 0
         * power : 1111
         */

        private int userid;
        private String user;
        private String psd;
        private int storeid;
        private int typeid;
        private String msg;
        private String power;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPsd() {
            return psd;
        }

        public void setPsd(String psd) {
            this.psd = psd;
        }

        public int getStoreid() {
            return storeid;
        }

        public void setStoreid(int storeid) {
            this.storeid = storeid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPower() {
            return power;
        }

        public void setPower(String power) {
            this.power = power;
        }
    }
}
