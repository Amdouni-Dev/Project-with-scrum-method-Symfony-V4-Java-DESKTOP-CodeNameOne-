package com.espritx.client.services.Service;

import com.codename1.io.*;
import com.codename1.processing.Result;
import com.codename1.ui.events.ActionListener;
import com.espritx.client.entities.Group;
import com.espritx.client.entities.Request;
import com.espritx.client.entities.Service;
import com.espritx.client.utils.Statics;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ServiceService {

    public ArrayList<Service> services;

    public static ServiceService instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    private ServiceService() {
        req = new ConnectionRequest();
    }

    public static ServiceService getInstance() {
        if (instance == null) {
            instance = new ServiceService();
        }
        return instance;
    }

    public ArrayList<Service> parseServices(String jsonText) {
        try {
            services = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> servicesListJson =
                    j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) servicesListJson.get("root");
            for (Map<String, Object> obj : list) {
                Service S = new Service();
                float id = Float.parseFloat(obj.get("id").toString());
                S.setId((int) id);
                if (obj.get("Name") == null)
                    S.setName("null");
                else
                    S.setName(obj.get("Name").toString());

                HashMap<String, Object> Resp = (HashMap<String, Object>) obj.get("Responsible");
                Group Res = new Group();
                Res.getPropertyIndex().populateFromMap(Resp);
                S.setResponsible(Res);

                ArrayList<HashMap> Rece = (ArrayList<HashMap>) obj.get("Recipient");
                for (HashMap H : Rece) {
                    Group Rec = new Group();
                    Rec.getPropertyIndex().populateFromMap(H);
                    S.addRecipient(Rec);
                }

                ArrayList<HashMap> Reqs = (ArrayList<HashMap>) obj.get("serviceRequests");
                for (HashMap R : Reqs) {
                    Request request = new Request();
                    request.getPropertyIndex().populateFromMap(R);
                    S.addRequest(request);
                }

                services.add(S);
            }
        } catch (IOException ex) {

        }
        return services;
    }

    public ArrayList<Service> getAllServices() {
        req = new ConnectionRequest();
        String url = Statics.BASE_URL + "/service/show";
        System.out.println("===>" + url);
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                services = parseServices(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return services;
    }

    public boolean addService(Service S) {
        String url = Statics.BASE_URL + "/service/add/";
        req.setUrl(url);
        req.setPost(true);
        Map<String, Object> auth = new HashMap<>();
        auth.put("Name", S.getName());
        auth.put("Responsible", S.getResponsible().toString());
        auth.put("Recipient", S.getRecipient());
        final String payload = Result.fromContent(auth).toString();
        req.setRequestBody(payload);
        req.setContentType("application/json");
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 201;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public boolean updateService(Service S){
        String url = Statics.BASE_URL + "/service/"+S.getId()+"/edit";
        req.setUrl(url);
        req.setPost(true);
        Map<String, Object> auth = new HashMap<>();
        auth.put("Name", S.getName());
        auth.put("Responsible", S.getResponsible().toString());
        auth.put("Recipient", S.getRecipient());
        final String payload = Result.fromContent(auth).toString();
        req.setRequestBody(payload);
        req.setContentType("application/json");
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public boolean deleteService(Service S){
        String url = Statics.BASE_URL + "/service/"+S.getId()+"/delete";
        req.setUrl(url);
        req.setPost(true);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 204;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

}
