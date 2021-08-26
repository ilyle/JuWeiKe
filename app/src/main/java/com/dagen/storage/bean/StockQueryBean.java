package com.dagen.storage.bean;

import java.util.List;

public class StockQueryBean {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : {"row1":"店仓","row2":"款号","row3":"颜色","row4":"尺寸","row5":"数量","row6":"条码","row7":"","row8":"","row9":"","list":[{"row1":"南方网店-电商","row2":"21001006","row3":"默认","row4":"17","row5":-5,"row6":"2100100611117","row7":"","row8":"","row9":""},{"row1":"","row2":"合计","row3":"","row4":"","row5":-5,"row6":"","row7":"","row8":"","row9":""}]}
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
         * row1 : 店仓
         * row2 : 款号
         * row3 : 颜色
         * row4 : 尺寸
         * row5 : 数量
         * row6 : 条码
         * row7 :
         * row8 :
         * row9 :
         * list : [{"row1":"南方网店-电商","row2":"21001006","row3":"默认","row4":"17","row5":-5,"row6":"2100100611117","row7":"","row8":"","row9":""},{"row1":"","row2":"合计","row3":"","row4":"","row5":-5,"row6":"","row7":"","row8":"","row9":""}]
         */

        private String row1;
        private String row2;
        private String row3;
        private String row4;
        private String row5;
        private String row6;
        private String row7;
        private String row8;
        private String row9;
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

        public String getRow7() {
            return row7;
        }

        public void setRow7(String row7) {
            this.row7 = row7;
        }

        public String getRow8() {
            return row8;
        }

        public void setRow8(String row8) {
            this.row8 = row8;
        }

        public String getRow9() {
            return row9;
        }

        public void setRow9(String row9) {
            this.row9 = row9;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * row1 : 南方网店-电商
             * row2 : 21001006
             * row3 : 默认
             * row4 : 17
             * row5 : -5
             * row6 : 2100100611117
             * row7 :
             * row8 :
             * row9 :
             */

            private String row1;
            private String row2;
            private String row3;
            private String row4;
            private String row5;
            private String row6;
            private String row7;
            private String row8;
            private String row9;

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

            public String getRow7() {
                return row7;
            }

            public void setRow7(String row7) {
                this.row7 = row7;
            }

            public String getRow8() {
                return row8;
            }

            public void setRow8(String row8) {
                this.row8 = row8;
            }

            public String getRow9() {
                return row9;
            }

            public void setRow9(String row9) {
                this.row9 = row9;
            }
        }
    }
}
