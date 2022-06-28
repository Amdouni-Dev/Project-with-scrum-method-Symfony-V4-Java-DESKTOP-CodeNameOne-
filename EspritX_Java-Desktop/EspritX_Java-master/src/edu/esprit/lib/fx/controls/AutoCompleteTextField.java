package edu.esprit.lib.fx.controls;

import edu.esprit.lib.fx.converter.EntityConverter;
import edu.esprit.lib.persistence.AbstractDAO;
import edu.esprit.lib.persistence.DAOLocator;
import edu.esprit.lib.persistence.IdentifiableEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class AutoCompleteTextField<T extends IdentifiableEntity> extends TextField {
    /**
     * The popup used to select an entry.
     */
    private final ContextMenu entriesPopup;

    private final EntityConverter<T> entityConverter;
    private final AbstractDAO<T> dao;

    private final SimpleObjectProperty<T> selectedItem = new SimpleObjectProperty<>();

    public T getSelectedItem() {
        return selectedItem.get();
    }

    public SimpleObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    @SuppressWarnings("unchecked")
    public AutoCompleteTextField(Field inflatingField) {
        super();
        Class<T> entityClass = (Class<T>) inflatingField.getDeclaringClass();
        this.entityConverter = new EntityConverter<T>(inflatingField);
        this.dao = DAOLocator.GetDAO(entityClass);

        entriesPopup = new ContextMenu();
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                if (getText().length() == 0) {
                    entriesPopup.hide();
                } else {
                    List<T> entities = dao.findBy("`" + inflatingField.getAnnotation(Column.class).name() + "` LIKE '%" + getText() + "%'");
                    LinkedList<T> searchResult = new LinkedList<T>(entities);
                    if (searchResult.size() > 0) {
                        populatePopup(searchResult);
                        if (!entriesPopup.isShowing()) {
                            entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    } else {
                        entriesPopup.hide();
                    }
                }
            }
        });
    }

    /**
     * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<T> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 5;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final String result = this.entityConverter.toString(searchResult.get(i));
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            int finalI = i; // because fuck java. I'm serious. I don't want to write this code twice.
            item.setOnAction(actionEvent -> {
                setText(result);
                entriesPopup.hide();
                setSelectedItem(searchResult.get(finalI));
                entriesPopup.hide();
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }


}