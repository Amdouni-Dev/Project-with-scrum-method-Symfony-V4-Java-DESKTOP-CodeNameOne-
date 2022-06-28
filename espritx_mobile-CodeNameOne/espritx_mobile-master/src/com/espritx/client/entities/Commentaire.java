package com.espritx.client.entities;

import java.util.Date;

public class Commentaire {

    private int id;
    private int idUser;
    private int idPost;
    private String message;
    private Date created_at;
    private String nom;
    private String prenom;

    public Commentaire(String message) {
        this.message=message;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Commentaire() {
this.created_at=created_at;
        this.id = id;
        this.idUser = idUser;
        this.idPost = idPost;
        this.message = message;

    }

    public Commentaire(int id, int idUser, int idPost, String message) {
        this.id = id;
        this.idUser = idUser;
        this.idPost = idPost;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idPost=" + idPost +
                ", message='" + message + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
