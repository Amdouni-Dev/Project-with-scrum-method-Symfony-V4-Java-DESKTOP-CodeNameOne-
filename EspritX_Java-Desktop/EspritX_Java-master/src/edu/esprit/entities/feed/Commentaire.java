package edu.esprit.entities.feed;

import edu.esprit.entities.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

public class Commentaire {
    private int id;
    private int userId;
    private int postId;

    private LocalDateTime createdAt;
    private String content;
    private ObjectProperty<Post> post;
    private ObjectProperty<User> user;
    public Commentaire() {
        this.id=id;
        this.content=content;
        this.createdAt=createdAt;
        this.user = new SimpleObjectProperty<>();
        this.post=new SimpleObjectProperty<>();


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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public ObjectProperty<User> userP() {
        return user;
    }
    public Commentaire(int userId, int postId, String content) {
        this.userId = userId;


        this.content = content;
    }

    public Commentaire(int id,  String content) {
        this.id = id;

        this.content = content;
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
////////////////////////////////
    public Post getPost() {
        return post.get();
    }

    public ObjectProperty<Post> postProperty() {
        return post;
    }

    public void setPost(Post post) {
        this.post.set(post);
    }
////////////////////////////////////////////
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
