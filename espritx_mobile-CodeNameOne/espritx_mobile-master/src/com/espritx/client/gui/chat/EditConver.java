package com.espritx.client.gui.chat;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.RadioButtonList;
import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import com.codename1.properties.InstantUI;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Conversation;
import com.espritx.client.entities.User;
import com.espritx.client.services.Conversation.ServiceConversation;
import com.espritx.client.services.User.AuthenticationService;
import com.espritx.client.services.User.UserService;

import java.util.ArrayList;
import java.util.List;

public class EditConver extends BaseForm {
    public EditConver(Resources resourceObjectInstance,Conversation convtochange) {
        installSidemenu(resourceObjectInstance);
        List<User> l1=new ArrayList<>();
        UserService s1=new UserService();
        l1=s1.GetAll();
        ServiceConversation c1=new ServiceConversation();
        List<Conversation>LoadConver=new ArrayList<>();
        LoadConver=  c1.afficher_conver();
        LoadConver= c1.Filter_conver(LoadConver);
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setInlineStylesTheme(resourceObjectInstance);
        setTitle("Add Conversation");
        setLayout(BoxLayout.y());
        ArrayList<String>r1=new ArrayList<>();

        ListModel listModel=new ListModel() {
            ArrayList<String>r1=new ArrayList<>();
            @Override
            public Object getItemAt(int index) {
                return r1.get(index);
            }

            @Override
            public int getSize() {
                return r1.size();
            }

            @Override
            public int getSelectedIndex() {
                return 0;
            }

            @Override
            public void setSelectedIndex(int index) {

            }

            @Override
            public void addDataChangedListener(DataChangedListener l) {

            }

            @Override
            public void removeDataChangedListener(DataChangedListener l) {

            }

            @Override
            public void addSelectionListener(SelectionListener l) {

            }

            @Override
            public void removeSelectionListener(SelectionListener l) {

            }

            @Override
            public void addItem(Object item) {
                r1.add((String) item);
            }

            @Override
            public void removeItem(int index) {
                r1.remove(index);
            }
        };
        for (int i = 0; i < l1.size(); i++) {
            listModel.addItem( (String)l1.get(i).email.get());

        }
        TextField tfTitle = new TextField("", "Request Title");

        AutoCompleteTextField ac = new AutoCompleteTextField(listModel);
        ac.getCompletion();

        ac.setMinimumElementsShownInPopup(5);
        Button btnValider=new Button("Changer");
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ServiceConversation s2=new ServiceConversation();
                List<User> l1=new ArrayList<>();
                UserService s1=new UserService();
                Conversation c4=new Conversation();
                l1=s1.GetAll();
                for (int i = 0; i < l1.size(); i++) {
                    if(l1.get(i).email.get().equals(ac.getText())){
                        convtochange.participants.add(l1.get(i));
                        convtochange.participants.add(AuthenticationService.getAuthenticatedUser());

                        s2.Edit_conversation(convtochange);
                    }
                }
            }
        });
        add(ac);
        add(btnValider);

    }

}