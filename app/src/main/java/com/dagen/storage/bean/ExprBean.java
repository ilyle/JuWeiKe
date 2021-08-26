package com.dagen.storage.bean;

public class ExprBean {
    private String condition;
    private String column;

    public ExprBean(String column,String condition) {
        this.condition = condition;
        this.column = column;
    }

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
