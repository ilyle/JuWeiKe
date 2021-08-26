package com.dagen.storage.bean;

public class QueryArrBean {
    private String name;
    private String value;

    public QueryArrBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
