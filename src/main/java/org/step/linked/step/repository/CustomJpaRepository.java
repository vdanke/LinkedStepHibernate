package org.step.linked.step.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends Repository<T, ID> {

    @Query("select t from #{#entityName} t")
    List<T> findAllWithGeneric();
}
