/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.user;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.Log;
import com.codename1.properties.InstantUI;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.User;
import com.espritx.client.services.User.AuthenticationService;
import com.espritx.client.services.User.UserService;

import java.util.ArrayList;

/**
 * @author Wahib
 */
public class UserForm extends BaseForm {
    private User user;
    private UserService userService;

    public UserForm() {
        this(Resources.getGlobalResources());
    }

    public UserForm(Resources resourceObjectInstance, User instance) {
        this.userService = new UserService(); // Who needs DI when you have CN1?
        this.user = instance;
        instance.getPropertyIndex().registerExternalizable();
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setName("AddUserForm");
        setInlineStylesTheme(resourceObjectInstance);
        initUserControls(resourceObjectInstance);
        installSidemenu(resourceObjectInstance);
        getToolbar().setTitleComponent(new Label("User Details", "Title"));
    }

    public UserForm(Resources resourceObjectInstance) {
        User user = new User();
        user.getPropertyIndex().registerExternalizable();
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setName("AddUserForm");
        setInlineStylesTheme(resourceObjectInstance);
        initUserControls(resourceObjectInstance);
        installSidemenu(resourceObjectInstance);
        getToolbar().setTitleComponent(new Label("Add a User", "Title"));
    }

    private void initUserControls(Resources resourceObjectInstance) {
        InstantUI iui = new InstantUI();
        iui.excludeProperty(this.user.id);
        iui.excludeProperty(this.user.avatarFile);
        iui.excludeProperty(this.user.about);
        iui.setMultiChoiceLabels(this.user.userStatus, "Active", "Pending", "Alumnus", "Restricted");
        iui.setMultiChoiceValues(this.user.userStatus, "active", "pending", "alumnus", "restricted");
        iui.setMultiChoiceLabels(this.user.identityType, "ID Card", "Passport", "N/A");
        iui.setMultiChoiceValues(this.user.identityType, "cin", "passport", "Unknown");
        iui.excludeProperty(this.user.groups);
        Container cnt = iui.createEditUI(user, true);
        cnt.setScrollableY(true);
        cnt.setInlineStylesTheme(resourceObjectInstance);
        addComponent(BorderLayout.CENTER, cnt);

        ArrayList<Button> actions = new ArrayList<Button>();
        Button saveButton = makeButton("SaveButton", "Save");
        saveButton.addActionListener(evt -> {
            Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
            try {
                if (user.id.get() != null) {
                    this.userService.Update(user);
                } else {
                    this.userService.Create(user);
                }
                dlg.dispose();
                (new ShowUsers()).show();
            } catch (Exception e) {
                Log.p(e.getMessage(), Log.ERROR);
                dlg.dispose();
                Dialog.show("error", e.getMessage(), "ok", null);
            }
        });
        actions.add(saveButton);
        if (user.id.get() != null) {
            Button deleteButton = makeButton("DeleteButton", "Delete");
            deleteButton.addActionListener(evt -> {
                if (user == AuthenticationService.getAuthenticatedUser()) {
                    Dialog.show("Error", "You can't delete yourself...", "ok", null);
                    return;
                }
                Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
                try {
                    this.userService.Delete(user);
                } catch (Exception e) {
                    Log.p(e.getMessage(), Log.ERROR);
                    dlg.dispose();
                    Dialog.show("error", e.getMessage(), "ok", null);
                }
                dlg.dispose();
                (new ShowUsers()).show();
            });
            actions.add(deleteButton);
        }
        Container actionsContainer = BoxLayout.encloseXCenter(actions.toArray(new Button[0]));
        addComponent(BorderLayout.SOUTH, actionsContainer);
    }
}
