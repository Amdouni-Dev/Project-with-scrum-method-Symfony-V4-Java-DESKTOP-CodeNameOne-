package com.espritx.client.services.ServiceForum;

import com.codename1.io.*;

import com.codename1.ui.events.ActionListener;
import com.espritx.client.entities.ForumPost;

import com.espritx.client.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class serviceForumPost {
    public ConnectionRequest req;
    public static boolean resultOK = true;
    private static serviceForumPost instance;
    private ArrayList<ForumPost> Posts;

    private serviceForumPost() {
        req = new ConnectionRequest();
    }

    public static serviceForumPost getInstance() {
        if (instance == null)
            instance = new serviceForumPost();
        return instance;
    }

    public boolean addPost(ForumPost post) {

        String url = Statics.BASE_URL + "/forum/add_post_blog_api?title=" + post.getSlug() + "&content=" + post .getBody();
        req.setUrl(url);

        req.setPost(false);
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

    public ArrayList<ForumPost> getAllPosts() {

        String url = Statics.BASE_URL + "/forum/show_blog_post_api";
        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
                                    @Override
                                    public void actionPerformed(NetworkEvent evt) {
                                        Posts = parseForumPost(new String(req.getResponseData()));
                                        req.removeResponseListener(this);
                                    }
                                }

        );
        NetworkManager.getInstance().addToQueueAndWait(req);

        return Posts;
    }

    private ArrayList<ForumPost> parseForumPost(String jsonText) {

        try {
            Posts = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> postsListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) postsListJson.get("root");

            for (Map<String,Object> obj : list){
                String title = "null";
                String body = "null";
                if (obj.get("title")!=null)
                    title = obj.get("title").toString() ;
                if (obj.get("body")!=null)
                    title = obj.get("body").toString() ;
                ForumPost p = new ForumPost(title , body);

                Posts.add(p);

            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return Posts;
    }


    public boolean modifierForumPost(ForumPost post) {

        req = new ConnectionRequest();
        String url = Statics.BASE_URL + "/update_blog_post_api" + post.getId();
        req.addArgument("Title", post.getSlug());
        req.addArgument("body", post.getBody());


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


    public Boolean deleteForumPost(int id) {
        req = new ConnectionRequest();

        String url = Statics.BASE_URL + "/delete_blog_post_api/" + id;

        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                req.removeResponseCodeListener(this);

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;

    }
}