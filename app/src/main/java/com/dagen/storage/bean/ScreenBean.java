package com.dagen.storage.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.util.List;

public class ScreenBean implements Serializable {

    /**
     * succeed : 0
     * value : ok
     * code : 200
     * msg : [{"name":"单据日期","dname":"BILLDATE","type":"date","value":"20210421"},{"name":"单据编号","dname":"DOCNO","type":"text","value":""},{"name":"店仓","dname":"C_STORE_ID","type":"select","selectarr":[{"name":"南方网店-电商","value":"437"}],"value":""}]
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

    public static class MsgBean implements Serializable {
        /**
         * name : 单据日期
         * dname : BILLDATE
         * type : date
         * value : 20210421
         * selectarr : [{"name":"南方网店-电商","value":"437"}]
         */

        private String name;
        private String dname;
        private String type;
        private String value;
        private String selectName;
        private String selectValue;
        private List<SelectarrBean> selectarr;

        private String startTime;
        private String endTime;

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getSelectValue() {
            return selectValue;
        }

        public void setSelectValue(String selectValue) {
            this.selectValue = selectValue;
        }

        public String getSelectName() {
            return selectName;
        }

        public void setSelectName(String selectName) {
            this.selectName = selectName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<SelectarrBean> getSelectarr() {
            return selectarr;
        }

        public void setSelectarr(List<SelectarrBean> selectarr) {
            this.selectarr = selectarr;
        }


        public static class SelectarrBean implements Serializable, IPickerViewData {
            /**
             * name : 南方网店-电商
             * value : 437
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

            @Override
            public String getPickerViewText() {
                return name;
            }
        }
    }
}
