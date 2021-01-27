package org.step.linked.step.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.step.linked.step.configuration.DatabaseConfiguration;
import org.step.linked.step.configuration.web.WebMvcConfiguration;
import org.step.linked.step.configuration.web.WebMvcInitializer;
import org.step.linked.step.model.Profile;
import org.step.linked.step.model.User;
import org.step.linked.step.model.dto.UserUsernamePasswordOnly;
import org.step.linked.step.model.predicates.SearchingObject;
import org.step.linked.step.model.predicates.UserSpecification;
import org.step.linked.step.model.projection.UsernameIdProjection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DatabaseConfiguration.class, WebMvcConfiguration.class, WebMvcInitializer.class})
@SqlConfig(dataSource = "testDataSource")
//@Sql(scripts = {"classpath:user_test/test_user_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = {"classpath:user_test/clean_test_user_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles({"test"})
public class UserSpringDataRepositoryTest {

    @Autowired
    private UserSpringDataRepositoryImpl userRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void userSpringDataRepository_FindAll() {
        final int expectedListSize = 1;
        List<User> users = userRepository.findAll();

        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(expectedListSize, users.size());
    }

    @Test
    public void userSpringDataRepository_FindById() {
        String id = "2138c016-5b28-11eb-b409-0242ac110002";

        Optional<User> userByID = userRepository.findById(id);

        Assertions.assertTrue(userByID.isPresent());
        Assertions.assertEquals(id, userByID.get().getId());
    }

    @Test
    public void userSpringDataRepository_SaveUser() {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .username("gopstop@mail.ru")
                .password("password")
                .age(25)
                .build();

        User second = User.builder()
                .id("03bb661f-0c35-49c2-aa71-5a0b5bdca512")
                .username("newhophop@mail.ru")
                .password("password")
                .age(25)
                .build();

        User userFromDB = userRepository.save(user);

        userRepository.save(second);

        Optional<User> userByID = userRepository.findById(userFromDB.getId());

        Assertions.assertTrue(userByID.isPresent());
        Assertions.assertEquals(userFromDB.getId(), userByID.get().getId());
    }

    @Test
    public void userSpringDataRepository_FindAllByUsernameContains() {
        final int expectedSize = 1;
        final String username = "@mail";

        List<User> users = userRepository.findAllByUsernameContains(username);

        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(expectedSize, users.size());
    }

    @Test
    public void userSpringDataRepository_FindAllByAgeAfter() {
        final int age = 27;

        List<User> users = userRepository.findAllByAgeAfter(age);

        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    public void userSpringDataRepository_FindAllByProfileDescription() {
        final String description = "some description";
        final int expectedSize = 1;

        List<User> users = userRepository.findAllByProfile_Description(description);

        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(expectedSize, users.size());
    }

    @Test
    public void userSpringDataRepository_FindAllWithPagination() {
//        Pageable unpaged = Pageable.unpaged();
        final PageRequest page = PageRequest.of(0, 20);

        Page<User> all = userRepository.findAll(page);

        Assertions.assertEquals(1, all.getTotalElements());
        Assertions.assertEquals(1, all.getTotalPages());
    }

    @Test
    public void userSpringDataRepository_FindAllWithSorting() {
        final Sort sort = Sort.by("username").ascending().and(Sort.by("age").descending());

        List<User> users = userRepository.findAll(sort);

        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(1, users.size());
    }

    @Test
    public void userSpringDataRepository_FindAllWithSortingTypeSafe() {
        final Sort.TypedSort<User> sort = Sort.sort(User.class);
        final Sort ascending = sort.by(User::getUsername).ascending();

        List<User> users = userRepository.findAll(ascending);

        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(1, users.size());
    }

    @Test
    public void userSpringDataRepository_FindAllWithStreamable() {
        Streamable<User> users = userRepository.findAllByUsername("foo@mail.ru");

        Set<User> userSet = users.toSet();

        Assertions.assertFalse(userSet.isEmpty());
        Assertions.assertEquals(1, userSet.size());
    }

    @Test
    public void userSpringDataRepository_FindAllWithAsync() {
        final String id = "2138c016-5b28-11eb-b409-0242ac110";

        userRepository.findUserById(id)
                .thenApply(User::getUsername)
                .thenAccept(System.out::println)
                .exceptionally(ex -> {
                    System.out.println(ex.getCause().getClass().getSimpleName());
                    return null;
                }).join();
    }

    @Test
    public void userSpringDataRepository_FindAllWithХлебушек() {
        List<User> usersWithХлебушек = userRepository.findAllByUsernameWithХлебушек("foo@mail.ru");

        Assertions.assertFalse(usersWithХлебушек.isEmpty());
        Assertions.assertEquals(1, usersWithХлебушек.size());
    }

    @Test
    public void test_FindAllGeneric() {

        List<User> users = userRepository.findAllWithGeneric();

        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void test_DeleteById() {
        final String userId = "2138c016-5b28-11eb-b409-0242ac110002";

        transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.executeWithoutResult(status -> {
            entityManager.createQuery("delete from Profile p where p.user.id=:id")
                    .setParameter("id", userId)
                    .executeUpdate();
        });
        userRepository.deleteById(userId);
    }

    @Test
    public void test_NamedEntityGraph() {
        List<User> users = userRepository.findAllWithEntityGraph();

        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void test_EntityGraph() {
        List<User> users = userRepository.findAllWithEntityGraphInMethod();

        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void test_Projection() {
        List<UsernameIdProjection> users = userRepository.findAllByProjection();

        users.forEach(p -> System.out.println(p.getFullDescription()));
    }

    @Test
    public void test_JPADTO() {
        List<UserUsernamePasswordOnly> users = entityManager.createQuery(
                "select new org.step.linked.step.model.dto.UserUsernamePasswordOnly(u.username, u.password) from User u", UserUsernamePasswordOnly.class
        ).getResultList();

        users.forEach(u -> System.out.printf("%s %s%n", u.getUsername(), u.getPassword()));

        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void test_SpringData() {
        List<UserUsernamePasswordOnly> users = userRepository
                .findAllByUsernameAndAge("foo@mail.ru", 25, UserUsernamePasswordOnly.class);

        users.forEach(u -> System.out.printf("%s %s%n", u.getUsername(), u.getPassword()));

        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void test_Specification() {
        SearchingObject searchingObject = new SearchingObject();

        searchingObject.setUsername("@mail");
        searchingObject.setAge(25);

        UserSpecification userSpecification = new UserSpecification(searchingObject);

        List<User> users = userRepository.findAll(userSpecification);

        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void test_QueryByExample() {
        User user = User.builder().username("foo@mail.ru").age(25).profile(Profile.builder().description("descr").build()).build();

        ExampleMatcher modernExample = ExampleMatcher.matchingAll()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("age", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("profile.description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnoreNullValues();

        Example<User> userExample = Example.of(user, modernExample);

        List<User> users = userRepository.findAll(userExample);

        Assertions.assertFalse(users.isEmpty());
    }

//    @Test
//    public void test_SpringDataQueryDTO() {
//        List<UserUsernamePasswordOnly> users = userRepository.findAllByUsernameХлебушек("foo@mail.ru", UserUsernamePasswordOnly.class);
//
//        users.forEach(u -> System.out.printf("%s %s%n", u.getUsername(), u.getPassword()));
//
//        Assertions.assertFalse(users.isEmpty());
//    }
}
