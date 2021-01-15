package org.step.linked.step.model.validators;

import org.step.linked.step.model.Authority;
import org.step.linked.step.model.BannVerifier;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BannValidator implements ConstraintValidator<BannVerifier, Set<Authority>> {

    private Authority[] authoritiesFromAnnotation;

    @Override
    public void initialize(BannVerifier constraintAnnotation) {
        this.authoritiesFromAnnotation = constraintAnnotation.authorities();
    }

    @Override
    public boolean isValid(Set<Authority> authorities, ConstraintValidatorContext constraintValidatorContext) {
        if (authorities.size() == 0) {
            return false;
        }
        List<Authority> auth = Arrays.asList(authoritiesFromAnnotation);
        for (Authority authority : authorities) {
            if (!auth.contains(authority)) {
                return false;
            }
        }
        return true;
    }
}
