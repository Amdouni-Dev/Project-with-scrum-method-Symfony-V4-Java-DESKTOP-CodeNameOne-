package com.espritx.client.gui.chat;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Conversation;
import com.espritx.client.entities.User;
import com.espritx.client.services.Conversation.ServiceConversation;
import com.espritx.client.services.User.AuthenticationService;
import com.espritx.client.services.User.UserService;

import java.util.ArrayList;
import java.util.List;

public class ConversationsShow extends BaseForm {
    public ConversationsShow() {
        this(Resources.getGlobalResources());
    }

    public ConversationsShow(Resources resourceObjectInstance) {
        installSidemenu(resourceObjectInstance);
        Button addConver = new Button("Ajouter");
        addConver.addActionListener((e) -> {
            (new AddConver()).show();
        });

        List<Conversation> listeConver = new ArrayList<>();
        ServiceConversation s1 = new ServiceConversation();
        List<Button> EditList = new ArrayList<>();
        List<Button> DeleteList = new ArrayList<>();
        List<MultiButton> mbList = new ArrayList<>();
        listeConver = s1.afficher_conver();
        listeConver = s1.Filter_conver(listeConver);
        Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
        List<User> userList = UserService.GetAll();
        Container list = new Container(BoxLayout.y());
        list.setScrollableY(true);
        list.setInlineStylesTheme(resourceObjectInstance);
        list.setWidth(1500);
        list.add(addConver);
        int i = 0;
        for (Conversation conver : listeConver) {

            if (conver.participants.get(0).id.getInt() == AuthenticationService.getAuthenticatedUser().id.getInt()) {
                MultiButton mb = new MultiButton(conver.participants.get(1).first_name.get());
                mb.setInlineStylesTheme(resourceObjectInstance);
                mb.setIcon(conver.participants.get(1).getEncodedAvatar(10));
                mb.setUIIDLine1("SlightlySmallerFontLabelLeft");
                mb.setTextLine2(conver.participants.get(1).email.get());
                mb.setUIIDLine2("BlueLabel");
                Container c1 = new Container(BoxLayout.x());
                Button Edit = new Button("Modifier");
                Button Delete = new Button("Supprimer");
                c1.add(mb);
                c1.add(Delete);
                c1.add(Edit);
                list.addComponent(c1);
                mb.putClientProperty("id", conver.participants.get(1).id.getInt());
                EditList.add(Edit);
                DeleteList.add(Delete);
                mbList.add(mb);
                EditList.get(i).addActionListener((e) -> {
                    /* (new EditConver(resourceObjectInstance,conver)).show();*/
                });
                DeleteList.get(i).addActionListener((e) -> {
                    s1.Delete_conversation(conver);
                });
                mbList.get(i).addActionListener((e) -> {
                    (new ChatMain(resourceObjectInstance, conver)).show();
                });

                i = i + 1;
            } else {
                MultiButton mb = new MultiButton(conver.participants.get(0).first_name.get());
                mb.setInlineStylesTheme(resourceObjectInstance);
                mb.setIcon(conver.participants.get(0).getEncodedAvatar(10));
                mb.setUIIDLine1("SlightlySmallerFontLabelLeft");
                mb.setTextLine2(conver.participants.get(0).email.get());
                mb.setUIIDLine2("BlueLabel");
                Container c1 = new Container(BoxLayout.x());
                Button Edit = new Button("Edit");
                Button Delete = new Button("Delete");
                c1.add(mb);
                c1.add(Delete);
                c1.add(Edit);
                list.addComponent(c1);
                mb.putClientProperty("id", conver.participants.get(0).id.getInt());
                EditList.add(Edit);
                DeleteList.add(Delete);
                mbList.add(mb);
                EditList.get(i).addActionListener((e) -> {
                    /*  (new EditConver(resourceObjectInstance,conver)).show();*/
                });
                DeleteList.get(i).addActionListener((e) -> {
                    s1.Delete_conversation(conver);
                });
                mbList.get(i).addActionListener((e) -> {
                    (new ChatMain(resourceObjectInstance, conver)).show();
                });
                i = i + 1;

            }
        }
        addComponent(list);

        list.animateLayout(150);

        dlg.dispose();

    }
}