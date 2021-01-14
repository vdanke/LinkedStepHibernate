package org.step.linked.step.repository.impl;

import org.step.linked.step.model.Profile;
import org.step.linked.step.repository.CRUDRepository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class ProfileRepository implements CRUDRepository<Profile> {

    private final EntityManagerFactory emf;

    public ProfileRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Profile> findAll() {
        return null;
    }

    @Override
    public void save(Profile profile) {

    }

    @Override
    public Optional<Profile> findById(String id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public Optional<Profile> update(Profile profile) {
        return Optional.empty();
    }
}