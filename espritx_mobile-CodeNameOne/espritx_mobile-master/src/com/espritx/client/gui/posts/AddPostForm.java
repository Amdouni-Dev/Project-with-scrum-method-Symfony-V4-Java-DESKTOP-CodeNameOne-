/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.espritx.client.gui.posts;

import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.maps.MapComponent;
import com.codename1.maps.layers.PointLayer;
import com.codename1.maps.layers.PointsLayer;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Style;
import com.espritx.client.entities.Post;
import com.espritx.client.services.ServicePost.ServicePost;

import java.io.IOException;
import java.util.List;


/**
 *
 * @author mouna
 */
public class AddPostForm extends Form  {
            
         Form f = new Form("Map");
  MapContainer cnt = null;
// passer en parametre la Form precedente
    public AddPostForm() {
     
        f.setTitle("Ajouter Post");
      
        TextField tfTitle=new TextField("","Taper le titre de post");
        TextField tfContent=new TextField("","Taper le contenu de post");
       // TextField tfLong=new TextField("","Taper le Long");
        //TextField tfLati=new TextField("","Taper le Lati");
        Button btnAdd=new Button("Publier");
       
        
        
        
        
        
        getToolbar().addMaterialCommandToLeftBar("Posts", FontImage.MATERIAL_POST_ADD, (evt)->{
        f.showBack();
        });
     
    
        
         
             try{
        cnt = new MapContainer("AIzaSyCy-fMWerzvXcPCV0FDI07hW2DAzs_mnpY");
    }catch(Exception ex) {
        ex.printStackTrace();
    }
Button btnMoveCamera = new Button("Mon Pays");
        btnMoveCamera.addActionListener(e->{
            cnt.setCameraPosition(new Coord(36.8189700, 10.1657900));
        });
        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
          FontImage im = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, Display.getInstance().convertToPixels(3));

        
    


        cnt.addTapListener(e->{
    
            
                cnt.clearMapLayers();
                cnt.addMarker(
                        EncodedImage.createFromImage(im, false),
                        cnt.getCoordAtPosition(e.getX(), e.getY()),
                        ""+cnt.getCameraPosition().toString(),
                        "",
                        e3->{
                                ToastBar.showMessage("You clicked "+cnt.getName(), FontImage.MATERIAL_PLACE);
                        }
                );
             ConnectionRequest r = new ConnectionRequest();
            r.setPost(false);
            r.setUrl("http://maps.google.com/maps/api/geocode/json?latlng="+cnt.getCameraPosition().getLatitude()+","+cnt.getCameraPosition().getLongitude()+"&oe=utf8&sensor=false");
               
            
            NetworkManager.getInstance().addToQueueAndWait(r);

            JSONParser jsonp = new JSONParser();
         try {
               java.util.Map<String, Object> tasks = jsonp.parseJSON(new CharArrayReader(new String(r.getResponseData()).toCharArray()));
                              System.out.println("roooooot:" +tasks.get("results"));
                              List<java.util.Map<String, Object>> list1 = (List<java.util.Map<String, Object>>)tasks.get("results");
                              java.util.Map<String, Object> list = (java.util.Map<String, Object>) list1.get(0);

                               List<java.util.Map<String, Object>> listf = (List<java.util.Map<String, Object>>) list.get("address_components");
String ch="";
                         for (java.util.Map<String, Object> obj : listf) {
                 ch=ch+obj.get("long_name").toString();
                         }
                       //
                        //  b.setAdresse(ch);

                        

           } catch (IOException ex) {
           }

            
            
        });
        
        
        
        
        
        
        
         btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                
                // Ajouter  un control de saisi 
                if(tfTitle.getText().equals("")){
                    Dialog.show("Error", "le titre doit avoir au moins trois caracteres", "OK", null);}
                       else
                {
                    try {
                       // tfLati.setText(String.valueOf( cnt.getCameraPosition().getLatitude()));
                     //   Post p = new Post(tfTitle.getText(), tfContent.getText().toString(), String.valueOf( cnt.getCameraPosition().getLatitude()).toString() ,tfLong.getText().toString()));
                     //Post p =new Post(tfTitle.getText(),"", tfContent.getText(), String.valueOf( cnt.getCameraPosition().getLatitude()), String.valueOf( cnt.getCameraPosition().getLongitude()));
                    /* if( ServicePost.getInstance().AddPost(p))
                        {
                           Dialog.show("Success","Connection accepted",new Command("OK"));
                        }else
                            Dialog.show("ERROR", "Server error", new Command("OK"));
*/                    
} catch (NumberFormatException e) {
                        Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                    }
                    
                }
                
                
            }
        });
        
        
        
        
        
        
        
        
        Container root = new Container();
       
          f.add( tfTitle);
             f.add( tfContent);
             
                     //f.add( tfLati);
             //f.add( tfLong);
             f.add( btnAdd);

         f.add( cnt);
        f.add( btnMoveCamera);

   f.show();

    //    getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());
    }
    
}
