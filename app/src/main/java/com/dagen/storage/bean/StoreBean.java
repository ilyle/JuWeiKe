package com.dagen.storage.bean;

import java.util.List;

public class StoreBean {

    /**
     * id : 437
     * code : 200
     * name : 南方网店-电商
     * succeed : 0
     * value : ok
     * msg : [{"id":82,"code":"10000005","name":"上海次品仓"}]
     */

    private int id;
    private int code;
    private String name;
    private int succeed;
    private String value;
    private List<MsgBean> msg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * id : 82
         * code : 10000005
         * name : 上海次品仓
         */

        private int id;
        private String code;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
