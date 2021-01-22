package org.step.linked.step.dto;

import org.step.linked.step.model.Authority;
import org.step.linked.step.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserPublicDTO {

    public String id;
    public String username;
    public Integer age;

    public UserPublicDTO(String id, String username, Integer age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }

    public static UserPublicDTO toUserPublicDTO(User user) {
        return new UserPublicDTO(user.getId(), user.getUsername(), user.getAge());
    }
}
