/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.entities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author mouna
 */
public class Post {

    private int id;
    private String title;
    private String content;
    private String longitude;
    private String latitude;
    private Date created_at;
    private Boolean isValid;
    private String image;
    private int idUser;
    private String nom;
    private String prenom;
private int post;
    private  int idCommentaire;
    private String commentaire;
    private int nbCommentaire;
    private int nblikes;
    private int totalPost;
    private int totalcomments;
    private String email;

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalcomments() {
        return totalcomments;
    }

    public void setTotalcomments(int totalcomments) {
        this.totalcomments = totalcomments;
    }

    public int getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(int totalPost) {
        this.totalPost = totalPost;
    }

    public int getNblikes() {
        return nblikes;
    }

    public void setNblikes(int nblikes) {
        this.nblikes = nblikes;
    }

    ArrayList<String> list2;

    public ArrayList<String> getList2() {
        return list2;
    }

    public void setList2(ArrayList<String> list2) {
        this.list2 = list2;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(int idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getNbCommentaire() {
        return nbCommentaire;
    }

    public void setNbCommentaire(int nbCommentaire) {
        this.nbCommentaire = nbCommentaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    //private user
    // Id auto increment
    public Post(String title, String content, String longitude, String latitude, Boolean isValid, int idUser) {
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isValid=isValid;
        this.idUser=idUser;
    }


    public Post(String title, String content, String longitude, String latitude, Boolean isValid) {
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isValid=isValid;

    }



    public Post(String title, String content, String latitude, String longitude, int id) {
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;


        this.id=id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Post() {
          this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
        this.created_at=created_at;
        this.isValid=isValid;
        this.image=image;
        this.nom=nom;
        this.prenom=prenom;
        
    }

    public Post(String text, String text1) {

        this.title=text;
        this.content=text1;

    }



    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Post(int id, String title, String content, String longitude, String latitude, Date created_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
        this.created_at = created_at;
    
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", title=" + title + ", content=" + content + ", longitude=" + longitude + ", latitude=" + latitude + ", created_at=" + created_at + "idUser "+idUser+'}';
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
