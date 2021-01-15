package org.step.linked.step.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.step.linked.step.model.Profile;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.impl.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.UUID;

public class ProfileRepositoryTest {

    public static CRUDRepository<User> crudRepository;
    public static SessionFactory sf;
    public static EntityManagerFactory emf;
    private final String testId = UUID.randomUUID().toString();
    private final String testDescr = "test description";

    @BeforeAll
    public static void setup() {
        sf = new Configuration()
                .configure("hibernate.cfg-test.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Profile.class)
                .buildSessionFactory();
        emf = Persistence.createEntityManagerFactory("linked-step-test-persistence-unit");

        crudRepository = new UserRepository(emf);
    }

    @BeforeEach
    public void setupBeforeEach() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(Profile.builder().id(testId).description(testDescr).build());
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @AfterEach
    public void cleanAfterEach() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from User u").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
