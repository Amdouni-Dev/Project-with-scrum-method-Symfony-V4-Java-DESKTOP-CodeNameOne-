package edu.esprit.lib.persistence;

import com.dooapp.fxform.annotation.NonVisual;
import edu.esprit.lib.persistence.annotation.Timestampable;
import javafx.beans.property.SimpleIntegerProperty;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

public abstract class IdentifiableEntity implements Comparable<IdentifiableEntity>, Serializable {

    @NonVisual
    protected SimpleIntegerProperty id = new SimpleIntegerProperty();

    @NonVisual
    protected Boolean persisted = false;

    public IdentifiableEntity() { }

    @Override
    public int hashCode() {
        final int prime = 23;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Boolean getPersisted() {
        return persisted != null && persisted;
    }

    public void setPersisted(Boolean persisted) {
        this.persisted = persisted;
    }

    @PreUpdate
    public void preUpdate() {
        if (this.getClass().getAnnotation(Timestampable.class) != null) {
            Field updatedAtField = null;
            try {
                updatedAtField = this.getClass().getDeclaredField(this.getClass().getAnnotation(Timestampable.class).updatedAtField());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            updatedAtField.setAccessible(true);
            try {
                updatedAtField.set(this, PropertyConverter.convertToProperty(new Timestamp(System.currentTimeMillis())));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @PrePersist
    public void prePersist() {
        if (this.getClass().getAnnotation(Timestampable.class) != null) {
            try {
                Field createdAtField = this.getClass().getDeclaredField(this.getClass().getAnnotation(Timestampable.class).createdAtField());
                createdAtField.setAccessible(true);
                Timestamp primitive = new Timestamp(System.currentTimeMillis());
                createdAtField.set(this, PropertyConverter.convertToProperty(primitive));
                Field updatedAtField = this.getClass().getDeclaredField(this.getClass().getAnnotation(Timestampable.class).updatedAtField());
                updatedAtField.setAccessible(true);
                updatedAtField.set(this, PropertyConverter.convertToProperty(primitive));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s(", this.getClass().getSimpleName()));
        try {
            Field idField = this.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            sb.append(String.format("id=%s, ", idField.get(this)));
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("serialVersionUID")) {
                    sb.append(String.format("%s=%s, ", field.getName(), field.get(this)));
                }
            }
            Field persistedField = this.getClass().getSuperclass().getDeclaredField("persisted");
            persistedField.setAccessible(true);
            sb.append(String.format("persisted=%s, ", persistedField.get(this)));
            sb.replace(sb.lastIndexOf(", "), sb.length(), "");
            sb.append(")");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        IdentifiableEntity other = (IdentifiableEntity) obj;
        if (this.getPersisted() != other.getPersisted()) return false;
        return this.getId() == other.getId();
    }

    @Override
    public int compareTo(IdentifiableEntity other) {
        return Integer.compare(this.id.get(), other.id.get());
    }
}
