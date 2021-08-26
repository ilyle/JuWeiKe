package com.dagen.storage.bean;

import java.util.List;

public class HomeBean {
    private String name;
    private String pwd;

    private List<HomeMoudleBean>homeMoudleBeans;

    public List<HomeMoudleBean> getHomeMoudleBeans() {
        return homeMoudleBeans;
    }

    public void setHomeMoudleBeans(List<HomeMoudleBean> homeMoudleBeans) {
        this.homeMoudleBeans = homeMoudleBeans;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class HomeMoudleBean{
        private String name;
        private int resId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }
    }
}
