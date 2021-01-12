package org.step.linked.step.repository;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<T> {

    List<T> findAll();

    void save(T t);

    Optional<T> findById(String id);

    boolean deleteById(String id);

    Optional<T> update(T t);
}
