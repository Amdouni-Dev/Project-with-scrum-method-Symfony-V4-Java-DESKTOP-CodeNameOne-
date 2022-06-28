package edu.esprit.lib.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Timestampable {
    public String createdAtField() default "createdAt";
    public String updatedAtField() default "updatedAt";
}
