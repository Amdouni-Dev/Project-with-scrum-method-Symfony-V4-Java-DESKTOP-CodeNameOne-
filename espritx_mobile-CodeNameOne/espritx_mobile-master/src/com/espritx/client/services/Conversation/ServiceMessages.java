package com.espritx.client.services.Conversation;

import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.properties.PropertyBusinessObject;
import com.espritx.client.entities.Message;
import com.espritx.client.utils.Statics;

import java.util.ArrayList;
import java.util.List;

public class ServiceMessages {
    public List<Message> afficher_message() {
        /*Rest.get(Statics.BASE_URL+"/AddMessage");*/
        Response<List<PropertyBusinessObject>> k = Rest.get(Statics.BASE_URL+"/showMessages").acceptJson().getAsPropertyList(Message.class);
        List<PropertyBusinessObject> res = k.getResponseData();

        List<Message> LoadedMessages = new ArrayList<>();
        for (PropertyBusinessObject r : res) {
            LoadedMessages.add((Message) r);
        }

        return LoadedMessages;
    }
    public void Add_Message(Message m) {
        System.out.println(Rest.post(Statics.BASE_URL+"/AddMessage").body(m).jsonContent().acceptJson().getAsJsonMap().getResponseData());
    }
    public void Delete_message(Message m) {
        System.out.println(Rest.post(Statics.BASE_URL+"/DeleteMessage").body(m).jsonContent().acceptJson().getAsJsonMap().getResponseData());
    }
    public void Edit_message(Message m) {
        System.out.println(Rest.post(Statics.BASE_URL+"/EditMessage").body(m).jsonContent().acceptJson().getAsJsonMap().getResponseData());
    }
}
