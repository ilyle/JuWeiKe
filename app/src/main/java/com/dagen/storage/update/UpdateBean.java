package com.dagen.storage.update;

public class UpdateBean {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : {"version":1,"name":"V1.0.0","content":"储位上下架、库存查询","downloadurl":"http://117.144.179.114:8877/MDInfaceSystem96cs/apk/stroage.apk"}
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
         * version : 1
         * name : V1.0.0
         * content : 储位上下架、库存查询
         * downloadurl : http://117.144.179.114:8877/MDInfaceSystem96cs/apk/stroage.apk
         */

        private int version;
        private String name;
        private String content;
        private String downloadurl;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDownloadurl() {
            return downloadurl;
        }

        public void setDownloadurl(String downloadurl) {
            this.downloadurl = downloadurl;
        }
    }
}
