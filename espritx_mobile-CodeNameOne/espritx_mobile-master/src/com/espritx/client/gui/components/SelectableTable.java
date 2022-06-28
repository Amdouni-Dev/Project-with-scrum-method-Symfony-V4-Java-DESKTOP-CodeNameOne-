package com.espritx.client.gui.components;

import com.codename1.properties.UiBinding;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.table.Table;

// <rant>
// Because apparently CN1 can't properly separate focus items on different scopes.
// Pressing anything else will make us lose the selection. **sigh**
// </rant>

public class SelectableTable extends Table {
    private int selectedRow = -1;

    public SelectableTable(UiBinding.BoundTableModel tb) {
        super(tb);
    }

    @Override
    protected Component createCell(Object value, int row, int column, boolean editable) {
        Component cell;
        if (editable) {
            cell = super.createCell(value, row, column, editable);
        } else {
            cell = new Button(value.toString());
            cell.setUIID("TableCell");
            ((Button) cell).addActionListener(e -> {
                selectedRow = row;
                this.refresh();
            });
            cell.getAllStyles().setBgColor(0xFFFFFF);
            cell.getAllStyles().setBgTransparency(100);
            if (selectedRow > -1 && selectedRow == row) {
                cell.getAllStyles().setBgColor(0xff0000);
            }
            return cell;
        }
        return cell;
    }

    @Override
    public int getSelectedRow() {
        return this.selectedRow;
    }

    public SelectableTable clearSelectedRow() {
        this.selectedRow = -1;
        return this;
    }

    public SelectableTable refresh() {
        setModel(getModel());
        return this;
    }
}
