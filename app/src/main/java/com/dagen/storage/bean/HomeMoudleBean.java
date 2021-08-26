package com.dagen.storage.bean;

import java.util.List;

public class HomeMoudleBean {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : [{"name":"出入库管理","table":[{"name":"入库单","img":"http://bos.henlo.net:92/pda_icon/4.png","tableid":"15036"},{"name":"出库单","img":"http://bos.henlo.net:92/pda_icon/5.png","tableid":"15020"}]},{"name":"储位上下架","table":[{"name":"入库上架单","img":"http://bos.henlo.net:92/pda_icon/6.png","tableid":"15036"},{"name":"出库下架单","img":"http://bos.henlo.net:92/pda_icon/5.png","tableid":"15020"}]},{"name":"库存查询","table":[{"name":"库存查询","img":"http://bos.henlo.net:92/pda_icon/7.png","tableid":"15036"},{"name":"储位库存查询","img":"http://bos.henlo.net:92/pda_icon/7.png","tableid":"15020"}]}]
     */

    private int succeed;
    private String value;
    private int code;
    private List<MsgBean> msg;
    private String appname;

    public String getAppname() {
        return appname;
    }


    public void setAppname(String appname) {
        this.appname = appname;
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

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * name : 出入库管理
         * table : [{"name":"入库单","img":"http://bos.henlo.net:92/pda_icon/4.png","tableid":"15036"},{"name":"出库单","img":"http://bos.henlo.net:92/pda_icon/5.png","tableid":"15020"}]
         */

        private String name;
        private List<TableBean> table;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<TableBean> getTable() {
            return table;
        }

        public void setTable(List<TableBean> table) {
            this.table = table;
        }

        public static class TableBean {
            /**
             * name : 入库单
             * img : http://bos.henlo.net:92/pda_icon/4.png
             * tableid : 15036
             */

            private String name;
            private String img;
            private String tableid;
            private String type;
            private String is_permitadd;

            public String getIs_permitadd() {
                return is_permitadd;
            }

            public void setIs_permitadd(String is_permitadd) {
                this.is_permitadd = is_permitadd;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getTableid() {
                return tableid;
            }

            public void setTableid(String tableid) {
                this.tableid = tableid;
            }
        }
    }
}
