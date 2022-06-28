/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.posts;

import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author mouna
 */
public class HomeForm extends Form  {
// deja fait un form (instanciation) c pas la peine d'instancier une autre fois ==> hi== this
   // Form hi=this;

    public HomeForm() {
        setTitle("Home Posts");
        setLayout(BoxLayout.y());
        Button BtnAddPost=new Button("Ajouter post");
        Button BtnShowPost= new Button("Voir les posts");
        BtnAddPost.addActionListener((evt)->new AddPostForm().show());
        BtnShowPost.addActionListener((evt)->{
        
        new ShowForm(this).show();
        });
        //add(BtnAddPost);
        //add(BtnShowPost);
        
        
        addAll(BtnAddPost,BtnShowPost);
        
        
        
        
    }
    
    
}
