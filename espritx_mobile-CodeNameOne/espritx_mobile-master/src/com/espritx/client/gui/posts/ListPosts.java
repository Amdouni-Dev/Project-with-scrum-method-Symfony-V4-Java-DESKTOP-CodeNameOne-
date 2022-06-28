/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.gui.posts;

import com.codename1.components.*;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.maps.Coord;
import com.codename1.ui.*;

import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;

import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.*;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Commentaire;
import com.espritx.client.entities.Post;
import com.espritx.client.services.ServicePost.ServicePost;
import com.espritx.client.services.User.AuthenticationService;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author mouna
 */

public class ListPosts extends BaseForm  {
    Form current;



    public ListPosts(Resources res){
        installSidemenu(res);
        //super("Feed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
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
        // RadioButton chercher = RadioButton.createToggle("Cherecher", barGroup);
        //chercher.setUIID("SelectBar");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");
        liste.addActionListener((e) -> {
            ListPosts a= new ListPosts(res);
            a.show();

            refreshTheme();
        });



        mesListes.addActionListener((e) -> {
            MesPosts a= new MesPosts(res);
            a.show();

            refreshTheme();
        });
        partage.addActionListener((e) -> {
            AjoutPost a= new AjoutPost(res);
            a.show();
            refreshTheme();
        });
        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(4, mesListes, liste, partage),
                FlowLayout.encloseBottom(arrow)
        ));







        // partage.setSelected(true);
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


        ArrayList<Post> list=ServicePost.getInstance().afficherAllPosts();
        ArrayList<Commentaire> listCommentaires = ServicePost.getInstance().afficherAllComments();





        // title.setUIID("TextFieldBlack");




        for(Post p:list) {

            if (p.getValid() == true) {
                String urlImage = "feed.jpg";
                Image placeholder = Image.createImage(1, 1);
                EncodedImage enc = EncodedImage.createFromImage(placeholder, false);

                URLImage urlim = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);





                ButtonGroup barGroup3 = new ButtonGroup();
                RadioButton AjouterCommentaire= RadioButton.createToggle(" Commenter ...",barGroup3);


                TextField title1 = new TextField("Commenter ....");
                AjouterCommentaire.addActionListener((evt) -> {

                    try {

                        if (title1.getText().equals("") ) {

                            Dialog.show("Cher utilisateur merci de verifier les données", "", "Annuler", "OK");

                        } else {

                            InfiniteProgress ip = new InfiniteProgress();;// loading after insert data
                            final Dialog idialog = ip.showInfiniteBlocking();
                            //  Post p = new Post(title.getText(), content.getText(), String.valueOf(lati), String.valueOf(longi));

                            Commentaire c =new Commentaire(title1.getText());

                            System.out.println("donnees==" + c);

                            // taw bech n3ayet lel  methode mte3 l'ajout ili mawjouda  f service bech najem nzid lel base
                            ServicePost.getInstance().AddCommentaire(c,p);
                            // na7i loading ba3d me3mlt l'ajout
                            idialog.dispose();
                            // nrefreshi
                            ToastBar.showMessage(" cher user votre commentaire est bien ajouté", FontImage.MATERIAL_PLACE);

                            new ListPosts(res).show();











                            refreshTheme();


                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                });

                //   AjouterCommentaire.setUIID("SelectBar");

                ButtonGroup barGroup2 = new ButtonGroup();
                RadioButton nbComm= RadioButton.createToggle(p.getNbCommentaire()+" Commentaires", barGroup2);

                nbComm.setUIID("SelectBar");






                TextArea gui_Text_Area_4 = new TextArea();
                Button gui_Button_4 = new Button();
                gui_Text_Area_4.setText("Commentaires");
                gui_Text_Area_4.setUIID("SlightlySmallerFontLabelLeft");
                gui_Text_Area_4.setInlineStylesTheme(res);
                gui_Text_Area_4.setName("Text_Area_4");
                gui_Text_Area_4.setEditable(false);
                gui_Button_4.setText("");
                gui_Button_4.setUIID("Label");
                gui_Button_4.setInlineStylesTheme(res);
                gui_Button_4.setName("Button_4");

                addButton(urlim, p, res);
                add(nbComm);
                add( title1);
                add(AjouterCommentaire);

                add(gui_Text_Area_4);

                //  add(gui_Button_4);
            }



            for (Commentaire c : listCommentaires) {
                if(p.getId()==c.getIdPost()) {
                    MultiButton gui_Multi_Button_3 = new MultiButton();

                    gui_Multi_Button_3.setUIID("Label");
                    gui_Multi_Button_3.setInlineStylesTheme(res);
                    gui_Multi_Button_3.setName("Multi_Button_3");

                    gui_Multi_Button_3.setPropertyValue("line1", c.getNom()+" "+c.getPrenom());
                    gui_Multi_Button_3.setPropertyValue("line2", c.getMessage());
                    gui_Multi_Button_3.setPropertyValue("uiid1", "Label");
                    gui_Multi_Button_3.setPropertyValue("uiid2", "RedLabel");


                    //  cnt.add(comm);

                    add(gui_Multi_Button_3);


                }

            }


        }


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
    private void addButton(Image img,Post p,Resources res) {

        MapContainer cnt1 = new MapContainer("AIzaSyCy-fMWerzvXcPCV0FDI07hW2DAzs_mnpY");


        // create a String
        String str = p.getLongitude();
        String str2 = p.getLatitude();
        // convert into Double
        double lat = Double.parseDouble(str);


        double lon = Double.parseDouble(str2);
        Button btnMoveCamera = new Button("Voir la position");
        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
        FontImage im = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, Display.getInstance().convertToPixels(3));


        btnMoveCamera.addActionListener(e -> {
            cnt1.setCameraPosition(new Coord(lat, lon));

            cnt1.clearMapLayers();
            cnt1.addMarker(
                    EncodedImage.createFromImage(im, false),
                    cnt1.getCoordAtPosition(e.getX(), e.getY()),
                    "" + cnt1.getCameraPosition().toString(),
                    "",
                    e3 -> {
                        ToastBar.showMessage("You clicked " + cnt1.getName(), FontImage.MATERIAL_PLACE);
                    })
            ;

        });

        cnt1.addTapListener(e -> {


            cnt1.clearMapLayers();
            cnt1.addMarker(
                    EncodedImage.createFromImage(im, false),
                    cnt1.getCoordAtPosition(e.getX(), e.getY()),
                    "" + cnt1.getCameraPosition().toString(),
                    "",
                    e3 -> {
                        ToastBar.showMessage("You clicked " + cnt1.getName(), FontImage.MATERIAL_PLACE);
                    }
            );
            ConnectionRequest r = new ConnectionRequest();
            r.setPost(false);
            r.setUrl("http://maps.google.com/maps/api/geocode/json?latlng=" + cnt1.getCameraPosition().getLatitude() + "," + cnt1.getCameraPosition().getLongitude() + "&oe=utf8&sensor=false");


            NetworkManager.getInstance().addToQueueAndWait(r);

            JSONParser jsonp = new JSONParser();

        });


        int height = Display.getInstance().convertToPixels(1f);
        int width = Display.getInstance().convertToPixels(1f);
        Button image = new Button(img.fill(width, height));
        image.setUIID("Label");

        Container cnt = BorderLayout.south(image);


        TextArea gui_Text_Area_1 = new TextArea();
        Button gui_Button_1 = new Button();
        MultiButton gui_Multi_Button_1 = new MultiButton();

        TextArea i = new TextArea("" + p.getId());
        i.setUIID("NewsTopLine");
        i.setEditable(false);

        //// ////////////////////Nom ////////////////

        gui_Multi_Button_1.setUIID("Label");
        gui_Multi_Button_1.setInlineStylesTheme(res);
        gui_Multi_Button_1.setName("Multi_Button_1");
        gui_Multi_Button_1.setIcon(res.getImage("contact-c.png"));
        gui_Multi_Button_1.setPropertyValue("line1", p.getNom());
        gui_Multi_Button_1.setPropertyValue("line2", p.getEmail());
        gui_Multi_Button_1.setPropertyValue("uiid1", "Label");
        gui_Multi_Button_1.setPropertyValue("uiid2", "RedLabel");


        MultiButton gui_LA = new MultiButton();
        String ch = p.getCreated_at().toString();
        gui_LA.setUIID("Label");
        gui_LA.setInlineStylesTheme(res);
        gui_LA.setName("gui_LA");
        gui_LA.setText("Crée le " + ch.substring(7, 9) + " " + ch.substring(4, 7));

        gui_LA.setPropertyValue("uiid1", "Label");


        // /////////////////////Content///////////////////
        gui_Text_Area_1.setText(p.getContent());
        gui_Text_Area_1.setUIID("SlightlySmallerFontLabelLeft");
        gui_Text_Area_1.setInlineStylesTheme(res);
        gui_Text_Area_1.setName("Text_Area_1");
        gui_Text_Area_1.setEditable(false);
        gui_Button_1.setText("");
        gui_Button_1.setUIID("Label");
        gui_Button_1.setInlineStylesTheme(res);
        gui_Button_1.setName("Button_1");


        //  int height=Display.getInstance().convertToPixels(11.5f);
        //    TextArea ta=new TextArea(p.getTitle());
        //   ta.setUIID("NewsTopLine");
        // ta.setEditable(false);


        Label titletxt = new Label("title" + p.getTitle(), "NewsTopLine2");
        TextArea content = new TextArea(p.getContent().toString());
        content.setUIID("NewsTopLine");
        content.setEditable(false);

        TextArea da = new TextArea(p.getCreated_at().toString());
        da.setUIID("NewsTopLine");
        da.setEditable(false);


        Label datetxt = new Label("created_at" + p.getCreated_at().toString(), "NewsTopLine2");


        TextArea valid = new TextArea(p.getCreated_at().toString());
        valid.setUIID("NewsTopLine");
        valid.setEditable(false);

        Label validtxt = new Label("" + p.getValid().toString(), "NewsTopLine2");
        //   if(isValid)
        if (p.getIdUser() == AuthenticationService.getAuthenticatedUser().id.getInt()) {
            if (p.getValid() == true) {
                validtxt.setText("approuvée");
            } else {

                validtxt.setText("non approuvée");

            }

        }


        Label lSupprimer = new Label(" ");

        if (p.getIdUser() == AuthenticationService.getAuthenticatedUser().id.getInt()) {

            lSupprimer.setUIID("NewsTopLine2");
            Style supprimerStyle = new Style(lSupprimer.getUnselectedStyle());
            FontImage supprimerImage = FontImage.createMaterial(FontImage.MATERIAL_DELETE, supprimerStyle);
            lSupprimer.setIcon(supprimerImage);
            lSupprimer.setTextPosition(RIGHT);
            supprimerStyle.setFgColor(0xf21f1f);

            lSupprimer.addPointerPressedListener(l -> {
                Dialog dig = new Dialog("Suppression Post");
                if (dig.show("Suppression", "Cher utilisateur voulez vous vraiment supprimer ce post ?", "Annuler", "Oui")) {
                    dig.dispose();

                } else

                    dig.dispose();
                if (ServicePost.getInstance().deletePost(p.getId())) {

                    InfiniteProgress ip = new InfiniteProgress();
                    ;// loading after insert data
                    final Dialog idialog = ip.showInfiniteBlocking();
                    idialog.dispose();
                    ToastBar.showMessage(" cher user votre le post a eté supprimé", FontImage.MATERIAL_DELETE);
                    // this.current.refreshTheme();
                    this.refreshTheme();

                }


            });
        }
        Label lModifier = new Label(" ");

        if (p.getIdUser() == AuthenticationService.getAuthenticatedUser().id.getInt()) {
            lModifier.setUIID("NewsTopLine2");
            Style modifierStyle = new Style(lModifier.getUnselectedStyle());
            FontImage modifierImage = FontImage.createMaterial(FontImage.MATERIAL_MODE_EDIT, modifierStyle);
            lModifier.setIcon(modifierImage);
            lModifier.setTextPosition(LEFT);
            modifierStyle.setFgColor(0xf7ad02);


            lModifier.addPointerPressedListener(l -> {

                InfiniteProgress ip = new InfiniteProgress();;// loading after insert data
                final Dialog idialog = ip.showInfiniteBlocking();
                idialog.dispose();
                new UpdatePost(res, p).show();

            });

        }


        ///////////////////////Comments


        TextArea gui_Text_Area_2 = new TextArea();
        Button gui_Button_2 = new Button();















        ShareButton sb = new ShareButton();
        if(p.getIdUser()  == AuthenticationService.getAuthenticatedUser().id.getInt()) {
            sb.setText("Share");
        }

        cnt.add(BorderLayout.CENTER, BoxLayout.encloseY(   BoxLayout.encloseY(BoxLayout.encloseY( gui_Multi_Button_1,  gui_LA),BoxLayout.encloseX(gui_Text_Area_1,gui_Button_1)) , BoxLayout.encloseY(BoxLayout.encloseXCenter(lSupprimer,validtxt,sb,lModifier))

        ));
        //cnt1.add(BorderLayout.south( BoxLayout.encloseY(
        //     BoxLayout.encloseY(BoxLayout.encloseX(gui_Text_Area_2,gui_Button_2)))));



        Container root = new Container();
        //   int height1=Display.getInstance().convertToPixels(100f);
        // int width2=Display.getInstance().convertToPixels(10f);
        //cnt1.setPreferredSize(new Dimension(width2,height1));


        add(cnt);
        if(p.getLongitude() != null && p.getLatitude() != null){
            add( cnt1);
            add( btnMoveCamera);}

    }


}
