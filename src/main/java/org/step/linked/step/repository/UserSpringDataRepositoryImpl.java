package org.step.linked.step.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.step.linked.step.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// , QuerydslPredicateExecutor<User>
@Repository
public interface UserSpringDataRepositoryImpl extends JpaRepository<User, String> {

    // List<User> by username like
    // select u from User u where u.username like %username%
    List<User> findAllByUsernameContains(String username);

    List<User> findAllByAgeAfter(Integer age);

    List<User> findAllByProfile_Description(String description);

    List<User> findFirst3ByUsernameContains(String username);

    Streamable<User> findAllByUsername(String username);

    @Nullable
    User findByIdAndProfile_Description(String id, String description);

    @Async
    CompletableFuture<User> findUserById(String id);
//    Stream<User> findAll();

    @Query("select u from User u where u.username=?1")
    List<User> findAllByUsernameWithХлебушек(String username);

    @Query(value = "SELECT * FROM USERS WHERE USERNAME=?1", nativeQuery = true)
    List<User> findAllByUsernameNativno(String username);

    @Query(
            value = "SELECT * FROM USERS WHERE USERNAME=?1",
            nativeQuery = true,
            countQuery = "SELECT count(*) FROM USERS WHERE USERNAME=?1"
    )
    Page<User> findAllByUsernameWithCount(String username, Pageable pageable);

    @Query("select u from User u where u.username=:username and u.age=:age")
    List<User> findAllByUsernameAndAge(@Param("username") String username, @Param("age") int age);
}
