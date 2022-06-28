package com.espritx.client.services.Service;

import com.codename1.io.CharArrayReader;
import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.properties.PropertyBusinessObject;
import com.espritx.client.entities.Group;
import com.espritx.client.entities.Request;
import com.espritx.client.entities.Service;
import com.espritx.client.utils.FileUtils;
import com.espritx.client.utils.MimeTypeMap;
import com.espritx.client.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestService {
    public static List<Request> GetAll() {
        Response<List<PropertyBusinessObject>> k = Rest.get(Statics.BASE_URL + "/request/showAll").acceptJson().getAsPropertyList(Request.class);
        String str = Rest.get(Statics.BASE_URL + "/request/showAll").acceptJson().getAsString().getResponseData();
        int i = 0;
        List<PropertyBusinessObject> res = k.getResponseData();
        List<Request> RequestList = new ArrayList<>();
        for (PropertyBusinessObject r : res) {
            Request req = (Request) r;
            Service ser = parseService(str, i);
            req.Type.set(ser);
            RequestList.add(req);
            i++;
        }
        return RequestList;
    }

    public static List<Request> GetUserRequests() {
        Response<List<PropertyBusinessObject>> k = Rest.get(Statics.BASE_URL + "/request/showUser").acceptJson().getAsPropertyList(Request.class);
        String str = Rest.get(Statics.BASE_URL + "/request/showUser").acceptJson().getAsString().getResponseData();
        int i = 0;
        List<PropertyBusinessObject> res = k.getResponseData();
        List<Request> RequestList = new ArrayList<>();
        for (PropertyBusinessObject r : res) {
            Request req = (Request) r;
            Service ser = parseService(str, i);
            req.Type.set(ser);
            RequestList.add(req);
            i++;
        }
        return RequestList;
    }

    public static List<Request> GetServiceRequests(Service s) {
        Response<List<PropertyBusinessObject>> k = Rest.get(Statics.BASE_URL + "/request/" + s.getId() + "/showService").acceptJson().getAsPropertyList(Request.class);
        String str = Rest.get(Statics.BASE_URL + "/request/" + s.getId() + "/showService").acceptJson().getAsString().getResponseData();
        int i = 0;
        List<PropertyBusinessObject> res = k.getResponseData();
        List<Request> RequestList = new ArrayList<>();
        for (PropertyBusinessObject r : res) {
            Request req = (Request) r;
            Service ser = parseService(str, i);
            req.Type.set(ser);
            RequestList.add(req);
            i++;
        }
        return RequestList;
    }

    public static List<Request> GetGroupRequests() {
        Response<List<PropertyBusinessObject>> k = Rest.get(Statics.BASE_URL + "/request/showGroup").acceptJson().getAsPropertyList(Request.class);
        String str = Rest.get(Statics.BASE_URL + "/request/showGroup").acceptJson().getAsString().getResponseData();
        int i = 0;
        List<PropertyBusinessObject> res = k.getResponseData();
        List<Request> RequestList = new ArrayList<>();
        for (PropertyBusinessObject r : res) {
            Request req = (Request) r;
            Service ser = parseService(str, i);
            req.Type.set(ser);
            RequestList.add(req);
            i++;
        }
        return RequestList;
    }

    public static Request GetRequest(Request R) {
        PropertyBusinessObject k = (PropertyBusinessObject) Rest.get(Statics.BASE_URL + "/request/" + R.id + "/delete").acceptJson().getAsProperties(Request.class);
        return (Request) k;
    }

    public static Service parseService(String str, int i) {
        Service S = new Service();
        try {
            JSONParser j = new JSONParser();
            Map<String, Object> servicesListJson =
                    j.parseJSON(new CharArrayReader(str.toCharArray()));

            List<Map<String, Object>> list2 = (List<Map<String, Object>>) servicesListJson.get("root");
            Map<String, Object> list = (Map<String, Object>) list2.get(i);
            Map<String, Object> list1 = (Map<String, Object>) list.get("Type");
            float id = Float.parseFloat(list1.get("id").toString());
            S.setId((int) id);
            if (list1.get("Name") == null)
                S.setName("null");
            else
                S.setName(list1.get("Name").toString());

            HashMap<String, Object> Resp = (HashMap<String, Object>) list1.get("Responsible");
            Group Res = new Group();
            Res.getPropertyIndex().populateFromMap(Resp);
            S.setResponsible(Res);

            ArrayList<HashMap> Rece = (ArrayList<HashMap>) list1.get("Recipient");
            for (HashMap H : Rece) {
                Group Rec = new Group();
                Rec.getPropertyIndex().populateFromMap(H);
                S.addRecipient(Rec);
            }
        } catch (IOException ex) {
        }
        return S;
    }

    public static Map CreateRequest(Request R) {
        return Rest.post(Statics.BASE_URL + "/request/add/").body(R).jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

    public static void UpdateRequest(Request R, String picturePath, String attachmentPath) throws IOException {
        //return Rest.patch(Statics.BASE_URL + "/request/" + R.id + "/delete").body(R).jsonContent().acceptJson().getAsJsonMap().getResponseData();
        MimeTypeMap mimeTypeMap = new MimeTypeMap();
        MultipartRequest updateProfileRequest = new MultipartRequest();
        updateProfileRequest.addData("PictureFile", picturePath, mimeTypeMap.getMimeType(FileUtils.getFileExtension(picturePath)));
        updateProfileRequest.addData("AttachementsFile", attachmentPath, mimeTypeMap.getMimeType(FileUtils.getFileExtension(picturePath)));
        updateProfileRequest.addArgumentNoEncoding("Request", R.getPropertyIndex().toJSON());
        updateProfileRequest.setPost(true);
        updateProfileRequest.setUrl(Statics.BASE_URL + "/request/edit/" + R.id);
        NetworkManager.getInstance().addToQueueAndWait(updateProfileRequest);
    }

    public static Map DeleteRequest(Request R) {
        return Rest.delete(Statics.BASE_URL + "/request/" + R.id + "/delete").jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

}
