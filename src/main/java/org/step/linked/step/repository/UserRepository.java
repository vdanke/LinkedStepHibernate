package org.step.linked.step.repository;

import org.step.linked.step.model.User;

public interface UserRepository {

    User findByUsername(String username);
}
