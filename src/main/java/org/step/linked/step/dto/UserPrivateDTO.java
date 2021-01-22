package org.step.linked.step.dto;

import org.step.linked.step.model.Authority;
import org.step.linked.step.model.User;

import java.util.Set;

public class UserPrivateDTO {

    private String id;
    private String username;
    private Integer age;
    private Set<Authority> authorities;

    public UserPrivateDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
