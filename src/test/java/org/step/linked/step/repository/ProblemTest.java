package org.step.linked.step.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.step.linked.step.model.*;
import org.step.linked.step.repository.impl.UserRepositoryImpl;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProblemTest {

    public static CRUDRepository<User> crudRepository;
    public static SessionFactory sf;
    public static EntityManagerFactory emf;

//    @BeforeAll
    public static void setup() {
        sf = new Configuration()
                .configure("hibernate.cfg-test.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Post.class)
                .addAnnotatedClass(Profile.class)
                .addAnnotatedClass(Course.class)
                .addAnnotatedClass(CourseRating.class)
                .addAnnotatedClass(CourseRatingComposite.class)
                .addAnnotatedClass(CourseRatingKey.class)
                .buildSessionFactory();
        emf = Persistence.createEntityManagerFactory("linked-step-test-persistence-unit");

        crudRepository = new UserRepositoryImpl();

        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        String testData = "test";
        User first = User.builder()
                .id(UUID.randomUUID().toString())
                .username(String.format(testData + "%d", 1))
                .password(testData)
                .posts(new HashSet<>())
                .build();
        User second = User.builder()
                .id(UUID.randomUUID().toString())
                .username(String.format(testData + "%d", 2))
                .password(testData)
                .posts(new HashSet<>())
                .build();
        User third = User.builder()
                .id(UUID.randomUUID().toString())
                .username(String.format(testData + "%d", 3))
                .password(testData)
                .posts(new HashSet<>())
                .build();
        Post firstPost = Post.builder().id(UUID.randomUUID().toString()).content(testData).build();
        Post secondPost = Post.builder().id(UUID.randomUUID().toString()).content(testData).build();
        Post thirdPost = Post.builder().id(UUID.randomUUID().toString()).content(testData).build();
        first.addPost(firstPost);
        second.addPost(secondPost);
        third.addPost(thirdPost);
        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.persist(third);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*
    N+1 - Hibernate в рамках сессии знает о сущностях и их связях
    Когда программист без предварительного похода в БД за нужными зависимостями
    пытается вызывать эти зависимости на объекте - Хиберней идет для каждого объекта и создает запрос
    в БД, что бы этот объект достать и избежать ошибки
    FETCHGRAPH - только то, что мы явно указали
    LOADGRAPH - то, что мы явно указали + EAGER зависимости
     */
//    @Test
    public void userRepositoryTest_OneToMany() {
        EntityManager entityManager = emf.createEntityManager();
//        EntityGraph<?> entityGraph = entityManager.getEntityGraph(USER_POSTS_ENTITY_GRAPH);
//        EntityGraph<User> entityGraph = entityManager.createEntityGraph(User.class);
//        entityGraph.addAttributeNodes("posts");
        List<User> users = entityManager.createQuery("select u from User u", User.class)
//                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
//                .setHint(QueryHints.HINT_LOADGRAPH, entityGraph)
                .getResultList();
//        .setHint(QueryHints.HINT_LOADGRAPH, entityGraph)
//        List<User> users = entityManager.createQuery("select u from User u join fetch u.posts", User.class).getResultList();
        for (User u : users) {
            // NullPointerException
            /*
            separated query (отдельную квери)
             */
            Set<Post> posts = u.getPosts();
            boolean isContains = posts.contains(Post.builder().id(UUID.randomUUID().toString()).build());
        }
    }

//    @Test
    public void userRepositoryTest_OneToOne() {
        EntityManager entityManager = emf.createEntityManager();

        TypedQuery<User> userQuery = entityManager.createQuery("select u from User u", User.class);
        EntityGraph<User> userGraph = entityManager.createEntityGraph(User.class);
        userGraph.addAttributeNodes("profile", "posts");
        userQuery.setHint(QueryHints.HINT_FETCHGRAPH, userGraph);
        List<User> users = userQuery.getResultList();
    }

//    @AfterAll
    public static void clean() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from User u").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
