package org.step.linked.step.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class PostTest {

    @Test
    public void createPost() {
        Post post = Post.builder().build();

        Assertions.assertNotNull(post);
    }
    
    @Test
    public void createPostFull() {
        String testData = "content";
        String testId = UUID.randomUUID().toString();
        Post post = Post.builder().id(testId).content(testData).build();

        Assertions.assertNotNull(post);
        Assertions.assertEquals(testId, post.getId());
        Assertions.assertEquals(testData, post.getContent());
    }
}
