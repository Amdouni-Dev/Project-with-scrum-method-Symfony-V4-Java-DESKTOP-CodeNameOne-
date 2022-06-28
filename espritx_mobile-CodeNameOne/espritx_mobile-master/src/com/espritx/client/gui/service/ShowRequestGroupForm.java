package com.espritx.client.gui.service;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.Log;
import com.codename1.properties.InstantUI;
import com.codename1.properties.PropertyBase;
import com.codename1.properties.UiBinding;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Request;
import com.espritx.client.entities.User;
import com.espritx.client.gui.components.SelectableTable;
import com.espritx.client.gui.user.ShowGroups;
import com.espritx.client.services.Service.RequestService;
import com.espritx.client.services.User.GroupService;

import java.util.ArrayList;
import java.util.List;

public class ShowRequestGroupForm extends BaseForm {
    public ShowRequestGroupForm() {
        this(Resources.getGlobalResources());
    }

    public ShowRequestGroupForm(Resources resourceObjectInstance) {
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setInlineStylesTheme(resourceObjectInstance);
        Button AddRequest = new Button("Add Request");
        AddRequest.addActionListener(evt -> {
            (new AddRequestForm()).show();
            ;
        });
        addComponent(BorderLayout.SOUTH, AddRequest);
        initUserControls(resourceObjectInstance);
        setTitle("Manage Requests of your groups");
        setName("ManageGroupRequests");
        installSidemenu(resourceObjectInstance);
    }

    private void initUserControls(Resources resourceObjectInstance) {
        List<Request> requestList = RequestService.GetGroupRequests();
        final List<Request> shadowCopy = new ArrayList<>(requestList);

        UiBinding ui = new UiBinding();
        Request prot = new Request();
        UiBinding.BoundTableModel tb = ui.createTableModel(shadowCopy, prot);
        //tb.excludeProperty(prot.id);
        tb.excludeProperty(prot.Attachement);
        tb.excludeProperty(prot.Description);
        tb.excludeProperty(prot.Email);
        tb.excludeProperty(prot.Picture);
        tb.excludeProperty(prot.RespondedAt);
        tb.excludeProperty(prot.UpdatedAt);
        tb.excludeProperty(prot.Response);
        tb.setEditable(prot.id, false);
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
        addComponent(BorderLayout.NORTH, t);

        Button Edit = new Button();
        Edit.setText("Respond");
        Edit.setInlineStylesTheme(resourceObjectInstance);
        Edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (t.getSelectedRow() > -1) {
                    Integer id = (Integer) t.getModel().getValueAt(t.getSelectedRow(), 0);
                    Request r = null;
                    for (Request req : requestList) {
                        if (req.id.get().equals(id)) {
                            r = req;
                            break;
                        }
                    }
                    assert r != null;
                    t.clearSelectedRow();
                    new RespondForm(resourceObjectInstance, r).show();
                }
            }
        });
        addComponent(BorderLayout.CENTER, Edit);

        getToolbar().addSearchCommand(e -> {
            Label l = new Label("Select a search criteria");
            Picker p = new Picker();
            p.setType(Display.PICKER_TYPE_STRINGS);
            p.setStrings("Title", "Requester", "Created At");
            p.setSelectedString("Title");
            Container a = FlowLayout.encloseCenter(l, p);
            a.setLayout(BoxLayout.y());
            add(BorderLayout.CENTER, a);

            int critval = 0;
            String crit = p.getSelectedString();
            if (crit.equals("Requester"))
                critval = 1;
            else if (crit.equals("Created At"))
                critval = 2;

            String text = (String) e.getSource();
            if (text != null) {
                shadowCopy.clear();
                switch (critval) {
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

        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_REMOVE, e -> {
            if (t.getSelectedRow() > -1) {
                Dialog confirm = new Dialog("Confirm");
                confirm.setLayout(BoxLayout.y());
                Button btn = new Button("Confirm");
                Button btn1 = new Button("Cancel");
                Container buttons = new Container(BoxLayout.x());
                buttons.addAll(btn,btn1);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        Integer id = (Integer) t.getModel().getValueAt(t.getSelectedRow(), 0);
                        Request r = null;
                        for (Request req : requestList) {
                            if (req.id.get().equals(id)) {
                                r = req;
                                break;
                            }
                        }
                        assert r != null;
                        Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
                        try {
                            shadowCopy.remove(r);
                            RequestService.DeleteRequest(r);
                            ToastBar.showInfoMessage(r+"has been deleted!");
                        } catch (Exception ex) {
                            Log.p(ex.getMessage(), Log.ERROR);
                            dlg.dispose();
                            Dialog.show("error", ex.getMessage(), "ok", null);
                        }
                        dlg.dispose();
                        confirm.dispose();
                        t.clearSelectedRow();
                        t.revalidate();
                        t.refresh();
                    }
                });
                btn1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        confirm.dispose();
                    }
                });


                confirm.add(new SpanLabel("Do you really want to delete this request?", "DialogBody"));
                confirm.add(buttons);
                confirm.show();
            }
        });
    }
}
