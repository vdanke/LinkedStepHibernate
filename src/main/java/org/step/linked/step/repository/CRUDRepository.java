package org.step.linked.step.repository;

import java.util.List;

public interface CRUDRepository<T> {

    List<T> findAll();

    void save(T t);
}
