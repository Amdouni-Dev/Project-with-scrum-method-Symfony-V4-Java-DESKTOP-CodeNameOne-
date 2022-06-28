package com.espritx.client.services.User;

import com.codename1.io.rest.Rest;
import com.espritx.client.entities.Group;
import com.espritx.client.utils.RestUtils;
import com.espritx.client.utils.Statics;

import java.util.List;
import java.util.Map;


public class GroupService {

    public static List<Group> GetAll() {
        return RestUtils.fetchListFrom(Statics.BASE_URL + "/group", Group.class);
    }

    public static Map Create(Group u) {
        return Rest.post(Statics.BASE_URL + "/group/").body(u).jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

    public static Map Update(Group u) {
        return Rest.patch(Statics.BASE_URL + "/group/" + u.id).body(u).jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

    public static Map Delete(Group u) {
        return Rest.delete(Statics.BASE_URL + "/group/" + u.id).jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

}

