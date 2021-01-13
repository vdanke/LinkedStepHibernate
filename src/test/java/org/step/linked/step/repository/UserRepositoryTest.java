package org.step.linked.step.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.step.linked.step.model.Post;
import org.step.linked.step.model.Profile;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.impl.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UserRepositoryTest {

    public static CRUDRepository<User> crudRepository;
    public static SessionFactory sf;
    public static EntityManagerFactory emf;
    private final String testId = UUID.randomUUID().toString();
    private final String username = "test username";
    private final String password = Base64.getEncoder().encodeToString("password".getBytes(StandardCharsets.UTF_8));

    @BeforeAll
    public static void setup() {
        sf = new Configuration()
                .configure("hibernate.cfg-test.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Post.class)
                .addAnnotatedClass(Profile.class)
                .buildSessionFactory();
        emf = Persistence.createEntityManagerFactory("linked-step-test-persistence-unit");

        crudRepository = new UserRepository(sf, emf);
    }

    @BeforeEach
    public void setupBeforeEach() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(User.builder().id(testId).username(username).password(password).build());
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @AfterEach
    public void cleanAfterEach() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Profile p").executeUpdate();
        entityManager.createQuery("delete from User u").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void userRepositoryTest_UpdateUser() {
        String newTestUsername = "new test username";

        Optional<User> user = crudRepository.update(User.builder().id(testId).username(newTestUsername).build());

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(newTestUsername, user.get().getUsername());
    }

    @Test
    public void userRepositoryTest_UpdateUser_UserNotExists() {
        Optional<User> user = crudRepository.update(User.builder().id("id").build());

        Assertions.assertFalse(user.isPresent());
    }

    @Test
    public void userRepositoryTest_FindById() {
        Optional<User> user = crudRepository.findById(testId);

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(testId, user.get().getId());
    }

    @Test
    public void userRepositoryTest_FindById_UserNotFound() {
        Optional<User> user = crudRepository.findById(UUID.randomUUID().toString());

        Assertions.assertFalse(user.isPresent());
    }

    @Test
    public void userRepositoryTest_DeleteUserById() {
        boolean isDeleted = crudRepository.deleteById(testId);

        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void userRepositoryTest_DeleteUserById_UserWasNotDeleted() {
        boolean isDeleted = crudRepository.deleteById(UUID.randomUUID().toString());

        Assertions.assertFalse(isDeleted);
    }

    @Test
    public void userRepositoryTest_SaveNewUser_WithProfile() {
        String profileId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String testData = "test data";
        Profile profile = Profile.builder().id(profileId).description(testData).build();
        User user = User.builder()
                .id(userId)
                .username(testData)
                .password(Base64.getEncoder().encodeToString(testData.getBytes(StandardCharsets.UTF_8)))
                .build();
        user.addProfile(profile);

        crudRepository.save(user);

        Optional<User> foundUser = crudRepository.findById(userId);

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get().getProfile());
        Assertions.assertEquals(testData, foundUser.get().getUsername());
        Assertions.assertEquals(testData, new String(Base64.getDecoder().decode(
                foundUser.get().getPassword()), StandardCharsets.UTF_8)
        );
    }

    @Test
    public void userRepositoryTest_SaveNewUser() {
        String testId = UUID.randomUUID().toString();
        String testUsername = "new username for test";
        User testUser = User.builder()
                .id(testId)
                .username(testUsername)
                .password(
                        Base64.getEncoder().encodeToString("different".getBytes(StandardCharsets.UTF_8))
                ).build();

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
