package edu.esprit.lib.fx.fxform2.view.property;

import edu.esprit.lib.persistence.fx.PersistentListProperty;
import javafx.scene.control.TableView;

public class TableViewProperty extends PersistentListProperty {
    public TableViewProperty(TableView tableView) {
        super();
        //Bindings.bindContentBidirectional(this.get(), tableView.getItems());
    }
}
