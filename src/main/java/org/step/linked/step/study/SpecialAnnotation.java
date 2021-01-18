package org.step.linked.step.study;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Inherited
public @interface SpecialAnnotation {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
    String secretMessage() default "";
}
