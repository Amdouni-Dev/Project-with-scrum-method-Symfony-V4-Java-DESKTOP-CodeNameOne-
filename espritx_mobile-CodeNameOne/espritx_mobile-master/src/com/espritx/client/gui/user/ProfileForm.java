/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.user;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageButton;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.io.Log;
import com.codename1.properties.InstantUI;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.User;
import com.espritx.client.services.User.AuthenticationService;
import com.espritx.client.services.User.ProfileService;

import java.util.ArrayList;

/**
 * @author Wahib
 */
public class ProfileForm extends BaseForm {
    private User user;

    public ProfileForm() {
        this(Resources.getGlobalResources());
    }

    public ProfileForm(Resources resourceObjectInstance, User instance) {
        this.user = instance;
        instance.getPropertyIndex().registerExternalizable();
        setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
        setName("ProfileForm");
        setInlineStylesTheme(resourceObjectInstance);
        initUserControls(resourceObjectInstance);
        installSidemenu(resourceObjectInstance);
        getToolbar().setTitleComponent(new Label("Edit Profile", "Title"));
    }

    public ProfileForm(Resources resourceObjectInstance) {
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
        // Avatar file
        final String[] newAvatarPath = new String[1];
        Container avatarContainer = new Container(BoxLayout.x());
        avatarContainer.setName("Avatar Container");
        avatarContainer.add(user.getEncodedAvatar(6));
        Label filePickerStatus = new Label("No file chosen.");
        avatarContainer.add(filePickerStatus);
        Style s = UIManager.getInstance().getComponentStyle("Button");
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_EDIT, s);
        ScaleImageButton scaleImageButton = new ScaleImageButton(icon);
        scaleImageButton.addActionListener((evt) -> {
            ActionListener callback = e -> {
                if (e != null && e.getSource() != null) {
                    newAvatarPath[0] = (String) e.getSource();
                    filePickerStatus.setText("File selected for upload.");
                }
            };
            if (FileChooser.isAvailable()) {
                FileChooser.showOpenDialog(".png,image/png,.jpg,image/jpg,.jpeg", callback);
            } else {
                Display.getInstance().openGallery(callback, Display.GALLERY_IMAGE);
            }
        });
        avatarContainer.add(scaleImageButton);
        addComponent(BorderLayout.NORTH, avatarContainer);

        /// Other properties..
        InstantUI iui = new InstantUI();
        iui.excludeProperty(user.id);
        iui.excludeProperty(user.avatarFile);
        iui.excludeProperty(user.userStatus);
        iui.excludeProperty(user.email);
        iui.excludeProperty(user.classe);
        iui.excludeProperty(user.groups);
        Container formContainer = iui.createEditUI(user, true);
        formContainer.setScrollableY(true);
        formContainer.setInlineStylesTheme(resourceObjectInstance);
        addComponent(BorderLayout.CENTER, formContainer);

        Button saveButton = makeButton("SaveButton", "Save");
        saveButton.addActionListener(evt -> {
            Dialog dlg = null;
            try {
                dlg = (new InfiniteProgress()).showInfiniteBlocking();
                User updatedUser = ProfileService.edit_profile(user, newAvatarPath[0]);
                AuthenticationService.updateAuthenticatedUser(updatedUser);
                new ProfileForm(resourceObjectInstance, updatedUser).show();
            } catch (Exception e) {
                Log.p(e.getMessage(), Log.ERROR);
                Dialog.show("error", e.getMessage(), "ok", null);
            } finally {
                assert dlg != null;
                dlg.dispose();
            }
        });
        Container actionsContainer = BoxLayout.encloseXCenter(saveButton);
        addComponent(BorderLayout.SOUTH, actionsContainer);
    }
}
