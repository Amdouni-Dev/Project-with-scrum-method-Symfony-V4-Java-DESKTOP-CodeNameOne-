/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.services.ServicePost;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;

import com.codename1.ui.events.ActionListener;
import com.espritx.client.entities.Commentaire;
import com.espritx.client.entities.Post;
import com.espritx.client.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author mouna
 */
public class ServicePost {

    // appliquer le patron de conception singleton bch menab9ach kol mara naamel f instanciation 
    public ConnectionRequest req;
    private static ServicePost instance = null;
    public static boolean resultOK = true;
    public ArrayList<Post> posts;

    public ServicePost() {
        req = new ConnectionRequest();
    }

    public static ServicePost getInstance() {
        if (instance == null) {
            instance = new ServicePost();
        }

        return instance;
    }

    public boolean AddPost(Post p) throws ParseException {


        String url = Statics.BASE_URL + "/addPost";

        req.setUrl(url);
        req.setPost(false);
        req.addArgument("title", p.getTitle());
        req.addArgument("content", p.getContent() + "");
        req.addArgument("longitude", p.getLongitude());
        req.addArgument("latitude", p.getLongitude());
        req.addArgument("image", p.getImage());


        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                System.out.println("Ajout en cours ....");
                String str = new String(req.getResponseData());
                System.out.println("données==" + str);

                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);


            }
        });


        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }





    public boolean AddCommentaire(Commentaire c, Post p) throws ParseException {


        String url = Statics.BASE_URL + "/comment/newwApi/"+p.getId();

        req.setUrl(url);
        req.setPost(false);

        req.addArgument("content", c.getMessage() + "");



        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                System.out.println("Ajout en cours ....");
                String str = new String(req.getResponseData());
                System.out.println("données==" + str);

                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);


            }
        });


        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }








    public ArrayList<Post> getAllpost() {

        String url = Statics.BASE_URL + "/post/all";
        req.setUrl(url);
        //req.setPost(true);
        req.addResponseListener((evt) -> {
            try {
                posts = parsePost(new String(req.getResponseData()));
                System.out.println(posts);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        System.out.println(posts);
        return posts;
    }


    public ArrayList<Post> afficherAllPosts() {

        ArrayList<Post> result = new ArrayList<>();
        String url = Statics.BASE_URL + "/post/all";
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                JSONParser jsonp;
                jsonp = new JSONParser();
                try {

                    Map<String, Object> mapPost = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));

                    List<Map<String, Object>> listofMaps = (List<Map<String, Object>>) mapPost.get("root");
                    for (Map<String, Object> obj : listofMaps) {
                        Date dateString;


                        Post p = new Post();
                        float id = Float.parseFloat(obj.get("id").toString());



                        //  float    idC = Float.parseFloat(obj.get("idC").toString());
                        //  float    idpc = Float.parseFloat(obj.get("post").toString());
                        //   int iddpc=Math.round(idpc);
                        float nbC = Float.parseFloat(obj.get("nbrcomments").toString());
                        float totalP = Float.parseFloat(obj.get("nbPub").toString());
                        float totalComments = Float.parseFloat(obj.get("totalComments").toString());
                        float nbL = Float.parseFloat(obj.get("nbLikes").toString());
                        String title = obj.get("title").toString();
                        List<Map<String, Object>> listofMaps2 = (List<Map<String, Object>>) obj.get("commentaires");
                        for (Map<String, Object> c : listofMaps2){
                            String message= c.get("commentaire").toString();
                            System.out.println("******************************************"+message);
                            p.setCommentaire(message);


                        }




                        //    String commentaire = obj.get("commentaire").toString();

                        String content = obj.get("content").toString();
                        //   'post'=>$idPc,
                        String nom=obj.get("nom").toString();
                        String prenom=obj.get("prenom").toString();

                        String lati=obj.get("latitude").toString();
                        String longi=obj.get("longitude").toString();
                        String mail=obj.get("email").toString();
                        Boolean isValid = Boolean.parseBoolean(obj.get("isValid").toString());
                        // String isValid=obj.get("isValid").toString();
                        float idUser = Float.parseFloat(obj.get("userId").toString());


                        dateString = new SimpleDateFormat("y-m-d").parse(obj.get("createdAt").toString());
                        p.setNom(nom);
                        p.setPrenom(prenom);
                        p.setEmail(mail);
                        p.setTotalPost(Math.round(totalP));
                        p.setTotalcomments(Math.round(totalComments));
                        p.setCreated_at(dateString);
                        p.setNblikes(Math.round(nbL));
                        System.out.println(Math.round(totalP)+""+Math.round(totalComments));

                        p.setIdUser(Math.round(idUser));
                        p.setId(Math.round(id));
                        p.setNbCommentaire(Math.round(nbC));
/*
if(id == iddpc) {
    p.setIdCommentaire(Math.round(idC));
}
else{
    p.setCommentaire(null);

}
p.setPost(Math.round(iddpc));*/
                        p.setTitle(title);
                        //  p.setCommentaire(commentaire);
                        p.setContent(content);
                        p.setLatitude(lati);
                        p.setLongitude(longi);
                        p.setValid(isValid);
                        result.add(p);

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        });


        NetworkManager.getInstance().addToQueueAndWait(req);
        return result;


    }



    public ArrayList<Commentaire> afficherAllComments() {

        ArrayList<Commentaire> result2 = new ArrayList<>();
        String url = Statics.BASE_URL + "/Commmm/all";
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                JSONParser jsonp;
                jsonp = new JSONParser();
                try {

                    Map<String, Object> mapPost = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));

                    List<Map<String, Object>> listofMaps = (List<Map<String, Object>>) mapPost.get("root");
                    for (Map<String, Object> obj : listofMaps) {
                        Commentaire com = new Commentaire();


                        float id = Float.parseFloat(obj.get("postId").toString());
                        String message= obj.get("commentaire").toString();
                        String nom= obj.get("nom").toString();
                        String prenom= obj.get("prenom").toString();
                        com.setNom(nom);
                        com.setPrenom(prenom);
                        com.setIdPost(Math.round(id));
                        com.setMessage(message);

                        result2.add(com);

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        });


        NetworkManager.getInstance().addToQueueAndWait(req);
        return result2;


    }




    private ArrayList<Post> parsePost(String jsonText) throws IOException {
        try {
            posts = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> postListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) postListJson.get("root");
            for (Map<String, Object> obj : list) {
                String title = "null";
                String content = null;
                String longi = null;
                String Lati = null;
                if (obj.get("title") != null && obj.get("content") != null) {
                    title = obj.get("title").toString();
                }
                content = obj.get("content").toString();

                //   Post p = new Post(title, content,longi,Lati);
                //   posts.add(p);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return posts;
    }


    public Post DetailPost(int id, Post p) {

        String url = Statics.BASE_URL + "/addPost?" + id;
        req.setUrl(url);
        String str = new String(req.getResponseData());

        req.addResponseListener((evt) -> {
            try {
                JSONParser jsonp = new JSONParser();
                Map<String, Object> obj = jsonp.parseJSON(new CharArrayReader(new String(str).toCharArray()));

                p.setTitle(obj.get("title").toString());
                p.setContent(obj.get("content").toString());
                p.setLatitude(obj.get("latitude").toString());
                p.setLongitude(obj.get("longitude").toString());

            } catch (IOException ex) {
                System.out.println("Erreur teb3a l sql" + ex.getMessage());
            }

            System.out.println("donnees" + str);

        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return p;
    }


    public int totalComments(Post p){
        int totalC=0;

        totalC+=p.getNbCommentaire();
        return  totalC;
    }

    public Boolean deletePost(int id) {
        req = new ConnectionRequest();

        String url = Statics.BASE_URL + "/deletePost/" + id;

        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                req.removeResponseCodeListener(this);
                //   resultOK = req.getResponseCode()==200;
            }
        });


        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;

    }


    public boolean modifierPost(Post post) {

        req = new ConnectionRequest();
        String url = Statics.BASE_URL + "/updatePost/" + post.getId();
        req.addArgument("title", post.getTitle());
        req.addArgument("content", post.getContent() + "");
        req.addArgument("isValid", post.getValid() + "");

        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {


                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;

    }


}
