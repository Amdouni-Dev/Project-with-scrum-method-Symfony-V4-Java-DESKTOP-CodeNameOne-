package edu.esprit.lib.persistence.annotation;

import edu.esprit.lib.persistence.QueryableEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Enumeration {
    public Class<? extends QueryableEnum> type();
}
