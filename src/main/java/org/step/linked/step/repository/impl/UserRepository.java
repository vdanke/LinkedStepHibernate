package org.step.linked.step.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class UserRepository implements CRUDRepository<User> {

    private final SessionFactory sessionFactory;
    private final EntityManagerFactory entityManagerFactory;

    public UserRepository(SessionFactory sessionFactory,
                          EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = sessionFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Optional<User> update(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        User foundUser = entityManager.find(User.class, user.getId());
        if (foundUser == null) {
            entityManager.getTransaction().rollback();
            entityManager.close();
            return Optional.empty();
        }
        foundUser.setUsername(user.getUsername());
        entityManager.getTransaction().commit();
        entityManager.close();
        return Optional.of(foundUser);
    }

    @Override
    public Optional<User> findById(String id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = entityManager.find(User.class, id);
        entityManager.close();
        return Optional.ofNullable(user);
    }

    @Override
    public boolean deleteById(String id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        int result = entityManager.createQuery("delete from User u where u.id=:id")
                .setParameter("id", id)
                .executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
        return result != 0;
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("select u from User u", User.class).getResultList();
        session.close();
        return users;
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
    }
}
