/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.calendar;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;

/**
 *
 * @author Mohzsen
 */
public class HomeEvent extends BaseForm {
    public HomeEvent() {
        setTitle("Home Page");
        setLayout(BoxLayout.y());
        Button btnAddEvent = new Button ("Add Event");
        Button btnShowEvent = new Button ("Show Events");
        Button btnAdmin = new Button ("Dashboard Admin");
        btnAddEvent.addActionListener((evt) -> {
            new AddEvent(this).show();
        });
        btnAdmin.addActionListener((evt) -> {
            new AdminEvent().show();
        });
        btnShowEvent.addActionListener((evt) -> {
            new ShowEventform(this).show();
        });
        Command back = new Command("Back") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showBack();
            }
        };
        installSidemenu(Resources.getGlobalResources());
        addAll(btnAddEvent,btnShowEvent);
    }
}
