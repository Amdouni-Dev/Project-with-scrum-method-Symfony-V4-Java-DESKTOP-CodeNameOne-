package com.espritx.client.gui.service;

import com.codename1.properties.UiBinding;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Request;
import com.espritx.client.gui.components.SelectableTable;
import com.espritx.client.services.Service.RequestService;

import java.util.ArrayList;
import java.util.List;

public class ShowRequestsForm extends BaseForm {
    public ShowRequestsForm() {
        this(Resources.getGlobalResources());
    }

    public ShowRequestsForm(Resources resourceObjectInstance) {
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setInlineStylesTheme(resourceObjectInstance);
        Button AddRequest = new Button("Add Request");
        AddRequest.addActionListener(evt -> {
            (new AddRequestForm()).show();
            ;
        });
        addComponent(BorderLayout.SOUTH,AddRequest);
        initUserControls(resourceObjectInstance);
        setTitle("Manage all requests");
        setName("ManageRequests");
        installSidemenu(resourceObjectInstance);
    }

    private void initUserControls(Resources resourceObjectInstance) {
        List<Request> requestList = RequestService.GetAll();
        final List<Request> shadowCopy = new ArrayList<>(requestList);

        UiBinding ui = new UiBinding();
        Request prot = new Request();
        UiBinding.BoundTableModel tb = ui.createTableModel(shadowCopy, prot);
        tb.excludeProperty(prot.id);
        tb.excludeProperty(prot.Attachement);
        tb.excludeProperty(prot.Description);
        tb.excludeProperty(prot.Email);
        tb.excludeProperty(prot.Picture);
        tb.excludeProperty(prot.RespondedAt);
        tb.excludeProperty(prot.UpdatedAt);
        tb.excludeProperty(prot.Response);
        tb.setEditable(prot.Title, false);
        tb.setEditable(prot.CreatedAt, false);
        tb.setEditable(prot.Type, false);
        tb.setEditable(prot.Requester, false);
        tb.setEditable(prot.Status, false);

        SelectableTable t = new SelectableTable(tb);

        t.setSortSupported(true);
        t.setScrollVisible(true);
        t.setScrollableY(true);
        t.setScrollableX(true);
        addComponent(BorderLayout.NORTH,t);

        getToolbar().addSearchCommand(e -> {
            Label l =new Label("Select a search criteria");
            Picker p=new Picker();
            p.setType(Display.PICKER_TYPE_STRINGS);
            p.setStrings("Title","Requester","Created At");
            p.setSelectedString("Title");
            Container a= FlowLayout.encloseCenter(l,p);
            a.setLayout(BoxLayout.y());
            add(BorderLayout.CENTER,a);

            int critval = 0;
            String crit=p.getSelectedString();
            if (crit.equals("Requester"))
                critval = 1;
            else if (crit.equals("Created At"))
                critval = 2;

            String text = (String) e.getSource();
            if (text != null) {
                shadowCopy.clear();
                switch (critval){
                    case 1:
                        for (Request req : requestList) {
                            if (req.Requester.get().toString().contains(text.trim())) {
                                shadowCopy.add(req);
                            }
                        }
                        break;
                    case 2:
                        for (Request req : requestList) {
                            if (req.CreatedAt.get().toString().contains(text.trim())) {
                                shadowCopy.add(req);
                            }
                        }
                        break;
                    default:
                        for (Request req : requestList) {
                            if (req.Title.get().contains(text.trim())) {
                                shadowCopy.add(req);
                            }
                        }
                }
                t.revalidate();
                t.refresh();
            }
        }, 4);
    }
}
