package org.step.linked.step.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.util.Streamable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.step.linked.step.model.User;
import org.step.linked.step.model.dto.UserUsernamePasswordOnly;
import org.step.linked.step.model.projection.UsernameIdProjection;

import javax.persistence.Converter;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.step.linked.step.model.User.USER_POSTS_ENTITY_GRAPH;

// , QuerydslPredicateExecutor<User>
@Repository
public interface UserSpringDataRepositoryImpl extends JpaRepository<User, String>,
        CustomJpaRepository<User, String>,
        JpaSpecificationExecutor<User>,
        QueryByExampleExecutor<User>
{

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
    @QueryHints(value = {
            @QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true")
    })
    List<User> findAllByUsernameAndAge(@Param("username") String username, @Param("age") int age);

    @Modifying
    @Query(value = "delete from User u where u.id=?1")
    void deleteById(@NonNull String id);

    @EntityGraph(value = USER_POSTS_ENTITY_GRAPH, type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "select u from User u")
    List<User> findAllWithEntityGraph();

    @EntityGraph(attributePaths = {"posts", "authorities"})
    @Query(value = "select u from User u")
    List<User> findAllWithEntityGraphInMethod();

    @Query(value = "select u from User u")
    List<UsernameIdProjection> findAllByProjection();

    <T> List<T> findAllByUsernameAndAge(String username, int age, Class<T> tClass);

//    @Query("select u.username, u.password from User u where u.username=?1")
//    <T> List<T> findAllByUsernameХлебушек(String username, Class<T> tClass);
}
