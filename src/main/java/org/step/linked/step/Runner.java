package org.step.linked.step;

import org.hibernate.SessionFactory;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;
import org.step.linked.step.repository.impl.UserRepository;

import java.util.UUID;

public class Runner {

    public static void main(String[] args) {
        SessionFactory sf = SessionFactoryAssistant.getSF();
        CRUDRepository<User> crudRepository = new UserRepository(sf);
        crudRepository.save(new User(UUID.randomUUID().toString(), "username"));
    }
}