package edu.esprit.entities.feed;

import java.time.LocalDateTime;

public class GroupPost {
    private int id;
    private int userId;
    private String nomGroupe;
    private String slug;
    private String image;
    private LocalDateTime createdat;
    private Boolean isValid;
    private Boolean isDeleted;
    private String but;

    public GroupPost() {
    }

    public GroupPost(int id, int userId, String nomGroupe, String slug, String image, String but) {
        this.id = id;
        this.userId = userId;
        this.nomGroupe = nomGroupe;
        this.slug = slug;
        this.image = image;
        this.createdat = createdat;
        this.isValid = isValid;
        this.isDeleted = isDeleted;
        this.but = but;
    }

    public GroupPost(int userId, String nomGroupe, String slug, String image, String but) {
        this.userId = userId;
        this.nomGroupe = nomGroupe;
        this.slug = slug;
        this.image = image;


        this.but = but;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getCreatedat() {
        return createdat;
    }

    public void setCreatedat(LocalDateTime createdat) {
        this.createdat = createdat;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getBut() {
        return but;
    }

    public void setBut(String but) {
        this.but = but;
    }
}
