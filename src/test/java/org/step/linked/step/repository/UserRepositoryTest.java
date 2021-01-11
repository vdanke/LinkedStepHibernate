package org.step.linked.step.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.impl.UserRepository;

import java.util.List;
import java.util.UUID;

public class UserRepositoryTest {

    public static CRUDRepository<User> crudRepository;
    public static SessionFactory sf;

    @BeforeAll
    public static void setup() {
        sf = new Configuration()
                .configure("hibernate.cfg-test.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        crudRepository = new UserRepository(sf);
    }

    @Test
    public void userRepositoryTest_SaveNewUser() {
        String testId = UUID.randomUUID().toString();
        String testUsername = "test username";
        User testUser = new User(testId, testUsername);

        crudRepository.save(testUser);

        User user = sf.openSession().find(User.class, testId);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(testId, user.getId());
        Assertions.assertEquals(testUsername, user.getUsername());
    }

    @Test
    public void userRepositoryTest_FindAll() {
        List<User> users = crudRepository.findAll();

        Assertions.assertNotNull(users);
    }
}
