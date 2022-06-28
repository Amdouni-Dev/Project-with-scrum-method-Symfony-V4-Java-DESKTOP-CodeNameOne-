package com.espritx.client.gui.calendar;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Calendar;
import com.espritx.client.services.serviceCalendar.ServiceCalendar;

import java.util.ArrayList;

public class AdminEvent extends BaseForm {
    public AdminEvent() {
        setTitle("DASHBOARD ADMIN");
        setLayout(new BorderLayout());

        ArrayList<Calendar> events ;
        events= ServiceCalendar.getInstance().getAllEvents();
        Container list = new Container(BoxLayout.y());
        for (Calendar event: events
        ) {
            MultiButton mb = new MultiButton("Id : "+event.getId());
            mb.setTextLine2("Title : " + event.getTitle());
            mb.setTextLine3("Start : " + event.getStart());
            mb.setTextLine4("Ends : " +event.getEnd());
            FontImage.setMaterialIcon(mb,FontImage.MATERIAL_EVENT);

            mb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    Dialog.show("Event information : ", "The event is about " + event.getDescription() +"\n"
                                    +"it start the " + event.getStart() +"\n"
                                    +"until the " + event.getEnd() +"\n"
                                    +"\n"
                                    +"created by \n"+ event.getFirstname() + " "+ event.getLastname()
                            , "OK", null);
                }
            });
            list.add(mb);
        }
        list.setScrollableY(true);
        list.setScrollableX(true);
        add(CENTER,list);

        Command back = new Command("Back") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showBack();
            }
        };
        installSidemenu(Resources.getGlobalResources());
    }
}
