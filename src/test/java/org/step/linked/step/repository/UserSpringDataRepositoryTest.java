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
import org.step.linked.step.configuration.DatabaseConfiguration;
import org.step.linked.step.configuration.web.WebMvcConfiguration;
import org.step.linked.step.configuration.web.WebMvcInitializer;
import org.step.linked.step.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DatabaseConfiguration.class, WebMvcConfiguration.class, WebMvcInitializer.class})
@SqlConfig(dataSource = "testDataSource")
@Sql(scripts = {"classpath:user_test/test_user_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:user_test/clean_test_user_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles({"test"})
public class UserSpringDataRepositoryTest {

    @Autowired
    private UserSpringDataRepositoryImpl userRepository;

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
                .username("test@mail.ru")
                .password("password")
                .age(25)
                .build();

        User userFromDB = userRepository.save(user);

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
}
