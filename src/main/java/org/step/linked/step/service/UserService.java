package org.step.linked.step.service;

import org.step.linked.step.model.User;

import java.util.List;

public interface UserService {

    void save(User user);

    List<User> findAll();

    User update(User user);

    User findById(String id);

    boolean deleteById(String id);

    List<User> findAllWithSorting(int age, String direction);

    String fileFileByFilename(String filename);

    String fetchExistsGithubProfile(String username);
}
