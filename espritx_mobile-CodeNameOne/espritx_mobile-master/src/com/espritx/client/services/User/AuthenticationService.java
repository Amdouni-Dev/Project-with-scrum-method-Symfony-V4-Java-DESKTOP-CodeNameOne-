package com.espritx.client.services.User;

import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.processing.Result;
import com.codename1.ui.Display;
import com.espritx.client.entities.User;
import com.espritx.client.utils.Statics;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthenticationService {
    private static User authenticatedUser = null;

    public static User getAuthenticatedUser() {
        if (authenticatedUser == null) {
            authenticatedUser = (User) Rest.get(Statics.BASE_URL + "/user/fetch_my_data").getAsProperties(User.class).getResponseData();
        }
        return authenticatedUser;
    }
    public static void updateAuthenticatedUser(User user) {
        authenticatedUser = user;
    }

    public static String attemptGetToken(String email, String password) throws Exception {
        String loginUrl = Statics.BASE_URL + "/login_check";
        Map<String, String> auth = new HashMap<>();
        auth.put("username", email);
        auth.put("password", password);
        final String payload = Result.fromContent(auth).toString();
        Response<Map> k = Rest.post(loginUrl).jsonContent().body(payload).getAsJsonMap();
        if (k.getResponseCode() != 200) {
            throw new Exception(k.getResponseErrorMessage());
        } else {
            LinkedHashMap<String, String> data = (LinkedHashMap<String, String>) k.getResponseData();
            String token = (String) data.values().toArray()[0];
            Log.p("Granted access with token " + token);
            return token;
        }
    }

    public static User Authenticate(String username, String password) throws Exception {
        String t = AuthenticationService.attemptGetToken(username, password);
        NetworkManager.getInstance().addDefaultHeader("Authorization", "Bearer " + t);
        return getAuthenticatedUser();
    }

    public static User SetAuthenticatedToken(String token){
        NetworkManager.getInstance().addDefaultHeader("Authorization", "Bearer " + token);
        return getAuthenticatedUser();
    }

    public static void Deauthenticate(){
        authenticatedUser = null;
        NetworkManager.getInstance().addDefaultHeader("Authorization", "" );
    }

    public static void StartDeviceAuthrorization(){
        String code = Rest.post(Statics.DOMAIN + "/authenticate_device/start").acceptJson().getAsJsonMap().getResponseData().get("code").toString();
        Display.getInstance().execute(Statics.DOMAIN + "/authenticated-device-authorization/mobile/" + code);
    }
}
