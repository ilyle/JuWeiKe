package com.dagen.storage.bean;

import java.util.List;

public class MatrixBean {

    /**
     * size : 00
     * saleprice : null
     * salepricename : 销售价
     * colorlist : [{"color":"默认","aliasid":80468,"sku":0}]
     * succeed : 0
     * value : ok
     * code : 200
     * msg : [{"size":"00","saleprice":null,"salepricename":"销售价","colorlist":[{"color":"默认","aliasid":80468,"sku":0}]}]
     */

    private String size;
    private Object saleprice;
    private String salepricename;
    private int succeed;
    private String value;
    private int code;
    private List<ColorlistBean> colorlist;
    private List<MsgBean> msg;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Object getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(Object saleprice) {
        this.saleprice = saleprice;
    }

    public String getSalepricename() {
        return salepricename;
    }

    public void setSalepricename(String salepricename) {
        this.salepricename = salepricename;
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

    public List<ColorlistBean> getColorlist() {
        return colorlist;
    }

    public void setColorlist(List<ColorlistBean> colorlist) {
        this.colorlist = colorlist;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class ColorlistBean {
        /**
         * color : 默认
         * aliasid : 80468
         * sku : 0
         */

        private String color;
        private int aliasid;
        private int sku;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getAliasid() {
            return aliasid;
        }

        public void setAliasid(int aliasid) {
            this.aliasid = aliasid;
        }

        public int getSku() {
            return sku;
        }

        public void setSku(int sku) {
            this.sku = sku;
        }
    }

    public static class MsgBean {
        /**
         * size : 00
         * saleprice : null
         * salepricename : 销售价
         * colorlist : [{"color":"默认","aliasid":80468,"sku":0}]
         */

        private String size;
        private Object saleprice;
        private String salepricename;
        private List<ColorlistBeanX> colorlist;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public Object getSaleprice() {
            return saleprice;
        }

        public void setSaleprice(Object saleprice) {
            this.saleprice = saleprice;
        }

        public String getSalepricename() {
            return salepricename;
        }

        public void setSalepricename(String salepricename) {
            this.salepricename = salepricename;
        }

        public List<ColorlistBeanX> getColorlist() {
            return colorlist;
        }

        public void setColorlist(List<ColorlistBeanX> colorlist) {
            this.colorlist = colorlist;
        }

        public static class ColorlistBeanX {
            /**
             * color : 默认
             * aliasid : 80468
             * sku : 0
             */

            private String color;
            private int aliasid;
            private int sku;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public int getAliasid() {
                return aliasid;
            }

            public void setAliasid(int aliasid) {
                this.aliasid = aliasid;
            }

            public int getSku() {
                return sku;
            }

            public void setSku(int sku) {
                this.sku = sku;
            }
        }
    }
}
