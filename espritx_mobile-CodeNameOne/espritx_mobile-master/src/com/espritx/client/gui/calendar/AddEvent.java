/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.calendar;

import com.codename1.components.ToastBar;
import com.codename1.messaging.Message;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.Validator;
import com.codename1.uikit.pheonixui.BaseForm;
import com.codename1.util.DateUtil;
import com.espritx.client.entities.Calendar;
import com.espritx.client.entities.User;
import com.espritx.client.services.User.UserService;
import com.espritx.client.services.serviceCalendar.ServiceCalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


/**
 *
 * @author Mohzsen
 */
public class AddEvent extends BaseForm {

    public AddEvent(Form prev) {
        setTitle("Add Event");
        setLayout(BoxLayout.y());
        Picker start = new Picker();
        start.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        Picker end = new Picker();
        Label lstart = new Label("StartDate");
        Label lend = new Label("EndDate");
        end.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        Label ltitle = new Label("Title");
        TextField tfTitle = new TextField("", "Taper le titre d\n'evenement");
        Label lDescription = new Label("Description");
        TextField tfDescription = new TextField("", "Taper la description d\n'evenement");
        CheckBox cbAllday = new CheckBox("All Day");
        Button btnAdd = new Button("Add");
             Command back = new Command("Back") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                prev.show();
            }
        };
             List<User> ListOfUsers = UserService.GetAll();
             ArrayList<String> ListOfUsersEmail = new ArrayList<>();
        for (User i:ListOfUsers
             ) {
            ListOfUsersEmail.add(i.email.get());
        }
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                boolean status = false;

                if (tfTitle.getText().equals("") || tfDescription.getText().equals("") || DateUtil.compare(start.getDate(),end.getDate())==1)
                    ToastBar.showMessage("Check again", FontImage.MATERIAL_WARNING);
                else {
                    if (cbAllday.isSelected()) {
                        status = true;
                    }

                    Calendar calendar = new Calendar(start.getDate(), end.getDate(), tfTitle.getText(), tfDescription.getText(), status);
                    if (ServiceCalendar.getInstance().addEvent(calendar)) {
                        Dialog.show("Success", "Event Added", new Command("OK"));
                        Message m = new Message("We have a new event, the "+ ServiceCalendar.getInstance().convertt(calendar.getStart())+" go and check it out");

                        for (String a : ListOfUsersEmail) {
                            Display.getInstance().sendMessage(new String[] {a}, "We have a new event", m);
                        }
                    } else {
                        Dialog.show("ERROR", "Server error", new Command("OK"));
                    }

                }
            }
        });

        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            prev.showBack();
        });
        installSidemenu(Resources.getGlobalResources());
        addAll(ltitle,tfTitle,lDescription, tfDescription,lstart, start,lend, end, cbAllday, btnAdd);
    }

}
