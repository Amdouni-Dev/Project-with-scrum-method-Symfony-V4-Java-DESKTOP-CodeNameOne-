package com.espritx.client.gui.ForumPost;

import com.codename1.ui.Button;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;

public class HomeForum extends BaseForm {
    public HomeForum() {
        setTitle("Forum EspritX");
        setLayout(BoxLayout.y());
        Button btnAdd = new Button("Add Blog");
        Button btnShow = new Button("Show Forum");
        Button btnSearch = new Button("Search ");
        btnAdd.addActionListener((evt -> new AddForumPost(this).show()));
        btnShow.addActionListener((evt -> new ShowForum(this).show()));
        btnSearch.addActionListener((evt -> new SearchForumPost() .show()));

        installSidemenu(Resources.getGlobalResources());
        addAll(btnAdd,btnShow,btnSearch);


    }
}