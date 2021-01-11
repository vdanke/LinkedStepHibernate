package org.step.linked.step.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;

import java.util.List;

public class UserRepository implements CRUDRepository<User> {

    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> findAll() {
        return sessionFactory.openSession().createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
    }
}
