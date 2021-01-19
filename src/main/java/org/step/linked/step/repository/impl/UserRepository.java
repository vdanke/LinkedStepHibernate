package org.step.linked.step.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.step.linked.step.exceptions.NotFoundException;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements CRUDRepository<User> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> update(User user) {
        return Optional.ofNullable(entityManager.find(User.class, user.getId()));
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public boolean deleteById(String id) {
        int result = entityManager.createQuery("delete from User u where u.id=:id")
                .setParameter("id", id)
                .executeUpdate();
        return result != 0;
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }
}
