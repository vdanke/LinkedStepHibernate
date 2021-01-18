package org.step.linked.step.service.impl;

import org.springframework.stereotype.Service;
import org.step.linked.step.service.IDGenerator;

import java.util.UUID;

@Service("uuidGenerator")
public class UUIDGeneratorImpl implements IDGenerator<String> {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
