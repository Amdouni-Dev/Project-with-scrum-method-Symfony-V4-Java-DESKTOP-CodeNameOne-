package com.espritx.client.gui.ForumPost;

import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.ForumPost;
import com.espritx.client.services.ServiceForum.serviceForumPost;

public class AddForumPost extends BaseForm {

    public AddForumPost(Form previous) {
        setTitle("Add Blog");
        setLayout(BoxLayout.y());
        TextField tfTitle = new TextField("","Blog Title");
        TextField tfContent = new TextField("","Blog Body");
        Button btnAdd = new Button("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ( tfTitle.getText() == ""){
                    Dialog.show("Error","Title not valid", "OK" , null);
                }
                else {
                    ForumPost postF = new ForumPost(tfTitle.getText(), tfContent.getText());
                    if (serviceForumPost.getInstance().addPost(postF)){
                        Dialog.show("Success","Post Added", "OK" , null);
                    }
                    else {
                        Dialog.show("Error","request error", "OK" , null);
                    }
                }

            }
        });
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK,(evt -> {
            previous.showBack();
        }));
        installSidemenu(Resources.getGlobalResources());
        addAll(tfTitle,tfContent,btnAdd);
    }
}