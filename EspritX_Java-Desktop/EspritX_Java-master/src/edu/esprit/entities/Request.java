package edu.esprit.entities;

import edu.esprit.enums.ReqStatus;
import javafx.beans.property.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Request {
    private IntegerProperty id;
    private StringProperty title;
    private StringProperty Description;
    private ObjectProperty<Date> created_at;
    private ObjectProperty<Date> updated_at;
    private ObjectProperty<Date> responded_at;
    private StringProperty email;
    private StringProperty response;
    private ObjectProperty<Service> type;
    private ObjectProperty<User> requester;
    private ObjectProperty<Enum<ReqStatus>> status;
    private File Attachements;
    private File Picture;
    private final SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Request() {
        this.id = new SimpleIntegerProperty(0);
        this.title = new SimpleStringProperty("");
        this.Description = new SimpleStringProperty("");
        this.created_at = new SimpleObjectProperty<Date>();
        this.updated_at = new SimpleObjectProperty<Date>();
        this.responded_at = new SimpleObjectProperty<Date>();
        this.email = new SimpleStringProperty("");
        this.response = new SimpleStringProperty("");
        this.requester = /*this.getcurrentUser*/new SimpleObjectProperty<>();
        this.type = new SimpleObjectProperty<Service>();
        this.status = new SimpleObjectProperty<Enum<ReqStatus>>(ReqStatus.unseen);
    }

    public Request(String title, String Description, String email,Service type, User user) {
        this.id= new SimpleIntegerProperty();
        this.title = new SimpleStringProperty(title);
        this.Description = new SimpleStringProperty(Description);
        this.created_at = new SimpleObjectProperty<Date>(new Date());
        this.updated_at = new SimpleObjectProperty<Date>(new Date());
        this.responded_at = new SimpleObjectProperty<Date>();
        this.email = (email!=null)? new SimpleStringProperty(email):new SimpleStringProperty("");
        this.requester = /*this.getcurrentUser*/new SimpleObjectProperty<>(user);
        this.type = new SimpleObjectProperty<Service>(type);
        this.status = new SimpleObjectProperty<Enum<ReqStatus>>(ReqStatus.unseen);
    }

    public IntegerProperty idProperty() {return id;}

    public int getId() {return id.get();}

    public StringProperty titleProperty() {return title;}

    public StringProperty descriptionProperty() {return Description;}

    public String getCreated_at() {return dtf.format(created_at.get());}

    public ObjectProperty<Date> created_atProperty() {return created_at;}

    public void setCreated_at(String created_at){
        try {
            this.created_at.set(dtf.parse(created_at));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUpdated_at() {return dtf.format(updated_at.get());}

    public ObjectProperty<Date> updated_atProperty() {return updated_at;}

    public void setUpdated_at(String updated_at){
        try {
            this.updated_at.set(dtf.parse(updated_at));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getResponded_at() {return dtf.format(responded_at.get());}

    public ObjectProperty<Date> responded_atProperty() {return responded_at;}

    public void setResponded_at(String responded_at) {
        try {
            if (responded_at!=null)
                this.responded_at.set(dtf.parse(responded_at));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public StringProperty emailProperty() {return email;}

    public StringProperty responseProperty() {return response;}

    public ObjectProperty<Service> typeProperty() {return type;}

    public ObjectProperty<User> requesterProperty() {return requester;}

    public ObjectProperty<Enum<ReqStatus>> statusProperty() {return status;}

    public void setstatus(String status) {this.status.set(ReqStatus.getStatus(status));}

    public File getAttachements() {return Attachements;}

    public void setAttachements(File attachements) {Attachements = attachements;}

    public File getPicture() {return Picture;}

    public void setPicture(File picture) {Picture = picture;}

    @Override
    public String toString() {
        return "Request{" +
                "title=" + title.get() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return id.get()==request.id.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
