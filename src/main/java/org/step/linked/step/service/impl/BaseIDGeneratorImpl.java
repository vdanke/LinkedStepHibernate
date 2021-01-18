package org.step.linked.step.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.step.linked.step.service.IDGenerator;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service("baseIdGenerator")
public class BaseIDGeneratorImpl implements IDGenerator<String> {

    @Value("${id.secret.key}")
    private String secret;

    @Override
    public String generate() {
        LocalDateTime now = LocalDateTime.now();
        return Base64
                .getEncoder()
                .encodeToString(String.format("%s-%s", secret, now.toString()).getBytes(StandardCharsets.UTF_8));
    }
}
