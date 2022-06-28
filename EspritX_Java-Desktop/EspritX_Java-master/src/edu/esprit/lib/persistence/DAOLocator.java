package edu.esprit.lib.persistence;

import edu.esprit.lib.persistence.annotation.DAO;
import org.atteo.classindex.ClassFilter;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.InvocationTargetException;

public class DAOLocator {
    @SuppressWarnings("unchecked")
    public static <E extends IdentifiableEntity> AbstractDAO<E> GetDAO(Class<E> entityClass) {
        Iterable<Class<?>> clazz = ClassFilter.only()
                .topLevelOrStaticNested()
                .withPublicDefaultConstructor()
                .from(ClassIndex.getAnnotated(DAO.class));

        for (Class<?> c : clazz) {
            if(c.isAnnotationPresent(DAO.class)){
                if(c.getAnnotation(DAO.class).targetEntity().equals(entityClass)){
                    try {
                        return (AbstractDAO<E>) c.getConstructors()[0].newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
