package edu.esprit.lib.fx.fxform2.extensions;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import edu.esprit.lib.fx.TableViewBuilder;
import edu.esprit.lib.fx.annotations.Inflatable;
import edu.esprit.lib.fx.fxform2.view.property.TableViewProperty;
import edu.esprit.lib.persistence.annotation.HasMany;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.pacesys.reflect.Reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SimpleRelationTableViewFactory implements Callback<Void, FXFormNode> {
    @Override
    public FXFormNode call(Void aVoid) {
        final SimpleRelationTableView[] tableView = {new SimpleRelationTableView()};

        return new FXFormNodeWrapper(tableView[0], new TableViewProperty(tableView[0])) { // dummy because java sucks...
            @Override
            public void init(Element element, AbstractFXForm fxForm) {
                try {
                    Class wrappedType = element.getWrappedType();
                    TableViewBuilder.populateTableColumns(wrappedType, tableView[0]);
                    tableView[0].setItems((ObservableList) element.getValue());
                    Annotation autoCompleteType = element.getAnnotation(Inflatable.class);
                    if (autoCompleteType != null) {
                        Inflatable inflatable = (Inflatable) autoCompleteType;
                        Class distant = ((HasMany) element.getAnnotation(HasMany.class)).TargetEntity();
                        Field remoteField = Reflect.on(distant).fields().named(inflatable.fieldName());
                        remoteField.setAccessible(true);
                        tableView[0].setInflatingField(remoteField);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
