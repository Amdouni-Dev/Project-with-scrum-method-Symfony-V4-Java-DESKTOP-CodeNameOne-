package com.espritx.client.services.User;

import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.processing.Result;
import com.codename1.properties.PropertyBusinessObject;
import com.espritx.client.entities.Group;
import com.espritx.client.entities.User;
import com.espritx.client.utils.RestUtils;
import com.espritx.client.utils.Statics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Any sane person might ask; why in the fuck isn't this a GenericCrudService<T>. You'd be right.
// Reflection isn't supported to get parametrized types.
// Because who the fuck includes a vTable when mangling their names anymore?

public class UserService {
    public static List<User> GetAll() {
        return RestUtils.fetchListFrom(Statics.BASE_URL + "/user", User.class);
    }

    public static Map Create(User u) {
        return Rest.post(Statics.BASE_URL + "/user/").body(u).jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

    public static Map Update(User u) {
        return Rest.patch(Statics.BASE_URL + "/user/" + u.id).body(u).jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

    public static Map Delete(User u) {
        return Rest.delete(Statics.BASE_URL + "/user/" + u.id).jsonContent().acceptJson().getAsJsonMap().getResponseData();
    }

    public static ArrayList<String> AutoComplete(String text) {
        Map<String, String> auth = new HashMap<>();
        auth.put("email", text);
        final String payload = Result.fromContent(auth).toString();
        Response<Map> k = Rest.post(Statics.BASE_URL + "/user/autocomplete_emails").body(payload).jsonContent().acceptJson().getAsJsonMap();
        Map res = k.getResponseData();
        ArrayList<String> arrayList = (ArrayList<String>) res.get("values");
        return arrayList;
    }

    public static User fetchUserByEmail(String email) {
        return (User) Rest.get(Statics.BASE_URL + "/user/fetch_by_email").acceptJson().jsonContent().queryParam("email", email).getAsProperties(User.class).getResponseData();
    }


}
