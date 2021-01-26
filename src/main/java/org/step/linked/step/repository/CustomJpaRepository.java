package org.step.linked.step.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findById(ID id);
}
