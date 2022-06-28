package edu.esprit.lib.fx.fxform2.extensions;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.factory.impl.FXFormChoiceBoxNode;
import edu.esprit.lib.persistence.QueryableEnum;
import edu.esprit.utils.EnumUtils;
import edu.esprit.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.Arrays;

public class BackedEnumChoiceBoxFactory implements Callback<Void, FXFormNode> {

    private final static System.Logger logger = System.getLogger(com.dooapp.fxform.view.factory.impl.EnumChoiceBoxFactory.class.getName());

    public FXFormNode call(Void aVoid) {

        return new FXFormChoiceBoxNode() {
            @Override
            public void init(Element element, AbstractFXForm fxForm) {
                Enum[] constants = new Enum[0];
                try {
                    constants = (Enum[]) element.getWrappedType().getEnumConstants();
                } catch (Exception e) {
                    logger.log(System.Logger.Level.WARNING, "Could not retrieve enum constants from element " + element, e);
                }

                choiceBox.setItems(FXCollections.observableList(Arrays.asList(constants)));
                choiceBox.setConverter(new StringConverter() {
                    @Override
                    public String toString(Object object) {
                        if (object == null) {
                            return "N/A";
                        }
                        return StringUtils.camelCaseToTitleCase(((QueryableEnum<String>) object).getValue());
                    }

                    @Override
                    public Object fromString(String string) {
                        Class<? extends QueryableEnum> clazz = (Class<? extends QueryableEnum>) element.sourceProperty().getValue().getClass();
                        return EnumUtils.getEnum(clazz, string);
                    }
                });
                choiceBox.getSelectionModel().select(element.getValue());
            }

            @Override
            public boolean isEditable() {
                return true;
            }

        };

    }

}
