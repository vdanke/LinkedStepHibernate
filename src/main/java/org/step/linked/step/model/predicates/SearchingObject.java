package org.step.linked.step.model.predicates;

public class SearchingObject {

    private String username;
    private Integer age;

    public SearchingObject() {
    }

    public SearchingObject(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
