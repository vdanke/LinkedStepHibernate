package org.step.linked.step.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UserTest {

    @Test
    public void userCreationTest() {
        final User user = User.builder().build();

        Assertions.assertNotNull(user);
    }

    @Test
    public void userCreationTestWithId() {
        final String id = UUID.randomUUID().toString();
        final User user = User.builder().id(id).username("username").password("password").build();

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(id, user.getId());
        Assertions.assertNotNull(user.getUsername());
        Assertions.assertNotNull(user.getPassword());
    }
}
