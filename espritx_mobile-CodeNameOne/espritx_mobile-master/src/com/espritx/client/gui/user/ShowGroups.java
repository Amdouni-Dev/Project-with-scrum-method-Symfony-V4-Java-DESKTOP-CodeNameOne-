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
import com.espritx.client.entities.Group;
import com.espritx.client.entities.User;
import com.espritx.client.services.User.GroupService;
import com.espritx.client.services.User.UserService;
import com.espritx.client.utils.StringUtils;

import java.util.List;

/**
 * @author Wahib
 */
public class ShowGroups extends BaseForm {

    public ShowGroups() {
        this(Resources.getGlobalResources());
    }

    public ShowGroups(Resources resourceObjectInstance) {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setInlineStylesTheme(resourceObjectInstance);
        initUserControls(resourceObjectInstance);
        setTitle("Manage Groups");
        setName("ManageGroups");
        installSidemenu(resourceObjectInstance);
    }

    private void initUserControls(Resources resourceObjectInstance) {
        Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
        List<Group> groupList = GroupService.GetAll();
        Container list = new Container(BoxLayout.y());
        list.setScrollableY(true);
        list.setInlineStylesTheme(resourceObjectInstance);
        int size = Display.getInstance().convertToPixels(8, true);
        EncodedImage placeholder = EncodedImage.createFromImage(FontImage.createFixed("" + FontImage.MATERIAL_PERSON, FontImage.getMaterialDesignFont(), 0xff, size, size), true);
        for (Group group : groupList) {
            MultiButton mb = new MultiButton(group.display_name.get());
            mb.setInlineStylesTheme(resourceObjectInstance);
            mb.setIcon(placeholder);
            mb.setUIIDLine1("SlightlySmallerFontLabelLeft");
            mb.setTextLine2(group.groupType.get());
            mb.setUIIDLine2("RedLabel");
            mb.addActionListener(e -> {
                new GroupForm(resourceObjectInstance, group).show();
            });
            list.addComponent(mb);
            mb.putClientProperty("id", group.id.getInt());
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
        getToolbar().addCommandToRightBar("", FontImage.createMaterial(FontImage.MATERIAL_PERSON_ADD, "FloatingActionButton", 4f), (e) -> {
            (new GroupForm(resourceObjectInstance, new Group())).show();
        });
        dlg.dispose();
    }
}
