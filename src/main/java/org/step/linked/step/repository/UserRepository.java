package org.step.linked.step.repository;

import org.step.linked.step.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User findByUsername(String username);

    List<User> findAllWithSorting(int age, String direction);

    Optional<String> findFileByFilename(String filename);
}
