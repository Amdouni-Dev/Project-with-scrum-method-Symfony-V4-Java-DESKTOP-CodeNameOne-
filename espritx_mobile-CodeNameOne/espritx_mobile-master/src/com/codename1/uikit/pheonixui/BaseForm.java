/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.codename1.uikit.pheonixui;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import com.codename1.util.DateUtil;
import com.espritx.client.entities.User;
import com.espritx.client.gui.ForumPost.HomeForum;
import com.espritx.client.gui.calendar.AdminEvent;
import com.espritx.client.gui.calendar.HomeEvent;
import com.espritx.client.gui.chat.ConversationsShow;
import com.espritx.client.gui.posts.*;
import com.espritx.client.gui.service.ShowForm;
import com.espritx.client.gui.service.ShowRequestGroupForm;
import com.espritx.client.gui.user.LoginForm;
import com.espritx.client.gui.user.ProfileForm;
import com.espritx.client.gui.user.ShowGroups;
import com.espritx.client.gui.user.ShowUsers;
import com.espritx.client.services.User.AuthenticationService;
import com.espritx.client.services.serviceCalendar.ServiceCalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility methods common to forms e.g. for binding the side menu
 *
 * @author Shai Almog
 */
public class BaseForm extends Form {
    private Resources resources;

    public void installSidemenu(Resources res) {
        User authenticatedUser = AuthenticationService.getAuthenticatedUser();

        this.resources = res;
        Image selection = res.getImage("selection-in-sidemenu.png");

        Image inboxImage = null;
        if (isCurrentInbox()) inboxImage = selection;

        Image trendingImage = null;
        if (isCurrentTrending()) trendingImage = selection;

        Image calendarImage = null;
        if (isCurrentCalendar()) calendarImage = selection;

        Image statsImage = null;
        if (isCurrentStats()) statsImage = selection;

        Button inboxButton = new Button("Inbox", inboxImage);
        inboxButton.setUIID("SideCommand");
        inboxButton.getAllStyles().setPaddingBottom(0);
        Container inbox = FlowLayout.encloseMiddle(inboxButton, new Label("18", "SideCommandNumber"));
        inbox.setLeadComponent(inboxButton);
        inbox.setUIID("SideCommand");
        inboxButton.addActionListener(e -> new InboxForm().show());
        getToolbar().addCommandToRightBar("", authenticatedUser.getEncodedAvatar(6), e -> {
            new ProfileForm(res, authenticatedUser).show();
        });
        getToolbar().addComponentToSideMenu(inbox);
        getToolbar().addCommandToSideMenu("Calendar", calendarImage, e -> new HomeEvent().show());
        getToolbar().addCommandToSideMenu("Chat", null, e -> new ConversationsShow(res).show());
        getToolbar().addCommandToSideMenu("Acceuil Posts", null, e -> new ListPosts(res).show());
        getToolbar().addCommandToSideMenu("Chercher Posts", null, e -> new AcceuilPost());
        if(!AuthenticationService.getAuthenticatedUser().isStudent())
            getToolbar().addCommandToSideMenu("Manage Events", calendarImage, e -> new AdminEvent().show());
        getToolbar().addCommandToSideMenu("Service", null, e -> new ShowForm(res).show());
        getToolbar().addCommandToSideMenu("Requests", null, e -> new ShowRequestGroupForm(res).show());
        getToolbar().addCommandToSideMenu("Trending", trendingImage, e -> new TrendingForm(res).show());
        getToolbar().addCommandToSideMenu("Posts", null, e -> new HomeForm().show());
        getToolbar().addCommandToSideMenu("Manage Users", null, e -> new ShowUsers(res).show());
        getToolbar().addCommandToSideMenu("Manage Groups", null, e -> new ShowGroups(res).show());
        getToolbar().addCommandToSideMenu("Acceuil Posts", null, e -> new ListPosts(res).show());
        getToolbar().addCommandToSideMenu("Chercher Posts", null, e -> new AcceuilPost());
        getToolbar().addCommandToSideMenu("Acceuil Posts", null, e -> new ListPosts(res).show());
        getToolbar().addCommandToSideMenu("Stat Posts", null, e -> new StatistiquePie(res).show());
        getToolbar().addCommandToSideMenu("Gerer Les posts", null, e -> new Admin().show());
        getToolbar().addCommandToSideMenu("Forum", trendingImage, e -> new HomeForum().show());
        getToolbar().addCommandToSideMenu("Gerer Les posts", null, e -> new Admin().show());
        getToolbar().addCommandToSideMenu("Logout", null, e -> {
            AuthenticationService.Deauthenticate();
            new LoginForm(resources).show();
        });
        // getToolbar().addMaterialCommandToLeftBar("Posts", FontImage.MATERIAL_POST_ADD, (evt)->{});

        // spacer
        getToolbar().addComponentToSideMenu(new Label(" ", "SideCommand"));
        getToolbar().addComponentToSideMenu(new Label(authenticatedUser.getEncodedAvatar(), "Container"));
        getToolbar().addComponentToSideMenu(new Label(authenticatedUser.getFullName(), "SideCommandNoPad"));
        getToolbar().addComponentToSideMenu(new Label(authenticatedUser.email.get(), "SideCommandSmall"));
        reminder();
    }

    public void reminder(){
        Timer timer = new Timer();
        ArrayList<com.espritx.client.entities.Calendar> events = ServiceCalendar.getInstance().getAllEvents();
        for (com.espritx.client.entities.Calendar c : events){
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    /*Media m = null;
                    try {
                        m = MediaManager.createBackgroundMedia("../res/theme/bell.mp3");
                        m.play();
                        //java.awt.Toolkit.getDefaultToolkit().beep();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    Dialog.show("Reminder", c.getTitle()+" is here!!", new Command("OK"));
                }
            };
            if(DateUtil.compare(c.getStart(), new Date()) == 1){
                timer.schedule(timerTask,c.getStart());
            }
        }

    }

    protected Button makeButton(String name, String text, String uiid) {
        Button button = this.makeButton(name, text);
        button.setUIID(uiid);
        return button;
    }

    protected Button makeButton(String name, String text) {
        Button button = new Button();
        button.setText(text);
        if (name != null)
            button.setName(name);
        button.setInlineStylesTheme(this.resources);
        return button;
    }

    protected TextField makeTextField(String name, String hint, String text) {
        TextField textField = this.makeTextField(name, hint);
        textField.setText(text);
        return textField;
    }

    protected TextField makeTextField(String name, String hint) {
        TextField textField = new TextField();
        textField.setHint(hint);
        if (name != null)
            textField.setName(name);
        textField.setInlineStylesTheme(this.resources);
        return textField;
    }

    protected ComponentGroup makeComponentGroup(String name) {
        ComponentGroup credentialsContainer = new ComponentGroup();
        credentialsContainer.setInlineStylesTheme(this.resources);
        if (name != null)
            credentialsContainer.setName(name);
        return credentialsContainer;
    }

    protected boolean isCurrentInbox() {
        return false;
    }

    protected boolean isCurrentTrending() {
        return false;
    }

    protected boolean isCurrentCalendar() {
        return false;
    }

    protected boolean isCurrentStats() {
        return false;
    }
}
