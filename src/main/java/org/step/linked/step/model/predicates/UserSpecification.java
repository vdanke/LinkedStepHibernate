package org.step.linked.step.model.predicates;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.step.linked.step.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {

    private final SearchingObject searchingObject;

    public UserSpecification(SearchingObject searchingObject) {
        this.searchingObject = searchingObject;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<>();

        if (searchingObject == null) {
            return builder.and(predicateList.toArray(new Predicate[]{}));
        }
        if (!StringUtils.isEmpty(searchingObject.getUsername())) {
            predicateList.add(
                    builder.like(
                            builder.lower(
                                    root.get("username").as(String.class)), wrapWithRegex(searchingObject.getUsername()))
            );
        }
        if (!StringUtils.isEmpty(searchingObject.getAge())) {
            predicateList.add(
                    builder.equal(
                            root.get("age"), searchingObject.getAge()
                    )
            );
        }
        return builder.and(predicateList.toArray(new Predicate[]{}));
    }

    private String wrapWithRegex(String data) {
        return "%" + data + "%";
    }
}
