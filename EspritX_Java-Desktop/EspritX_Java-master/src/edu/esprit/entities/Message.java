package edu.esprit.entities;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Message {
    private IntegerProperty id;
    private ObjectProperty<Date> created_at;
    private ObjectProperty<Date> updated_at;
    private StringProperty content;
    private ObjectProperty<User> author;
    private ObjectProperty<Channel> CurrentConversation;

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public Message() {
        this.id=new SimpleIntegerProperty(0);
        this.content = new SimpleStringProperty("");
        this.author = new SimpleObjectProperty<>();
        this.CurrentConversation =new SimpleObjectProperty<>();
    }

    public Message(IntegerProperty id, ObjectProperty<Date> created_at, ObjectProperty<Date> updated_at, ObjectProperty<User> author, ObjectProperty<Channel> currentConversation) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.content = new SimpleStringProperty("");
        this.author = author;
        CurrentConversation = currentConversation;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", author=" + author +
                '}';
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Date getCreated_at() {
        return created_at.get();
    }

    public ObjectProperty<Date> created_atProperty() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at.set(created_at);
    }

    public Date getUpdated_at() {
        return updated_at.get();
    }

    public ObjectProperty<Date> updated_atProperty() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at.set(updated_at);
    }

    public User getAuthor() {
        return author.get();
    }

    public ObjectProperty<User> authorProperty() {
        return author;
    }

    public void setAuthor(User author) {
        this.author.set(author);
    }

    public Channel getCurrentConversation() {
        return CurrentConversation.get();
    }

    public ObjectProperty<Channel> currentConversationProperty() {
        return CurrentConversation;
    }

    public void setCurrentConversation(Channel currentConversation) {
        this.CurrentConversation.set(currentConversation);
    }
}