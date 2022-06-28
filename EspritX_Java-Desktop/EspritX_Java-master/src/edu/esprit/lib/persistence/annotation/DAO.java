package edu.esprit.lib.persistence.annotation;

import edu.esprit.lib.persistence.IdentifiableEntity;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IndexAnnotated
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DAO {
    Class<? extends IdentifiableEntity> targetEntity();
}
