/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.user;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ToastBar;
import com.codename1.io.Log;
import com.codename1.properties.InstantUI;
import com.codename1.properties.UiBinding;
import com.codename1.ui.*;
import com.codename1.ui.events.FocusListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Group;
import com.espritx.client.entities.User;
import com.espritx.client.gui.components.SelectableTable;
import com.espritx.client.services.User.GroupService;
import com.espritx.client.services.User.UserService;

import java.util.ArrayList;

/**
 * @author Wahib
 */
public class GroupForm extends BaseForm {
    private Group group;

    public GroupForm() {
        this(Resources.getGlobalResources());
    }

    public GroupForm(Resources resourceObjectInstance, Group instance) {
        this.group = instance;
        instance.getPropertyIndex().registerExternalizable();
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setName("AddGroupForm");
        setInlineStylesTheme(resourceObjectInstance);
        initUserControls(resourceObjectInstance);
        installSidemenu(resourceObjectInstance);
        getToolbar().setTitleComponent(new Label("Group Details", "Title"));
    }

    public GroupForm(Resources resourceObjectInstance) {
        User user = new User();
        user.getPropertyIndex().registerExternalizable();
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setName("AddUserForm");
        setInlineStylesTheme(resourceObjectInstance);
        initUserControls(resourceObjectInstance);
        installSidemenu(resourceObjectInstance);
        getToolbar().setTitleComponent(new Label("Group Details", "Title"));
    }

    private void initUserControls(Resources resourceObjectInstance) {

        //// Basic information
        InstantUI iui = new InstantUI();
        iui.excludeProperty(this.group.id);
        iui.setMultiChoiceLabels(this.group.groupType, "Super Admin", "Student", "Site Staff", "Faculty Staff", "Teachers");
        iui.setMultiChoiceValues(this.group.groupType, "super admin", "student", "site staff", "faculty staff", "teachers");
        iui.excludeProperty(this.group.members);
        Container cnt = iui.createEditUI(group, true);
        cnt.setScrollableY(true);
        cnt.setInlineStylesTheme(resourceObjectInstance);
        addComponent(BorderLayout.NORTH, cnt);

        //////// Group members.
        UiBinding ui = new UiBinding();
        User prot = new User();
        UiBinding.BoundTableModel tb = ui.createTableModel(group.members, prot);

        tb.excludeProperty(prot.groups);
        tb.excludeProperty(prot.avatarFile);
        tb.excludeProperty(prot.classe);
        tb.excludeProperty(prot.identityType);
        tb.excludeProperty(prot.identityDocumentNumber);
        tb.excludeProperty(prot.phoneNumber);
        tb.excludeProperty(prot.plainPassword);
        tb.excludeProperty(prot.userStatus);
        tb.setEditable(prot.first_name, false);
        tb.setEditable(prot.last_name, false);
        tb.setEditable(prot.email, false);
        tb.setEditable(prot.id, false);

        SelectableTable t = new SelectableTable(tb);
        t.setSortSupported(true);
        t.setScrollVisible(true);
        t.setScrollableY(true);
        t.setScrollableX(true);
        addComponent(BorderLayout.CENTER, t);

        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_ADD, e -> {


            final DefaultListModel<String> options = new DefaultListModel<>();
            AutoCompleteTextField ac = new AutoCompleteTextField(options) {
                @Override
                protected boolean filter(String text) {
                    if (text.length() < 3) {
                        return false;
                    }
                    ArrayList<String> l = UserService.AutoComplete(text);
                    if (l == null || l.size() == 0) {
                        return false;
                    }
                    options.removeAll();
                    for (String s : l) {
                        options.addItem(s);
                    }
                    return true;
                }
            };
            ac.setMinimumElementsShownInPopup(1);
            ac.setPopupPosition(AutoCompleteTextField.POPUP_POSITION_UNDER);

            Dialog d = new Dialog("Add a User to " + group.display_name);

            Button button = this.makeButton("Add", "Add");
            button.addActionListener(evt -> {
                Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
                User newUser = UserService.fetchUserByEmail(ac.getText());
                dlg.dispose();
                if (group.members.contains(newUser)) {
                    ToastBar.showInfoMessage("User is already in group");
                    return;
                }
                d.dispose();
                group.members.add(newUser);
                t.clearSelectedRow().refresh();
                ToastBar.showInfoMessage(newUser.getFullName() + " added. Don't forget to save.");
            });
            d.setLayout(new BorderLayout());
            d.add(BorderLayout.CENTER, ac);
            d.add(BorderLayout.SOUTH, button);
            d.show();
        });

        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_REMOVE, e -> {
            if (t.getSelectedRow() > -1) {
                Integer id = (Integer) t.getModel().getValueAt(t.getSelectedRow(), 0);
                User u = null;
                for (User _elided : group.members) {
                    if (_elided.id.get().equals(id)) {
                        u = _elided;
                        break;
                    }
                }
                group.members.remove(u);
                t.clearSelectedRow().refresh();
                ToastBar.setDefaultMessageTimeout(5000);
                assert u != null;
                ToastBar.showInfoMessage(u.getFullName() + "removed. Don't forget to save.");
            }
        });

        /// Actions
        ArrayList<Button> actions = new ArrayList<Button>();
        Button saveButton = makeButton("SaveButton", "Save");
        saveButton.addActionListener(evt -> {
            Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
            try {
                if (group.id.get() != null) {
                    GroupService.Update(group);
                } else {
                    GroupService.Create(group);
                }
            } catch (Exception e) {
                Log.p(e.getMessage(), Log.ERROR);
                dlg.dispose();
                Dialog.show("error", e.getMessage(), "ok", null);
            }
            dlg.dispose();
            (new ShowGroups()).show();
        });
        actions.add(saveButton);
        if (group.id.get() != null) {
            if (group.id.get() != null) {
                Button deleteButton = makeButton("DeleteButton", "Delete");
                deleteButton.addActionListener(evt -> {
                    Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
                    try {
                        GroupService.Delete(group);
                    } catch (Exception e) {
                        Log.p(e.getMessage(), Log.ERROR);
                        dlg.dispose();
                        Dialog.show("error", e.getMessage(), "ok", null);
                    }
                    dlg.dispose();
                    (new ShowGroups()).show();
                });
                actions.add(deleteButton);
            }
        }
        Container actionsContainer = BoxLayout.encloseXCenter(actions.toArray(new Button[0]));
        addComponent(BorderLayout.SOUTH, actionsContainer);
    }
}
