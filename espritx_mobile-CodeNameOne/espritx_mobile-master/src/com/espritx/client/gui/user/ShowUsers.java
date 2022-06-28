/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.user;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.codename1.util.StringUtil;
import com.espritx.client.entities.User;
import com.espritx.client.services.User.UserService;
import com.espritx.client.utils.StringUtils;

import java.util.List;

/**
 * @author Wahib
 */
public class ShowUsers extends BaseForm {

    public ShowUsers() {
        this(Resources.getGlobalResources());
    }

    public ShowUsers(Resources resourceObjectInstance) {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setInlineStylesTheme(resourceObjectInstance);
        initUserControls(resourceObjectInstance);
        setTitle("Manage Users");
        setName("ManageUsers");
        installSidemenu(resourceObjectInstance);
    }

    private void initUserControls(Resources resourceObjectInstance) {
        Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
        List<User> userList = UserService.GetAll();
        Container list = new Container(BoxLayout.y());
        list.setScrollableY(true);
        list.setInlineStylesTheme(resourceObjectInstance);


        for (User user : userList) {
            MultiButton mb = new MultiButton(user.getFullName());
            mb.setInlineStylesTheme(resourceObjectInstance);
            mb.setIcon(user.getEncodedAvatar(8));
            mb.setUIIDLine1("SlightlySmallerFontLabelLeft");
            mb.setTextLine2(user.email.get());
            mb.setUIIDLine2("RedLabel");
            mb.addActionListener(e -> {
                new UserForm(resourceObjectInstance, user).show();
            });
            list.addComponent(mb);
            mb.putClientProperty("id", user.id.getInt());
        }
        addComponent(list);
        getToolbar().addSearchCommand(e -> {
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                for (Component cmp : list) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
            } else {
                text = text.toLowerCase();
                for (Component cmp : list) {
                    MultiButton mb = (MultiButton) cmp;
                    String line1 = mb.getTextLine1();
                    String line2 = mb.getTextLine2();
                    boolean show = line1 != null && line1.toLowerCase().contains(text) ||
                            line2 != null && line2.toLowerCase().contains(text);
                    mb.setHidden(!show);
                    mb.setVisible(show);
                }
            }
            list.animateLayout(150);
        }, 4);
        getToolbar().addCommandToRightBar("", FontImage.createMaterial(FontImage.MATERIAL_PERSON_ADD, "FloatingActionButton", 4f), (e) -> (new UserForm(resourceObjectInstance, new User())).show());
        dlg.dispose();
    }
}
