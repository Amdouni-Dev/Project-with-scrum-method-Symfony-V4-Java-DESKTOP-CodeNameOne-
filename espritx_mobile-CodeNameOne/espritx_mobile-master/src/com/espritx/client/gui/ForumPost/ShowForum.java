package com.espritx.client.gui.ForumPost;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.ForumPost;
import com.espritx.client.services.ServiceForum.serviceForumPost;

import java.util.ArrayList;

public class ShowForum extends BaseForm {
    public ShowForum(Form previous) {

        ArrayList<ForumPost> posts;
        setTitle("Show Forum EspritX");
        setLayout(BoxLayout.y());


        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK,(evt -> {
            previous.showBack();
        }));

        posts = serviceForumPost.getInstance().getAllPosts();

        for(ForumPost p : posts){
            CheckBox cbPosts = new CheckBox(p.getSlug()) ;
            cbPosts.setEnabled(false);
            add(cbPosts);

        }
        installSidemenu(Resources.getGlobalResources());


    }


}