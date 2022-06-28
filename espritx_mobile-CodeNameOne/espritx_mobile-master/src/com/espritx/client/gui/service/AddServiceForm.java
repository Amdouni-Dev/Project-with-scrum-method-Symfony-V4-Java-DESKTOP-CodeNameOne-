package com.espritx.client.gui.service;

import com.codename1.components.*;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Group;
import com.espritx.client.entities.Service;
import com.espritx.client.services.Service.ServiceService;
import com.espritx.client.services.User.GroupService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddServiceForm extends BaseForm {
    public AddServiceForm() {
        this(Resources.getGlobalResources());
    }

    public AddServiceForm(Resources resourceObjectInstance) {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setInlineStylesTheme(resourceObjectInstance);
        setTitle("Add a new Service");
        setLayout(BoxLayout.y());

        TextField tfName = new TextField("", "Service Name");

        List<Group> groupList = GroupService.GetAll();

        Label Respo = new Label("Responsible");

        RadioButtonList radioButtonList = new RadioButtonList(new DefaultListModel());
        radioButtonList.setLayout(BoxLayout.y());
        for(Group g:groupList) {
            radioButtonList.getMultiListModel().addItem(g);
        }
        Button b = new Button("Pick A Responsible");
        b.addActionListener(e -> {
            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(true);
            d.add(radioButtonList);
            radioButtonList.addActionListener(ee -> {
                b.setText(radioButtonList.getModel().getItemAt(radioButtonList.getModel().getSelectedIndex()).toString());
                d.dispose();
            });
            d.showPopupDialog(b);
            d.removeComponent(radioButtonList);
        });

        Label Reciep = new Label("Recipients");

        Container list = new Container(BoxLayout.y());
        CheckBoxList checkBoxList = new CheckBoxList(new DefaultListModel());
        list.setScrollableY(true);
        for (Group g : groupList) {
            checkBoxList.getMultiListModel().addItem(g);
        }
        list.add(checkBoxList);
        Button btnValider = new Button("Add task");

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (tfName.getText().length() == 0)
                    Dialog.show("Alert", "Please add a service name", new Command("Sorry"));
                else if ((radioButtonList.getModel().getSelectedIndex())==0)
                    Dialog.show("Alert", "Please add a responsible for the service", new Command("On it"));
                else if ((checkBoxList.getMultiListModel().getSelectedIndices().length)==0)
                    Dialog.show("Alert", "Please select a recipient for the service", new Command("Sure"));
                else {
                    try {
                        Group Responsible=(Group) radioButtonList.getModel().getItemAt(radioButtonList.getModel().getSelectedIndex());
                        ArrayList<Group> Recipients = new ArrayList<Group>();
                        for (int i:checkBoxList.getMultiListModel().getSelectedIndices()) {
                            Group g=(Group)checkBoxList.getMultiListModel().getItemAt(i);
                            Recipients.add(g);
                        }
                        Service S = new Service(tfName.getText(),Responsible,Recipients);

                        if (ServiceService.getInstance().addService(S)) {
                            Dialog success = new Dialog("Success");
                            success.setLayout(BoxLayout.y());
                            Button btn = new Button("Great");
                            btn.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    Form f=new ShowForm();
                                    f.show();
                                }
                            });
                            success.add(new SpanLabel("Service Created Successfully", "DialogBody"));
                            success.add(btn);
                            success.show();
                        } else
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                    } catch (NumberFormatException e) {
                    }
                }
            }
        });
        addAll(tfName,Respo,b, Reciep, list, btnValider);
        Form f=new ShowForm();
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> f.show());
        setTitle("Add Service");
        setName("AddService");
    }
}
