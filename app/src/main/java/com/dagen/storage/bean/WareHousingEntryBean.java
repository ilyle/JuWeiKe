package com.dagen.storage.bean;

import java.util.List;

public class WareHousingEntryBean {

    /**
     * msg : [{"name":"单号","value":"SA2009160000269"},{"name":"来源店仓","value":"南通配送仓"},{"name":"单据数量","value":40},{"name":"入库数量","value":0}]
     * succeed : 0
     * value : ok
     * code : 200
     * item : {"row1":"款号","row2":"颜色","row3":"尺寸","row4":"单据数","row5":"入库数","row6":"","list":[{"itemid":2615272,"row1":"QQB0096T65300","row2":"棕","row3":"T6","row4":"6","row5":"0","row6":""},{"itemid":2282272,"row1":"XXB032110E3G4","row2":"蓝","row3":"10","row4":"4","row5":"0","row6":""},{"itemid":2282372,"row1":"QQA7019135300","row2":"默认","row3":"13","row4":"4","row5":"0","row6":""},{"itemid":2525272,"row1":"BBA8099124100","row2":"白","row3":"12","row4":"8","row5":"0","row6":""},{"itemid":2525372,"row1":"QQ2037001600","row2":"默认","row3":"00","row4":"18","row5":"0","row6":""}]}
     */

    private int succeed;
    private String value;
    private int code;
    private ItemBean item;
    private List<MsgBean> msg;

    private int totqty;
    private int totqtyscan;

    private String ifcontrim;

    public String getIfcontrim() {
        return ifcontrim;
    }

    public void setIfcontrim(String ifcontrim) {
        this.ifcontrim = ifcontrim;
    }

    public int getTotqty() {
        return totqty;
    }

    public void setTotqty(int totqty) {
        this.totqty = totqty;
    }

    public int getTotqtyscan() {
        return totqtyscan;
    }

    public void setTotqtyscan(int totqtyscan) {
        this.totqtyscan = totqtyscan;
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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class ItemBean {
        /**
         * row1 : 款号
         * row2 : 颜色
         * row3 : 尺寸
         * row4 : 单据数
         * row5 : 入库数
         * row6 :
         * list : [{"itemid":2615272,"row1":"QQB0096T65300","row2":"棕","row3":"T6","row4":"6","row5":"0","row6":""},{"itemid":2282272,"row1":"XXB032110E3G4","row2":"蓝","row3":"10","row4":"4","row5":"0","row6":""},{"itemid":2282372,"row1":"QQA7019135300","row2":"默认","row3":"13","row4":"4","row5":"0","row6":""},{"itemid":2525272,"row1":"BBA8099124100","row2":"白","row3":"12","row4":"8","row5":"0","row6":""},{"itemid":2525372,"row1":"QQ2037001600","row2":"默认","row3":"00","row4":"18","row5":"0","row6":""}]
         */

        private String row1;
        private String row2;
        private String row3;
        private String row4;
        private String row5;
        private String row6;
        private List<ListBean> list;

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

        public String getRow5() {
            return row5;
        }

        public void setRow5(String row5) {
            this.row5 = row5;
        }

        public String getRow6() {
            return row6;
        }

        public void setRow6(String row6) {
            this.row6 = row6;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * itemid : 2615272
             * row1 : QQB0096T65300
             * row2 : 棕
             * row3 : T6
             * row4 : 6
             * row5 : 0
             * row6 :
             */

            private int itemid;
            private String row1;
            private String row2;
            private String row3;
            private String row4;
            private String row5;
            private String row6;

            public int getItemid() {
                return itemid;
            }

            public void setItemid(int itemid) {
                this.itemid = itemid;
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

            public String getRow5() {
                return row5;
            }

            public void setRow5(String row5) {
                this.row5 = row5;
            }

            public String getRow6() {
                return row6;
            }

            public void setRow6(String row6) {
                this.row6 = row6;
            }
        }
    }

    public static class MsgBean {
        /**
         * name : 单号
         * value : SA2009160000269
         */

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
