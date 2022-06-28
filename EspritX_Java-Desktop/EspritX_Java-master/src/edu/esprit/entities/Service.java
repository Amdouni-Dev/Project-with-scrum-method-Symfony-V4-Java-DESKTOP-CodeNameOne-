package edu.esprit.entities;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Service {
    private IntegerProperty id;
    private StringProperty Name;
    private ObjectProperty<Group> Responsible;
    private ListProperty<Group> Recipients;
    private ListProperty<Request> Requests;

    public Service() {
        this.id=new SimpleIntegerProperty(0);
        this.Name = new SimpleStringProperty("");
        this.Responsible = new SimpleObjectProperty<Group>();
        this.Recipients = new SimpleListProperty<Group>();
        this.Requests = new SimpleListProperty<Request>();
    }

    public Service(String Name,Group Responsible,List<Group> GroupList,List<Request> RequestList) {
        this.id=new SimpleIntegerProperty(0);
        this.Name = new SimpleStringProperty(Name);
        this.Responsible = new SimpleObjectProperty<Group>(Responsible);
        ObservableList<Group> observableGroupList = FXCollections.observableArrayList(GroupList);
        this.Recipients = new SimpleListProperty<Group>(observableGroupList);
        ObservableList<Request> observableRequestList = FXCollections.observableArrayList(RequestList);
        this.Requests = new SimpleListProperty<Request>(observableRequestList);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {return id.get();}

    public StringProperty nameProperty() {
        return Name;
    }

    public ObjectProperty<Group> responsibleProperty() {
        return Responsible;
    }

    public ListProperty<Group> recipientsProperty() {
        return Recipients;
    }

    public ListProperty<Request> requestsProperty() {
        return Requests;
    }


    @Override
    public String toString() {
        String RecipientsStr = "";
        for (Group g:this.Recipients) {
            RecipientsStr+=g.toString();
        }
        String RequestsStr = "";
        for (Request r:this.Requests) {
            RequestsStr+=r.toString();
        }
        return "Service{" +
                "id=" + id +
                ", Name=" + Name +
                ", Responsible=" + Responsible+
                ", Requests="+RequestsStr+
                ", RecipientStr="+RecipientsStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        Service service = (Service) o;
        return id.get()==service.id.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
