package edu.esprit.entities.feed;

import edu.esprit.entities.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PostLikes {
private int id;
private int postId;
private int userId;
    private ObjectProperty<Post> post;
    private ObjectProperty<User> user;

    public PostLikes() {
        this.id=id;

        this.user = new SimpleObjectProperty<>();
        this.post=new SimpleObjectProperty<>();

    }

    public Post getPost() {
        return post.get();
    }

    public ObjectProperty<Post> postProperty() {
        return post;
    }

    public void setPost(Post post) {
        this.post.set(post);
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

    public PostLikes(int postId, int userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public PostLikes(int id, int postId, int userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
