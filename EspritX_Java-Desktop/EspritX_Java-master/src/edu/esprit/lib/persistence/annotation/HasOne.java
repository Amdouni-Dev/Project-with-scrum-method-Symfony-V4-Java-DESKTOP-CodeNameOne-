package edu.esprit.lib.persistence.annotation;

import edu.esprit.lib.persistence.IdentifiableEntity;

public @interface HasOne {
    Class<? extends IdentifiableEntity> relatedClass();
    String ForeignKey();
}
