package org.step.linked.step.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.step.linked.step.exceptions.NotFoundException;
import org.step.linked.step.model.User;
import org.step.linked.step.repository.CRUDRepository;
import org.step.linked.step.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository("userRepositoryImpl")
public class UserRepositoryImpl implements CRUDRepository<User>, UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByUsername(String username) {
        return Optional.ofNullable(
                entityManager.createQuery("select u from User u where u.username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult()
        ).orElseThrow(() -> new NotFoundException(String.format("User with Username %s not found", username)));
    }

    @Override
    public User update(User user) {
        User userFromDB = Optional.ofNullable(entityManager.find(User.class, user.getId()))
                .orElseThrow(() -> new NotFoundException(String.format("User with ID %s not found", user.getId())));
        userFromDB.setUsername(user.getUsername());
        return userFromDB;
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
