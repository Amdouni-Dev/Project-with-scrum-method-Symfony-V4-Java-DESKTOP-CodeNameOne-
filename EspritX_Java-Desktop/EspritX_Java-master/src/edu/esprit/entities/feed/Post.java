package edu.esprit.entities.feed;


import edu.esprit.entities.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private int id;
    private String title;
    private String content;
    private int userId;
    private String slug;
    private LocalDateTime ceatedAt;
    private Boolean isValid;
    private LocalDateTime  updatedAt;
    private Boolean isDeleted;
    private String longitude;
    private String latitude;
    private String image;
    private Integer nbCommentaire;

    public Integer getNbCommentaire() {
        return nbCommentaire;
    }

    public void setNbCommentaire(Integer nbCommentaire) {
        this.nbCommentaire = nbCommentaire;
    }

    private ObjectProperty<User> user;


    public Post() {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ceatedAt = ceatedAt;
        this.isValid = isValid;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
        this.user = new SimpleObjectProperty<>();
    }

    public User getUser() {
        return user.get();
    }

    public ObjectProperty<User> userProperty() {
        return user;
    }

    public void setUser(User user) {
        this.user.set(user);
    }




    public ObjectProperty<User> userP() {
        return user;
    }





    public Post(int id, Boolean isDeleted) {
        this.id = id;
        this.isDeleted = isDeleted;
    }

    public Post(int id) {
        this.id = id;
    }





    public Post(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getCeatedAt() {
        return ceatedAt;
    }

    public void setCeatedAt(LocalDateTime ceatedAt) {
        this.ceatedAt = ceatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public String toString() {
        return "Message{" +

                ", Annonceur=" + user +
                '}';
    }
}
