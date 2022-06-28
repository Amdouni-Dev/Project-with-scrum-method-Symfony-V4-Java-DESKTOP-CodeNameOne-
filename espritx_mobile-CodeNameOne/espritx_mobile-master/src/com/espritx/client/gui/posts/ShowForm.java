/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.posts;

import com.codename1.components.SpanLabel;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import com.espritx.client.entities.Post;
import com.espritx.client.services.ServicePost.ServicePost;

import java.util.ArrayList;


/**
 *
 * @author mouna
 */
public class ShowForm extends Form  {

    public ShowForm(Form previous) {
        ArrayList<Post> posts;
        
        setTitle("Afficher Posts");
        setLayout(BoxLayout.y());
          getToolbar().addMaterialCommandToLeftBar("Posts", FontImage.MATERIAL_POST_ADD, (evt)->{
        previous.showBack();
        });
         SpanLabel sp = new SpanLabel();
        sp.setText(ServicePost.getInstance().getAllpost().toString());
        add(sp);
    }
    
}
