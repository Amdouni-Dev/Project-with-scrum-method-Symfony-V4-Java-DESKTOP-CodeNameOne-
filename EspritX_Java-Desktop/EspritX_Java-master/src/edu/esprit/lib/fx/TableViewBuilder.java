package edu.esprit.lib.fx;

import edu.esprit.lib.persistence.IdentifiableEntity;
import edu.esprit.lib.fx.annotations.IndexView;
import edu.esprit.utils.StringUtils;
import javafx.beans.property.Property;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.pacesys.reflect.Reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableViewBuilder<T extends IdentifiableEntity> {
    private TableViewBuilder() {
    }

    public static <T> TableView<T> makeForEntity(Class<T> model) {
        TableView<T> table = new TableView<>();
        populateTableColumns(model, table);
        return table;
    }

    public static <T> TableView<T> populateTableColumns(Class<T> model, TableView<T> table) {
        ArrayList<TableColumn<T, ?>> columns = new ArrayList<>();

        List<Field> fields = Reflect
                .on(model)
                .fields()
                .annotatedWith(IndexView.class);

        for (Field field : fields) {
            if (Property.class.isAssignableFrom(field.getType())) {
                IndexView indexView = field.getAnnotation(IndexView.class);
                TableColumn<T, ?> col = new TableColumn(StringUtils.camelCaseToTitleCase(field.getName()));
                col.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
                col.setSortable(indexView.sortable());
                columns.add(col);
            } else {
                System.out.println("Warning: Tried to display " + field.getName() + " in a table, and is not a property");
            }
        }
        table.getColumns().addAll(columns);
        return table;
    }
}
