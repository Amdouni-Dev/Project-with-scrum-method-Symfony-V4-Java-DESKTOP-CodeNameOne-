package com.espritx.client.services.ServiceCommentaire;

import com.codename1.io.*;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.events.ActionListener;
import com.espritx.client.entities.Commentaire;
import com.espritx.client.entities.Post;
import com.espritx.client.services.ServicePost.ServicePost;
import com.espritx.client.utils.Statics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ServiceCommentaire {
    private static ServiceCommentaire instance = null;
    public static ServiceCommentaire getInstance() {
        if (instance == null) {
            instance = new ServiceCommentaire();
        }

        return instance;
    }

    public ArrayList<Commentaire> afficherAllComments() {
        ConnectionRequest req=new ConnectionRequest();
        ArrayList<Commentaire> result = new ArrayList<>();
        String url = Statics.BASE_URL + "/Comments/all";
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                JSONParser jsonp;
                jsonp = new JSONParser();
                try {

                    Map<String, Object> mapComment = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));

                    List<Map<String, Object>> listofMaps = (List<Map<String, Object>>) mapComment.get("root");
                    for (Map<String, Object> obj : listofMaps) {
                        Date dateString;


                        Commentaire c = new Commentaire();
                        float id = Float.parseFloat(obj.get("id").toString());
                        float idP = Float.parseFloat(obj.get("idpost").toString());
System.out.println(Math.round(idP));

                        String message= obj.get("commentaire").toString();


                        float idUser = Float.parseFloat(obj.get("userId").toString());


                        dateString = new SimpleDateFormat("y-m-d").parse(obj.get("createdAt").toString());


                        c.setCreated_at(dateString);

                        c.setIdUser(Math.round(idUser));
                        c.setIdPost(Math.round(idP));
                        c.setId(Math.round(id));

                        c.setMessage(message);

                        result.add(c);

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        });


        NetworkManager.getInstance().addToQueueAndWait(req);
        return result;


    }


}
