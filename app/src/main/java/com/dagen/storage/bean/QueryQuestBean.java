package com.dagen.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QueryQuestBean {

    /**
     * id : 112
     * nds.control.ejb.UserTransaction : Y
     * command : Query
     * params : {"columns":["ID"],"params":{"expr1":{"condition":"zl@nf.com","column":"EMAIL"},"expr2":{"condition":"kbzl880925","column":"PASSWORDHASH"},"combine":"and"},"table":"USERS"}
     */

    private String command;
    private ParamsBeanX params;


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ParamsBeanX getParams() {
        return params;
    }

    public void setParams(ParamsBeanX params) {
        this.params = params;
    }

    public static class ParamsBeanX {
        /**
         * columns : ["ID"]
         * params : {"expr1":{"condition":"zl@nf.com","column":"EMAIL"},"expr2":{"condition":"kbzl880925","column":"PASSWORDHASH"},"combine":"and"}
         * table : USERS
         */

        private ParamsBean params;
        private String table;
        private List<String> columns;

        public ParamsBean getParams() {
            return params;
        }

        public void setParams(ParamsBean params) {
            this.params = params;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public List<String> getColumns() {
            return columns;
        }

        public void setColumns(List<String> columns) {
            this.columns = columns;
        }

        public static class ParamsBean {
            /**
             * expr1 : {"condition":"zl@nf.com","column":"EMAIL"}
             * expr2 : {"condition":"kbzl880925","column":"PASSWORDHASH"}
             * combine : and
             */

            private Expr1Bean expr1;
            private Expr2Bean expr2;
            private String combine;

            public Expr1Bean getExpr1() {
                return expr1;
            }

            public void setExpr1(Expr1Bean expr1) {
                this.expr1 = expr1;
            }

            public Expr2Bean getExpr2() {
                return expr2;
            }

            public void setExpr2(Expr2Bean expr2) {
                this.expr2 = expr2;
            }

            public String getCombine() {
                return combine;
            }

            public void setCombine(String combine) {
                this.combine = combine;
            }

            public static class Expr1Bean {
                /**
                 * condition : zl@nf.com
                 * column : EMAIL
                 */

                private String condition;
                private String column;

                public String getCondition() {
                    return condition;
                }

                public void setCondition(String condition) {
                    this.condition = condition;
                }

                public String getColumn() {
                    return column;
                }

                public void setColumn(String column) {
                    this.column = column;
                }
            }

            public static class Expr2Bean {
                /**
                 * condition : kbzl880925
                 * column : PASSWORDHASH
                 */

                private String condition;
                private String column;

                public String getCondition() {
                    return condition;
                }

                public void setCondition(String condition) {
                    this.condition = condition;
                }

                public String getColumn() {
                    return column;
                }

                public void setColumn(String column) {
                    this.column = column;
                }
            }
        }
    }
}
