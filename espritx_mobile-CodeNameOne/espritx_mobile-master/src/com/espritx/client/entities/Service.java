/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.entities;

import java.util.ArrayList;

/**
 * @author Hedi
 */
public class Service {
    private int id;
    private String Name;
    private Group Responsible;
    ArrayList<Group> Recipient;
    ArrayList<Request> ServiceRequests;

    @Override
    public String toString() {
        return Name ;
    }

    public Service() {
        Recipient = new ArrayList<>();
        ServiceRequests = new ArrayList<>();
    }

    public Service(String name, Group responsible, ArrayList<Group> recipient) {
        Name = name;
        Responsible = responsible;
        Recipient = recipient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Group getResponsible() {
        return Responsible;
    }

    public void setResponsible(Group responsible) {
        Responsible = responsible;
    }

    public ArrayList<Group> getRecipient() {
        return Recipient;
    }

    public void setRecipient(ArrayList<Group> recipient) {
        Recipient = recipient;
    }

    public void addRecipient(Group g) {
        Recipient.add(g);
    }

    public ArrayList<Request> getServiceRequests() {
        return ServiceRequests;
    }

    public void setServiceRequest(ArrayList<Request> serviceRequests) {
        ServiceRequests = serviceRequests;
    }

    public void addRequest(Request r) {ServiceRequests.add(r);}
}
