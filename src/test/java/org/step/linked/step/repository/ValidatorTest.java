package org.step.linked.step.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.step.linked.step.model.Authority;
import org.step.linked.step.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.validation.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ValidatorTest {

    public static EntityManagerFactory emf;

//    @BeforeAll
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("linked-step-test-persistence-unit");
    }

//    @AfterAll
    public static void clean() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DELETE FROM AUTHORITIES").executeUpdate();
        entityManager.createQuery("delete from Profile p").executeUpdate();
        entityManager.createQuery("delete from User u").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

//    @Test
    public void firstValidatorTest() {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .username("abcd@email.ru")
                .password("password")
                .age(28)
                .authorities(new HashSet<>() {{
                    add(Authority.ROLE_USER);
                }})
                .build();

        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

//    @Test(expected = ConstraintViolationException.class)
//    @Test
    public void validationTestWithException() {
        final User user = User.builder().id(UUID.randomUUID().toString()).username("abc").password("password").build();

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityManager entityManager = emf.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            entityManager.close();
        });
    }

//    @Test
    public void secondValidatorTest() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        User user = User.builder().id(UUID.randomUUID().toString()).username("abc").password("password").build();

        Set<ConstraintViolation<User>> validate = validator.validate(user);

        Assertions.assertNotEquals(0, validate.size());
    }
}
