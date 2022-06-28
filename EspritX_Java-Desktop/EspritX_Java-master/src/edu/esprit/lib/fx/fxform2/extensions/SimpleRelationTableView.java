package edu.esprit.lib.fx.fxform2.extensions;

import edu.esprit.lib.fx.controls.AutoCompleteTextField;
import edu.esprit.lib.persistence.IdentifiableEntity;
import edu.esprit.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.Optional;

public class SimpleRelationTableView<E extends IdentifiableEntity> extends TableView<E> {
    private Field inflatingField = null;

    public void setInflatingField(Field field) {
        this.inflatingField = field;
    }

    public SimpleRelationTableView() {
        ContextMenu menu = new ContextMenu();

        MenuItem mi1 = new MenuItem("Delete");
        mi1.disableProperty().bind(this.getSelectionModel().selectedItemProperty().isNull());
        mi1.setOnAction((ActionEvent event) -> {
            Object item = getSelectionModel().getSelectedItem();
            System.out.println("[Context Menu]: Deleting related item item: " + item);
            this.getItems().remove(item);
        });
        menu.getItems().add(mi1);


        MenuItem mi2 = new MenuItem("Add");
        menu.getItems().add(mi2);
        mi2.setOnAction((ActionEvent event) -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add new " + inflatingField.getDeclaringClass().getSimpleName());
            AutoCompleteTextField<E> autoCompleteTextField = new AutoCompleteTextField<E>(inflatingField);
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(5);
            Label label = new Label(String.format("Search for %s by %s ", inflatingField.getDeclaringClass().getSimpleName(), StringUtils.camelCaseToTitleCase(inflatingField.getName())));
            vBox.getChildren().addAll(label, autoCompleteTextField);
            dialog.getDialogPane().setContent(vBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(autoCompleteTextField.selectedItemProperty().isNull());

            Optional<ButtonType> s = dialog.showAndWait();
            s.ifPresent(s1 -> {
                if (s1.equals(ButtonType.OK)) {
                    E item = autoCompleteTextField.getSelectedItem();
                    System.out.println("[Context Menu]: Adding related item item: " + item);
                    this.getItems().add(item);
                    autoCompleteTextField.clear();
                    dialog.close();
                } else if (s1.equals(ButtonType.CLOSE)) {
                    dialog.close();
                }
            });
        });


        setContextMenu(menu);
    }
}
