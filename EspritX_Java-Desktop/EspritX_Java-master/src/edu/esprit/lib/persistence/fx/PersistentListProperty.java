package edu.esprit.lib.persistence.fx;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PersistentListProperty<E> extends SimpleListProperty<E> {
    private final Supplier<List<E>> supplier;
    private boolean populated = false;
    private boolean populatable;
    private final transient Logger log;

    {
        this.log = Logger.getLogger(getClass().getName());
    }

    public PersistentListProperty() {
        super(FXCollections.<E>observableArrayList());
        this.supplier = null;
        populatable = false;
    }

    public PersistentListProperty(Supplier<List<E>> supplier) {
        super(FXCollections.observableArrayList());
        this.supplier = supplier;
        populatable = true;
    }

    private synchronized void populate() { // synchronized to avoid the debugger calling this method twice
        if (populatable && !populated) {
            List<E> entities = supplier.get();
            ObservableList<E> list = FXCollections.<E>observableArrayList(entities);
            this.set(list);
        }
        populated = true;
    }

    @Override
    public ObservableList<E> get() {
        populate();
        return super.get();
    }

    @Override
    public int size() {
        populate();
        return super.get().size();
    }

    @Override
    public boolean isEmpty() {
        populate();
        return super.get().isEmpty();
    }

    @Override
    public boolean add(E element) {
        populate();
        return super.add(element);
    }

    @Override
    public boolean remove(Object o) {
        populate();
        return super.remove(o);
    }

    @Override
    public String toString() {
        return "Lazy loaded collection";
    }
}
