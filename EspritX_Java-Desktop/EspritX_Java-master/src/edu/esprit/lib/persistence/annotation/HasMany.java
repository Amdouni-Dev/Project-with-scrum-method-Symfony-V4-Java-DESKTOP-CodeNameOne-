package edu.esprit.lib.persistence.annotation;

import edu.esprit.lib.persistence.IdentifiableEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface HasMany {
    Class<? extends IdentifiableEntity> TargetEntity();
    String JoinTable();
    String LocalColumn();
    String ForeignColumn();
}
