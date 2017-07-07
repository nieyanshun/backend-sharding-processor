package com.letv.backend;

public class Bean {
    private String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String toString() {
        return "Bean [beanName=" + beanName + "]";
    }

}
