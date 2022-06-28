/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.posts;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Post;
import com.espritx.client.services.ServicePost.ServicePost;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author mouna
 */
public class AcceuilPost extends BaseForm {

    Image duke = null;
    public AcceuilPost(){




        int fiveMM = Display.getInstance().convertToPixels(5);
        // final Image finalDuke = duke.scaledWidth(fiveMM);
        Toolbar.setGlobalToolbar(true);
        Form hi = new Form("Search", BoxLayout.y());
        hi.add(new InfiniteProgress());
        Display.getInstance().scheduleBackgroundTask(()-> {
            // this will take a while...
            List<Post> posts= ServicePost.getInstance().afficherAllPosts();
            Display.getInstance().callSerially(() -> {
                hi.removeAll();
                for(Post c : posts) {
                    MultiButton m = new MultiButton();
                    m.setTextLine1(c.getTitle());
                    m.setTextLine2(c.getContent());


                    //    m.setIcon(finalDuke);

                    hi.add(m);
                }
                hi.revalidate();
            });
        });

        hi.getToolbar().addSearchCommand(e -> {
            String text = (String)e.getSource();
            if(text == null || text.length() == 0) {
                // clear search
                for(Component cmp : hi.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
                hi.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                for(Component cmp : hi.getContentPane()) {
                    MultiButton mb = (MultiButton)cmp;
                    String line1 = mb.getTextLine1();
                    String line2 = mb.getTextLine2();
                    boolean show = line1 != null && line1.toLowerCase().indexOf(text) > -1 ||
                            line2 != null && line2.toLowerCase().indexOf(text) > -1;
                    mb.setHidden(!show);
                    mb.setVisible(show);
                }
                hi.getContentPane().animateLayout(150);
            }
        }, 4);
        hi.show();


    }
}
