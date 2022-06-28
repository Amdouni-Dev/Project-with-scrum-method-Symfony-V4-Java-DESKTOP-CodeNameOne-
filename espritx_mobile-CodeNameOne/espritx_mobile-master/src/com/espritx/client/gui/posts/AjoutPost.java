package com.espritx.client.gui.posts;

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
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.maps.Coord;
import com.codename1.maps.layers.Layer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
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
public class AjoutPost extends BaseForm {

    Form current;
MapContainer cnt = null;
    public AjoutPost(Resources res) {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
       // super("Feed", BoxLayout.y());
        installSidemenu(res);
        Toolbar tb = new Toolbar(true);
        current=this;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Ajouter Post");

        tb.addSearchCommand((evt) -> {
        });
        Tabs swipe = new Tabs();
        Label s1 = new Label();
        Label s2 = new Label();
        
        
       addTab(swipe,s1,res.getImage("feed.jpg"),"","",res);
        
        
       
       
       
       
       
         swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();

        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for (int iter = 0; iter < rbs.length; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }

        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if (!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });

        Component.setSameSize(radioContainer, s1, s2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));

        ButtonGroup barGroup = new ButtonGroup();
        RadioButton mesListes = RadioButton.createToggle("Mes publications", barGroup);
        mesListes.setUIID("SelectBar");
        RadioButton liste = RadioButton.createToggle("Autres", barGroup);
        liste.setUIID("SelectBar");
        RadioButton partage = RadioButton.createToggle("poster", barGroup);
        partage.setUIID("SelectBar");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");


        mesListes.addActionListener((e) -> {
               InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipDlg = ip.showInifiniteBlocking();
        
        //  ListReclamationForm a = new ListReclamationForm(res);
          //  a.show();
            refreshTheme();
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3, mesListes, liste, partage),
                FlowLayout.encloseBottom(arrow)
        ));

        partage.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(partage, arrow);
        });
        bindButtonSelection(mesListes, arrow);
        bindButtonSelection(liste, arrow);
        bindButtonSelection(partage, arrow);
        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });

       
       
       
       
       
       
       

        getContentPane().setScrollVisible(false);

        TextField title = new TextField("", "Taper le titre de post", 40, TextArea.ANY);
       title.setRows(1);
        Style titlestyle = title.getAllStyles();

        Stroke borderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);

        titlestyle.setBorder(RoundBorder.create().
                rectangle(true).
                color(0xffffff).
                strokeColor(0).
                strokeOpacity(120).
                stroke(borderStroke));
        titlestyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        titlestyle.setMargin(Component.BOTTOM, 3);
       // title.setUIID("TextFieldBlack");
        addStringValue("titre", title);

        TextField content = new TextField("", "Taper le contenu de post",40, TextArea.ANY);

        content.setRows(2);
        Style contentstyle = content.getAllStyles();

        Stroke borderStroke2 = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);

        contentstyle.setBorder(RoundBorder.create().
                rectangle(true).
                color(0xffffff).
                strokeColor(0).
                strokeOpacity(120).
                stroke(borderStroke2));
        contentstyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        contentstyle.setMargin(Component.BOTTOM, 3);
        // title.setUIID("TextFieldBlack");
        addStringValue("Contenu", content);



        Button btnAjouter = new Button("Ajouter");
        
        
        // ///////////////////////////////////////////////////////////////////////////////////////////Map/////////////////
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
       
              });      
                  
        
         Container root = new Container();
            add( cnt);
        add( btnMoveCamera);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        addStringValue("", btnAjouter);

        btnAjouter.addActionListener((evt) -> {

            try {

                if (title.getText().equals("") || content.getText().equals("")) {

                    Dialog.show("Cher utilisateur merci de verifier les données", "", "Annuler", "OK");

                } else {

                    InfiniteProgress ip = new InfiniteProgress();;// loading after insert data
                    final Dialog idialog = ip.showInfiniteBlocking();
                  //  Post p = new Post(title.getText(), content.getText(), String.valueOf(lati), String.valueOf(longi));
                   
                                         Post p =new Post(title.getText(), content.getText(), String.valueOf( cnt.getCameraPosition().getLatitude()), String.valueOf( cnt.getCameraPosition().getLongitude()),SessionManager.getId());

                    System.out.println("donnees==" + p);

                    // taw bech n3ayet lel  methode mte3 l'ajout ili mawjouda  f service bech najem nzid lel base 
                    ServicePost.getInstance().AddPost(p);
                    // na7i loading ba3d me3mlt l'ajout
                    idialog.dispose();
                    // nrefreshi
                                                               ToastBar.showMessage(" cher user votre post a eté posté mais doit etre approuvée par notre admin", FontImage.MATERIAL_PLACE);

                      new ListPosts(res).show();
                    
                  
                    
                    
                    
                    
                    
                    
                    

                    
                    refreshTheme();
                    

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        });

    }

    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PaddedLabel")).add(BorderLayout.CENTER, v));
         // add(createLineSeparator(0xeeeeee));
    }

    private void addTab(Tabs swipe, Label spacer,Image image, String string, String text, Resources res) {
int size= Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());

if( image.getHeight() < size ){

image =image.scaledHeight(size);


}


if(image.getHeight()>Display.getInstance().getDisplayHeight()/2){

image= image.scaledHeight(Display.getInstance().getDisplayHeight()/2);
}

        ScaleImageLabel imagescale=new ScaleImageLabel(image);
       imagescale.setUIID("Container");
        imagescale.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
Label overLay=new Label("","ImageOverlay");



Container page1= LayeredLayout.encloseIn(
imagescale,
        overLay, 
        BorderLayout.south(
        BoxLayout.encloseY(
        new SpanLabel(text, "LargeWhiteText"),
              spacer
        )
        
        )

);
swipe.addTab("", res.getImage("feed.jpg"),page1);

    }
    
    
    public void bindButtonSelection(Button btn,Label l){
    
    btn.addActionListener((evt) -> {
    
        if(btn.isSelected()){
        
        updateArrowPosition(btn,l);
        }
        
        
    });
    
    
    
    }

    private void updateArrowPosition(Button btn, Label l) {
        l.getUnselectedStyle().setMargin(LEFT, btn.getX()+btn.getWidth()/2 - l.getWidth());
        l.getParent().repaint();

    }
    
  

}
