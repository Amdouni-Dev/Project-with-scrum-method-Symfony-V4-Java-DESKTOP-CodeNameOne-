package com.espritx.client.utils;

import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.properties.PropertyBusinessObject;

import java.util.ArrayList;
import java.util.List;

public class RestUtils {
    public static <T extends PropertyBusinessObject> List<T> fetchListFrom(String url, Class<T> tClass) {
        Response<List<PropertyBusinessObject>> k = Rest.get(url).acceptJson().getAsPropertyList(tClass);
        List<PropertyBusinessObject> res = k.getResponseData();
        List<T> u = new ArrayList<>(); // why can't streams be used in android build?!
        for (PropertyBusinessObject r : res) {
            u.add((T) r);
        }
        return u;
    }
}
