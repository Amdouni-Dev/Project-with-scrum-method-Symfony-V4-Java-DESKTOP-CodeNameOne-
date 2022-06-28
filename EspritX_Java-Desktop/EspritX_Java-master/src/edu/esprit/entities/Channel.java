package edu.esprit.entities;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.SimpleDateFormat;
import java.util.*;

public class Channel {
    private IntegerProperty id;
    private ObjectProperty<Date> created_at;
    private ObjectProperty<Date> updated_at;
    private final SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ListProperty<User> participants;

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", participants=" + participants +
                '}';
    }

    public Channel(int id) {
        this.id=new SimpleIntegerProperty(id);
        this.participants=  new SimpleListProperty<User>();
    }

    public Channel(IntegerProperty id, ObjectProperty<Date> created_at, ObjectProperty<Date> updated_at, ListProperty<User> participants) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.participants = participants;
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

    public ObservableList<User> getParticipants() {
        return participants.get();
    }

    public ListProperty<User> participantsProperty() {
        return participants;
    }

    public void setParticipants(ObservableList<User> participants) {
        this.participants.set(participants);
    }
    public void addParticipant (User u1){
        this.participants.add(u1);
    }
}