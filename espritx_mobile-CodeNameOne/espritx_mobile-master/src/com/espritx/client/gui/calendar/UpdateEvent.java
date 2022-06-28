package com.espritx.client.gui.calendar;

import com.codename1.components.ShareButton;
import com.codename1.components.ToastBar;
import com.codename1.l10n.ParseException;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.codename1.util.DateUtil;
import com.espritx.client.entities.Calendar;
import com.espritx.client.services.serviceCalendar.ServiceCalendar;

import java.util.Date;

public class UpdateEvent extends BaseForm {

    public UpdateEvent(Calendar calendar, Form prev) {
        setTitle("Update Event");
        setLayout(BoxLayout.y());

        Picker start = new Picker();
        start.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        start.setDate(calendar.getStart());
        Picker end = new Picker();
        end.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        end.setDate(calendar.getEnd());
        Label lstart = new Label("StartDate");
        Label lend = new Label("EndDate");
        Label ltitle = new Label("Title");
        TextField tfTitle = new TextField(calendar.getTitle());
        Label lDescription = new Label("Description");
        TextField tfDescription = new TextField(calendar.getDescription());
        CheckBox cbAllday = new CheckBox("All Day");
        cbAllday.setSelected(calendar.isAllDay());
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnCancel = new Button("Cancel");
        Style supprimerStyle = new Style(btnDelete.getUnselectedStyle());
        btnDelete.setIcon(FontImage.createMaterial(FontImage.MATERIAL_DELETE, supprimerStyle));
        btnUpdate.setIcon(FontImage.createMaterial(FontImage.MATERIAL_UPDATE, supprimerStyle));
        btnCancel.setIcon(FontImage.createMaterial(FontImage.MATERIAL_CANCEL, supprimerStyle));
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                prev.showBack();
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Dialog dig = new Dialog("Delete event");
                if (dig.show("Delete", "Are u sure abt that?", "Yes", "No")) {
                    dig.dispose();
                } else
                    dig.dispose();

                if (ServiceCalendar.getInstance().deleteEvent(calendar.getId())) {
                    Dialog.show("Success", "Event deleted", new Command("OK"));
                    new ShowEventform(prev).show();
                } else {
                    Dialog.show("ERROR", "Server error", new Command("OK"));
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                boolean status = false;
                int id = calendar.getId();
                if (tfTitle.getText().equals("") || tfDescription.getText().equals("") || DateUtil.compare(start.getDate(), end.getDate()) == 1)
                    ToastBar.showMessage("Check again", FontImage.MATERIAL_WARNING);
                else {
                    if (cbAllday.isSelected()) {
                        status = true;
                    }
                    com.espritx.client.entities.Calendar calendar = new Calendar(id, start.getDate(), end.getDate(), tfTitle.getText(), tfDescription.getText(), status);
                    if (ServiceCalendar.getInstance().updateEvent(calendar)) {
                        Dialog.show("Success", "Event Updated", new Command("OK"));
                        new ShowEventform(prev).show();
                    } else {
                        Dialog.show("ERROR", "Server error", new Command("OK"));
                    }
                }
            }
        });
        ShareButton sb = new ShareButton();
        sb.setText("Share");
        sb.setTextToShare("We have new Event : " + calendar.getTitle() +
                "\nit will be : " + calendar.getStart() +
                "\n We are looking forword to meet you");
        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            prev.showBack();
        });
        installSidemenu(Resources.getGlobalResources());
        addAll(ltitle, tfTitle, lDescription, tfDescription, lstart, start, lend, end, cbAllday, btnUpdate, btnDelete, btnCancel, sb);
    }
}
