package org.step.linked.step.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ProfileTest {

    @Test
    public void createProfile() {
        Profile profile = Profile.builder().build();

        Assertions.assertNotNull(profile);
    }

    @Test
    public void createProfileWithFields() {
        String testDescr = "desc";
        String testId = UUID.randomUUID().toString();
        Profile profile = Profile.builder().id(testId).description(testDescr).build();

        Assertions.assertNotNull(profile);
        Assertions.assertEquals(testId, profile.getId());
        Assertions.assertEquals(testDescr, profile.getDescription());
    }
}
