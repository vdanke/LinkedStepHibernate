package org.step.linked.step.study;

import java.util.Objects;

public class EasyBean {

    private int id;
    private String message;

    public EasyBean() {
    }

    public EasyBean(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EasyBean easyBean = (EasyBean) o;
        return id == easyBean.id && Objects.equals(message, easyBean.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }
}
