package org.step.linked.step.repository;

import org.step.linked.step.model.User;

import java.util.List;

public interface UserRepository {

    User findByUsername(String username);

    List<User> findAllWithSorting(int age, String direction);
}
