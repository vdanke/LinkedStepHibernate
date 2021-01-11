package org.step.linked.step.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UserTest {

    @Test
    public void userCreationTest() {
        final User user = new User();

        Assertions.assertNotNull(user);
    }

    @Test
    public void userCreationTestWithId() {
        final String id = UUID.randomUUID().toString();
        final User user = new User(id, "username");

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(id, user.getId());
        Assertions.assertNotNull(user.getUsername());
    }
}
