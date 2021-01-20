package org.step.linked.step.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.step.linked.step.configuration.DatabaseConfiguration;
import org.step.linked.step.model.Authority;
import org.step.linked.step.model.User;

import javax.persistence.NoResultException;
import java.util.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@SqlConfig(dataSource = "testDataSource")
@Sql(scripts = {"classpath:user_test/test_user_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:user_test/clean_test_user_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles({"test"})
public class UserRepositoryTest {

    @Autowired
    private CRUDRepository<User> crudRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    private final String testUserID = "2138c016-5b28-11eb-b409-0242ac110002";
    private final String testUsername = "foo@mail.ru";
    private final String testPassword = "testpassword";
    private final Integer testAge = 25;

    @Test
    public void userRepository_FindById() {
        Optional<User> userByID = crudRepository.findById(testUserID);

        Assertions.assertTrue(userByID.isPresent());
        Assertions.assertEquals(testUserID, userByID.get().getId());
    }

    @Test
    public void userRepository_FindAll() {
        final int userListSize = 1;
        List<User> users = crudRepository.findAll();

        Assertions.assertEquals(userListSize, users.size());
        Assertions.assertTrue(users.contains(User.builder().id(testUserID).build()));
    }

    @Test
//    @Transactional
    public void userRepository_SaveNewUser() {
        final String userID = UUID.randomUUID().toString();
        final String username = "bar@mail.ru";
        final Set<Authority> authorities = new HashSet<>() {{
            add(Authority.ROLE_USER);
        }};
        final User user = User.builder()
                .id(userID)
                .username(username)
                .password(testPassword)
                .authorities(authorities)
                .age(testAge)
                .build();

        transactionTemplate = new TransactionTemplate(transactionManager);

        Optional<User> userByID = transactionTemplate.execute(transactionStatus -> {
            crudRepository.save(user);
            return crudRepository.findById(user.getId());
        });

        Assertions.assertTrue(userByID.isPresent());
        Assertions.assertEquals(userID, userByID.get().getId());
    }

    @Test
    public void userRepository_DeleteById() {
        transactionTemplate = new TransactionTemplate(transactionManager);

        Boolean isExecuted = transactionTemplate.execute(transactionStatus -> crudRepository.deleteById(testUserID));

        Assertions.assertTrue(isExecuted);
    }

    @Test
    public void userRepository_Update() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        final String newTestEmail = "newemail@mail.ru";

        User user = User.builder().id(testUserID).username(newTestEmail).build();

        User updatedUser = transactionTemplate.execute(transactionStatus -> crudRepository.update(user));

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(testUserID, updatedUser.getId());
        Assertions.assertEquals(newTestEmail, updatedUser.getUsername());
    }

    @Test
    public void userRepository_FindByUsername() {
        User user = userRepository.findByUsername(testUsername);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(testUsername, user.getUsername());
    }

    @Test
    public void userRepository_FindByUsername_ReturnNotFoundException() {
        Assertions.assertThrows(NoResultException.class, () -> userRepository.findByUsername("not exists"));
    }
}
