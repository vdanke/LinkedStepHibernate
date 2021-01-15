package org.step.linked.step.study;

public class SuperBean {

    private EasyBean easyBean;

    public SuperBean() {
    }

    public SuperBean(EasyBean easyBean) {
        this.easyBean = easyBean;
    }

    public EasyBean getBean() {
        return easyBean;
    }

    public void setBean(EasyBean easyBean) {
        this.easyBean = easyBean;
    }
}
