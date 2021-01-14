package org.step.linked.step.repository;

import net.sf.ehcache.CacheManager;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.*;
import org.step.linked.step.model.User;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
public class CacheTest {

    public static EntityManagerFactory emf;
    public static final String id = UUID.randomUUID().toString();

    @BeforeAll
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("linked-step-test-persistence-unit");

        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        String testData = "test";
        User first = User.builder()
                .id(id)
                .username(String.format(testData + "%d", 1))
                .password(testData)
                .posts(new HashSet<>())
                .build();

        entityManager.persist(first);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void acacheFirstLevel() {
        EntityManager entityManager = emf.createEntityManager();
//        User user = entityManager.find(User.class, id);
//        User second = entityManager.find(User.class, id);
        EntityGraph<User> entityGraph = entityManager.createEntityGraph(User.class);
        entityGraph.addAttributeNodes("courses");
//        entityManager.getTransaction().begin();
        List<User> user = entityManager.createQuery("select u from User u", User.class)
//                .setParameter("id", CacheTest.id)
                .setHint(QueryHints.HINT_CACHEABLE, true)
//                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
//                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .getResultList();
        User second = entityManager.find(User.class, id);
//        entityManager.getTransaction().commit();
    }

    @Test
    public void bcacheSecondLevel() {
        int size = CacheManager.ALL_CACHE_MANAGERS.get(0).getCache("org.step.linked.step.model.User").getSize();
        System.out.println(size);
    }

    @Test
    public void cthirdLevelCache() {
        EntityManager entityManager = emf.createEntityManager();

        List<User> user = entityManager.createQuery("select u from User u", User.class)
//                .setParameter("id", CacheTest.id)
                .getResultList();
    }

    @AfterAll
    public static void clean() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Profile p").executeUpdate();
        entityManager.createQuery("delete from User u").executeUpdate();
    }
}
