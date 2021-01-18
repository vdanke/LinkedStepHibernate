package org.step.linked.step.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.step.linked.step.model.Authority;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;
import org.step.linked.step.service.IDGenerator;
import org.step.linked.step.service.UserService;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final IDGenerator<String> idGenerator;
    private final CRUDRepository<User> userCRUDRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("baseIdGenerator") IDGenerator<String> idGenerator,
                           CRUDRepository<User> userCRUDRepository) {
        this.idGenerator = idGenerator;
        this.userCRUDRepository = userCRUDRepository;
    }

    @Override
    public void save(User user) {
        user.setId(idGenerator.generate());
        user.setAuthorities(Collections.singleton(Authority.ROLE_USER));
        userCRUDRepository.save(user);
    }
}
